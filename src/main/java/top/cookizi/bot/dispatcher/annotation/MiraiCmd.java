package top.cookizi.bot.dispatcher.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 命令注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MiraiCmd {


}
