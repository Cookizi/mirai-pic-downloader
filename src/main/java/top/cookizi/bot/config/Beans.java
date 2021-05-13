package top.cookizi.bot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cookizi.bot.config.gsonAdapter.BaseJsonDeserializerAdapter;

import java.util.List;
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
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }
 /*   @Bean("proxyClient")
    public OkHttpClient proxyClient() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool())
                .connectTimeout(60, TimeUnit.SECONDS)
                .proxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress(appConfig.getProxyHost(),appConfig.getProxyPort())))
                .build();
    }*/

    @Bean(name = "goodGson")
    public Gson GoodGson() {
        GsonBuilder builder = new GsonBuilder();
        adapterList.forEach(x -> builder.registerTypeAdapter(x.getClazz(), x));
        return builder.create();
    }
}
