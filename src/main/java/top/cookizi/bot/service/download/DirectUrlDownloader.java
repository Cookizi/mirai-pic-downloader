package top.cookizi.bot.service.download;

import org.springframework.stereotype.Service;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.MsgResp;

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
    public String handleFileName(Msg msg) {
        String[] split = ((PlainTextMsg) msg).getText().split("[./]");
        return split[split.length - 2];
    }

    @Override
    public String handleImageUrl(Msg msg) {
        return ((PlainTextMsg) msg).getText();
    }


    @Override
    public boolean isTypeMatch(MsgResp msg) {
        return msg.getMessageChain().stream()
                .filter(x -> x instanceof PlainTextMsg)
                .map(x -> (PlainTextMsg) x)
                .anyMatch(x -> x.getText().matches(".*\\.(jpg|png|gif)"));
    }
}
