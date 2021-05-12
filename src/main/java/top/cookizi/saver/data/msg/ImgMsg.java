package top.cookizi.saver.data.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.cookizi.saver.data.enums.MsgChainType;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ImgMsg extends Msg {
    String url;
    //{83BECB58-442E-E948-3BF3-8B681DD7EB6F}.jpg
    String imageId;
    //不知道是啥
    String path;


    public ImgMsg(String url) {
        this.url = url;
    }

    @Override
    public String getType() {
        return MsgChainType.IMAGE.type;
    }
}
