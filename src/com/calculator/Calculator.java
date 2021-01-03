package com.calculator;

import java.util.*;

import static com.calculator.Operators.*;

public class Calculator {
    private static HashMap<String, String> variables = new HashMap<>();

    public static boolean addVariable(String name, String value, boolean change) {
        if (!change && variables.containsKey(name)) return false;
        variables.put(name, value);
        return true;
    }

    public static void removeVariable(String name) {
        variables.remove(name);
    }

    public static Set<Map.Entry<String, String>> variables() {
        return variables.entrySet();
    }

    public static String binaryEval(String operand1, String operand2, int operator) throws NumberFormatException {
        double x, y;
        x = Double.parseDouble(operand1);
        y = Double.parseDouble(operand2);

        return switch(operator) {
            case ADDITION -> String.valueOf(x+y);
            case SUBTRACTION -> String.valueOf(x-y);
            case MULTIPLICATION -> String.valueOf(x*y);
            case DIVISION -> String.valueOf(x/y);
            case EXPONENTIATION-> String.valueOf(Math.pow(x,y));
            case RADICAL -> String.valueOf(Math.pow(x, 1/y));
            case MODULUS -> String.valueOf(x%y);
            default -> throw new UnsupportedOperationException();
        };
    }
}
