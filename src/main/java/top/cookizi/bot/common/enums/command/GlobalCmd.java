package top.cookizi.bot.common.enums.command;

import lombok.AllArgsConstructor;
import top.cookizi.bot.common.constant.MemoryConst;
import top.cookizi.bot.modle.domain.CmdRes;

import java.util.function.Function;

/**
 * @author heq
 * @date 2021/5/14 3:13 下午
 * @description
 */
@AllArgsConstructor
public enum GlobalCmd implements ICommand {

    SESSION_RESET("session", (commands) -> {
        return CmdRes.BeanCmd();
    });

    public String command;

    public Function<String[], ? extends CmdRes> run;

    @Override
    public void init() {
        MemoryConst.addCommand(command, run);
    }
}
