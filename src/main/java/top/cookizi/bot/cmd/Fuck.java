package top.cookizi.bot.cmd;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.config.CommandScope;

import java.io.IOException;

@Slf4j
@MiraiCmd
public class Fuck {

    @MiraiCmdDefine(name = "fuck", scope = CommandScope.GROUP, desc = "祖安bot")
    public String fuck() throws IOException {
        String url = "https://zuanbot.com/api.php?level=min&lang=zh_cn";
        Connection.Response response = Jsoup.connect(url)
                .ignoreContentType(true)
                .timeout(10 * 1000)
                .execute();
        return response.body();
    }
}
