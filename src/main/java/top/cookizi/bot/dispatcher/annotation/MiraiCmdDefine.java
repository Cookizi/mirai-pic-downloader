package top.cookizi.bot.dispatcher.annotation;


import top.cookizi.bot.dispatcher.config.CommandScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 命令定义
 *
 * 这个注解定义了各种命令的格式，通过扫描注解的内容来来判断需要走到那个方法来执行。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MiraiCmdDefine {
    /**
     * 命令名称，全局不能重复，否则报错
     */
    String name();

    /**
     * 命令类型生效范围，默认所有范围都生效（好友消息和群消息）
     */
    CommandScope[] scope() default {CommandScope.ALL};

    /**
     * 文本内容是否需要解析成命令
     * 如果不需要解析，则直接传输给调用方法来自己解析
     */
    boolean parseText() default true;

    /* *//**
     * 是否使用网络代理
     *//*
    boolean proxy() default false;
*/
}
