package top.cookizi.bot.listener;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import top.cookizi.bot.common.constant.MemoryConst;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.dispatcher.MiraiCmdDispatcher;
import top.cookizi.bot.modle.resp.MsgResp;
import top.cookizi.bot.service.MsgHandleService;
import top.cookizi.bot.service.MiraiApiService;

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
    private MsgHandleService msgHandleService;
    @Autowired
    private MiraiCmdDispatcher miraiCmdDispatcher;


    @Autowired
    @Qualifier("goodGson")
    private Gson goodGson;

    public MiraiWebSocketListener() {
        super();
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        log.info("已与Mirai创建连接");
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
        miraiApiService.releaseSession(MemoryConst.getSession());
        log.info("重新获取session");
        String session = miraiApiService.enableWebsocket();
        MemoryConst.setSession(session);

        log.info("开始尝试重新连接");
        Request request = new Request.Builder()
                .url(appConfig.getWsUrl() + session)
                .build();
        okHttpClient.newWebSocket(request, this);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        log.info("收到原始消息：{}", text);
        MsgResp msgResp = goodGson.fromJson(text, MsgResp.class);
//        msgHandleService.messageHandle(msgResp, sessionKey);
        miraiCmdDispatcher.doDispatcher(msgResp);
        log.info("消息处理完成");
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }


}