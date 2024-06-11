package org.javex.test;

import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.FunctionExpression;
import org.javex.functions.LogicalFunction;
import org.javex.functions.MathFunction;
import org.javex.functions.DateFunction;
import org.javex.functions.StringFunction;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        List<FunctionExpression> externalFunctions = expression.getExternalFunctions();
        Assert.assertNotNull(externalFunctions);
        Assert.assertEquals(0, externalFunctions.size());
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("1 + 2 * 3");
        Assert.assertNotNull(expression);
        ExpressionStack stack = expression.getStackCopy();
        Assert.assertEquals(5, stack.size());
        Assert.assertEquals("1", stack.get(0));
        Assert.assertEquals("+", stack.get(1));
        Assert.assertEquals("2", stack.get(2));
        Assert.assertEquals("*", stack.get(3));
        Assert.assertEquals("3", stack.get(4));
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("1 + 2 * 3");
        expression.prepare();
        ExpressionStack stack = expression.getStackCopy();
        Object result = null;
        try {
            result = expression.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(result);
        Assert.assertEquals(7.0, result);
        Assert.assertEquals(5, stack.size());
        Assert.assertEquals("1", stack.get(0));
        Assert.assertEquals("+", stack.get(1));
        Assert.assertEquals("2", stack.get(2));
        Assert.assertEquals("*", stack.get(3));
        Assert.assertEquals("3", stack.get(4));
        Assert.assertEquals(0, stack.size());
    }

    @Test
    public void testIsFunctionWithReservedFunction() {
        Expression expression = new Expression("and(1,2)");
        Assert.assertTrue(expression.isFunction("and"));
    }

    @Test
    public void testIsFunctionWithExternalFunction() {
        Expression expression = new Expression("myFunction(1)");
        expression.setExternalFunctions(new ArrayList<FunctionExpression>() {{
            add(new MyFunction());
        }});
        Assert.assertTrue(expression.isFunction("myFunction"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsFunctionWithInvalidSymbol() {
        Expression expression = new Expression("test");
        Assert.assertFalse(expression.isFunction("invalidSymbol"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("and(true, false)");
        Assert.assertTrue(expression.isLogicalFunction("and"));

        expression = new Expression("or(true, false)");
        Assert.assertTrue(expression.isLogicalFunction("or"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("abs(-5)");
        Assert.assertTrue(expression.isMathFunction("abs"));

        expression = new Expression("sqrt(9)");
        Assert.assertTrue(expression.isMathFunction("sqrt"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("dateAdd(date('2022-01-01'), interval '1 day')");
        Assert.assertTrue(expression.isDateFunction("dateAdd"));

        expression = new Expression("dateDiff(date('2022-01-01'), date('2022-01-02'))");
        Assert.assertTrue(expression.isDateFunction("dateDiff"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("length('test')");
        Assert.assertTrue(expression.isStringFunction("length"));

        expression = new Expression("subString('test', 1, 2)");
        Assert.assertTrue(expression.isStringFunction("subString"));
    }

    @Test
    public void testGetFunctionWithReservedFunction() {
        Expression expression = new Expression("and(true, false)");
        FunctionExpression function = expression.getFunction("and");
        Assert.assertNotNull(function);
        Assert.assertTrue(function instanceof LogicalFunction);
    }

    @Test
    public void testGetFunctionWithExternalFunction() {
        Expression expression = new Expression("myFunction(1)");
        expression.setExternalFunctions(new ArrayList<FunctionExpression>() {{
            add(new MyFunction());
        }});
        FunctionExpression function = expression.getFunction("myFunction");
        Assert.assertNotNull(function);
        Assert.assertTrue(function instanceof MyFunction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFunctionWithInvalidSymbol() {
        Expression expression = new Expression("test");
        Assert.assertNull(expression.getFunction("invalidSymbol"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFunctionWithNonExistentFunction() {
        Expression expression = new Expression("testFunction");
        Assert.assertNull(expression.getFunction("testFunction"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetFunctionWithNullExpression() {
        Expression expression = null;
        Assert.assertNull(expression.getFunction("test"));
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("1 + 2 * 3");
        expression.prepare();
        ExpressionStack stack = expression.getStackCopy();
        Object result = null;
        try {
            result = expression.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(result);
        Assert.assertEquals(7.0, result);
        Assert.assertEquals(0, stack.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrepareWithInvalidExpression() {
        Expression expression = new Expression("testFunction");
        Assert.assertThrows(IllegalArgumentException.class, () -> expression.prepare());
    }

    @Test
    public void testEvaluateWithBasicArithmeticExpression() {
        Expression expression = new Expression("1 + 2 * 3");
        Object result = null;
        try {
            result = expression.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(result);
        Assert.assertEquals(7.0, result);
    }

    @Test
    public void testEvaluateWithBasicLogicalExpression() {
        Expression expression = new Expression("true and false");
        Object result = null;
        try {
            result = expression.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(result);
        Assert.assertEquals(false, result);

        expression = new Expression("true or false");
        result = null;
        try {
            result = expression.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(result);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testEvaluateWithBasicDateExpression() {
        Expression expression = new Expression("dateAdd(date('2022-01-01'), interval '1 day')");
        Object result = null;
        try {
            result = expression.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getClass().getDeclaredField("value"));
        Assert.assertTrue(result.getClass() instanceof Date);
    }

    @Test
    public void testEvaluateWithBasicStringExpression() {
        Expression expression = new Expression("length('test')");
        Object result = null;
        try {
            result = expression.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(result);
        Assert.assertEquals(4, result.intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateWithInvalidExpression() {
        Expression expression = new Expression("testFunction");
        Assert.assertThrows(IllegalArgumentException.class, () -> expression.evaluate());
    }

    @Test(expected = NullPointerException.class)
    public void testEvaluateWithNullExpression() {
        Expression expression = null;
        Assert.assertThrows(NullPointerException.class, () -> expression.evaluate());
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("and(true, false)");
        Assert.assertTrue(expression.isReservedFunction("and"));

        expression = new Expression("or(true, false)");
        Assert.assertTrue(expression.isReservedFunction("or"));
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("myFunction(1)");
        expression.setExternalFunctions(new ArrayList<FunctionExpression>() {{
            add(new MyFunction());
        }});
        Assert.assertTrue(expression.isExternalFunction("myFunction"));
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("and(true, false)");
        Assert.assertTrue(expression.isFunction("and"));

        expression = new Expression("myFunction(1)");
        expression.setExternalFunctions(new ArrayList<FunctionExpression>() {{
            add(new MyFunction());
        }});
        Assert.assertTrue(expression.isFunction("myFunction"));
    }

    @Test
    public void testPutFunction() {
        Map<FunctionExpression, FunctionExpression> reservedFunctions = new HashMap<>();
        Expression.putFunction(new LogicalFunction("and"), reservedFunctions);
        FunctionExpression function = reservedFunctions.get(new LogicalFunction("and"));
        Assert.assertNotNull(function);
        Assert.assertTrue(function instanceof LogicalFunction);
        Assert.assertEquals("and", function.getSimbol());
    }

    @Test
    public void testGetReservedFunctionsWithEmptyMap() {
        Map<FunctionExpression, FunctionExpression> reservedFunctions = new HashMap<>();
        FunctionExpression function = Expression.getReservedFunctions("and");
        Assert.assertNull(function);
    }

    @Test
    public void testGetReservedFunctionsWithFunctionPresent() {
        Map<FunctionExpression, FunctionExpression> reservedFunctions = new HashMap<>();
        Expression.putFunction(new LogicalFunction("and"), reservedFunctions);
        FunctionExpression function = Expression.getReservedFunctions("and");
        Assert.assertNotNull(function);
        Assert.assertTrue(function instanceof LogicalFunction);
        Assert.assertEquals("and", function.getSimbol());
    }

    @Test
    public void testGetReservedFunctionsWithNonExistentFunction() {
        Map<FunctionExpression, FunctionExpression> reservedFunctions = new HashMap<>();
        FunctionExpression function = Expression.getReservedFunctions("or");
        Assert.assertNull(function);
    }

    @Test
    public void testGetReservedFunctionsWithNullMap() {
        Map<FunctionExpression, FunctionExpression> reservedFunctions = null;
        FunctionExpression function = Expression.getReservedFunctions("and");
        Assert.assertNull(function);
    }

    static class MyFunction extends FunctionExpression {
        public MyFunction() {
            super("myFunction", FunctionExpression.USER_DEFINED);
        }

        @Override
        public Object evaluate(Map<String, Object> context) throws Exception {
            Object arg = context.get("arg");
            if (arg instanceof Number) {
                return ((Number) arg).intValue() * 2;
            }
            throw new IllegalArgumentException("Invalid argument type: " + arg.getClass());
        }
    }
}