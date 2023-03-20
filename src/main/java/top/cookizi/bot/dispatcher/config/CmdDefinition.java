package top.cookizi.bot.dispatcher.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import top.cookizi.bot.dispatcher.data.CmdExecuteResult;
import top.cookizi.bot.modle.resp.MsgResp;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 命令定义的一些参数
 */
@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CmdDefinition {
    //需要自行具体逻辑的类的bean
    Object cmdBean;
    Method cmdExecuteMethod;
    String name;
    CmdType cmdType;
    //可自行范围，默认是全都可以
    List<CommandScope> scopeList = new ArrayList<>();
    //命令包含的参数，
    List<OptionDefinition> optionDefinitionList;
    List<ArgDefinition> argDefinitionList;
    int paramCount;
    boolean isResponse;
    CmdRespType cmdRespType;
    boolean isSpecial;

    //springmvc自带的类型转换器，基本类型转换足够了
    private ConversionService conversionService = DefaultConversionService.getSharedInstance();

    //执行命令
    public CmdExecuteResult<?> execute(String cmd, MsgResp msgResp) throws Exception {
        //设置为监听状态的命令需要自己解析
        if (cmdType == CmdType.LISTENER) {
            log.debug("开始执行监听命令：{}", this.getName());
            Object result = cmdExecuteMethod.invoke(cmdBean, msgResp);
            if (result instanceof CmdExecuteResult) {
                return (CmdExecuteResult<?>) result;
            } else {
                return CmdExecuteResult.ok(result);
            }
        }
        CommandLine cmdLine = parseCmd(cmd);

        Object[] cmdParamValues = new Object[paramCount];

        List<String> argList = cmdLine.getArgList();
        argDefinitionList.sort(Comparator.comparingInt(ArgDefinition::getOrder));
        if (argList.size() < argDefinitionList.size()) {
            throw new RuntimeException("参数缺失");
        }
        //填充参数
        for (int i = 0; i < argDefinitionList.size(); i++) {
            ArgDefinition argDefinition = argDefinitionList.get(i);
            Object paramValue = conversionService.convert(argList.get(i), argDefinition.getParamClass());
            cmdParamValues[argDefinition.getOrder()] = paramValue;
        }
        //填充option
        optionDefinitionList.sort(Comparator.comparingInt(OptionDefinition::getOrder));
        for (OptionDefinition optDef : optionDefinitionList) {
            String optionValue = cmdLine.getOptionValue(optDef.getName());
            if (optionValue == null) {
                if (optDef.isHasArg()) {
                    throw new RuntimeException("参数缺失");
                }
                continue;
            }
            Object paramValue = conversionService.convert(optionValue, optDef.getParamClass());
            cmdParamValues[optDef.getOrder()] = paramValue;
        }
        log.info("开始执行命令:{}", cmd);
        Object result = cmdExecuteMethod.invoke(cmdBean, cmdParamValues);
        if (result instanceof CmdExecuteResult) {
            return (CmdExecuteResult<?>) result;
        } else {
            return CmdExecuteResult.ok(result);
        }
    }

    //解析命令
    private CommandLine parseCmd(String cmd) throws ParseException {
        String[] argsWithCmd = cmd.split("\\s+");
        String[] args = new String[argsWithCmd.length - 1];
        System.arraycopy(argsWithCmd, 1, args, 0, args.length);
        Options options = new Options();
        for (OptionDefinition optDef : optionDefinitionList) {

            Option opt = new Option(optDef.getName(), optDef.isHasArg(), optDef.getDesc());
            opt.setRequired(optDef.isRequired());
            options.addOption(opt);
        }
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args, true);

    }

    public String cmdInfoText() {
        return "命令名称：" + this.name + "\n" +
                "\t参数：\n\t\t" +
                this.argDefinitionList.stream().map(ArgDefinition::info).collect(Collectors.joining("\n\t\t\t")) +
                "\n\t选项：\n\t\t" +
                this.optionDefinitionList.stream().map(OptionDefinition::info).collect(Collectors.joining("\n\t\t\t"));
    }
}
