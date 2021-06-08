package top.cookizi.bot.modle.msg;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import top.cookizi.bot.common.enums.MsgChainType;
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
}
