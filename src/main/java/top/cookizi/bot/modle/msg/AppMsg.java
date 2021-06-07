package top.cookizi.bot.modle.msg;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import top.cookizi.bot.common.enums.MsgChainType;
import top.cookizi.bot.common.utils.JsonUtils;

/**
 * 分享的小程序
 */
@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppMsg extends Msg {

    String content;

    @Override
    public String getType() {
        return MsgChainType.APP.type;
    }

    public AppContent getAppContent(){
        return JsonUtils.parseObject(content, AppContent.class);
    }

    public String getJumpUrl() {
        return getAppContent().getMeta().getNews().getJumpUrl();
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AppContent {
        String app;
        Config config;
        String desc;
        Extra extra;
        Meta meta;
        String prompt;
        String ver;
        String view;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Config {
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Extra {
        int app_type;
        long appid;
        long uin;//QQ号
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Meta {
        News news;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class News {
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
}
