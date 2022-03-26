package top.cookizi.bot.service.download;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import top.cookizi.bot.common.utils.ImageUtil;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.manage.mirai.MiraiApiClient;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.resp.MsgResp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public abstract class AbstractDownloadService {

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

    public boolean isProxy() {
        return false;
    }

    public String getSource(Msg msg) {
        return null;
    }


    public List<String> download(List<Msg> imgMsgList) throws Exception {
        log.info("收到【{}】条图片消息，开始下载", imgMsgList.size());

        List<Future<String>> futureList = imgMsgList.stream()
                .flatMap(msg -> handleImageUrl(msg).stream()
                        .map(url -> threadPool.submit(() -> download(url, handleFileName(msg, url), getSource(msg))))
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
    public String download(String url, String name, String source) throws Exception {
        Request request = new Request.Builder().get().url(url).build();

        OkHttpClient client = isProxy() ? proxyClient : normalClient;
        log.info("开始下载图片，地址：{}", url);

        var respBytes = Objects.requireNonNull(client.newCall(request).execute().body()).bytes();
        return ImageUtil.write(respBytes, url, name, appConfig.getSavePath(), source);
    }
}
