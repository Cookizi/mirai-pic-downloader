package top.cookizi.bot.common.utils.modle;

import top.cookizi.bot.common.utils.StringMathUtils;

/**
 * @Author: WangHeQi
 * @Date: 2019/9/5 上午11:16
 * @Description
 */
public class Figure {

    public String integer = "0";
    public String decimal = "0";
    public boolean positive = true;
    public short[] ints;
    public short[] decs;
    public int symbol = 1;

    /**
     * 是否相同
     */
    public boolean same(Figure figure) {
        if (decimal.equals(figure.decimal)
                && integer.equals(figure.integer)
                && positive == figure.positive) {
            return true;
        }
        return false;
    }

    /*** 加在自己身上 ***/

    public Figure Plus(Figure b) {
        this.copy(StringMathUtils.plus(this, b));
        return this;
    }

    public Figure Minus(Figure b) {
        this.copy(StringMathUtils.minus(this, b));
        return this;
    }

    public Figure Times(Figure b) {
        this.copy(StringMathUtils.times(this, b));
        return this;
    }

    public Figure Divide(Figure b) {
        this.copy(StringMathUtils.divide(this, b));
        return this;
    }

    /*** 仅返回结果不加在自己身上 ***/

    public Figure plus(Figure b) {
        return StringMathUtils.plus(this, b);
    }

    public Figure minus(Figure b) {
        return StringMathUtils.minus(this, b);
    }

    public Figure times(Figure b) {
        return StringMathUtils.times(this, b);
    }

    public Figure divide(Figure b) {
        return StringMathUtils.divide(this, b);
    }

    /**
     * 比较大小
     *
     * @return 1:a>b 0:a=b -1:a<b
     */
    public int compare(Figure b) {
        if (this.same(b))
            return 0;
        if (this.positive && !b.positive)
            return 1;
        if (!this.positive && b.positive)
            return -1;

        int result = 0;
        if (this.integer.length() > b.integer.length()) {
            result = 1;
        } else if (this.integer.length() < b.integer.length()) {
            result = -1;
        } else {
            for (int i = 0; i < this.integer.length(); i++) {
                if (this.integer.charAt(i) > b.integer.charAt(i)) {
                    result = 1;
                    break;
                } else if (this.integer.charAt(i) < b.integer.charAt(i)) {
                    result = -1;
                    break;
                }
            }
            if (result == 0) {
                int longDecimal = Math.max(this.decimal.length(), b.decimal.length());
                for (int i = 0; i < longDecimal; i++) {
                    char figure1C = '0';
                    char figure2C = '0';
                    if (i + 1 < this.decimal.length()) {
                        figure1C = this.decimal.charAt(i);
                    }
                    if (i + 1 < b.decimal.length()) {
                        figure2C = b.decimal.charAt(i);
                    }
                    if (figure1C > figure2C) {
                        result = 1;
                    } else {
                        result = -1;
                    }
                }
            }
        }

        if (!this.positive && !b.positive) return -result;

        return result;
    }

    public Figure(String num) {
        try {
            if (num == null || num.equals("")) {
                return;
            }
            positive = num.charAt(0) != '-';
            if (!positive || num.charAt(0) == '+') {
                num = num.substring(1);
            }
            if (num.contains(".")) {
                String[] numSplit = num.split("\\.");
                if (numSplit[0].length() > 0) {
                    integer = numSplit[0];
                    for (int i = 0; i < integer.length(); i++) {
                        if (integer.charAt(i) != '0') {
                            integer = integer.substring(i);
                            break;
                        }
                        if (i == integer.length() - 1) {
                            integer = "0";
                        }
                    }
                }
                if (numSplit.length == 2) {
                    decimal = numSplit[1];
                    for (int i = decimal.length(); i > 0; i--) {
                        if (decimal.charAt(i - 1) != '0') {
                            decimal = decimal.substring(0, i);
                            break;
                        }
                        if (i == 1) {
                            decimal = "0";
                        }
                    }
                }
            } else {
                integer = num;
                for (int i = 0; i < integer.length(); i++) {
                    if (integer.charAt(i) != '0') {
                        integer = integer.substring(i);
                        break;
                    }
                }
            }
            if (integer.equals("0") && decimal.equals("0")) {
                positive = true;
            }
            if (!positive) {
                symbol = -1;
            }
        } catch (Exception e) {
            System.out.println("数字不符规则:" + num);
        }
    }

    /**
     * 转换为数组
     */
    public void toArr() {
        ints = new short[integer.length()];
        decs = new short[decimal.length()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = (short) (integer.charAt(i) - 48);
        }
        for (int i = 0; i < decs.length; i++) {
            decs[i] = (short) (decimal.charAt(i) - 48);
        }
        symbol = positive ? 1 : -1;
        if (decimal.equals("0")
                && integer.equals("0")) {
            symbol = 0;
        }
    }

    public void arrToStr() {
        boolean notZero = false;
        integer = "";
        decimal = "";
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] != 0) {
                notZero = true;
            }
            if (notZero || i == ints.length - 1) {
                integer += ints[i];
            }
        }
        notZero = false;
        for (int i = 0; i < decs.length; i++) {
            if (decs[decs.length - i - 1] != 0) {
                notZero = true;
            }
            if (notZero || i == decs.length - 1) {
                decimal = decs[decs.length - i - 1] + decimal;
            }
        }
        positive = symbol != -1;
    }

    @Override
    public String toString() {
        String figure = positive ? "" : "-";
        figure += integer;
        if (!decimal.equals("0")) {
            figure += "." + decimal;
        }
        return figure.length() == 0 ? "0" : figure;
    }

    public void copy(Figure figure) {
        this.integer = figure.integer;
        this.decimal = figure.decimal;
        this.positive = figure.positive;
        this.ints = figure.ints;
        this.decs = figure.decs;
        this.symbol = figure.symbol;
    }
}
