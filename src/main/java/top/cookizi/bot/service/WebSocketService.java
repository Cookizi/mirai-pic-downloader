package top.cookizi.bot.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.dispatcher.MiraiCmdDispatcher;
import top.cookizi.bot.listener.MiraiWebSocketListener;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class WebSocketService {

    @Autowired
    private AppConfig appConfig;
    @Autowired
    @Qualifier("goodGson")
    private Gson goodGson;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public synchronized void connect(MiraiCmdDispatcher miraiCmdDispatcher) {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
        Request request = new Request.Builder()
                .url(appConfig.getWsUrl())
                .header("verifyKey", appConfig.getAuthKey())
                .header("qq", String.valueOf(appConfig.getQq()))
                .build();
        MiraiWebSocketListener listener = MiraiWebSocketListener
                .newInstance(this, miraiCmdDispatcher, goodGson, threadPoolExecutor);
        client.newWebSocket(request, listener);

        try {
            this.wait();
        } catch (InterruptedException ignored) {

        }
    }

}
