package top.cookizi.bot.dispatcher.annotation;

import java.lang.annotation.*;

/**
 * 命令参数
 * <p>
 * git tag xxx中的tag和xxx
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MiraiCmdArg {

    /**
     * 参数名称，可以不赋值，取值按照参数里面的顺序来，如果需要指定名称请使用{@link MiraiCmdOption}
     */
    String name() default "";

    /**
     * 如果不是必填，这个参数会跳过。
     * 参数设置格式和给定的参数数量不一致的话，会报错
     */
    boolean required() default true;


}
