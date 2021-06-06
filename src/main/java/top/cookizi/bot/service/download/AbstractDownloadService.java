package top.cookizi.bot.service.download;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import top.cookizi.bot.common.enums.ImgSuffixType;
import top.cookizi.bot.common.enums.RespType;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.manage.mirai.MiraiApiClient;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.MsgResp;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public abstract class AbstractDownloadService {

    protected static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .callTimeout(Duration.ofMillis(Integer.MAX_VALUE))
            .connectTimeout(Duration.ofMillis(Integer.MAX_VALUE))
            .connectionPool(new ConnectionPool())
            .build();

    @Autowired
    protected AppConfig appConfig;
    @Autowired
    protected MiraiApiClient miraiApiClient;
    @Autowired
    private ThreadPoolExecutor threadPool;
    @Autowired
    @Qualifier("normalClient")
    protected OkHttpClient normalClient;
    @Autowired
    @Qualifier("proxyClient")
    protected OkHttpClient proxyClient;


    /**
     * 过滤出来需要的消息
     */
    public abstract List<Msg> handleMsg(MsgResp msg);

    /**
     * 从消息或者图片地址中获取到文件名
     */
    public abstract String handleFileName(Msg msg, String url);

    public abstract List<String> handleImageUrl(Msg msg);

    public abstract boolean isProxy();


    public List<String> download(List<Msg> imgMsgList) throws Exception {
        log.info("收到【{}】条图片消息", imgMsgList.size());

        List<Future<String>> futureList = imgMsgList.stream()
                .flatMap(msg -> handleImageUrl(msg).stream()
                        .map(url -> threadPool.submit(() -> download(url, handleFileName(msg,url))))
                ).collect(Collectors.toList());

        List<String> result = new ArrayList<>();
        for (Future<String> future : futureList) {
            result.add(future.get());
        }
        return result;
    }


    /**
     * 下载图片，返回图片名称
     */
    private String download(String url, String name) throws IOException {
        Request request = new Request.Builder()
                .get().url(url)
                .build();

        Msg.MsgBuilder msgBuilder = Msg.MsgBuilder.newBuilder();
        OkHttpClient client = isProxy() ? proxyClient : normalClient;

        byte[] respStream = Objects.requireNonNull(client.newCall(request).execute().body()).bytes();
        ImgSuffixType type = ImgSuffixType.checkType(respStream);
        if (type == ImgSuffixType.OTHER) {
            log.warn("unknown image type,filename={}", name);
        }
        String filename = type.getName(name);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(appConfig.getSavePath() + filename));

        outputStream.write(respStream);
        outputStream.flush();
        outputStream.close();
        msgBuilder.append(new PlainTextMsg(RespType.DOWNLOAD_SUCCESS.respText(filename)));

        return filename;
    }
}
