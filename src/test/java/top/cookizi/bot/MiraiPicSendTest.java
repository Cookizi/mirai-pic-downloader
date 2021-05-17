package top.cookizi.bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.manage.mirai.MiraiApiClient;
import top.cookizi.bot.modle.msg.ImgMsg;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.MusicShareMsg;
import top.cookizi.bot.modle.resp.AuthResp;
import top.cookizi.bot.modle.resp.ImgUploadResp;
import top.cookizi.bot.modle.resp.VerifyResp;

import java.io.File;
import java.util.List;
import java.util.Map;


@SpringBootTest
public class MiraiPicSendTest {

    @Autowired
    private MiraiApiClient miraiApiClient;
    @Autowired
    private AppConfig appConfig;

    @Test
    public void testSendPic() {
        AuthResp authKey = miraiApiClient.auth(appConfig.getAuthKey());
        String session = authKey.getSession();
        miraiApiClient.verify(session, appConfig.getQq());

//        String path = "file:////media/cookizi/null/qqBotSavedFile/image/417951F6673C17A6EF2C477045D107B8.jpg";
        String path = "D:\\Desktop\\D706A6EBCDEDE8E.png";
        File file = new File(path);
        ImgUploadResp friend = miraiApiClient.uploadImage(session, "group", file);
        System.out.println(friend);

//        List<Msg> list = Msg.MsgBuilder.newBuilder().append(new ImgMsg(null,null,path)).build();
//        Map<String, String> friendMessage = miraiApiClient.sendFriendMessage(session, 811452031L, list);
//        System.out.println(friendMessage);

        //{
        //  "type": "MusicShare",
        //  "kind": "NeteaseCloudMusic",
        //  "title": "相见恨晚,",
        //  "summary": "彭佳慧",
        //  "jumpUrl": "https://y.music.163.com/m/song/280761/",
        //  "pictureUrl": "http://p4.music.126.net/GpsgjHB_9XgtrBVXt8XX4w==/93458488373078.jpg",
        //  "musicUrl": "http://music.163.com/song/media/outer/url?id=280761&userid=52707509",
        //  "brief": "[分享]相见恨晚"
        //}
//        miraiApiClient.sendGroupMessage(session,,null)
    }

}
