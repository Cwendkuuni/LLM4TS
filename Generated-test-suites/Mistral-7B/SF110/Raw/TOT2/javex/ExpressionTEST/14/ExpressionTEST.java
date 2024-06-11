package org.junit.jupiter.api;

import org.junit.Test;
import static org.junit.Assert.*;
import org.javex.Expression;
import org.javex.FunctionExpression;
import org.javex.ExpressionStack;
import org.javex.utils.JavexUtils;
import java.util.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("expression");
        List<Expression> expectedFunctions = new ArrayList<>();
        expectedFunctions.add(new FunctionExpression("function1", FunctionExpression.STRING));
        expectedFunctions.add(new FunctionExpression("function2", FunctionExpression.MATH));
        assertEquals(expectedFunctions, expression.getExternalFunctions());
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("expression");
        assertFalse(expression.isPrepared());
        assertEquals(0, expression.getStack().size());
        assertEquals(0, expression.getExternalFunctions().size());
    }

    @Test(expected = Exception.class)
    public void testExpressionConstructorWithInvalidExpression() {
        Expression expression = new Expression(null);
    }

    @Test(expected = Exception.class)
    public void testExpressionConstructorWithEmptyExpression() {
        Expression expression = new Expression("");
    }

    @Test
    public void testPrepare() throws Exception {
        Expression expression = new Expression("(1 + 2) * 3 + 4");
        expression.prepare();
        Object result = expression.evaluate();
        assertEquals(13.0, result);
    }

    @Test(expected = Exception.class)
    public void testPrepareWithInvalidFunction() throws Exception {
        Expression expression = new Expression("invalidFunction()");
        expression.prepare();
        // The prepare method should throw an exception for an invalid function
    }

    @Test(expected = Exception.class)
    public void testPrepareWithDivisionByZero() throws Exception {
        Expression expression = new Expression("5 / 0");
        expression.prepare();
        // The prepare method should throw an exception for division by zero
    }

    @Test
    public void testPrepareWithParentheses() throws Exception {
        Expression expression = new Expression("(1 + 2) * 3 + 4");
        expression.prepare();
        Object result = expression.evaluate();
        assertEquals(13.0, result);
    }

    @Test(expected = Exception.class)
    public void testPrepareWithInvalidOperator() throws Exception {
        Expression expression = new Expression("5 % 2");
        expression.prepare();
        // The prepare method should throw an exception for an invalid operator
    }

    @Test
    public void testPrepareWithStringFunction() throws Exception {
        Expression expression = new Expression("Length(concat('hello', 'world'))");
        expression.prepare();
        Object result = expression.evaluate();
        assertEquals(11.0, result);
    }

    @Test(expected = Exception.class)
    public void testPrepareWithInvalidStringFunction() throws Exception {
        Expression expression = new Expression("Length(InvalidStringFunction('hello', 'world'))");
        expression.prepare();
        // The prepare method should throw an exception for an invalid string function
    }

    @Test
    public void testPrepareWithDateFunction() throws Exception {
        Expression expression = new Expression("DateAdd(Date(2022, 1, 1), Interval('1 year'))");
        expression.prepare();
        Object result = expression.evaluate();
        assertEquals(new Date(2023, 1, 1), result);
    }

    @Test(expected = Exception.class)
    public void testPrepareWithInvalidDateFunction() throws Exception {
        Expression expression = new Expression("DateAdd(Date(2022, 1, 1), Interval('invalid interval'))");
        expression.prepare();
        // The prepare method should throw an exception for an invalid date function
    }

    @Test
    public void testPrepareWithNestedParentheses() throws Exception {
        Expression expression = new Expression("(1 + (2 + 3)) / 2");
        expression.prepare();
        Object result = expression.evaluate();
        assertEquals(2.6, result);
    }
}