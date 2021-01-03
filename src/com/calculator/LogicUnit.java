package com.calculator;

import com.calculator.Calculator;
import com.calculator.Operators;

import static com.calculator.Operators.*;
import static com.parser.Parser.*;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class LogicUnit {

    public static String solve(final String expression) {
        String internalRep = expression.intern();

        int[] sub = findSubExpression.apply(internalRep);
        if (subExpressionExists.test(sub)) {
            internalRep = internalRep.replace(internalRep.substring(sub[0], sub[1]), solve(internalRep.substring(sub[0]+1, sub[1]-1)));
        }

        return solveRaw.andThen(tryToInt).apply(internalRep);
    }

    private static final BiFunction<String, Integer, String> binaryEval = (expression, operator) -> {
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

    private static final UnaryOperator<String> evalExponent = expression -> binaryEval.apply(expression, EXPONENTIATION);

    private static final UnaryOperator<String> evalRadical = expression -> binaryEval.apply(expression, RADICAL);

    private static final UnaryOperator<String> evalMultiplication = expression -> binaryEval.apply(expression, MULTIPLICATION);

    private static final UnaryOperator<String> evalDivision = expression -> binaryEval.apply(expression, DIVISION);

    private static final UnaryOperator<String> evalAddition = expression -> binaryEval.apply(expression, ADDITION);

    private static final UnaryOperator<String> evalSubtraction = expression -> binaryEval.apply(expression, SUBTRACTION);

    private static final UnaryOperator<String> solveRaw =
            expression -> evalExponent
            .andThen(evalRadical)
            .andThen(evalMultiplication)
            .andThen(evalDivision)
            .andThen(evalAddition)
            .andThen(evalSubtraction)
            .apply(expression);
}
