package top.cookizi.bot.modle.msg;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import top.cookizi.bot.common.enums.MsgType;
import top.cookizi.bot.modle.resp.MsgResp;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendMsg extends MsgResp {

    @Override
    public String getType() {
        return MsgType.FRIEND_MESSAGE.getType();
    }
}
