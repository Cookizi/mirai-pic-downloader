package top.cookizi.bot.modle.msg;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import top.cookizi.bot.common.enums.MsgChainType;

/**
 * @author heq
 * @date 2021/6/7 5:30 下午
 * @description
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlMsg extends Msg {

    /*
     <?xml version=\"1.0\" encoding=\"utf-8\"?>
     <msg templateID=\"12345\" brief=\"[图片]\" serviceID=\"5\">
         <item layout=\"0\">
             <image md5=\"A2CAA7A4DE5F2B32E32B2CCBFFBA2D46\" uuid=\"{A2CAA7A4-DE5F-2B32-E32B-2CCBFFBA2D46}.jpg\"/>
         </item>
         <source/>
     </msg>
    */
    String xml;

    @Override
    public String getType() {
        return MsgChainType.XML.type;
    }
}
