package top.cookizi.bot.modle.resp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import top.cookizi.bot.common.utils.StringUtils;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImgUploadResp {
    String imageId;
    String url;
    String path;

    public static boolean isValid(ImgUploadResp resp) {
        if (resp == null) return false;

        return StringUtils.isNotBlank(resp.imageId) || StringUtils.isNotBlank(resp.url) || StringUtils.isNotBlank(resp.path);
    }
}
