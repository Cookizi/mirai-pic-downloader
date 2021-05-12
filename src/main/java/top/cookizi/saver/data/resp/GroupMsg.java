package top.cookizi.saver.data.resp;

import top.cookizi.saver.data.enums.MsgType;

public class GroupMsg extends MsgResp {

    @Override
    public String getType() {
        return MsgType.GROUP_MESSAGE.type;
    }
}
