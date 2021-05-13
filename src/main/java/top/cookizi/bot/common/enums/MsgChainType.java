package top.cookizi.bot.common.enums;

import lombok.AllArgsConstructor;
import top.cookizi.bot.modle.msg.ImgMsg;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.msg.SourceMsg;

@AllArgsConstructor
public enum MsgChainType {
    IMAGE("Image", ImgMsg.class),
    PLAIN("Plain", PlainTextMsg.class),
    SOURCE("Source", SourceMsg.class),
    ;
    public final String type;
    public final Class<? extends Msg> clazz;
}
