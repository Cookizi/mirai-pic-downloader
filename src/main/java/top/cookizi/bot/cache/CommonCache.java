package top.cookizi.bot.cache;

import lombok.extern.slf4j.Slf4j;

/**
 * @author heq
 * @date 2021/5/13 5:30 下午
 * @description
 */
@Slf4j
public class CommonCache {

    /**
     * 全局token
     */
    private static String GLOBAL_SESSION;

    public static String getSession() {
        return GLOBAL_SESSION;
    }

    public static void setSession(String globalSession) {
        CommonCache.GLOBAL_SESSION = globalSession;
    }


}
