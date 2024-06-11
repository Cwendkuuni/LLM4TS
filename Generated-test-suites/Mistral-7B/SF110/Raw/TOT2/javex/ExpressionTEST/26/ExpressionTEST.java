package org.javex.test;

import org.junit.Test;
import org.javex.Expression;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("expression");
        List<Expression> externalFunctions = expression.getExternalFunctions();
        assertFalse(externalFunctions.isEmpty());
    }

    @Test
    public void testExpressionConstructor_ValidExpression() {
        Expression expression = new Expression("1 + 2");
        assertNotNull(expression);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpressionConstructor_EmptyString() {
        new Expression("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpressionConstructor_InvalidExpression() {
        new Expression("a + b");
    }

    @Test(expected = ArithmeticException.class)
    public void testExpressionDivisionByZero() {
        Expression expression = new Expression("1 / 0");
        expression.prepare();
        expression.evaluate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpressionInvalidFunction() {
        Expression expression = new Expression("invalidFunction()");
        expression.prepare();
        expression.evaluate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpressionMissingFunctionArgument() {
        Expression expression = new Expression("abs(a)");
        expression.prepare();
        expression.evaluate();
        assertFalse(expression.isPrepared());

        expression = new Expression("abs()");
        expression.prepare();
        expression.evaluate();
        assertFalse(expression.isPrepared());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpressionIncorrectNumberOfArguments() {
        Expression expression = new Expression("abs(1, 2)");
        expression.prepare();
        expression.evaluate();
        assertFalse(expression.isPrepared());

        expression = new Expression("abs(1)");
        expression.prepare();
        expression.evaluate();
        assertTrue(expression.isPrepared());
    }
}