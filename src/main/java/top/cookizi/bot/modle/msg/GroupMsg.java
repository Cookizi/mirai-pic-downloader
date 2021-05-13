package top.cookizi.bot.modle.msg;

import top.cookizi.bot.common.enums.MsgType;
import top.cookizi.bot.modle.resp.MsgResp;

public class GroupMsg extends MsgResp {

    @Override
    public String getType() {
        return MsgType.GROUP_MESSAGE.getType();
    }
}
