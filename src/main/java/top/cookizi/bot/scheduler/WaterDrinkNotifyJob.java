package top.cookizi.bot.scheduler;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.cookizi.bot.cache.CommonCache;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.manage.mirai.MiraiApiClient;
import top.cookizi.bot.modle.msg.ImgMsg;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.GroupInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
    private final String[] does = new String[]{"喝", "提"};
    private final String[] things = new String[]{"肛", "水"};



    @Scheduled(cron = "0 0 9-20 * * ?")
    public void rinkNotify() {
        final Random random = new Random(System.currentTimeMillis());
        var sessionKey = Map.<String, Object>of("sessionKey", CommonCache.getSession());
        var groupList = appConfig.getJobWhiteGroup();
        if (groupList.isEmpty()) {
            groupList = miraiApiClient.getGroupList(sessionKey)
                    .stream().map(GroupInfo::getId)
                    .collect(Collectors.toList());
        }
        var api = picType[random.nextInt(picType.length)];
        String url = "http://shibe.online/api/";

        var msg = Msg.MsgBuilder.newBuilder()
                .append(new PlainTextMsg(
                        String.format("%s%s小助手提醒您：该%s%s了。",
                                does[random.nextInt(does.length)],
                                things[random.nextInt(things.length)],
                                does[random.nextInt(does.length)],
                                things[random.nextInt(things.length)]
                        )));
//                .append(new PlainTextMsg("Job测试"))
        ;
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

        }

        groupList.forEach(id -> miraiApiClient.sendGroupMessage(CommonCache.getSession(), id, msg.build()));

    }
}
