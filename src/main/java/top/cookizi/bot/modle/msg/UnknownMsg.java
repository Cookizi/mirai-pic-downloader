package top.cookizi.bot.modle.msg;

import top.cookizi.bot.common.enums.MsgChainType;

/**
 * @author heq
 * @date 2021/5/14 2:25 下午
 * @description
 */
public class UnknownMsg extends Msg {

    @Override
    public String getType() {
        return MsgChainType.NON.type;
    }
}
