package top.cookizi.bot.service.download;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.MsgResp;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class E621DownloadService extends AbstractDownloadService {
    @Override
    public List<Msg> handleMsg(MsgResp msg) {
        return msg.getMessageChain().stream().filter(x -> x instanceof PlainTextMsg)
                .map(x -> ((PlainTextMsg) x))
                .filter(x -> x.getText() != null && x.getText().matches("https://e621\\.net/post/show/\\d+"))
                .collect(Collectors.toList());
    }

    @Override
    public String handleFileName(Msg msg, String url) {
        String[] split = url.split("[./]");
        return split[split.length - 2];
    }

    @Override
    public List<String> handleImageUrl(Msg msg) {
        String url = ((PlainTextMsg) msg).getText();
        try {
            String imgUrl = Jsoup.connect(url)
                    .get().body().getElementById("image-download-link")
                    .getElementsByTag("a")
                    .attr("href");
            return Collections.singletonList(imgUrl);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isProxy() {
        return false;
    }

}
