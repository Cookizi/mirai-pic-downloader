package top.cookizi.bot.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import top.cookizi.bot.dispatcher.annotation.MiraiCmd;

import java.util.Map;

@Component
public class CmdHandler implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Map<String, Object> miraiCmdMap = context.getBeansWithAnnotation(MiraiCmd.class);

    }
}
