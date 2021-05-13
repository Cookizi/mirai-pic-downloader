package top.cookizi.bot.common.enums.command;

import top.cookizi.bot.modle.domain.CmdRes;

import java.util.function.Function;

/**
 * @author heq
 * @date 2021/5/14 4:34 下午
 * @description
 */
public interface ICommand {

    void init();

    Function<String[], ? extends CmdRes> getRun();
}
