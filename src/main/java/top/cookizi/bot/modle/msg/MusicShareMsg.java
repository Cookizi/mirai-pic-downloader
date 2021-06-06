package top.cookizi.bot.modle.msg;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MusicShareMsg extends Msg {
    @Override
    public String getType() {
        return "MusicShare";
    }

    String kind = "NeteaseCloudMusic";
    String title = "色图";
    String summary = "很多色图";
    String jumpUrl = "https://www.baidu.com";
    String pictureUrl = "http://p4.music.126.net/GpsgjHB_9XgtrBVXt8XX4w==/93458488373078.jpg";
    String musicUrl = "http://music.163.com/song/media/outer/url?id=280761&userid=52707509";
    String brief = "[分享]色图";

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
}
