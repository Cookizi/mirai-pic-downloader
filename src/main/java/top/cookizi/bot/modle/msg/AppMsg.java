package top.cookizi.bot.modle.msg;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import top.cookizi.bot.common.enums.MsgChainType;
import top.cookizi.bot.common.utils.JsonUtils;
import top.cookizi.bot.modle.msg.data.AppContent;

import java.util.Map;

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

    public String getJumpUrl() {
        Gson gson = new Gson();
        AppContent appContent = gson.fromJson(content, AppContent.class);
        Map<String, Object> news = appContent.getMeta().get("news");
        if (news != null) {
            return String.valueOf(news.get("jumpUrl"));
        }else {
            Map<String, Object> map = appContent.getMeta().get("detail_1");
            return String.valueOf(map.get("qqdocurl"));
        }
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
