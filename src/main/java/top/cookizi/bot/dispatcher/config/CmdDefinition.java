package top.cookizi.bot.dispatcher.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.cli.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 命令定义的一些参数
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CmdDefinition {
    //需要自行具体逻辑的类的bean
    Object cmdBean;
    Method cmdExecuteMethod;
    String name;
    boolean parseText;
    //可自行范围，默认是全都可以
    List<CommandScope> scopeList = new ArrayList<>();
    //命令包含的参数，
    List<OptionDefinition> optionDefinitionList = new ArrayList<>();

    //springmvc自带的类型转换器，基本类型转换足够了
    private ConversionService conversionService = DefaultConversionService.getSharedInstance();

    //执行命令
    public void execute(String cmd) throws Exception {
        //不需要解析命令的直接执行
        if (!parseText) {
            cmdExecuteMethod.invoke(cmdBean, cmd);
            return;
        }
        CommandLine cmdLine = parseCmd(cmd);

        optionDefinitionList.sort(Comparator.comparingInt(OptionDefinition::getOrder));
        int paramCount = optionDefinitionList.size();

        Object[] cmdParamValues = new Object[paramCount];
        for (int i = 0; i < paramCount; i++) {
            OptionDefinition optDef = optionDefinitionList.get(i);
            String optionValue = cmdLine.getOptionValue(optDef.getName());
            Object paramValue = conversionService.convert(optionValue, optDef.getParamClass());
            cmdParamValues[i] = paramValue;
        }
        cmdExecuteMethod.invoke(cmdBean, cmdParamValues);

    }

    //解析命令
    private CommandLine parseCmd(String cmd) throws ParseException {
        String[] argsWithCmd = cmd.split("\\s+");
        String[] args = new String[]{};
        System.arraycopy(argsWithCmd, 1, args, 0, argsWithCmd.length - 2);
        Options options = new Options();
        optionDefinitionList.forEach(x -> options.addOption(x.getName(), x.isHasArg(), x.getDesc()));
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);

    }
}
