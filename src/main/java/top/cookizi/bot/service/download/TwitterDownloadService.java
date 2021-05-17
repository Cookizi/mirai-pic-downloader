package top.cookizi.bot.service.download;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.MsgResp;
import top.cookizi.bot.service.download.twitter.TwitterAPI;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TwitterDownloadService extends AbstractDownloadService {
    @Autowired
    private TwitterAPI twitterAPI;

    @Override
    public List<Msg> handleMsg(MsgResp msg) {
        return msg.getMessageChain().stream().filter(x -> x instanceof PlainTextMsg)
                .map(x -> ((PlainTextMsg) x))
                .filter(x -> x.getText() != null && x.getText().matches("https://twitter\\.com/.+/status/\\d+.*"))
                .collect(Collectors.toList());

    }

    @Override
    public String handleFileName(Msg msg, String url) {
        String[] split = url.split("[./]");
        return split[split.length - 2];
    }

    @Override
    public List<String> handleImageUrl(Msg msg) {
        String text = ((PlainTextMsg) msg).getText();

       return twitterAPI.tweet(text);

    }

    @Override
    public boolean isProxy() {
        return true;
    }

}

