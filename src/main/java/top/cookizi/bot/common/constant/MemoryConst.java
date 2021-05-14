package top.cookizi.bot.common.constant;

import lombok.extern.slf4j.Slf4j;
import top.cookizi.bot.common.enums.command.Command;
import top.cookizi.bot.common.enums.command.ICommand;
import top.cookizi.bot.modle.domain.CmdRes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author heq
 * @date 2021/5/13 5:30 下午
 * @description
 */
@Slf4j
public class MemoryConst {

    /**
     * 全局token
     */
    private static String GLOBAL_SESSION;

    /**
     * 全部指令
     */
    private static final Map<String, Function<String[], ? extends CmdRes>> COMMAND_MAP = new ConcurrentHashMap<>();

    static {
        for (Command command : Command.values()) {
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

    public static String getSession() {
        return GLOBAL_SESSION;
    }

    public static void setSession(String globalSession) {
        MemoryConst.GLOBAL_SESSION = globalSession;
    }

    public static void addCommand(String command, Function<String[], ? extends CmdRes> function) {
        COMMAND_MAP.put(command, function);
    }

    public static Function<String[], ? extends CmdRes> getCommand(String command) {
        return COMMAND_MAP.get(command);
    }

}
