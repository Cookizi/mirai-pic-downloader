package top.cookizi.bot.dispatcher.annotation;

import java.lang.annotation.*;

/**
 * 命令参数
 *
 * git tag xxx中的tag和xxx
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MiraiCmdArg {

    //参数名称
    String name() ;

    //等同于name()
//    String value() ;

}
