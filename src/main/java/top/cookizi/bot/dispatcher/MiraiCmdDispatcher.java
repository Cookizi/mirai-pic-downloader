package top.cookizi.bot.dispatcher;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.cookizi.bot.common.constant.MemoryConst;
import top.cookizi.bot.common.enums.MsgType;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdArg;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdOption;
import top.cookizi.bot.dispatcher.config.*;
import top.cookizi.bot.dispatcher.data.CmdExecuteResult;
import top.cookizi.bot.manage.mirai.MiraiApiClient;
import top.cookizi.bot.modle.domain.Sender;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.MsgResp;
import top.cookizi.bot.service.WebSocketService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 命令分发
 * <p>
 * todo 添加包扫描
 */
@Slf4j
@Component
public class MiraiCmdDispatcher implements ApplicationRunner {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private MiraiApiClient miraiApiClient;
    @Autowired
    private ApplicationContext context;

    @Getter
    //<命令名称，命令定义>
    Map<String, CmdDefinition> cmdDefinitionMap = new HashMap<>();

    private static final String MSG_RESP = MsgResp.class.getName();

    @Override
    public void run(ApplicationArguments args) {
        log.info("启动完毕，初始化参数:{}", appConfig);
        Map<String, Object> miraiCmdObj = context.getBeansWithAnnotation(MiraiCmd.class);

        for (Object bean : miraiCmdObj.values()) {
            Class<?> clazz = bean.getClass();
            defineTheCmd(clazz, bean);
        }
        log.info("命令注册完毕，开始连接bot");
        webSocketService.connect(this);
    }

    private void defineTheCmd(Class<?> clazz, Object bean) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getAnnotation(MiraiCmdDefine.class) != null)
                .map(method -> getCmdDefinition(bean, method))
                .forEach(cmd -> {
                    CmdDefinition oldCmd = cmdDefinitionMap.put(cmd.getName(), cmd);
                    if (oldCmd != null) {
                        throw new RuntimeException("命令定义重复，命令名称：" + cmd.getName());
                    }
                });

    }

    @NotNull
    private CmdDefinition getCmdDefinition(Object bean, Method method) {

        Parameter[] parameters = method.getParameters();
        MiraiCmdDefine def = method.getAnnotation(MiraiCmdDefine.class);
        CmdType cmdType = def.cmdType();
        if (cmdType == CmdType.LISTENER) {
            if (parameters.length != 1) {
                throw new RuntimeException("监听类别的方法有且只能有一个为MsgResp类型的参数，错误方法：" + method.getName());
            }
            Parameter parameter = parameters[0];
            String paramTypeNameWithPackage = parameter.getType().getName();
            if (!MSG_RESP.equals(paramTypeNameWithPackage)) {
                throw new RuntimeException("监听类别的方法有且只能有一个为MsgResp类型的参数，错误方法：" + method.getName());
            }
        }

        CmdDefinition cmdDef = new CmdDefinition();
        cmdDef.setCmdType(cmdType);
        cmdDef.setName(def.name());
        cmdDef.setCmdBean(bean);
        cmdDef.setCmdExecuteMethod(method);
        cmdDef.setScopeList(Arrays.asList(def.scope()));
        cmdDef.setCmdRespType(def.respType());
        cmdDef.setResponse(def.isResponse());
        cmdDef.setSpecial(def.special());

        List<OptionDefinition> optionDefinitionList = new ArrayList<>();
        List<ArgDefinition> argDefinitionList = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            MiraiCmdOption opt = param.getAnnotation(MiraiCmdOption.class);
            MiraiCmdArg arg = param.getAnnotation(MiraiCmdArg.class);
            if (opt != null) {
                OptionDefinition optionDefinition = new OptionDefinition(i, opt, param.getType());
                optionDefinitionList.add(optionDefinition);
            } else if (arg != null) {
                ArgDefinition argDefinition = new ArgDefinition(i, param.getType());
                argDefinitionList.add(argDefinition);
            } else {
                if (cmdType != CmdType.LISTENER) {
                    throw new RuntimeException("未定义的命令选项，方法：" + method.getName());
                }
            }
        }

        cmdDef.setOptionDefinitionList(optionDefinitionList);
        cmdDef.setArgDefinitionList(argDefinitionList);
        cmdDef.setParamCount(optionDefinitionList.size() + argDefinitionList.size());

        return cmdDef;
    }

    public void doDispatcher(MsgResp msgResp) {
        List<String> cmdList = msgResp.getMessageChain().stream().filter(x -> x instanceof PlainTextMsg)
                .map(x -> (PlainTextMsg) x)
                .map(PlainTextMsg::getText)
                .collect(Collectors.toList());
        log.debug("执行监听");
        List<CmdDefinition> specialCmdList = cmdDefinitionMap.values().stream()
                .filter(x -> x.getCmdType() == CmdType.LISTENER)
                .filter(x -> CommandScope.isScopeMatch(msgResp.getType(), x.getScopeList()))
                .collect(Collectors.toList());
        execute(msgResp, null, specialCmdList);
        //解析参数和命令
        log.debug("执行命令");
        if (!cmdList.isEmpty()) {
            String cmdName = cmdList.get(0);
            List<CmdDefinition> cmdDefList = getCmdDefList(msgResp, cmdName);
            if (cmdDefList.isEmpty()) {
                log.info("未发现命令");
                return;
            }
            //执行命令
            execute(msgResp, cmdName, cmdDefList);
        }
    }

    private void execute(MsgResp msgResp, String cmdName, List<CmdDefinition> cmdDefList) {
        for (CmdDefinition cmdDef : cmdDefList) {
            try {
                CmdExecuteResult<?> executeResult = cmdDef.execute(cmdName, msgResp);
                if (executeResult.isResp()) {
                    doResp(cmdDef, executeResult.getData(), msgResp);
                }
            } catch (Exception e) {
                log.warn("执行命令失败，命令名称={}", cmdDef.getName(), e);
                List<Msg> errorMsg = Msg.MsgBuilder.newBuilder()
                        .append(new PlainTextMsg("命令执行失败，错误原因：" + e.getMessage()))
                        .build();
                doResp(cmdDef, errorMsg, msgResp);
                break;
            }
        }
    }

    @NotNull
    private List<CmdDefinition> getCmdDefList(MsgResp msgResp, String cmdStr) {
        List<CmdDefinition> cmdDefList = new ArrayList<>();
        if (cmdStr.startsWith(appConfig.getCmdPrefix())) {
            String cmdWithoutPrefix = cmdStr.substring(1);
            String[] cmdWithArgs = cmdWithoutPrefix.split("\\s+");
            String cmdName = cmdWithArgs[0];

            CmdDefinition cmdDefinition = cmdDefinitionMap.get(cmdName);
            //普通命令，只会有一个
            if (cmdDefinition != null) {
                List<CommandScope> scopeList = cmdDefinition.getScopeList();
                if (scopeList.stream().anyMatch(x -> x.isScopeMatch.apply(msgResp.getType()))) {
                    cmdDefList.add(cmdDefinition);
                }
            }
        }
        return cmdDefList;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void doResp(CmdDefinition cmdDef, Object executeResult, MsgResp msgResp) {

        List<Msg> msgChain = null;
        if (executeResult instanceof List) {
            List list = (List) executeResult;
            if (list.size() > 0) {
                Object o = list.get(0);
                if (o instanceof Msg) {
                    msgChain = (List<Msg>) list;
                }
            }
        }
        if (msgChain == null) {
            msgChain = Msg.MsgBuilder.newBuilder()
                    .append(new PlainTextMsg(String.valueOf(executeResult)))
                    .build();
        }

        MsgType msgType = MsgType.parse(msgResp.getType());
        Sender sender = msgResp.getSender();
        long senderQQ = sender.getId();
        Long mgsId = msgResp.getMsgId();
        if (msgType == MsgType.FRIEND_MESSAGE) {
            miraiApiClient.sendFriendMessage(MemoryConst.getSession(), senderQQ, mgsId, msgChain);
        } else {
            long group = sender.getGroup().getId();
            miraiApiClient.sendGroupMessage(MemoryConst.getSession(), group, mgsId, msgChain);
        }
    }


}
