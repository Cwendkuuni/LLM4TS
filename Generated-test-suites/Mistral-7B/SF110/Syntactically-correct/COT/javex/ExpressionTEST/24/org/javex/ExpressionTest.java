package org.javex.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("1 + 2");
        assertFalse(expression.isPrepared());
        assertEquals(1, expression.getStack().size());
        assertEquals("1", expression.getStack().get(0));
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("1 + 2");
        expression.doEvaluationStack();
        assertTrue(expression.isPrepared());
        assertEquals(2, expression.getStack().size());
        assertEquals("1", expression.getStack().get(0));
        assertEquals("2", expression.getStack().get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testDoEvaluationStackWithEmptyExpression() {
        Expression expression = new Expression("");
        expression.doEvaluationStack();
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("and(1,2)");
        FunctionExpression function = expression.getFunction("and");
        assertNotNull(function);
        assertEquals(FunctionExpression.LOGICAL, function.getType());
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("and(1,2)");
        assertTrue(expression.isFunction("and"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("and(1,2)");
        assertTrue(expression.isLogicalFunction("and"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("sqrt(4)");
        assertTrue(expression.isMathFunction("sqrt"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("DateAdd(Date('2022-01-01'), 1, 'day')");
        assertTrue(expression.isDateFunction("DateAdd"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("ToUpper(concat('Hello', 'World'))");
        assertTrue(expression.isStringFunction("ToUpper"));
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("and(1,2)");
        assertTrue(expression.isReservedFunction("and"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("myCustomFunction(1,2)");
        FunctionExpression function = expression.getExternalFunction("myCustomFunction");
        assertNull(function);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("myCustomFunction(1,2)");
        assertFalse(expression.isExternalFunction("myCustomFunction"));
    }

    @Test
    public void testStackMethods() {
        Expression expression = new Expression("1 + 2 * 3 + 4");
        expression.prepare();
        ExpressionStack stack = expression.getStackCopy();
        assertEquals(6, stack.size());
        assertEquals("1", stack.get(0));
        assertEquals("2", stack.get(1));
        assertEquals("*", stack.get(2));
        assertEquals("3", stack.get(3));
        assertEquals("+", stack.get(4));
        assertEquals("4", stack.get(5));
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression("1 + 2 * 3 + 4");
        expression.prepare();
        double result = (double) expression.evaluate();
        assertEquals(7.0, result, 0.0);
    }

    @Test(expected = Exception.class)
    public void testEvaluateWithInvalidExpression() {
        Expression expression = new Expression("1 + 2 *");
        expression.prepare();
        expression.evaluate();
    }

    @Test(expected = Exception.class)
    public void testEvaluateWithInvalidFunction() {
        Expression expression = new Expression("invalidFunction(1,2)");
        expression.prepare();
        expression.evaluate();
    }

    @Test(expected = Exception.class)
    public void testEvaluateWithDivisionByZero() {
        Expression expression = new Expression("10 / 0");
        expression.prepare();
        expression.evaluate();
    }

    @Test
    public void testPutFunction() {
        Map<FunctionExpression, FunctionExpression> reservedFunctions = new HashMap<>();
        Expression.putFunction(new AndFunction(), reservedFunctions);
        FunctionExpression andFunction = reservedFunctions.get(new AndFunction());
        assertNotNull(andFunction);
        assertEquals(FunctionExpression.LOGICAL, andFunction.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testPutFunctionWithNullFunction() {
        Map<FunctionExpression, FunctionExpression> reservedFunctions = new HashMap<>();
        Expression.putFunction(null, reservedFunctions);
    }
}