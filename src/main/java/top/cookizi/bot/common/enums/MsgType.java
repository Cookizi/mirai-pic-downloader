package top.cookizi.bot.common.enums;

import lombok.AllArgsConstructor;
import top.cookizi.bot.common.utils.StringUtils;
import top.cookizi.bot.dispatcher.config.CommandScope;
import top.cookizi.bot.modle.resp.FriendMsg;
import top.cookizi.bot.modle.resp.GroupMsg;
import top.cookizi.bot.modle.resp.MsgResp;

@AllArgsConstructor
public enum MsgType {

    /**
     * 默认异常
     */
    NON("null", MsgResp.class, null),

    GROUP_MESSAGE("GroupMessage", GroupMsg.class, CommandScope.GROUP),
    FRIEND_MESSAGE("FriendMessage", FriendMsg.class, CommandScope.FRIEND),
    ;

    public final String type;

    public final Class<? extends MsgResp> clazz;
    public final CommandScope scope;

    public static MsgType parse(String type) {
        if (StringUtils.isBlank(type)) {
            return NON;
        }
        for (MsgType value : MsgType.values()) {
            if (value.type.equals(type)) return value;
        }
        return NON;
    }

    public static MsgType parse(Class<? extends MsgResp> clazz) {
        if (clazz == null) {
            return NON;
        }
        for (MsgType value : MsgType.values()) {
            if (value.clazz.equals(clazz)) return value;
        }
        return NON;
    }
}

