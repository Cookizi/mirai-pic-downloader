package top.cookizi.bot;

//import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import top.cookizi.bot.config.AppConfig;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({AppConfig.class})
//@NacosPropertySource(dataId = "example", autoRefreshed = true)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}