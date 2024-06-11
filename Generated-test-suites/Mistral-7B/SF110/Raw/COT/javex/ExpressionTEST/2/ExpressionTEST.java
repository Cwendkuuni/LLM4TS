package org.javex.test;

import org.junit.Assert;
import org.junit.Test;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("1 + 2");
        Assert.assertNotNull(expression);
        Assert.assertEquals(1, expression.getStack().size());
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("1 + and(2, 3)");
        Assert.assertTrue(expression.isFunction("and"));
        Assert.assertFalse(expression.isFunction("asd"));
    }

    @Test
    public void testGetReservedFunctions() {
        String simbol = "and";
        FunctionExpression function = Expression.getReservedFunctions(simbol);
        Assert.assertNotNull(function);
        Assert.assertEquals(FunctionExpression.LOGICAL, function.getType());
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("1 + and(2, 3)");
        FunctionExpression function = expression.getFunction("and");
        Assert.assertNotNull(function);
        Assert.assertEquals(FunctionExpression.LOGICAL, function.getType());
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("1 + and(2, 3)");
        Assert.assertTrue(expression.isLogicalFunction("and"));
        Assert.assertFalse(expression.isLogicalFunction("asd"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("1 + 2 + sqrt(4)");
        Assert.assertTrue(expression.isMathFunction("sqrt"));
        Assert.assertFalse(expression.isMathFunction("asd"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("dateDiff(day, '2022-01-01', '2022-01-02')");
        Assert.assertTrue(expression.isDateFunction("dateDiff"));
        Assert.assertFalse(expression.isDateFunction("asd"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("ToUpper(Hello)");
        Assert.assertTrue(expression.isStringFunction("ToUpper"));
        Assert.assertFalse(expression.isStringFunction("asd"));
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("MyFunction(1, 2)");
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new FunctionExpression("MyFunction", FunctionExpression.CUSTOM, null));
        Expression customExpression = new Expression("", functions);
        Assert.assertTrue(customExpression.isExternalFunction("MyFunction"));
        Assert.assertFalse(customExpression.isExternalFunction("asd"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("MyFunction(1, 2)");
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new FunctionExpression("MyFunction", FunctionExpression.CUSTOM, null));
        Expression customExpression = new Expression("", functions);
        FunctionExpression function = customExpression.getExternalFunction("MyFunction");
        Assert.assertNotNull(function);
        Assert.assertEquals("MyFunction", function.getSimbol());
    }

    @Test
    public void testDoEvaluationStackWithSimpleExpression() {
        Expression expression = new Expression("1 + 2");
        expression.prepare();
        ExpressionStack stack = expression.getStackCopy();
        List<Object> expectedStack = new ArrayList<>();
        expectedStack.add(1L);
        expectedStack.add(1.0);
        expectedStack.add("+");
        expectedStack.add(2L);
        expectedStack.add(2.0);
        for (int i = 0; i < stack.size(); i++) {
            Assert.assertEquals(expectedStack.get(i), stack.get(i).toString());
        }
    }

    @Test
    public void testDoEvaluationStackWithFunctionExpression() {
        Expression expression = new Expression("1 + and(2, 3)");
        expression.prepare();
        ExpressionStack stack = expression.getStackCopy();
        List<Object> expectedStack = new ArrayList<>();
        expectedStack.add(1L);
        expectedStack.add(1.0);
        expectedStack.add("+");
        expectedStack.add(2L);
        expectedStack.add(2.0);
        expectedStack.add("and");
        expectedStack.add(3L);
        expectedStack.add(3.0);
        for (int i = 0; i < stack.size(); i++) {
            Assert.assertEquals(expectedStack.get(i), stack.get(i).toString());
        }
    }

    @Test(expected = Exception.class)
    public void testDoEvaluationStackWithInvalidExpression() {
        Expression expression = new Expression("asd");
        expression.prepare();
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("1 + 2");
        expression.prepare();
        ExpressionStack stack = expression.getStackCopy();
        List<Object> expectedStack = new ArrayList<>();
        expectedStack.add(1L);
        expectedStack.add(1.0);
        expectedStack.add("+");
        expectedStack.add(2L);
        expectedStack.add(2.0);
        for (int i = 0; i < stack.size(); i++) {
            Assert.assertEquals(expectedStack.get(i), stack.get(i).toString());
        }
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression("1 + 2");
        expression.prepare();
        Object result = expression.evaluate();
        Assert.assertEquals(3.0, result);
    }

    @Test(expected = Exception.class)
    public void testEvaluateWithInvalidExpression() throws Exception {
        Expression expression = new Expression("asd");
        expression.prepare();
        expression.evaluate();
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression("1 + 2 * 3 + 4 / 5");
        expression.tokenize();
        ExpressionStack stack = expression.getStackCopy();
        List<Object> expectedStack = new ArrayList<>();
        expectedStack.add("1");
        expectedStack.add("+");
        expectedStack.add("2");
        expectedStack.add("*");
        expectedStack.add("3");
        expectedStack.add("+");
        expectedStack.add("4");
        expectedStack.add("/");
        expectedStack.add("5");
        for (int i = 0; i < stack.size(); i++) {
            Assert.assertEquals(expectedStack.get(i), stack.get(i).toString());
        }
    }
}