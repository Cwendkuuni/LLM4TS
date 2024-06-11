package org.javex.Expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testExpressionConstruction() {
        String expression = "1+2*3";
        Expression e = new Expression(expression);
        assertEquals("1+2*3", e.getExpression());
        assertEquals(0, e.getStack().size());
        assertFalse(e.isPrepared());
    }

    @Test
    public void testIsReservedFunction() {
        Expression e = new Expression("1+and(2,3)");
        assertTrue(e.isReservedFunction("and"));

        e = new Expression("1+or(2,3)");
        assertTrue(e.isReservedFunction("or"));

        e = new Expression("1+isFunction(2)");
        assertTrue(e.isReservedFunction("isFunction"));

        e = new Expression("1+isNull(2)");
        assertTrue(e.isReservedFunction("isNull"));

        e = new Expression("1+isNullFunction()");
        assertFalse(e.isReservedFunction("isNullFunction"));

        e = new Expression("1+not(2)");
        assertTrue(e.isReservedFunction("not"));

        e = new Expression("1+notChar(2)");
        assertTrue(e.isReservedFunction("notChar"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression e = new Expression("1+and(2,3)");
        assertTrue(e.isLogicalFunction("and"));

        e = new Expression("1+or(2,3)");
        assertTrue(e.isLogicalFunction("or"));

        e = new Expression("1+not(2)");
        assertTrue(e.isLogicalFunction("not"));

        e = new Expression("1+notChar(2)");
        assertTrue(e.isLogicalFunction("notChar"));

        e = new Expression("1+isFunction(2)");
        assertFalse(e.isLogicalFunction("isFunction"));

        e = new Expression("1+isNull(2)");
        assertFalse(e.isLogicalFunction("isNull"));

        e = new Expression("1+isFunction(2) and 3=3");
        assertTrue(e.isLogicalFunction("and"));
    }

    @Test
    public void testIsMathFunction() {
        Expression e = new Expression("1+abs(2)");
        assertTrue(e.isMathFunction("abs"));

        e = new Expression("1+divide(2,3)");
        assertTrue(e.isMathFunction("divide"));

        e = new Expression("1+mult(2,3)");
        assertTrue(e.isMathFunction("mult"));

        e = new Expression("1+power(2,3)");
        assertTrue(e.isMathFunction("power"));

        e = new Expression("1+sqrt(2)");
        assertTrue(e.isMathFunction("sqrt"));

        e = new Expression("1+subtract(2,3)");
        assertTrue(e.isMathFunction("subtract"));

        e = new Expression("1+add(2,3)");
        assertFalse(e.isMathFunction("add"));
    }

    @Test
    public void testIsDateFunction() {
        Expression e = new Expression("1+dateAdd(now(), '1 day')");
        assertTrue(e.isDateFunction("dateAdd"));

        e = new Expression("1+dateDiff(now(), '2022-12-31')");
        assertTrue(e.isDateFunction("dateDiff"));

        e = new Expression("1+length(now())");
        assertTrue(e.isDateFunction("length"));

        e = new Expression("1+substring(now(), 1, 5)");
        assertTrue(e.isDateFunction("substring"));

        e = new Expression("1+toLower(now())");
        assertTrue(e.isDateFunction("toLower"));

        e = new Expression("1+toUpper(now())");
        assertTrue(e.isDateFunction("toUpper"));

        e = new Expression("1+abs(now())");
        assertFalse(e.isDateFunction("abs"));
    }

    @Test
    public void testIsStringFunction() {
        Expression e = new Expression("1+contains(concat('hello', ' world'), ' world')");
        assertTrue(e.isStringFunction("contains"));

        e = new Expression("1+equals(concat('hello', ' '), 'hello ')");
        assertTrue(e.isStringFunction("equals"));

        e = new Expression("1+startsWith(concat('hello', ' world'), 'hello')");
        assertTrue(e.isStringFunction("startsWith"));

        e = new Expression("1+matches(concat('hello', ' world'), '.*o.*')");
        assertTrue(e.isStringFunction("matches"));

        e = new Expression("1+length(concat('hello', ' world'))");
        assertTrue(e.isStringFunction("length"));

        e = new Expression("1+substring(concat('hello', ' world'), 1, 5)");
        assertTrue(e.isStringFunction("substring"));

        e = new Expression("1+toLower(concat('HELLO', ' WORLD'))");
        assertTrue(e.isStringFunction("toLower"));

        e = new Expression("1+toUpper(concat('HELLO', ' WORLD'))");
        assertTrue(e.isStringFunction("toUpper"));

        e = new Expression("1+abs(concat('hello', ' world'))");
        assertFalse(e.isStringFunction("abs"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression e = new Expression("1+myCustomFunction(2, 3)");
        List<FunctionExpression> functions = Arrays.asList(new MyCustomFunctionExpression("myCustomFunction"));
        e.setExternalFunctions(functions);

        FunctionExpression function = e.getExternalFunction("myCustomFunction");
        assertNotSame(null, function);
        assertEquals("myCustomFunction", function.getSymbol());
    }

    @Test
    public void testPrepareExpression() throws Exception {
        Expression e = new Expression("1+2*3");
        e.prepare();

        ExpressionStack stack = e.getStackCopy();
        List<String> expected = new ArrayList<>();
        expected.add("1");
        expected.add("+");
        expected.add("2");
        expected.add("*");
        expected.add("3");

        assertEquals(expected.size(), stack.size());
        for (int i = 0; i < stack.size(); ++i) {
            assertEquals(expected.get(i), stack.get(i));
        }

        assertTrue(e.isPrepared());

        try {
            e.prepare();
            fail("Expression should be prepared already");
        } catch (Exception ex) {
            // expected
        }
    }

    @Test(expected = Exception.class)
    public void testPrepareExpressionWithInvalidExpression() throws Exception {
        Expression e = new Expression("1+");
        e.prepare();
        fail("An exception should be thrown");
    }

    @Test
    public void testEvaluateExpression() throws Exception {
        Expression e = new Expression("1+2*3");
        e.prepare();

        Object result = e.evaluate();

        assertEquals(7.0, result);
    }

    @Test
    public void testEvaluateExpressionWithLogicalFunction() throws Exception {
        Expression e = new Expression("1+and(2, true)");;
        e.prepare();

        Object result = e.evaluate();

        assertTrue(result);
    }

    @Test
    public void testEvaluateExpressionWithMathFunction() throws Exception {
        Expression e = new Expression("1+sqrt(16)");
        e.prepare();

        Object result = e.evaluate();

        assertEquals(4.0, result);
    }

    @Test
    public void testEvaluateExpressionWithDateFunction() throws Exception {
        Expression e = new Expression("1+dateAdd(now(), '1 day')");
        e.prepare();

        Object result = e.evaluate();

        Map<String, Object> expected = new HashMap<>();
        expected.put("day", 1);
        expected.put("month", 1);
        expected.put("year", 2023);

        assertEquals(expected, result);
    }

    @Test
    public void testEvaluateExpressionWithStringFunction() throws Exception {
        Expression e = new Expression("1+substring(concat('hello', ' world'), 1, 5)");
        e.prepare();

        Object result = e.evaluate();

        assertEquals("hello", result);
    }

    @Test
    public void testEvaluateExpressionWithCustomFunction() throws Exception {
        Expression e = new Expression("1+myCustomFunction(2, 3, 4)");
        List<FunctionExpression> functions = Arrays.asList(new MyCustomFunctionExpression("myCustomFunction"));
        e.setExternalFunctions(functions);
        e.prepare();

        Object result = e.evaluate();

        assertEquals(10.0, result);
    }

    private static class MyCustomFunctionExpression implements FunctionExpression {
        private final String symbol;

        public MyCustomFunctionExpression(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public FunctionExpressionType getType() {
            return FunctionExpressionType.CUSTOM;
        }

        @Override
        public String getSymbol() {
            return symbol;
        }

        @Override
        public Object evaluate(Map<String, Object> context) throws Exception {
            Object a = context.get("a");
            Object b = context.get("b");
            Object c = context.get("c");

            return a + ((Number) b).doubleValue() + ((Number) c).doubleValue();
        }
    }
}