package top.cookizi.saver.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.saver.config.AppConfig;
import top.cookizi.saver.config.MiraiWebSocketListener;

@Service
public class MiraiFriendImgListenerService {

    @Autowired
    public MiraiWebSocketListener webSocketListener;

    @Autowired
    private MiraiApiService miraiApiService;

    @Autowired
    private AppConfig appConfig;

    public synchronized void connect() {
        String session = miraiApiService.enableWebsocket();
//        String session = "";

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
