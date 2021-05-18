package top.cookizi.bot.service.download;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.resp.MsgResp;
import top.cookizi.bot.service.download.twitter.TwitterAPI;

import java.util.List;

@Service
public class TwitterDownloadService extends AbstractDownloadService {
    @Autowired
    private TwitterAPI twitterAPI;

    @Override
    public List<Msg> handleMsg(MsgResp msg) {
        return null;
    }

    @Override
    public String handleFileName(Msg msg) {
        return null;
    }

    @Override
    public String handleImageUrl(Msg msg) {
        return null;
    }

    @Override
    public boolean isTypeMatch(MsgResp msg) {
        return false;
    }

}

