package top.cookizi.bot.common.enums.command;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author heq
 * @date 2021/5/14 3:13 下午
 * @description
 */
@AllArgsConstructor
@Slf4j
public enum Command {

    GLOBAL_CMD("global", GlobalCmd.class),
    TOOLS_CMD("tools", ToolsCmd.class),

    ;

    public final String type;

    public final Class<? extends ICommand> clazz;

}
