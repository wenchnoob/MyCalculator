package com.calculator;

public enum Operators {
    ADDITION("+"), MULTIPLICATION("*"), DIVISION("/"), EXPONENTIATION("**"), RADICAL("root"), MODULUS("%");

    String rep;
    Operators(String rep) {
        this.rep = rep;
    }

    public static String get(Operators operator) {
        return operator.rep;
    }

    public static boolean isOperator(String subject) {
        for (Operators operator: Operators.values()) if (operator.rep.equals(subject)) return true;
        return false;
    }
}
