package top.cookizi.bot.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.cookizi.bot.modle.msg.FriendMsg;
import top.cookizi.bot.modle.msg.GroupMsg;
import top.cookizi.bot.modle.resp.MsgResp;
import top.cookizi.bot.common.utils.StringUtils;

@Getter
@AllArgsConstructor
public enum MsgType {

    /**
     * 默认异常
     */
    NON("null", null),

    GROUP_MESSAGE("GroupMessage", GroupMsg.class),
    FRIEND_MESSAGE("FriendMessage", FriendMsg.class),
    ;

    private final String type;

    private final Class<? extends MsgResp> clazz;

    public static MsgType parse(String type) {
        if (StringUtils.isBlank(type)) {
            return NON;
        }
        for (MsgType value : MsgType.values()) {
            if (value.type.equals(type)) return value;
        }
        return NON;
    }
}

