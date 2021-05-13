package top.cookizi.bot.service.download;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.common.enums.ImgSuffixType;
import top.cookizi.bot.common.enums.RespType;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.MsgResp;
import top.cookizi.bot.service.download.afterDownload.AbstractAfterDownloadService;
import top.cookizi.bot.manage.mirai.MiraiApiClient;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
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
    private List<AbstractAfterDownloadService> afterDownloadServiceList;


    public abstract List<Msg> handleMsg(MsgResp msg);

    public abstract String handleFileName(Msg msg);

    public abstract String handleImageUrl(Msg msg);

    public abstract boolean isTypeMatch(MsgResp msg);


    public void download(MsgResp msg, String sessionKey) {
        List<Msg> imgMsgList = handleMsg(msg);
        log.info("收到【{}】条图片消息", imgMsgList.size());
        imgMsgList.parallelStream().forEach(x -> download(handleImageUrl(x), handleFileName(x), msg.getSender().getId(), sessionKey));

        afterDownloadServiceList.parallelStream().forEach(x -> x.process(sessionKey, imgMsgList.stream().map(this::handleImageUrl).collect(Collectors.toList())));

    }


    private void download(String url, String name, long sender, String sessionKey) {
        Request request = new Request.Builder()
                .get().url(url)
                .build();


        Msg.MsgBuilder msgBuilder = Msg.MsgBuilder.newBuilder();
        String filename = null;
        try {
            byte[] respStream = Objects.requireNonNull(httpClient.newCall(request).execute().body()).bytes();
            ImgSuffixType type = ImgSuffixType.checkType(respStream);
            if (type == ImgSuffixType.OTHER) {
                log.warn("unknown image type,filename={}", name);
            }
            filename = type.getName(name);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(appConfig.getSavePath() + filename));

            outputStream.write(respStream);
            outputStream.flush();
            outputStream.close();
            msgBuilder.append(new PlainTextMsg(RespType.DOWNLOAD_SUCCESS.respText(filename)));
        } catch (IOException e) {
            msgBuilder.append(new PlainTextMsg(RespType.DOWNLOAD_FAIL.respText(filename, url)));
        }

        miraiApiClient.sendFriendMessage(sessionKey, sender, msgBuilder.build());
    }
}
