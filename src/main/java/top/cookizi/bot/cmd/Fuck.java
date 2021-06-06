package top.cookizi.bot.cmd;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdArg;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdOption;
import top.cookizi.bot.dispatcher.config.CommandScope;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@MiraiCmd
public class Fuck {

    @MiraiCmdDefine(name = "fuck", scope = CommandScope.GROUP,desc = "祖安bot")
    public String fuck(@MiraiCmdOption(name = "hard", required = false) String hard) throws IOException {
        String url = "https://zuanbot.com/api.php";
        Map<String, String> param = new HashMap<>();
        param.put("lang", "zh_cn");
        if (hard == null) {
            param.put("level", "mini");
        } else {
            log.info("fuck in hard mode");
        }
        Connection.Response response = Jsoup.connect(url).data(param)
                .ignoreContentType(true)
                .timeout(10 * 1000)
                .execute();
        return response.body();
    }
}
