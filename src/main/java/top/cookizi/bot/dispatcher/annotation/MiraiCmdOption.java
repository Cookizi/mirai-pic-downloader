package top.cookizi.bot.dispatcher.annotation;


import java.lang.annotation.*;

/**
 * 命令选项
 * <p>
 * ls -a 中的 a
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MiraiCmdOption {
    /**
     * 参数名称
     */
    String name();

    /**
     * 参数作用描述
     */
    String desc() default "";

    /**
     * 是否有参数
     */
    boolean hasArg() default true;

    String defaultValue() default "";

    /**
     * 是否必填
     */
    boolean required() default true;
}
