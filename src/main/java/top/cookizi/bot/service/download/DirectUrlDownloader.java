package top.cookizi.bot.service.download;

import org.springframework.stereotype.Service;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.MsgResp;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectUrlDownloader extends AbstractDownloadService {
    @Override
    public List<Msg> handleMsg(MsgResp msg) {
        return msg.getMessageChain().stream().filter(x -> x instanceof PlainTextMsg &&
                ((PlainTextMsg) x).getText().matches(".*\\.(jpg|png|gif)"))
                .collect(Collectors.toList());
    }

    @Override
    public String handleFileName(Msg msg, String url) {
        String[] split = ((PlainTextMsg) msg).getText().split("[./]");
        return split[split.length - 2];
    }

    @Override
    public List<String> handleImageUrl(Msg msg) {
        return Collections.singletonList(((PlainTextMsg) msg).getText());
    }

    @Override
    public boolean isProxy() {
        return false;
    }
}
