package org.javex.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.javex.utils.JavexUtils;
import org.javex.functions.FunctionExpression;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import org.javex.Expression;
import java.util.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression e = new Expression("test");
        List<FunctionExpression> externalFunctions = e.getExternalFunctions();
        assertNull(externalFunctions);
    }

    @Test
    public void testExpressionConstructor() {
        Expression e = new Expression("(1+2)*3");
        assertFalse(e.isPrepared());
        assertEquals(6, e.stackSize());
        // Test evaluation in next test methods
    }

    @Test
    public void testDoEvaluationStack() {
        Expression e = new Expression("(1+2)*3");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(6, result);
    }

    @Test(expected = Exception.class)
    public void testEvaluateException() {
        Expression e = new Expression("1+)");
        e.prepare();
        e.evaluate();
        // This test should throw an exception
    }

    @Test
    public void testIsFunction() {
        Expression e = new Expression("test");
        assertFalse(e.isFunction("+"));
        Expression e1 = new Expression("1+2");
        assertTrue(e1.isFunction("+"));
    }

    @Test
    public void testIsReservedFunction() {
        assertTrue(Expression.isReservedFunction("and"));
        assertFalse(Expression.isReservedFunction("test"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression e = new Expression("test");
        assertFalse(e.isLogicalFunction("and"));
        Expression e1 = new Expression("a and b");
        assertTrue(e1.isLogicalFunction("and"));
    }

    @Test
    public void testIsMathFunction() {
        Expression e = new Expression("test");
        assertFalse(e.isMathFunction("+"));
        Expression e1 = new Expression("1+2");
        assertTrue(e1.isMathFunction("+"));
    }

    @Test
    public void testIsDateFunction() {
        Expression e = new Expression("test");
        assertFalse(e.isDateFunction("DateAdd"));
        Expression e1 = new Expression("DateAdd(date1, interval '1 day')");
        assertTrue(e1.isDateFunction("DateAdd"));
    }

    @Test
    public void testIsStringFunction() {
        Expression e = new Expression("test");
        assertFalse(e.isStringFunction("SubString"));
        Expression e1 = new Expression("SubString(string1, 2, 3)");
        assertTrue(e1.isStringFunction("SubString"));
    }

    @Test
    public void testGetFunction() {
        Expression e = new Expression("test");
        FunctionExpression function = e.getFunction("and");
        assertNull(function);
        Expression e1 = new Expression("a and b");
        FunctionExpression function1 = e1.getFunction("and");
        assertNotNull(function1);
    }

    @Test
    public void testGetReservedFunctions() {
        Expression e = new Expression("test");
        FunctionExpression function = Expression.getReservedFunctions("and");
        assertNotNull(function);
        assertEquals(AndFunction.class, function.getClass());
    }

}