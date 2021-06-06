package top.cookizi.bot.config;


import feign.Feign;
import feign.form.FormEncoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.cookizi.bot.manage.mirai.MiraiApiClient;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@Component
@Configuration
public class FeignClientConfig {

    @Autowired
    private AppConfig appConfig;

    @Bean
    public MiraiApiClient miraiApiClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool())
                .connectTimeout(10, TimeUnit.SECONDS);
        if (appConfig.isProxy()) {
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(appConfig.getProxyHost(), appConfig.getProxyPort())));
        }

        return new Feign.Builder()
                .client(new feign.okhttp.OkHttpClient(builder.build()))
                .encoder(new FormEncoder(new GsonEncoder()))
                .decoder(new GsonDecoder())
                .target(MiraiApiClient.class, appConfig.getApiUrl());
    }

}


