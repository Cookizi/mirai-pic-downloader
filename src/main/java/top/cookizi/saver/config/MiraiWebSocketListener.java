package top.cookizi.saver.config;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import top.cookizi.saver.data.enums.MsgType;
import top.cookizi.saver.data.resp.MsgResp;
import top.cookizi.saver.service.MiraiApiService;
import top.cookizi.saver.service.download.AbstractDownloadService;

import java.util.List;

@Slf4j
@Component
public class MiraiWebSocketListener extends WebSocketListener {

    @Autowired
    private AppConfig appConfig;
    @Autowired
    @Qualifier("normalClient")
    private OkHttpClient okHttpClient;
    @Autowired
    private MiraiApiService miraiApiService;
    @Autowired
    private List<AbstractDownloadService> downloadServiceList;

    @Autowired
    @Qualifier("goodGson")
    private Gson goodGson;

    public MiraiWebSocketListener() {
        super();
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.info("已经与mirai断开连接");
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.info("正在与mirai断开连接");
    }


    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        log.warn("发生错误，与mirai断开连接，异常信息:{}", t.getMessage(), t);
        log.info("释放原有session");
        String sessionKey = webSocket.request().url().queryParameter("sessionKey");
        miraiApiService.releaseSession(sessionKey);
        log.info("重新获取session");
        String session = miraiApiService.enableWebsocket();

        log.info("开始尝试重新连接");
        Request request = new Request.Builder()
                .url(appConfig.getWsUrl() + session)
                .build();
        okHttpClient.newWebSocket(request, this);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        String sessionKey = webSocket.request().url().queryParameter("sessionKey");
        MsgResp msgResp = goodGson.fromJson(text, MsgResp.class);
        if (MsgType.GROUP_MESSAGE.type.equals(msgResp.getType())) {
            return;
        }
        log.debug("收到原始消息：{}", text);
        downloadServiceList.stream()
                .filter(x -> x.isTypeMatch(msgResp))
                .forEach(x -> x.download(msgResp, sessionKey));
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        log.info("已与Mirai创建连接");
    }


}