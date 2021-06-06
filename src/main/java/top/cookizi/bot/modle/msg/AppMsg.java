package top.cookizi.bot.modle.msg;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import top.cookizi.bot.common.enums.MsgChainType;
import top.cookizi.bot.modle.msg.appMsg.AppContent;

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
    public String getJumpUrl(){
        Gson gson = new Gson();
        AppContent appContent = gson.fromJson(content, AppContent.class);
        return appContent.getMeta().getNews().getJumpUrl();
    }
}
