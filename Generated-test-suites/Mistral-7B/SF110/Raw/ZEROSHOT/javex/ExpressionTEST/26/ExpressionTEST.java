package org.javex.expression;

import static org.junit.Assert.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.utils.JavexUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExpressionTest {

    @org.junit.Test
    public void testExpressionConstruction() {
        Expression e = new Expression("(1 + 2) * 3");
        assertFalse(e.isPrepared());
        assertEquals("(1+2)*3", e.getExpression());
        assertEquals(3, e.getStack().size());
    }

    @org.junit.Test
    public void testTokenize() {
        Expression e = new Expression("(1 + 2) * 3");
        e.tokenize();
        List<String> expected = Arrays.asList("(", "1", "+", "2", "*", "3", ")");
        assertEquals(expected, e.getStack());
    }

    @org.junit.Test
    public void testPrepare() throws Exception {
        Expression e = new Expression("(1 + 2) * 3");
        e.prepare();
        assertEquals(6, e.getStack().size());
        assertTrue(e.isPrepared());
    }

    @org.junit.Test(expected = Exception.class)
    public void testPrepareWithInvalidExpression() throws Exception {
        Expression e = new Expression("(1 + 2) * 3)");
        e.prepare();
        fail();
    }

    @org.junit.Test
    public void testIsReservedFunction() {
        Expression e = new Expression("(1 + and(2, 3))");
        assertTrue(e.isReservedFunction("and"));
    }

    @org.junit.Test
    public void testIsLogicalFunction() {
        Expression e = new Expression("logical.and(1, 2)");
        assertTrue(e.isLogicalFunction("and"));
    }

    @org.junit.Test
    public void testIsMathFunction() {
        Expression e = new Expression("math.sqrt(9)");
        assertTrue(e.isMathFunction("sqrt"));
    }

    @org.junit.Test
    public void testIsDateFunction() {
        Expression e = new Expression("date.add(1, '2022-01-01', 'day')");
        assertTrue(e.isDateFunction("add"));
    }

    @org.junit.Test
    public void testIsStringFunction() {
        Expression e = new Expression("string.length('hello')");
        assertTrue(e.isStringFunction("length"));
    }

    @org.junit.Test
    public void testGetFunction() {
        Expression e = new Expression("logical.and(1, 2)");
        FunctionExpression and = e.getFunction("and");
        assertNotNull(and);
        assertEquals("and", and.getSymbol());
        assertEquals(FunctionExpression.LOGICAL, and.getType());
    }

    @org.junit.Test
    public void testIsFunction() {
        Expression e = new Expression("logical.and(1, 2)");
        assertTrue(e.isFunction("and"));
    }

    @org.junit.Test
    public void testGetExternalFunction() {
        Expression e = new Expression("myFunction(1, 2)");
        FunctionExpression myFunction = new FunctionExpression("myFunction", FunctionExpression.USER_DEFINED);
        e.setExternalFunctions(Collections.singletonList(myFunction));
        FunctionExpression result = e.getExternalFunction("myFunction");
        assertNotNull(result);
        assertEquals("myFunction", result.getSymbol());
    }

    @org.junit.Test
    public void testEvaluate() throws Exception {
        Expression e = new Expression("1 + 2");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(3, result);
    }

    @org.junit.Test(expected = Exception.class)
    public void testEvaluateWithInvalidExpression() throws Exception {
        Expression e = new Expression("(1 + 2) * (3 + 4) )");
        e.prepare();
        e.evaluate();
        fail();
    }

    @org.junit.Test
    public void testEvaluateWithLogicalFunction() throws Exception {
        Expression e = new Expression("logical.and(1, 2)");
        e.prepare();
        Object result = e.evaluate();
        assertTrue(result);
    }

    @org.junit.Test
    public void testEvaluateWithMathFunction() throws Exception {
        Expression e = new Expression("math.sqrt(9) * 2");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(6.0, result);
    }

    @org.junit.Test
    public void testEvaluateWithDateFunction() throws Exception {
        Expression e = new Expression("date.add(1, '2022-01-01', 'day')");
        e.prepare();
        Object result = e.evaluate();
        assertEquals("2022-01-02", result.toString());
    }

    @org.junit.Test
    public void testEvaluateWithStringFunction() throws Exception {
        Expression e = new Expression("string.length('hello')");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(5, result);
    }
}