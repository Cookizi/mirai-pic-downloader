package top.cookizi.bot.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.cookizi.bot.common.utils.StringUtils;
import top.cookizi.bot.modle.msg.ImgMsg;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.msg.SourceMsg;

@AllArgsConstructor
public enum MsgChainType {
    /**
     * 默认异常
     */
    NON("null", null),

    IMAGE("Image", ImgMsg.class),
    PLAIN("Plain", PlainTextMsg.class),
    SOURCE("Source", SourceMsg.class),
    ;
    public final String type;
    public final Class<? extends Msg> clazz;

    public static MsgChainType parse(String type) {
        if (StringUtils.isBlank(type)) {
            return NON;
        }
        for (MsgChainType value : MsgChainType.values()) {
            if (value.type.equals(type)) return value;
        }
        return NON;
    }

    public static MsgChainType parse(Class<? extends Msg> clazz) {
        if (clazz == null) {
            return NON;
        }
        for (MsgChainType value : MsgChainType.values()) {
            if (value.clazz.equals(clazz)) return value;
        }
        return NON;
    }
}
