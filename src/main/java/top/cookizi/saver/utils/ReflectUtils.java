package top.cookizi.saver.utils;

import java.lang.reflect.Field;

public class ReflectUtils {

    public static <T> T getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            return getFieldValue(obj, field);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object obj, Field field) {
        try {
        field.setAccessible(true);
            return (T) field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}