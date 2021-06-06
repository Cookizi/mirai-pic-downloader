package top.cookizi.bot.modle.msg;

import top.cookizi.bot.common.enums.MsgChainType;

public class XmlMsg extends Msg{
    @Override
    public String getType() {
        return MsgChainType.XML.type;
    }
    //{"type":"GroupMessage","messageChain":[{"type":"Source","id":826,"time":1621527291},{"type":"Xml","xml":"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<msg templateID=\"12345\" brief=\"[图片]\" serviceID=\"5\"><item layout=\"0\"><image md5=\"A2CAA7A4DE5F2B32E32B2CCBFFBA2D46\" uuid=\"{A2CAA7A4-DE5F-2B32-E32B-2CCBFFBA2D46}.jpg\"/></item><source/></msg>\n"}],"sender":{"id":811452031,"memberName":"■■■■■","permission":"OWNER","group":{"id":1142341316,"name":"bot测试","permission":"MEMBER"}}}
}