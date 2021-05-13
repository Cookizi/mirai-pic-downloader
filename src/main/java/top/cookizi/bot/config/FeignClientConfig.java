package top.cookizi.bot.config;


import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import top.cookizi.bot.manage.mirai.MiraiApiClient;

@Component
@Configuration
@PropertySource("classpath:application.properties")
public class FeignClientConfig {

    @Autowired
    private AppConfig appConfig;
    @Value("${bot.api-url}")
    String apiUrl;

    @Value("${bot.authKey}")
    String authKey;

    @Bean
    public MiraiApiClient miraiApiClient() {
        return new Feign.Builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(MiraiApiClient.class, appConfig.getApiUrl());
    }

}


