package top.cookizi.bot.cmd;

import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.config.CmdType;
import top.cookizi.bot.modle.msg.AppMsg;
import top.cookizi.bot.modle.resp.MsgResp;

import java.util.Optional;

@MiraiCmd
public class AppJumpUrlExtract {

    @MiraiCmdDefine(name = "获取小程序源地址", cmdType = CmdType.LISTENER, special = true)
    public String extract(MsgResp msgResp) {
        Optional<AppMsg> msg = msgResp.getMessageChain().stream().filter(x -> x instanceof AppMsg)
                .map(x -> (AppMsg) x)
                .findFirst();
        if (msg.isPresent()) {
            return msg.get().getJumpUrl();
        } else {
            return "";
        }

    }

}
