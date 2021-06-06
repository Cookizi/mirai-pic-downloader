package top.cookizi.bot.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdOption;
import top.cookizi.bot.dispatcher.config.CmdDefinition;
import top.cookizi.bot.dispatcher.config.OptionDefinition;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.MsgResp;
import top.cookizi.bot.service.WebSocketService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 命令分发
 *
 * todo 添加包扫描
 */
@Slf4j
@Component
public class MiraiCmdDispatcher {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private AppConfig appConfig;

    //每个命令类中的对应的执行方法，记录一下参数，方便解析
    private Map<Method, Options> methodOptionsMap = new HashMap<>();

    //<命令名称，命令定义>
    Map<String, CmdDefinition> cmdDefinitionMap = new HashMap<>();
    //不需要解析的命令
    List<Object> notRequiredParseCmdList = new ArrayList<>();


    public void init(ApplicationContext context) {

        Map<String, Object> miraiCmdObj = context.getBeansWithAnnotation(MiraiCmd.class);

        for (Object bean : miraiCmdObj.values()) {
            Class<?> clazz = bean.getClass();
            defineTheCmd(clazz, bean);

//            mapMethod2Options(clazz);
        }
        webSocketService.connect();
    }

    //todo 处理MiraiCmdArg
    private void defineTheCmd(Class<?> clazz, Object bean) {
        cmdDefinitionMap = Arrays.stream(clazz.getDeclaredMethods())
                .filter(x -> x.getAnnotation(MiraiCmdDefine.class) != null)
                .map(method -> {
                    List<OptionDefinition> optionDefinitionList = new ArrayList<>();
                    Parameter[] parameters = method.getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter x = parameters[i];
                        MiraiCmdOption opt = x.getAnnotation(MiraiCmdOption.class);
                        if (opt == null) throw new RuntimeException("未定义的命令选项，方法：" + method.getName());

                        OptionDefinition optionDefinition = new OptionDefinition(i, opt, x.getType());
                        optionDefinitionList.add(optionDefinition);
                    }
                    CmdDefinition cmdDef = new CmdDefinition();
                    cmdDef.setOptionDefinitionList(optionDefinitionList);

                    MiraiCmdDefine def = method.getAnnotation(MiraiCmdDefine.class);
                    cmdDef.setName(def.name());
                    cmdDef.setCmdBean(bean);
                    cmdDef.setCmdExecuteMethod(method);
                    cmdDef.setParseText(def.parseText());
                    cmdDef.setScopeList(Arrays.asList(def.scope()));

                    return cmdDef;
                })
                .collect(Collectors.toMap(CmdDefinition::getName, x -> x, (a, b) -> {
                    throw new RuntimeException("命令定义重复，命令名称：" + a.getName());
                }));

    }

    private void mapMethod2Options(Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            Options options = new Options();

            boolean isMiraiCmdMethod = true;
            for (Parameter parameter : method.getParameters()) {
                MiraiCmdOption miraiCmdOption = parameter.getAnnotation(MiraiCmdOption.class);
                if (miraiCmdOption == null) {
                    log.info("{}类中的{}方法没有标注@MiraiCmdOption，此方法不被认为是需要执行的命令", clazz.getName(), method.getName());
                    isMiraiCmdMethod = false;
                    break;
                }
                options.addOption(miraiCmdOption.name(), miraiCmdOption.hasArg(), miraiCmdOption.desc());
            }
            if (isMiraiCmdMethod) {
                options.addOption("h", false, "显示帮助");
                methodOptionsMap.put(method, options);
            }
        }

    }

    public void doDispatcher(MsgResp msgResp) {
        List<String> cmdList = msgResp.getMessageChain().stream().filter(x -> x instanceof PlainTextMsg)
                .map(x -> (PlainTextMsg) x)
                .map(PlainTextMsg::getText)
                .collect(Collectors.toList());
        if (cmdList.isEmpty()) {
            log.debug("没有发现命令");
            return;
        }
//        Method method = findMethod();
        //解析参数和命令
        String cmdStr = cmdList.get(0);
        List<CmdDefinition> cmdDefList = new ArrayList<>();
        if (cmdStr.startsWith(appConfig.getCmdPrefix())) {
            String cmdWithoutPrefix = cmdStr.substring(1);
            String[] cmdWithArgs = cmdWithoutPrefix.split("\\s+");
            String cmdName = cmdWithArgs[0];

            CmdDefinition cmdDefinition = cmdDefinitionMap.get(cmdName);
            if (cmdDefinition != null) cmdDefList.add(cmdDefinition);
        } else {
            cmdDefList = cmdDefinitionMap.values().stream().filter(x -> !x.isParseText())
                    .collect(Collectors.toList());
        }
        cmdDefList.forEach(cmdDef -> {
            try {
                cmdDef.execute(cmdStr);
            } catch (Exception e) {
                log.warn("执行命令失败，命令名称={}", cmdDef.getName(), e);
            }
        });

    }


}
