package top.cookizi.bot.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.listener.MiraiWebSocketListener;

@Slf4j
@Service
public class WebSocketService {

    @Autowired
    public MiraiWebSocketListener webSocketListener;

    @Autowired
    private AppConfig appConfig;

    public synchronized void connect() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
        Request request = new Request.Builder()
                .url(appConfig.getWsUrl())
                .header("verifyKey", appConfig.getAuthKey())
                .header("qq", String.valueOf(appConfig.getQq()))
                .build();
        client.newWebSocket(request, webSocketListener);

        try {
            this.wait();
        } catch (InterruptedException ignored) {

        }
    }

}
