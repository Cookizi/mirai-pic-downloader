package top.cookizi.bot.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * @author heq
 * @date 2021/6/7 5:37 下午
 * @description
 */
public class JsonUtils {

    public static final Gson GSON = new Gson();

    public static <T> T parseObject(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T parseObject(JsonElement json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }
}
