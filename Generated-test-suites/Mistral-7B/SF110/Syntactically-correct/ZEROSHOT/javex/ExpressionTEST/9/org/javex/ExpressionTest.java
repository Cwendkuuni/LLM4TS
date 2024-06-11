package org.javex.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.javex.functions.FunctionExpression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.utils.ExpressionStack;
import org.javex.utils.JavexUtils;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testConstruction() {
        Expression expression = new Expression("1 + 2 * 3");
        assertFalse(expression.isPrepared());
        assertEquals(3, expression.getStack().size());
        assertEquals("1", expression.getStack().get(0));
        assertEquals("+", expression.getStack().get(1));
        assertEquals("2", expression.getStack().get(2));
        assertEquals("*", expression.getStack().get(3));
        assertEquals("3", expression.getStack().get(4));
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("1 + 2 * 3");
        assertFalse(expression.isFunction("*"));
        assertTrue(expression.isFunction("+"));
        assertFalse(expression.isFunction("A"));
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("1 + and(2, 3)");
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("AND"));
        assertFalse(expression.isReservedFunction("aND"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("1 + and(2, 3)");
        FunctionExpression function = expression.getFunction("and");
        assertSame(AndFunction.class, function.getClass());
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("1 + and(2, 3)");
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("*"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("1 + 2 * 3");
        assertFalse(expression.isMathFunction("+"));
        assertTrue(expression.isMathFunction("*"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("dateAdd(now(), 1)");
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("add"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("toUpper(substring(name, 1, 3))");
        assertTrue(expression.isStringFunction("toUpper"));
        assertTrue(expression.isStringFunction("substring"));
        assertFalse(expression.isStringFunction("toUpperSubstring"));
    }

    @Test
    public void testIsFunctionWithExternalFunctions() {
        Expression expression = new Expression("myFunction(a, b)");
        List<FunctionExpression> functions = Arrays.asList(new MyFunctionExpression("myFunction"));
        expression.setExternalFunctions(functions);
        assertTrue(expression.isFunction("myFunction"));
        assertFalse(expression.isFunction("MyFunction"));
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("1 + 2 * 3");
        expression.prepare();
        assertTrue(expression.isPrepared());
        assertEquals(1, expression.getStack().size());
        assertEquals("1 + 2 * 3", expression.getExpression());
    }

    @Test
    public void testEvaluateSimpleExpression() throws Exception {
        Expression expression = new Expression("1 + 2 * 3");
        expression.prepare();
        Object result = expression.evaluate();
        assertEquals(7, result);
    }

    @Test
    public void testEvaluateExpressionWithLogicalFunctions() throws Exception {
        Expression expression = new Expression("(a > b) and (c > d)");
        expression.setExternalFunctions(Arrays.asList(new AndFunction()));
        expression.setExternalFunctions(Arrays.asList(new MyFunctionExpression("myFunction"), new MyFunctionExpression("myFunction2")));
        expression.prepare();
        Object result = expression.evaluate();
        assertTrue(result);
    }

    @Test
    public void testEvaluateExpressionWithDateFunctions() throws Exception {
        Expression expression = new Expression("dateAdd(now(), 1)");
        expression.prepare();
        Object result = expression.evaluate();
        assertNotNull(result);
    }

    @Test
    public void testEvaluateExpressionWithStringFunctions() throws Exception {
        Expression expression = new Expression("toUpper(substring(name, 1, 3))");
        expression.prepare();
        Object result = expression.evaluate();
        assertNotNull(result);
        assertEquals("name.substring(0, 3).toUpperCase()", expression.getExpression());
    }

    @Test
    public void testEvaluateExpressionWithCustomFunction() throws Exception {
        Expression expression = new Expression("myFunction(a, b)");
        List<FunctionExpression> functions = Arrays.asList(new MyFunctionExpression("myFunction"));
        expression.setExternalFunctions(functions);
        expression.prepare();
        Object result = expression.evaluate();
        assertEquals(42, result);
    }

    @Test
    public void testEvaluateExpressionWithCustomFunctionsAndLogicalFunctions() throws Exception {
        Expression expression = new Expression("myFunction(a, b) and myFunction2(c, d)");
        List<FunctionExpression> functions = Arrays.asList(new AndFunction(), new MyFunctionExpression("myFunction"), new MyFunctionExpression("myFunction2"));
        expression.setExternalFunctions(functions);
        expression.prepare();
        Object result = expression.evaluate();
        assertTrue(result);
    }

    @Test
    public void testEvaluateExpressionWithCustomFunctionsAndLogicalFunctionsAndStringFunctions() throws Exception {
        Expression expression = new Expression("toUpper(myFunction(a, b)) and myFunction2(c, d)");
        List<FunctionExpression> functions = Arrays.asList(new AndFunction(), new MyFunctionExpression("myFunction"), new MyFunctionExpression("myFunction2"), new ToUpperFunction());
        expression.setExternalFunctions(functions);
        expression.prepare();
        Object result = expression.evaluate();
        assertTrue(result);
    }

    @Test
    public void testEvaluateExpressionWithCustomFunctionsAndLogicalFunctionsAndDateFunctions() throws Exception {
        Expression expression = new Expression("dateAdd(myFunction(a, b), 1) and myFunction2(c, d)");
        List<FunctionExpression> functions = Arrays.asList(new AndFunction(), new DateAddFunction(), new MyFunctionExpression("myFunction"), new MyFunctionExpression("myFunction2"));
        expression.setExternalFunctions(functions);
        expression.prepare();
        Object result = expression.evaluate();
        assertNotNull(result);
    }

    @Test
    public void testEvaluateExpressionWithCustomFunctionsAndLogicalFunctionsAndStringFunctionsAndDateFunctions() throws Exception {
        Expression expression = new Expression("toUpper(dateAdd(myFunction(a, b), 1)) and myFunction2(c, d)");
        List<FunctionExpression> functions = Arrays.asList(new AndFunction(), new DateAddFunction(), new ToUpperFunction(), new MyFunctionExpression("myFunction"), new MyFunctionExpression("myFunction2"));
        expression.setExternalFunctions(functions);
        expression.prepare();
        Object result = expression.evaluate();
        assertNotNull(result);
    }

    @Test
    public void testEvaluateExpressionWithCustomFunctionsAndLogicalFunctionsAndStringFunctionsAndDateFunctionsAndCustomFunctions() throws Exception {
        Expression expression = new Expression("myFunction3(toUpper(dateAdd(myFunction(a, b), 1)), myFunction2(c, d)) and myFunction4(e, f)");
        List<FunctionExpression> functions = Arrays.asList(new AndFunction(), new DateAddFunction(), new ToUpperFunction(), new MyFunctionExpression("myFunction"), new MyFunctionExpression("myFunction2"), new MyFunctionExpression("myFunction3"), new MyFunctionExpression("myFunction4"));
        expression.setExternalFunctions(functions);
        expression.prepare();
        Object result = expression.evaluate();
        assertNotNull(result);
    }

    @Test
    public void testEvaluateExpressionWithCustomFunctionsAndLogicalFunctionsAndStringFunctionsAndDateFunctionsAndCustomFunctionsAndExternalFunctions() throws Exception {
        Expression expression = new Expression("myFunction3(toUpper(dateAdd(myFunction(a, b), 1)), myFunction2(c, d)) and myFunction4(e, f) and myExternalFunction(g, h)");
        List<FunctionExpression> functions = Arrays.asList(new AndFunction(), new DateAddFunction(), new ToUpperFunction(), new MyFunctionExpression("myFunction"), new MyFunctionExpression("myFunction2"), new MyFunctionExpression("myFunction3"), new MyFunctionExpression("myFunction4"), new MyExternalFunctionExpression("myExternalFunction"));
        expression.setExternalFunctions(functions);
        expression.prepare();
        Object result = expression.evaluate();
        assertNotNull(result);
    }

    @Test
    public void testEvaluateExpressionWithCustomFunctionsAndLogicalFunctionsAndStringFunctionsAndDateFunctionsAndCustomFunctionsAndExternalFunctionsAndCustomFunctions() throws Exception {
        Expression expression = new Expression("myFunction3(toUpper(dateAdd(myFunction(a, b), 1)), myFunction2(c, d)) and myFunction4(e, f) and myFunction5(g, h) and myFunction6(i, j)");
        List<FunctionExpression> functions = Arrays.asList(new AndFunction(), new DateAddFunction(), new ToUpperFunction(), new MyFunctionExpression("myFunction"), new MyFunctionExpression("myFunction2"), new MyFunctionExpression("myFunction3"), new MyFunctionExpression("myFunction4"), new MyFunctionExpression("myFunction5"), new MyFunctionExpression("myFunction6"));
        expression.setExternalFunctions(functions);
        expression.prepare();
        Object result = expression.evaluate();
        assertNotNull(result);
    }

    private static class MyFunctionExpression implements FunctionExpression {

        private final String symbol;

        public MyFunctionExpression(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public Object evaluate(List<Object> arguments) throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object evaluate(Object object) throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getSimbol() {
            return symbol;
        }

        @Override
        public int getType() {
            return 0;
        }
    }

    private static class MyExternalFunctionExpression implements FunctionExpression {

        private final String symbol;

        public MyExternalFunctionExpression(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public Object evaluate(List<Object> arguments) throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object evaluate(Object object) throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getSimbol() {
            return symbol;
        }

        @Override
        public int getType() {
            return 0;
        }
    }
}