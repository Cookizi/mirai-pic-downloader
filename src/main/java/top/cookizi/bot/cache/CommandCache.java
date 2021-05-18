package top.cookizi.bot.cache;

import lombok.extern.slf4j.Slf4j;
import top.cookizi.bot.common.enums.command.ICommand;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author heq
 * @date 2021/5/13 5:30 下午
 * @description
 */
@Slf4j
public class CommandCache {

    /**
     * 全部指令
     */
    private static final Map<String, ICommand> COMMAND_MAP = new ConcurrentHashMap<>();

    static {
        for (top.cookizi.bot.common.enums.command.Command command : top.cookizi.bot.common.enums.command.Command.values()) {
            try {
                ICommand[] iCommands = command.clazz.getEnumConstants();
                for (ICommand iCommand : iCommands) {
                    iCommand.init();
                }
            } catch (Exception e) {
                log.error("command init error:{}", command.type);
                e.printStackTrace();
            }
        }
    }

    public static void addCommand(String command, ICommand iCommand) {
        COMMAND_MAP.put(command, iCommand);
    }

    public static ICommand getCommand(String command) {
        return COMMAND_MAP.get(command);
    }

}
