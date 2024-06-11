package org.javex.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.javex.functions.FunctionExpression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.utils.Expression;
import org.javex.utils.ExpressionStack;
import org.javex.utils.JavexUtils;
import org.javex.utils.Utils;

public class ExpressionTest {

    @Test
    public void testExpressionConstruction() {
        final Expression expr = new Expression("1 + 2 * 3");
        assertEquals("1 + 2 * 3", expr.getExpression());
        assertFalse(expr.isPrepared());
        assertEquals(0, expr.getExternalFunctions().size());
    }

    @Test
    public void testEmptyExpression() {
        final Expression expr = new Expression("");
        assertEquals("", expr.getExpression());
        assertFalse(expr.isPrepared());
        assertEquals(0, expr.getExternalFunctions().size());
    }

    @Test
    public void testPrepare() {
        final Expression expr = new Expression("1 + 2 * 3");
        expr.prepare();
        assertEquals(Arrays.asList("1", "2", "*", "3", "+"), expr.getStackCopy().toList());
        assertTrue(expr.isPrepared());
    }

    @Test(expected = Exception.class)
    public void testPrepareWithInvalidExpression() {
        final Expression expr = new Expression("1 + 2 * 3)");
        expr.prepare();
    }

    @Test
    public void testEvaluate() throws Exception {
        final Expression expr = new Expression("1 + 2 * 3");
        expr.prepare();
        final Object result = expr.evaluate();
        assertEquals(7.0, result);
    }

    @Test
    public void testEvaluateWithExternalFunctions() throws Exception {
        final List externalFunctions = new ArrayList();
        externalFunctions.add(new AndFunction());
        final Expression expr = new Expression("a and b");
        expr.setExternalFunctions(externalFunctions);
        expr.prepare();
        final Object result = expr.evaluate();
        assertTrue(result);
    }

    @Test
    public void testIsFunction() {
        final Expression expr = new Expression("1 + 2 * 3");
        expr.prepare();
        assertTrue(expr.isFunction("+"));
        assertTrue(expr.isFunction("*"));
    }

    @Test
    public void testIsLogicalFunction() {
        final Expression expr = new Expression("a and b");
        expr.prepare();
        assertTrue(expr.isLogicalFunction("and"));
    }

    @Test
    public void testIsMathFunction() {
        final Expression expr = new Expression("1 + 2 * 3");
        expr.prepare();
        assertTrue(expr.isMathFunction("+"));
        assertTrue(expr.isMathFunction("*"));
    }

    @Test
    public void testIsDateFunction() {
        final Expression expr = new Expression("DateAdd(Date('2022-01-01'), Day(3))");
        expr.prepare();
        assertTrue(expr.isDateFunction("DateAdd"));
    }

    @Test
    public void testIsStringFunction() {
        final Expression expr = new Expression("ToUpper('Hello')");
        expr.prepare();
        assertTrue(expr.isStringFunction("ToUpper"));
    }

    @Test
    public void testGetFunction() {
        final Expression expr = new Expression("1 + 2 * 3");
        expr.prepare();
        final FunctionExpression plusFunction = Utils.getReservedFunctions("+");
        final FunctionExpression multiplyFunction = Utils.getReservedFunctions("*");
        assertEquals(plusFunction, expr.getFunction("+"));
        assertEquals(multiplyFunction, expr.getFunction("*"));
    }

    @Test
    public void testGetExternalFunction() {
        final List externalFunctions = new ArrayList();
        externalFunctions.add(new AndFunction());
        final Expression expr = new Expression("a and b");
        expr.setExternalFunctions(externalFunctions);
        expr.prepare();
        final FunctionExpression andFunction = Utils.getReservedFunctions("and");
        assertEquals(andFunction, expr.getExternalFunction("and"));
    }

    @Test
    public void testIsExternalFunction() {
        final List externalFunctions = new ArrayList();
        externalFunctions.add(new AndFunction());
        final Expression expr = new Expression("a and b");
        expr.setExternalFunctions(externalFunctions);
        expr.prepare();
        assertTrue(expr.isExternalFunction("and"));
    }

    @Test
    public void testIsReservedFunction() {
        final Expression expr = new Expression("1 + 2 * 3");
        expr.prepare();
        assertTrue(expr.isReservedFunction("+"));
        assertTrue(expr.isReservedFunction("*"));
    }

    @Test
    public void testTokenize() {
        final Expression expr = new Expression("1 + 2 * 3");
        expr.prepare();
        assertEquals(Arrays.asList("1", "+", "2", "*", "3"), expr.getStackCopy().toList());
    }

    @Test
    public void testGetStackCopy() {
        final Expression expr = new Expression("1 + 2 * 3");
        expr.prepare();
        final ExpressionStack stack = expr.getStackCopy();
        assertEquals(Arrays.asList("1", "+", "2", "*", "3"), stack.toList());
    }
}