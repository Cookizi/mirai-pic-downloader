package top.cookizi.bot.common.enums;

import lombok.AllArgsConstructor;
import top.cookizi.bot.common.utils.StringUtils;
import top.cookizi.bot.modle.msg.*;
import top.cookizi.bot.modle.msg.AppMsg;

@AllArgsConstructor
public enum MsgChainType {
    /**
     * 默认异常
     */
    NON("null", UnknownMsg.class),

    APP("App", AppMsg.class),
    IMAGE("Image", ImgMsg.class),
    PLAIN("Plain", PlainTextMsg.class),
    SOURCE("Source", SourceMsg.class),
    XML("Xml", XmlMsg.class),
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
