package top.cookizi.saver.data.enums;

import lombok.AllArgsConstructor;
import top.cookizi.saver.data.msg.ImgMsg;
import top.cookizi.saver.data.msg.Msg;
import top.cookizi.saver.data.msg.PlainTextMsg;
import top.cookizi.saver.data.msg.SourceMsg;

@AllArgsConstructor
public enum MsgChainType {
    IMAGE("Image", ImgMsg.class),
    PLAIN("Plain", PlainTextMsg.class),
    SOURCE("Source", SourceMsg.class),
    ;
    public final String type;
    public final Class<? extends Msg> clazz;
}
