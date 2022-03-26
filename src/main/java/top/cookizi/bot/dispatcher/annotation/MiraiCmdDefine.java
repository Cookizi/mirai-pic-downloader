package top.cookizi.bot.dispatcher.annotation;


import top.cookizi.bot.dispatcher.config.CmdRespType;
import top.cookizi.bot.dispatcher.config.CmdType;
import top.cookizi.bot.dispatcher.config.CommandScope;
import top.cookizi.bot.modle.resp.MsgResp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 命令定义
 * <p>
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
     * 是否需要返回响应，配合返回类型实现不同类型的返回响应信息
     */
    boolean isResponse() default true;

    /**
     * 处理之后返回的类型
     */
    CmdRespType respType() default CmdRespType.QUOTE;


    /**
     * 命令类型，可以是监听或者普通命令
     * 如果是监听,参数有且只能是{@link MsgResp}
     */
    CmdType cmdType() default CmdType.NORMAL;

    /**
     * 简短描述一下功能，获取命令列表的时候可以用
     */
    String desc() default "";

    /**
     * 用来标记一些需要特殊处理的命令或者监听
     * <p>
     * 现阶段命令都是文本形式的，但是出现了需要监听app格式的功能，
     * 先暂时用这种方法来标记一下，以后看看是否有更好的方法来解决
     */
    boolean special() default false;
}
