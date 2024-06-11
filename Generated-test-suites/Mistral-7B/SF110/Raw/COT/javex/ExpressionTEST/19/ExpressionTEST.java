package org.javex.test;

import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testExpressionConstructor() {
        String expression = "1 + 2";
        Expression testExpression = new Expression(expression);
        Assert.assertNotNull(testExpression);
    }

    @Test
    public void testGetExternalFunctions() {
        Expression testExpression = new Expression("1 + 2");
        List<FunctionExpression> expectedFunctions = Arrays.asList(
                new PlusFunction(),
                new NumberFunction(1),
                new NumberFunction(2)
        );
        List<FunctionExpression> actualFunctions = testExpression.getExternalFunctions();
        Assert.assertEquals(expectedFunctions, actualFunctions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpressionConstructorWithNullString() {
        Expression testExpression = new Expression(null);
    }

    @Test
    public void testDoEvaluationStackWithSimpleExpression() {
        String expression = "1 + 2";
        Expression testExpression = new Expression(expression);
        testExpression.prepare();
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("2");
        expectedStack.add("+");
        ExpressionStack actualStack = testExpression.getStackCopy();
        Assert.assertEquals(expectedStack, actualStack);
    }

    @Test
    public void testDoEvaluationStackWithFunctionExpression() {
        String expression = "abs(1)";
        Expression testExpression = new Expression(expression);
        testExpression.prepare();
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("abs");
        expectedStack.add("1");
        ExpressionStack actualStack = testExpression.getStackCopy();
        Assert.assertEquals(expectedStack, actualStack);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoEvaluationStackWithInvalidExpression() {
        String expression = "abc";
        Expression testExpression = new Expression(expression);
        testExpression.prepare();
    }

    @Test
    public void testIsReservedFunction() {
        Expression testExpression = new Expression("and(1, 2)");
        Assert.assertTrue(testExpression.isReservedFunction("and"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression testExpression = new Expression("and(1, 2)");
        Assert.assertTrue(testExpression.isLogicalFunction("and"));
    }

    @Test
    public void testIsMathFunction() {
        Expression testExpression = new Expression("abs(1)");
        Assert.assertTrue(testExpression.isMathFunction("abs"));
    }

    @Test
    public void testIsDateFunction() {
        Expression testExpression = new Expression("dateAdd(Now(), '1 day')");
        Assert.assertTrue(testExpression.isDateFunction("dateAdd"));
    }

    @Test
    public void testIsStringFunction() {
        Expression testExpression = new Expression("length(concat('hello', ' world'))");
        Assert.assertTrue(testExpression.isStringFunction("length"));
    }

    @Test
    public void testGetFunctionWithReservedFunction() {
        String expression = "and(1 > 0, 2 > 0)";
        Expression testExpression = new Expression(expression);
        FunctionExpression andFunction = testExpression.getFunction("and");
        Assert.assertNotNull(andFunction);
        Assert.assertEquals(FunctionExpression.LOGICAL, andFunction.getType());
    }

    @Test
    public void testGetFunctionWithExternalFunction() {
        Expression testExpression = new Expression("myFunction(1, 2)");
        testExpression.setExternalFunctions(getTestFunctions());
        FunctionExpression myFunction = testExpression.getFunction("myFunction");
        Assert.assertNotNull(myFunction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFunctionWithInvalidFunction() {
        Expression testExpression = new Expression("invalidFunction(1, 2)");
        testExpression.getFunction("invalidFunction");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFunctionWithEmptyExpression() {
        Expression testExpression = new Expression("");
        testExpression.getFunction("and");
    }

    private List<FunctionExpression> getTestFunctions() {
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new FunctionExpression("myFunction", FunctionExpression.USER_DEFINED));
        Expression externalFunctions = new Expression("function myFunction(x, y) { return x + y; }");
        functions.addAll(externalFunctions.getExternalFunctions());
        return functions;
    }
}