package top.cookizi.bot.common.enums.command;

import top.cookizi.bot.cache.CommandCache;
import top.cookizi.bot.common.utils.AppUtils;
import top.cookizi.bot.modle.domain.CmdRes;
import top.cookizi.bot.service.MiraiApiService;

import java.util.function.Function;

/**
 * @author heq
 * @date 2021/5/14 3:13 下午
 * @description
 */
public enum GlobalCmd implements ICommand {

    SESSION_RESET("session", (commands) -> {
        MiraiApiService apiService = AppUtils.getBean(MiraiApiService.class);
        apiService.resetSession();
        return CmdRes.BeanCmd();
    });

    public String command;

    public Command commandType;

    public Function<String[], ? extends CmdRes> run;

    GlobalCmd(String command, Function<String[], ? extends CmdRes> run) {
        this.command = command;
        this.commandType = Command.GLOBAL_CMD;
        this.run = run;
    }

    @Override
    public void init() {
        CommandCache.addCommand(command, this);
    }

    @Override
    public Function<String[], ? extends CmdRes> getRun() {
        return run;
    }

    public static GlobalCmd parse(String command) {
        for (GlobalCmd globalCmd : GlobalCmd.values()) {
            if (globalCmd.command.equals(command)) {
                return globalCmd;
            }
        }
        return null;
    }
}
