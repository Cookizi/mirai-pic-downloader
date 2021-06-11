package top.cookizi.bot.service.download;

import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.msg.XmlMsg;
import top.cookizi.bot.modle.msg.data.XmlContent;
import top.cookizi.bot.modle.resp.MsgResp;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class E621DownloadService extends AbstractDownloadService {
    // 不知道为什么，e621的地址从QQ里面接收到之后变成了xml格式，
    // 这里抽取出来转换成文本
    @Override
    public List<Msg> handleMsg(MsgResp msg) {
        Msg.MsgBuilder builder = Msg.MsgBuilder.newBuilder();
        try {
            for (Msg x : msg.getMessageChain()) {
                if (x instanceof XmlMsg) {
                    XmlMsg xmlMsg = (XmlMsg) x;
                    XmlContent content = xmlMsg.getXmlContent();
                    String url = content.getUrl();
                    builder.append(new PlainTextMsg(url));
                } else if (x instanceof PlainTextMsg) {
                    if (((PlainTextMsg) x).getText().startsWith("https://e621.net")) {
                        builder.append(x);
                    }
                }
            }
        } catch (JAXBException e) {
            log.warn("解析xml失败");
            throw new RuntimeException("解析e621的xml失败", e);
        }
        return builder.build();
    }

    @Override
    public String handleFileName(Msg msg, String url) {
        String[] split = url.split("[./]");
        return split[split.length - 2];
    }

    @Override
    public List<String> handleImageUrl(Msg msg) {
        String url = ((PlainTextMsg) msg).getText();
        if (!url.startsWith("https://e621.net")) {
            return Collections.emptyList();
        }
        try {
            String imgUrl = Jsoup.connect(url)
                    .get().body().getElementById("image-download-link")
                    .getElementsByTag("a")
                    .attr("href");
            return Collections.singletonList(imgUrl);
        } catch (IOException e) {
            log.warn("获取e621图片下载失败", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isProxy() {
        return false;
    }

}
