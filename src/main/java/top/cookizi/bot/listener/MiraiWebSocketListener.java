package top.cookizi.bot.listener;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cookizi.bot.common.constant.MemoryConst;
import top.cookizi.bot.dispatcher.MiraiCmdDispatcher;
import top.cookizi.bot.modle.resp.MsgResp;
import top.cookizi.bot.service.WebSocketService;

import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class MiraiWebSocketListener extends WebSocketListener {

    private WebSocketService webSocketService;
    private MiraiCmdDispatcher miraiCmdDispatcher;
    private Gson goodGson;
    private ThreadPoolExecutor threadPoolExecutor;

    public static MiraiWebSocketListener newInstance(WebSocketService webSocketService,
                                                     MiraiCmdDispatcher miraiCmdDispatcher,
                                                     Gson goodGson,
                                                     ThreadPoolExecutor threadPoolExecutor) {

        MiraiWebSocketListener listener = new MiraiWebSocketListener();
        listener.webSocketService = webSocketService;
        listener.miraiCmdDispatcher = miraiCmdDispatcher;
        listener.goodGson = goodGson;
        listener.threadPoolExecutor = threadPoolExecutor;
        return listener;
    }

    private MiraiWebSocketListener() {
    }

    private long SLEEP_INTERVAL_SEC = 1;

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        SLEEP_INTERVAL_SEC = 1;
        log.info("已与Mirai创建连接，响应：{}", response);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.info("已经与mirai断开连接，原因：{}", reason);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.info("正在与mirai断开连接，原因：{}", reason);
    }


    @Override
    @SneakyThrows
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        log.warn("发生错误，与mirai断开连接，异常信息:{}", t.getMessage(), t);

        log.info("等待{}秒后开始尝试重新连接", ++SLEEP_INTERVAL_SEC);
        Thread.sleep(SLEEP_INTERVAL_SEC * 1000);
        webSocketService.connect(miraiCmdDispatcher);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        log.info("收到原始消息：{}", text);
        var jsonObject = goodGson.fromJson(text, JsonObject.class);
        var data = jsonObject.get("data").getAsJsonObject();
        var code = Optional.ofNullable(data.get("code")).map(JsonElement::getAsBigInteger).map(BigInteger::intValue).orElse(null);
        if (code != null && code == 0) {
            var session = data.get("session").getAsString();
            log.info("获取session = {}", session);
            MemoryConst.setSession(session);
            return;
        }
        MsgResp msgResp = goodGson.fromJson(data, MsgResp.class);
//        msgHandleService.messageHandle(msgResp, sessionKey);
        threadPoolExecutor.execute(() -> miraiCmdDispatcher.doDispatcher(msgResp));
//        miraiCmdDispatcher.doDispatcher(msgResp);
        log.info("消息处理完成");
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }


}