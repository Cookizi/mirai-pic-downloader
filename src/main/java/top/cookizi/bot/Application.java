package top.cookizi.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.service.WebSocketService;
import top.cookizi.bot.service.download.TwitterDownloadService;

import java.io.IOException;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({AppConfig.class})
public class Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        WebSocketService bean = context.getBean(WebSocketService.class);
        bean.connect();

    }

}