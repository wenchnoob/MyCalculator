package com.calculator;

public final class Operators {
    public static final int SUBTRACTION = 1, ADDITION = 2, MULTIPLICATION = 3, DIVISION = 4, EXPONENTIATION = 5, RADICAL = 6, MODULUS = 7;

    public static String get(int operator) {
        return switch (operator) {
            case EXPONENTIATION -> "**";
            case RADICAL -> "root";
            case MULTIPLICATION -> "*";
            case DIVISION -> "/";
            case MODULUS -> "%";
            case ADDITION -> "+";
            case SUBTRACTION -> "-";
            default -> throw new IllegalArgumentException("That is not a currently supported operation.");
        };
    }
}
