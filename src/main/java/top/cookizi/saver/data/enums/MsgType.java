package top.cookizi.saver.data.enums;

import lombok.AllArgsConstructor;
import top.cookizi.saver.data.resp.FriendMsg;
import top.cookizi.saver.data.resp.GroupMsg;
import top.cookizi.saver.data.resp.MsgResp;


@AllArgsConstructor
public enum MsgType {
    GROUP_MESSAGE("GroupMessage", GroupMsg.class),
    FRIEND_MESSAGE("FriendMessage", FriendMsg.class),
    ;

    public final String type;

    public final Class<? extends MsgResp> clazz;

    public static MsgType parse(String type) {
        for (MsgType value : MsgType.values()) {
            if(value.type.equals(type)) return value;
        }
        throw new RuntimeException("未知消息类型：" + type);
    }
}

