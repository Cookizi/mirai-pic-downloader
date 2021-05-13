package top.cookizi.bot.common.utils;

public class PrintUtils {

    /**** 字符串上色 ****/

    public static String red(String str) {
        return color(str, 31);
    }

    public static String green(String str) {
        return color(str, 32);
    }

    public static String yellow(String str) {
        return color(str, 33);
    }

    public static String blue(String str) {
        return color(str, 34);
    }

    /**
     * 字符串上色
     *
     * @param color 30 白 31 红 32 绿 33 黄 34 蓝 35 紫 36 青 37 灰
     */
    public static String color(String str, int color) {
        return "\033[" + color + "m" + str + "\033[0m";
    }

    /**
     * 格式化数字
     *
     * @param num    数字
     * @param length 打印数字总长度
     * @return n个空格+数字
     */
    public static String printNum(int num, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(num);
        int blank = length - stringBuilder.length();
        for (int i = 0; i < blank; i++) {
            stringBuilder.insert(0, " ");
        }
        return stringBuilder.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 字母序号
     *
     * @param i 序号 从1开始
     * @return 从A开始的字母序号
     */
    public static String orderLetter(int i) {
        return letterOption(i, new StringBuilder()).toString();
    }

    private static StringBuilder letterOption(int i, StringBuilder stringBuilder) {
        if (i == 0) {
            return stringBuilder;
        }
        if (i <= 26) {
            return stringBuilder.insert(0, (char) (i + 64));
        }
        return letterOption((i - 1) / 26, stringBuilder.insert(0, (char) ((i - 1) % 26 + 65)));
    }
}
