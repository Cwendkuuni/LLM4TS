package org.javex.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.utils.JavexUtils;
import org.javex.functions.FunctionExpression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.javex.functions.CustomFunction;
import org.javex.exceptions.ExpressionEvaluationException;
import org.javex.exceptions.ExpressionPreparationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testExpressionConstruction() {
        Expression e = new Expression("1 + 2");
        assertFalse(e.isPrepared());
        assertEquals("1 + 2", e.getExpression());
        e.prepare();
        assertTrue(e.isPrepared());
    }

    @Test
    public void testGetExpression() {
        Expression e = new Expression("1 + 2");
        assertEquals("1 + 2", e.getExpression());
    }

    @Test
    public void testGetStack() {
        Expression e = new Expression("1 + 2");
        ExpressionStack stack = e.getStackCopy();
        assertEquals(6, stack.size());
        assertTrue(stack.contains("1"));
        assertTrue(stack.contains("+"));
        assertTrue(stack.contains("2"));
    }

    @Test
    public void testIsReservedFunction() {
        Expression e = new Expression("a = b and c = d");
        assertTrue(e.isReservedFunction("and"));
        assertTrue(e.isReservedFunction("="));
        assertFalse(e.isReservedFunction("="));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression e = new Expression("a = and(b, c)");
        FunctionExpression andFunction = new AndFunction();
        assertTrue(e.isLogicalFunction("and"));
        assertEquals(FunctionExpression.LOGICAL, andFunction.getType());
    }

    @Test
    public void testIsMathFunction() {
        Expression e = new Expression("sqrt(9)");
        FunctionExpression sqrtFunction = new SqrtFunction();
        assertTrue(e.isMathFunction("sqrt"));
        assertEquals(FunctionExpression.MATH, sqrtFunction.getType());
    }

    @Test
    public void testIsDateFunction() {
        Expression e = new Expression("dateAdd(now(), interval '1 day')");
        FunctionExpression dateAddFunction = new DateAddFunction();
        assertTrue(e.isDateFunction("dateAdd"));
        assertEquals(FunctionExpression.DATE, dateAddFunction.getType());
    }

    @Test
    public void testIsStringFunction() {
        Expression e = new Expression("upper(lower('Hello World'))");
        FunctionExpression upperFunction = new ToUpperFunction();
        FunctionExpression lowerFunction = new ToLowerFunction();
        assertTrue(e.isStringFunction("upper"));
        assertEquals(FunctionExpression.STRING, upperFunction.getType());
        assertTrue(e.isStringFunction("lower"));
        assertEquals(FunctionExpression.STRING, lowerFunction.getType());
    }

    @Test
    public void testGetFunction() {
        Expression e = new Expression("a = and(b, c)");
        FunctionExpression andFunction = new AndFunction();
        FunctionExpression bFunction = new CustomFunction("b");
        FunctionExpression cFunction = new CustomFunction("c");
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(bFunction);
        functions.add(cFunction);
        e.setExternalFunctions(functions);
        FunctionExpression function = e.getFunction("and");
        assertEquals(andFunction, function);
    }

    @Test
    public void testGetExternalFunction() {
        Expression e = new Expression("a = customFunction(b, c)");
        FunctionExpression customFunction = new CustomFunction("customFunction");
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(customFunction);
        e.setExternalFunctions(functions);
        FunctionExpression function = e.getExternalFunction("customFunction");
        assertEquals(customFunction, function);
    }

    @Test
    public void testPrepare() {
        Expression e = new Expression("1 + 2");
        e.prepare();
        ExpressionStack stack = e.getStackCopy();
        assertEquals(5, stack.size());
        assertTrue(stack.contains("1"));
        assertTrue(stack.contains("+"));
        assertTrue(stack.contains("2"));
        assertTrue(stack.contains("+"));
        assertTrue(stack.contains("3"));
    }

    @Test(expected = ExpressionPreparationException.class)
    public void testPrepareWithInvalidExpression() {
        Expression e = new Expression("1 a + 2");
        e.prepare();
    }

    @Test
    public void testEvaluateNormalExpression() {
        Expression e = new Expression("5 + 3 * 2");
        e.prepare();
        assertEquals(10, e.evaluate());
    }

    @Test
    public void testEvaluateLogicalExpression() {
        Expression e = new Expression("true and false");
        e.prepare();
        assertEquals(false, e.evaluate());
    }

    @Test
    public void testEvaluateMathExpression() {
        Expression e = new Expression("sqrt(25) * 3");
        e.prepare();
        assertEquals(12.0, e.evaluate());
    }

    @Test
    public void testEvaluateDateExpression() {
        Expression e = new Expression("dateAdd(now(), interval '1 day')");
        e.prepare();
        java.util.Date date = new java.util.Date();
        date.setTime(System.currentTimeMillis());
        java.util.Date result = (java.util.Date) e.evaluate();
        assertTrue(result.after(date));
        assertTrue(result.before(new java.util.Date(System.currentTimeMillis() + 86400000)));
    }

    @Test
    public void testEvaluateStringExpression() {
        Expression e = new Expression("upper(lower('Hello World'))");
        e.prepare();
        assertEquals("HELLO WORLD", e.evaluate().toString());
    }

    @Test
    public void testEvaluateCustomFunction() {
        Expression e = new Expression("customFunction(1, 'test')");
        FunctionExpression customFunction = new CustomFunction("customFunction");
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(customFunction);
        e.setExternalFunctions(functions);
        e.prepare();
        assertEquals("customFunction(1, 'test')", e.getExpression());
        Object result = e.evaluate();
        assertEquals(result.getClass(), customFunction.getClass());
    }
}