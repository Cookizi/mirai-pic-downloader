package top.cookizi.saver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import top.cookizi.saver.config.AppConfig;
import top.cookizi.saver.service.MiraiFriendImgListenerService;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({AppConfig.class})
public class Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        MiraiFriendImgListenerService bean = context.getBean(MiraiFriendImgListenerService.class);
        bean.connect();

    }

}