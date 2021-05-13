package top.cookizi.bot.common.utils;

import org.springframework.util.StringUtils;
import top.cookizi.bot.common.utils.modle.Figure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: HeQ
 * @Date: 2019/9/4 下午8:03
 * @Description
 */
public class MathUtils {

    public static int ACCURACY = 20;

    public static List<Long> PRIME = new ArrayList<>();

    public static final Figure NUM_1_PER_10 = figure("0.1");

    public static final Figure NUM_2 = figure("2");

    public static final Figure NUM_5 = figure("5");

    static {
        prime(200);
    }

    /**
     * a + b
     */
    public static Figure plus(Figure a, Figure b) {
        Figure result = figure("0");
        a.toArr();
        b.toArr();
        int longInt = Math.max(a.ints.length, b.ints.length);
        int longDec = Math.max(a.decs.length, b.decs.length);
        result.ints = new short[longInt + 1];
        result.decs = new short[longDec];
        short advance = 0;
        for (int i = longDec - 1; i >= 0; i--) {
            short short1 = i > a.decs.length - 1 ? 0 : a.decs[i];
            short short2 = i > b.decs.length - 1 ? 0 : b.decs[i];
            short bit = (short) (short1 * a.symbol + short2 * b.symbol + advance);
            if (bit >= 10) {
                bit = (short) (bit - 10);
                advance = 1;
            } else if (bit <= -10) {
                advance = -1;
                bit = (short) (bit + 10);
            } else advance = 0;
            result.decs[i] = bit;
            if (bit < 0) result.symbol = -1;
            if (bit > 0) result.symbol = 1;
        }
        for (int i = 1; i <= longInt; i++) {
            short short1 = i > a.ints.length ? 0 : a.ints[a.ints.length - i];
            short short2 = i > b.ints.length ? 0 : b.ints[b.ints.length - i];
            short bit = (short) (short1 * a.symbol + short2 * b.symbol + advance);
            if (bit <= -10) {
                advance = -1;
                bit = (short) (bit + 10);
            } else if (bit >= 10) {
                bit = (short) (bit - 10);
                advance = 1;
            } else advance = 0;
            result.ints[longInt + 1 - i] = bit;
            if (bit < 0) result.symbol = -1;
            if (bit > 0) result.symbol = 1;
        }
        result.ints[0] = advance;
        advance = 0;
        boolean notZero = false;
        for (int i = 1; i <= longDec; i++) {
            short bit = result.decs[longDec - i];
            if (bit != 0) notZero = true;
            if (notZero) {
                int resBit = bit * result.symbol + advance;
                advance = (short) ((resBit + 20) / 10 - 2);
                if (resBit <= -10) {
                    advance = -2;
                    result.decs[longDec - i] = (short) (20 + resBit);
                } else if (resBit < 0) {
                    result.decs[longDec - i] = (short) (10 + resBit);
                } else if (resBit >= 10) {
                    result.decs[longDec - i] = (short) (resBit - 10);
                } else {
                    result.decs[longDec - i] = (short) (resBit);
                }
            }
        }
        for (int i = 0; i <= longInt; i++) {
            short bit = result.ints[longInt - i];
            int resBit = bit * result.symbol + advance;
            advance = (short) ((resBit + 20) / 10 - 2);
            if (resBit <= -10) {
                advance = -2;
                result.ints[longInt - i] = (short) (20 + resBit);
            } else if (resBit < 0) {
                result.ints[longInt - i] = (short) (10 + resBit);
            } else if (resBit >= 10) {
                result.ints[longInt - i] = (short) (resBit - 10);
            } else {
                result.ints[longInt - i] = (short) (resBit);
            }
        }
        result.arrToStr();
        return result;
    }

    public static String plus(String a, String b) {
        Figure result = plus(figure(a), figure(b));
        return result.toString();
    }

    /**
     * a - b
     */
    public static Figure minus(Figure a, Figure b) {
        b.positive = !b.positive;
        b.symbol = -b.symbol;
        return plus(a, b);
    }

    public static String minus(String a, String b) {
        if (b == null || b.equals("")) {
            b = "0";
        }
        b = b.charAt(0) == '-' ? b.substring(1) : ("-" + b);
        return plus(a, b);
    }

    /**
     * a × b
     */
    public static Figure times(Figure a, Figure b) {
        Figure result = figure("0");
        result.positive = a.positive == b.positive;
        Figure longNum;
        Figure shortNum;
        if (a.integer.length() + a.decimal.length() >= b.integer.length() + b.decimal.length()) {
            longNum = a;
            shortNum = b;
        } else {
            longNum = b;
            shortNum = a;
        }
        String longStr = longNum.integer + longNum.decimal;
        String shortStr = shortNum.integer + shortNum.decimal;
        int resLen = longStr.length() + shortStr.length();
        int floatLen = longNum.decimal.length() + shortNum.decimal.length();
        short[] res = new short[resLen];
        for (int i = 0; i < shortStr.length(); i++) {
            int advance = 0;
            for (int j = 0; j < longStr.length(); j++) {
                int longBit = longStr.charAt(longStr.length() - j - 1) - 48;
                int shortBit = shortStr.charAt(shortStr.length() - i - 1) - 48;
                int resBit = advance + longBit * shortBit + res[resLen - i - j - 1];
                res[resLen - i - j - 1] = (short) (resBit % 10);
                advance = resBit / 10;
            }
            res[resLen - i - longStr.length() - 1] = (short) advance;
        }
        result.integer = "";
        result.decimal = "";
        if (floatLen > 0) {
            boolean notZero = false;
            for (int i = 1; i <= floatLen; i++) {
                if (res[resLen - i] != 0) {
                    notZero = true;
                }
                if (notZero || i == floatLen) {
                    result.decimal = res[resLen - i] + result.decimal;
                }
            }
        }
        boolean notZero = false;
        for (int i = 0; i < resLen - floatLen; i++) {
            if (res[i] != 0) {
                notZero = true;
            }
            if (notZero || i == resLen - floatLen - 1) {
                result.integer = result.integer + res[i];
            }
        }
        return result;
    }

    public static String times(String a, String b) {
        if ("0".equals(a) || "0".equals(b)) {
            return "0";
        }
        Figure result = times(figure(a), figure(b));
        return result.toString();
    }

    /**
     * a ÷ b
     */
    public static Figure divide(Figure a, Figure b) {
        Figure result = new Figure(null);
        String resultPositive = a.positive == b.positive ? "" : "-";
        String bStr = b.integer + b.decimal;
        String aStr;
        if (b.decimal.length() >= a.decimal.length()) {
            a.integer = a.integer + a.decimal;
            for (int i = 0; i < b.decimal.length() - a.decimal.length(); i++) {
                a.integer += "0";
            }
            a.decimal = "0";
        } else {
            a.integer = a.integer + a.decimal.substring(0, b.decimal.length());
            a.decimal = a.decimal.substring(b.decimal.length());
        }
        boolean flag = true;
        while (flag) {
            flag = !flag;
        }
        return result;
    }

    public static String divide(String a, String b) {
        return divide(figure(a), figure(b)).toString();
    }

    /**
     * 比较大小 1:a>b 0:a=b -1:a<b
     */
    public static int compare(Figure a, Figure b) {
        return a.compare(b);
    }

    /**
     * 正整数相除 [0]有限位 [1]无限位 null/""为整除
     */
    public static String[] intDivide(String factor, String num) {
        if (num == null || "".equals(num) || "0".equals(num))
            return null;
        if ("1".equals(num))
            return new String[]{factor, null};

        String[] result = new String[2];
        //有限小数的位数
        Figure pointFiniteDigit = new Figure("1");
        Figure factorNum = new Figure(factor);

        //将因数中的 2 和 5 去除
        while (true) {
            char end = num.charAt(num.length() - 1);
            if (end == '0') {
                pointFiniteDigit.Times(NUM_1_PER_10);
                num = num.substring(0, num.length() - 1);
            } else if (end % 2 == 0) {
                pointFiniteDigit.Times(NUM_1_PER_10);
                factorNum.Times(NUM_5);
                num = times(num, "5");
                num = num.substring(0, num.length() - 1);
            } else if (end == '5') {
                pointFiniteDigit.Times(NUM_1_PER_10);
                factorNum.Times(NUM_2);
                num = times(num, "2");
                num = num.substring(0, num.length() - 1);
            } else {
                break;
            }
        }
        if ("1".equals(num)) {
            result[0] = times(pointFiniteDigit, factorNum).toString();
            return result;
        }

        //费马小定理 米迪定理 除去 2 5 因数的num与10互质 有 10^(num-1)-1 能被 num 整除
        Map<Character, String> supply = supplyNine(num);
        String advance = "0";
        String resStr = "";
        //循环位位数
        int circleDigit = 0;
        //用当前的num去凑 999...99 纯循环小数 = 循环位 / 10^n-1（n∈N*） 并计算循环位位数circleDigit
        do {
            circleDigit++;
            assert supply != null;
            String[] supArr = supply.get(advance.charAt(advance.length() - 1)).split(":");
            resStr = supArr[0] + resStr;
            advance = plus(advance.substring(0, advance.length() - 1), supArr[1]);
        } while (!"0".equals(advance));

        //把之前去掉的 2 5 的因数乘回去 超出循环位的加到前面有限位里
        resStr = times(resStr, factorNum.toString());
        String supplyFinite = "";
        while (resStr.length() > circleDigit) {
            advance = resStr.substring(0, resStr.length() - circleDigit);
            resStr = positivePlus(advance, resStr.substring(resStr.length() - circleDigit));
            if (!"0".equals(advance)) {
                supplyFinite = positivePlus(supplyFinite, advance);
            }
            if (resStr.length() <= circleDigit) {
                break;
            }
        }

        Figure finiteNum = times(pointFiniteDigit, figure(supplyFinite));
        result[0] = finiteNum.toString();
        //位数转换为循环位前的有限位
        if ("0".equals(result[0])) {
            if ("1".equals(pointFiniteDigit.integer)) {
                result[0] = "0.";
            } else {
                result[0] = pointFiniteDigit.toString().replace("1", "0");
            }
        }
        if (eachIs0Or9(resStr) && resStr.charAt(0) == '9') {
            result[0] = plus(pointFiniteDigit, figure(result[0])).toString();
        } else {
            result[1] = supplyZero(circleDigit - resStr.length()) + resStr;
        }
        return result;
    }

    /**
     * 正整数倒数 [0]有限位 [1]无限位 null/""为整除
     */
    public static String[] intReciprocal(String num) {
        return intDivide("1", num);
    }

    /**
     * 生成前n个质数
     */
    public static void prime(long n) {
        PRIME = new ArrayList<>();
        PRIME.add(2L);
        for (long i = 3L; n > 1L; i++) {
            boolean flag = true;
            for (long prime : PRIME) {
                if (i % prime == 0L) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                n--;
                PRIME.add(i);
            }
        }
    }

    /**
     * 正整数相加
     */
    private static String positivePlus(String num1, String num2) {
        if (StringUtils.isEmpty(num1)) {
            num1 = "0";
        }
        if (StringUtils.isEmpty(num2)) {
            num2 = "0";
        }
        int advance = 0;
        int longLength;
        int shortLength;
        String shortNum;
        String longNum;
        StringBuilder result = new StringBuilder();
        if (num1.length() >= num2.length()) {
            longLength = num1.length();
            shortLength = num2.length();
            longNum = num1;
            shortNum = num2;
        } else {
            longLength = num2.length();
            shortLength = num1.length();
            longNum = num2;
            shortNum = num1;
        }
        for (int i = 1; i <= longLength; i++) {
            int longAt = longLength - i;
            int shortAt = shortLength - i;
            int longStep = Integer.parseInt(String.valueOf(longNum.charAt(longAt)));
            int shortStep = shortAt < 0 ? 0 : Integer.parseInt(String.valueOf(shortNum.charAt(shortAt)));
            String step;
            if (shortStep + longStep + advance >= 10) {
                step = String.valueOf(shortStep + longStep + advance - 10);
                advance = 1;
            } else {
                step = String.valueOf(shortStep + longStep + advance);
                advance = 0;
            }
            result.insert(0, step);
        }
        if (advance == 1) {
            result.insert(0, "1");
        }
        return result.toString();
    }

    /**
     * 9 - (正整数num × 1-9 后 积的个位数) 集合
     *
     * @return <json>{"x":"y:z"}</json>  x为0-9, (num × y + x) % 10 ≡ 9, z = (num × y) / 10
     */
    private static Map<Character, String> supplyNine(String num) {
        Map<Character, String> result = new HashMap<>(16);
        char endChar = num.charAt(num.length() - 1);
        if (endChar % 2 == 0 || endChar == '5') {
            return null;
        }
        int endNum = endChar - 48;
        for (int i = 0; i < 10; i++) {
            char key = (char) (57 - endNum * i % 10);
            String value = times(num, String.valueOf(i));
            if (value.length() == 1) {
                value = i + ":0";
            } else {
                value = i + ":" + value.substring(0, value.length() - 1);
            }
            result.put(key, value);
        }
//        System.out.println("supplement:" + JSON.toJSONString(result));
        return result;
    }

    public static Figure figure(String num) {
        return new Figure(num);
    }

    private static String supplyZero(long count) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < count; i++) {
            res.append("0");
        }
        return res.toString();
    }

    private static boolean eachIs0Or9(String str) {
        if (PrintUtils.isEmpty(str)) {
            return false;
        }
        boolean allSame = true;
        for (int i = 0; i < str.length() - 2; i++) {
            if (str.charAt(i) != str.charAt(i + 1)) {
                allSame = false;
            }
        }
        return allSame && (str.charAt(0) == '0' || str.charAt(0) == '9');
    }
}
