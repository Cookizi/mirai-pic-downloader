package top.cookizi.bot.service.group;

import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;
import top.cookizi.bot.common.utils.StringUtils;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RollService {

    public String calc(String rollArgs) {
        rollArgs = format(rollArgs);
        rollArgs = rollArgs.replaceAll(" ", "");
        String regex = "(\\d*)d(\\d*)(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rollArgs);
        matcher.find();
        String diceCountStr = matcher.group(1);
        int diceCount = 1;
        String diceFaceStr = matcher.group(2);
        int diceFace = 100;
        String diceExp = matcher.group(3);
        if (StringUtils.isNotBlank(diceCountStr)) {
            diceCount = Integer.parseInt(diceCountStr);
        }
        if (StringUtils.isNotBlank(diceFaceStr)) {
            diceFace = Integer.parseInt(diceFaceStr);
        }

        Random random = new Random(System.currentTimeMillis());
        List<Integer> resultList = new ArrayList<>(diceCount);
        int result = 0;
        for (int i = 0; i < diceCount; i++) {
            int point = random.nextInt(diceFace) + 1;
            resultList.add(point);
            result += point;
        }
        StringBuilder sb = new StringBuilder("Roll【").append(rollArgs).append("】=>\n[");
        String join = Joiner.on(",").join(resultList);
        sb.append(join).append("]=").append(result);
        if (StringUtils.isBlank(diceExp)) {
            return sb.toString();
        }
        String expression = result + diceExp;
        sb.append("\n").append(expression).append("=");
        double v = evaluateExpression(expression);

        sb.append(String.format("%.2f", v));
        return sb.toString();

    }

    private String format(String rollArgs) {
        return rollArgs
                .replaceAll("（", "(")
                .replaceAll("）", ")")
                .replaceAll("x|X", "*")
                .replaceAll("D", "d")
                .replaceAll(" ", "");
    }

    public static double evaluateExpression(String expression) {
        Stack<Double> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        int index = 0;
        while (index < expression.length()) {
            char ch = expression.charAt(index);
            if (ch == ' ') {
                index++;
            } else if (ch == '(') {
                operators.push(ch);
                index++;
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    double num2 = operands.pop();
                    double num1 = operands.pop();
                    char operator = operators.pop();
                    double result = applyOperator(num1, num2, operator);
                    operands.push(result);
                }
                operators.pop();
                index++;
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^') {
                while (!operators.isEmpty() && precedence(ch) <= precedence(operators.peek())) {
                    double num2 = operands.pop();
                    double num1 = operands.pop();
                    char operator = operators.pop();
                    double result = applyOperator(num1, num2, operator);
                    operands.push(result);
                }
                operators.push(ch);
                index++;
            } else {
                int start = index;
                while (index < expression.length() && isNumber(expression.charAt(index))) {
                    index++;
                }
                double num = Double.parseDouble(expression.substring(start, index));
                operands.push(num);
            }
        }

        while (!operators.isEmpty()) {
            double num2 = operands.pop();
            double num1 = operands.pop();
            char operator = operators.pop();
            double result = applyOperator(num1, num2, operator);
            operands.push(result);
        }

        return operands.pop();
    }

    private static boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9' || ch == '.';
    }

    private static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return 0;
        }
    }

    private static double applyOperator(double num1, double num2, char operator) {
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                return num1 / num2;
            case '^':
                return Math.pow(num1, num2);
            default:
                return 0;
        }
    }


    public static void main(String[] args) throws ScriptException {

        RollService service = new RollService();
        String exp = "4d6";
        String calc = service.calc(exp);
        System.out.println(calc);

    }


}
