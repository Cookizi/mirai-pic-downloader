package top.cookizi.bot.modle.msg.appMsg;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class News {
    String action;
    String android_pkg_name;
    int app_type;
    long appid;
    String desc;
    String jumpUrl;
    String preview;
    String source_icon;
    String source_url;
    String tag;
    String title;
}
