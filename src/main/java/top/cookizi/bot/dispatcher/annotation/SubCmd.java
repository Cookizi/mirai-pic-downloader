package top.cookizi.bot.dispatcher.annotation;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SubCmd {
    String[] names();
}
