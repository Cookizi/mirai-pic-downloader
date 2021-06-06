package top.cookizi.bot.cmd;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.config.CommandScope;

import java.io.IOException;

@MiraiCmd
public class Fuck {
    private Connection.Response getBotResp(String url) throws IOException {
        return Jsoup.connect(url)
                .ignoreContentType(true)
                .timeout(10 * 1000)
                .execute();
    }

    @MiraiCmdDefine(name = "祖安", scope = CommandScope.GROUP, desc = "祖安bot")
    public String fuck() throws IOException {
        String url = "https://zuanbot.com/api.php?level=min&lang=zh_cn";
        Connection.Response response = getBotResp(url);
        return response.body();
    }

    @MiraiCmdDefine(name = "彩虹屁", scope = CommandScope.GROUP, desc = "彩虹屁bot")
    public String suck() throws IOException {
        String url = "https://chp.shadiao.app/api.php";
        Connection.Response response = getBotResp(url);
        return response.body();
    }

    @MiraiCmdDefine(name = "毒鸡汤", scope = CommandScope.GROUP, desc = "毒鸡汤bot")
    public String chickenSoup() throws IOException {
        String url = "https://du.shadiao.app/api.php";
        Connection.Response response = getBotResp(url);
        return response.body();
    }
}
