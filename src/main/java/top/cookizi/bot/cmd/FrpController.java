package top.cookizi.bot.cmd;

import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdArg;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.config.CommandScope;

import java.io.IOException;

@MiraiCmd
public class FrpController {
    private Process exec = null;

    @MiraiCmdDefine(name = "frp", scope = CommandScope.FRIEND, desc = "frp启停")
    public String frp(@MiraiCmdArg String opr,@MiraiCmdArg(required = false) String timeout) throws IOException {
        stop();
        if ("start".equalsIgnoreCase(opr)) {
            var runtime = Runtime.getRuntime();
            exec = runtime.exec("");
        }
        return null;
    }

    private void stop() {
        if (exec != null) {
            exec.destroy();
        }
        exec = null;
    }
}
