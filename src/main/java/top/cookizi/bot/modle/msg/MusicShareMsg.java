package top.cookizi.bot.modle.msg;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import top.cookizi.bot.common.enums.MsgChainType;

/**
 * @author heq
 * @date 2021/6/7 5:46 下午
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MusicShareMsg extends Msg {

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

    /**
     * 外层显示文案
     */
    String brief ;
    /**
     * 标题
     */
    String title;
    /**
     * 类型 NeteaseCloudMusic
     */
    String kind;

    String summary;

    String jumpUrl;

    String pictureUrl;

    String musicUrl;

    @Override
    public String getType() {
        return MsgChainType.MUSIC_SHARE.type;
    }
}