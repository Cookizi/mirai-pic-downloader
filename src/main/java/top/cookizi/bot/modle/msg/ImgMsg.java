package top.cookizi.bot.modle.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.cookizi.bot.common.enums.MsgChainType;
import top.cookizi.bot.modle.resp.ImgUploadResp;

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
        super();
        this.url = url;
    }

    public ImgMsg(ImgUploadResp resp) {
        super();
        this.url = resp.getUrl();
        this.imageId = resp.getImageId();
        this.path = resp.getPath();
    }

    public ImgMsg(String url, String imageId, String path) {
        super();
        this.url = url;
        this.imageId = imageId;
        this.path = path;
    }

    @Override
    public String getType() {
        return MsgChainType.IMAGE.type;
    }
}
