package top.cookizi.bot.scheduler;


import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.cookizi.bot.common.constant.MemoryConst;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.manage.mirai.MiraiApiClient;
import top.cookizi.bot.modle.msg.ImgMsg;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.BaseResponse;
import top.cookizi.bot.modle.resp.GroupInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WaterDrinkNotifyJob {
    @Autowired
    private MiraiApiClient miraiApiClient;

    @Autowired
    private AppConfig appConfig;

    private final String[] picType = new String[]{"shibes", "cats", "birds"};
    private final String[] words = new String[]{"喝水", "提肛", "doi", "手冲", "饮茶", "拉屎", "摸鱼", "吃鸡"};


    @Scheduled(cron = "0 29 9-20 * * ?")
    public void rinkNotify() {
        final Random random = new Random(System.currentTimeMillis());
        var sessionKey = Map.<String, Object>of("sessionKey", MemoryConst.getSession());
        var groupList = appConfig.getJobWhiteGroup();
        if (groupList.isEmpty()) {
            var list = miraiApiClient.getGroupList(sessionKey);
            groupList = Optional.ofNullable(list.getData()).orElse(Lists.newArrayList())
                    .stream().map(GroupInfo::getId)
                    .collect(Collectors.toList());
        }
        var api = picType[random.nextInt(picType.length)];
        String url = "http://shibe.online/api/";

  /*
       try {
            var body = Jsoup.connect(url + api)
                    .data("httpsUrls", "false")
                    .ignoreContentType(true)
                    .execute().body();
            List<String> img = new Gson().fromJson(body, new TypeToken<List<String>>() {
            }.getType());
            if (!img.isEmpty()) {
                msg.append(new ImgMsg(img.get(0)));
            }
        } catch (IOException ignored) {

        }*/
        
        var msg = Msg.MsgBuilder.newBuilder()
                .append(new PlainTextMsg(
                        String.format("%s小助手提醒您：该%s了。",
                                words[random.nextInt(words.length)],
                                words[random.nextInt(words.length)]
                        )));
        groupList.forEach(id -> miraiApiClient.sendGroupMessage(MemoryConst.getSession(), id, msg.build()));

    }
}
