package top.cookizi.bot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cookizi.bot.config.gsonAdapter.BaseJsonDeserializerAdapter;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class Beans {

    @Autowired
    private List<BaseJsonDeserializerAdapter<?>> adapterList;
    @Autowired
    private AppConfig appConfig;

    @Bean("normalClient")
    public OkHttpClient normalClient() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool())
                .connectTimeout(2, TimeUnit.MINUTES)
                .callTimeout(2, TimeUnit.MINUTES)
                .build();
    }

    @Bean("proxyClient")
    public OkHttpClient proxyClient() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool())
                .callTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(appConfig.getProxyHost(), appConfig.getProxyPort())))
                .build();
    }

    @Bean(name = "goodGson")
    public Gson GoodGson() {
        GsonBuilder builder = new GsonBuilder();
        adapterList.forEach(x -> builder.registerTypeAdapter(x.getClazz(), x));
        return builder.create();
    }

    @Bean
    public ThreadPoolExecutor threadPool() {
        return new ThreadPoolExecutor(1, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }
}
