package top.cookizi.bot.dispatcher.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdOption;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OptionDefinition {
    //参数的顺序，反射中参数需要按照顺序来执行
    int order;
    String name;
    String desc;
    Class<?> paramClass;
    boolean hasArg;
    public OptionDefinition(int order,MiraiCmdOption opt,Class<?> paramClass) {
        this.order = order;
        this.name = opt.name();
        this.desc = opt.desc();
        this.hasArg = opt.hasArg();
        this.paramClass = paramClass;
    }
}
