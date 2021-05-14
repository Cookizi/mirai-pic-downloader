package top.cookizi.bot.common.enums.command;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import top.cookizi.bot.common.constant.MemoryConst;
import top.cookizi.bot.common.utils.StringMathUtils;
import top.cookizi.bot.modle.domain.CmdRes;
import top.cookizi.bot.modle.domain.SendMsg;

import java.util.List;
import java.util.function.Function;

/**
 * @author heq
 * @date 2021/5/14 3:45 下午
 * @description
 */
@AllArgsConstructor
public enum ToolsCmd implements ICommand {

    CALCULATE("计算", (commands) -> {
        List<String> postFix = StringMathUtils.infixToPostfix(commands[1]);
        String res = "结果：" + StringMathUtils.calculate(postFix);
        return CmdRes.builder().sendMsg(true)
                .msgBody(SendMsg.TextMsg(res)).build();
    }),

    RANDOM("random", (commands) -> {
        double random = commands.length > 1 ? (Integer.parseInt(commands[1]) * Math.random()) : (100 * Math.random());
        String res = String.valueOf(((int) (random * 100)) / 100.0);
        return CmdRes.builder().sendMsg(true)
                .msgBody(SendMsg.TextMsg(res)).build();
    }),


    ;

    public String command;

    public Function<String[], ? extends CmdRes> run;

    ToolsCmd() {
    }

    @Override
    public void init() {
        MemoryConst.addCommand(command, run);
    }
}
