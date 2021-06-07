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