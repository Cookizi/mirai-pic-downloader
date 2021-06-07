package top.cookizi.bot.modle.resp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImgUploadResp {

    String url;

    String imageId;

    String path;
}
