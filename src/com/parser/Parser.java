package com.parser;

import com.calculator.Operators;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;

public class Parser {

    public static final UnaryOperator<String> removeWhiteSpace =
            str -> str.codePoints()
                    .boxed()
                    .filter(code -> !Character.isWhitespace((char)code.intValue()))
                    .collect(Collector.of(
                            () -> new String[] {""},
                            (result, item) -> result[0] += (char) item.intValue(),
                            (r1, r2) ->  {
                                r1[0] += r2[0];
                                return r1; },
                            r -> r[0]));

    public static final Function<String, int[]> findSubExpression = s -> {
        int start = 0;
        int end = s.length()-1;

        while (start < end && (s.charAt(start) != '(' || s.charAt(end) != ')')) {
            if (s.charAt(start) != '(') start++;
            if (s.charAt(end) != ')') end--;
        }

        return new int[]{start, end+1};
    };

    public static final UnaryOperator<String> tryToInt = num -> {
        int indexOfDecimal = num.indexOf(".");
        if(indexOfDecimal > -1) {
            try{
                if (Integer.parseInt(num.substring(indexOfDecimal+1)) == 0) return num.substring(0, indexOfDecimal);
            } catch (NumberFormatException ex) { }
        }
        return num;
    };

    public static final BiFunction<String, String, Integer> findOperator = (expression, operator) -> {
        int width = operator.length();
        for (int i = 0; i < expression.length() - width; i++) {
            if (expression.substring(i, i+width).equals(operator)) return i;
        }
        return -1;
    };

    public static final UnaryOperator<String> parseNegative = expression -> {
        StringBuffer expressionBuf = new StringBuffer(expression);

        for (int i = expressionBuf.indexOf("-", 1); i != -1; i = expressionBuf.indexOf("-", i+2)) {
            if (i == -1) break;
            if (Character.isDigit(expressionBuf.charAt(i-1))) expressionBuf.insert(i, '+');
            System.out.println(expressionBuf);
        }
        return expressionBuf.toString();
    };

    public static final UnaryOperator<String> parseDoubleNegative = expression -> expression.replaceAll("--", "+");

    public static final BiFunction<String, Integer, Integer> parseLeftOperand = (target, start) -> {
        start--;
        while(start > 0 && (Character.isDigit(target.charAt(start))  | target.charAt(start) == '-' | target.charAt(start) == '.')) --start;
        return start > 0? start + 1: start;
    };

    public static final BiFunction<String, Integer, Integer> parseRightOperand = (target, start) -> {
        start++;
        while(start < target.length()  && (Character.isDigit(target.charAt(start)) | target.charAt(start) == '-' |target.charAt(start++) == '.'));
        return start < target.length() ? start-1: start;
    };

    public static int parseWideOperatorRightOperand(String target, int start, Operators operator) {
        start += Operators.get(operator).length()-1;
        return parseRightOperand.apply(target, start);
    }

    public static final Predicate<int[]> subExpressionExists = indices -> indices[0] - indices[1] < -1;

}
