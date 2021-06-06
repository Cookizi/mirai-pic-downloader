package top.cookizi.bot.service;

import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdArg;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdOption;
import top.cookizi.bot.dispatcher.config.CommandScope;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;

import java.util.ArrayList;
import java.util.List;

@MiraiCmd
public class RepeatCmd {

    @MiraiCmdDefine(name = "复读",scope = CommandScope.FRIEND)
    public List<Msg> repeat(@MiraiCmdOption(name = "c", desc = "重复次数") int repeatCount,
                            @MiraiCmdArg(name = "content") String content) {
        String msg;
        if (repeatCount < 0) msg = "你逗我玩呢？";
        else if (repeatCount > 10) msg = "复读次数太多";
        else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < repeatCount; i++) {
                builder.append(content);
            }
            msg = builder.toString();
        }
        return Msg.MsgBuilder.newBuilder()
                .append(new PlainTextMsg(msg))
                .build();
    }
}
