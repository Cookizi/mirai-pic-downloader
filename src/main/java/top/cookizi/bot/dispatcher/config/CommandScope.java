package top.cookizi.bot.dispatcher.config;

import lombok.AllArgsConstructor;
import top.cookizi.bot.common.enums.MsgType;

import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public enum CommandScope {

    FRIEND(type -> MsgType.parse(type) == MsgType.FRIEND_MESSAGE),
    GROUP(type -> MsgType.parse(type) == MsgType.GROUP_MESSAGE),
    ALL(x -> true);
    public final Function<String, Boolean> isScopeMatch;

    public static boolean isScopeMatch(String msgType, List<CommandScope> scope) {
        if (scope.contains(ALL)) return true;
        return scope.contains(MsgType.parse(msgType).scope);
    }
}
