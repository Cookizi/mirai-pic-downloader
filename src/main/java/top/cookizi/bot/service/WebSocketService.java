package top.cookizi.bot.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.cache.CommonCache;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.listener.MiraiWebSocketListener;

@Slf4j
@Service
public class WebSocketService {

    @Autowired
    public MiraiWebSocketListener webSocketListener;

    @Autowired
    private MiraiApiService miraiApiService;

    @Autowired
    private AppConfig appConfig;

    public synchronized void connect() {
        String session = miraiApiService.enableWebsocket();
        log.info("get session={}", session);
        CommonCache.setSession(session);
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
        Request request = new Request.Builder()
                .url(appConfig.getWsUrl() + session)
                .build();
        WebSocket webSocket = client.newWebSocket(request, webSocketListener);

        try {
            this.wait();
        } catch (InterruptedException ignored) {

        }
    }

}
