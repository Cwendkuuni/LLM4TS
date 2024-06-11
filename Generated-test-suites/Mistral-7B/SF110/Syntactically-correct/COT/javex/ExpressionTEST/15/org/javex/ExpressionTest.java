package org.javex.test;

import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static org.javex.Expression.getReservedFunctions;
import static org.javex.Expression.isFunction;
import static org.javex.Expression.isLogicalFunction;
import static org.javex.Expression.isMathFunction;
import static org.javex.Expression.isReservedFunction;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("testExpression");
        List<FunctionExpression> expectedFunctions = Arrays.asList(
                new AndFunction(),
                new BetweenFunction(),
                // ... other functions
                new InListFunction(),
                // ... other functions
                new ToUpperFunction()
        );
        List<FunctionExpression> actualFunctions = expression.getExternalFunctions();
        Assert.assertEquals(expectedFunctions.size(), actualFunctions.size());
        for (int i = 0; i < expectedFunctions.size(); ++i) {
            FunctionExpression expectedFunction = expectedFunctions.get(i);
            FunctionExpression actualFunction = actualFunctions.get(i);
            Assert.assertEquals(expectedFunction.getSimbol(), actualFunction.getSimbol());
            Assert.assertEquals(expectedFunction.getType(), actualFunction.getType());
        }
    }

    @Test
    public void testConstructor() {
        Expression expression = new Expression("1 + 2");
        Stack<String> expectedStack = new Stack<String>() {{
            add("1");
            add("+");
            add("2");
        }};
        Stack<String> actualStack = expression.getStackCopy();
        Assert.assertEquals(expectedStack, actualStack);
    }

    @Test
    public void testDoEvaluationStack_EmptyExpression() {
        Expression expression = new Expression("");
        Stack<String> expectedStack = new Stack<String>();
        Stack<String> actualStack = expression.getStackCopy();
        Assert.assertEquals(expectedStack, actualStack);
    }

    @Test
    public void testDoEvaluationStack_SimpleExpression() {
        Expression expression = new Expression("1 + 2");
        Stack<String> expectedStack = new Stack<String>() {{
            add("1");
            add("+");
            add("2");
        }};
        Stack<String> actualStack = expression.getStackCopy();
        Assert.assertEquals(expectedStack, actualStack);
        Object result = null;
        try {
            result = expression.evaluate();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(3.0, result);
    }

    @Test
    public void testDoEvaluationStack_ComplexExpression() {
        Expression expression = new Expression("(1 + 2) * (3 + 4)");
        Stack<String> expectedStack = new Stack<String>() {{
            add("(");
            add("1");
            add("+");
            add("2");
            add(")");
            add("*");
            add("(");
            add("3");
            add("+");
            add("4");
            add(")");
        }};
        Stack<String> actualStack = expression.getStackCopy();
        Assert.assertEquals(expectedStack, actualStack);
        Object result = null;
        try {
            result = expression.evaluate();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(14.0, result);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("test");
        boolean expectedResult = false;
        boolean actualResult = expression.isLogicalFunction("and");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("test");
        expectedResult = true;
        actualResult = expression.isLogicalFunction("and");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("test and true");
        expectedResult = true;
        actualResult = expression.isLogicalFunction("and");
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("test");
        boolean expectedResult = false;
        boolean actualResult = expression.isMathFunction("sqrt");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("test");
        expectedResult = false;
        actualResult = expression.isMathFunction("+");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("sqrt(2)");
        expectedResult = true;
        actualResult = expression.isMathFunction("sqrt");
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("test");
        boolean expectedResult = false;
        boolean actualResult = expression.isDateFunction("dateadd");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("test");
        expectedResult = false;
        actualResult = expression.isDateFunction("dateadd");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("dateadd(day, '2022-01-01', 1)");
        expectedResult = true;
        actualResult = expression.isDateFunction("dateadd");
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("test");
        boolean expectedResult = false;
        boolean actualResult = expression.isStringFunction("length");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("test");
        expectedResult = false;
        actualResult = expression.isStringFunction("length");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("length('test')");
        expectedResult = true;
        actualResult = expression.isStringFunction("length");
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("test");
        boolean expectedResult = false;
        boolean actualResult = expression.isFunction("+");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("test");
        expectedResult = false;
        actualResult = expression.isFunction("and");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("and(true, true)");
        expectedResult = true;
        actualResult = expression.isFunction("and");
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("test");
        boolean expectedResult = false;
        boolean actualResult = expression.isReservedFunction("and");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("test and true");
        expectedResult = false;
        actualResult = expression.isReservedFunction("and");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("and(true, true)");
        expectedResult = false;
        actualResult = expression.isReservedFunction("and");
        Assert.assertEquals(expectedResult, actualResult);

        expression = new Expression("test");
        expectedResult = true;
        actualResult = expression.isReservedFunction("is");
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("test");
       expression.prepare();
        Stack<String> expectedStack = new Stack<String>();
        Stack<String> actualStack = expression.getStackCopy();
        Assert.assertEquals(expectedStack, actualStack);

        expression = new Expression("1 + 2");
        expression.prepare();
        expectedStack = new Stack<String>() {{
            add("1");
            add("+");
            add("2");
        }};
        actualStack = expression.getStackCopy();
        Assert.assertEquals(expectedStack, actualStack);

        Object result = null;
        try {
            result = expression.evaluate();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(3.0, result);
    }

    @Test(expected = Exception.class)
    public void testPrepare_InvalidExpression() {
        Expression expression = new Expression("test +");
        expression.prepare();
    }

    @Test(expected = Exception.class)
    public void testEvaluate_EmptyStack() {
        Expression expression = new Expression("");
        try {
            expression.evaluate();
        } catch (Exception e) {
            Assert.assertEquals("Expression stack underflow", e.getMessage());
        }
    }

    @Test(expected = Exception.class)
    public void testEvaluate_InvalidFunctionCall() {
        Expression expression = new Expression("test(test)");
        try {
            expression.evaluate();
        } catch (Exception e) {
            Assert.assertEquals("Function 'test' not found", e.getMessage());
        }
    }

    @Test
    public void testGetReservedFunctions() {
        Map<FunctionExpression, FunctionExpression> expectedFunctions = getReservedFunctions("is");
        Map<FunctionExpression, FunctionExpression> actualFunctions = getReservedFunctions("is");
        Assert.assertEquals(expectedFunctions.size(), actualFunctions.size());
        FunctionExpression isFunction = expectedFunctions.get(new IsFunction());
        FunctionExpression actualIsFunction = actualFunctions.get(new IsFunction());
        Assert.assertEquals(isFunction.getSimbol(), actualIsFunction.getSimbol());
        Assert.assertEquals(isFunction.getType(), actualIsFunction.getType());
    }

    @Test
    public void testPutFunction() {
        Map<FunctionExpression, FunctionExpression> expectedFunctions = new HashMap<>();
        Expression.putFunction(new AndFunction(), expectedFunctions);
        Map<FunctionExpression, FunctionExpression> actualFunctions = expectedFunctions;
        FunctionExpression andFunction = new AndFunction();
        Assert.assertNull(expectedFunctions.get(andFunction));
        Expression.putFunction(andFunction, expectedFunctions);
        FunctionExpression actualAndFunction = expectedFunctions.get(andFunction);
        Assert.assertNotNull(actualAndFunction);
        Assert.assertEquals(andFunction.getSimbol(), actualAndFunction.getSimbol());
        Assert.assertEquals(andFunction.getType(), actualAndFunction.getType());
    }
}