package top.cookizi.saver.data.resp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import top.cookizi.saver.data.enums.MsgType;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendMsg extends MsgResp {

    @Override
    public String getType() {
        return MsgType.FRIEND_MESSAGE.type;
    }
}
