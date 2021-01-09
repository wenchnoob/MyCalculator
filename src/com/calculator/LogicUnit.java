package com.calculator;

import static com.calculator.Operators.*;
import static com.parser.Parser.*;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class LogicUnit {

    public static String solve(final String expression) {
        String internalRep = parseNegative.apply(expression.intern());

        int[] sub = findSubExpression.apply(internalRep);
        if (subExpressionExists.test(sub)) {
            internalRep = internalRep.replace(internalRep.substring(sub[0], sub[1]), solve(internalRep.substring(sub[0]+1, sub[1]-1)));
        }

        try {
            return solveRaw.andThen(tryToInt).apply(internalRep);
        } catch (NumberFormatException ex) {
            return "NaN";
        }
    }

    private static final BiFunction<String, Operators, String> binaryEval = (expression, operator) -> {
        String operation = Operators.get(operator);

        while(expression.contains(operation)) {
            int start = findOperator.apply(expression, operation);
            int leftBound = parseLeftOperand.apply(expression, start);
            int rightBound = parseWideOperatorRightOperand(expression, start, operator);

            if(operation.length() == 1) {
                expression = expression.replace(
                        expression.substring(leftBound, rightBound),
                        Calculator.binaryEval(
                                expression.substring(leftBound, start),
                                expression.substring(start + 1, rightBound),
                                operator));
            } else {
                expression = expression.replace(
                        expression.substring(leftBound, rightBound),
                        Calculator.binaryEval(
                                expression.substring(leftBound, start),
                                expression.substring(start + operation.length(), rightBound),
                                operator));
            }
        }

        return expression;
    };

    private static final UnaryOperator<String> substituteVariables = expression -> {
        for (Map.Entry<String, String> entry: Calculator.variables()) {
            expression = expression.replaceAll(entry.getKey(), entry.getValue());
        }
        return expression;
    };

    private static final UnaryOperator<String> evalExponent = expression -> binaryEval.apply(expression, EXPONENTIATION);

    private static final UnaryOperator<String> evalRadical = expression -> binaryEval.apply(expression, RADICAL);

    private static final UnaryOperator<String> evalMultiplication = expression -> binaryEval.apply(expression, MULTIPLICATION);

    private static final UnaryOperator<String> evalModulus = expression -> binaryEval.apply(expression, MODULUS);

    private static final UnaryOperator<String> evalDivision = expression -> binaryEval.apply(expression, DIVISION);

    private static final UnaryOperator<String> evalAddition = expression -> binaryEval.apply(expression, ADDITION);

    private static final UnaryOperator<String> solveRaw =
            expression -> substituteVariables
                    .andThen(evalExponent)
                    .andThen(parseDoubleNegative)
                    .andThen(evalRadical)
                    .andThen(parseDoubleNegative)
                    .andThen(evalMultiplication)
                    .andThen(parseDoubleNegative)
                    .andThen(evalDivision)
                    .andThen(parseDoubleNegative)
                    .andThen(evalModulus)
                    .andThen(parseDoubleNegative)
                    .andThen(evalAddition)
                    .apply(expression);
}
