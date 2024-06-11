package org.javex.test;

import org.junit.Assert;
import org.junit.Test;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("1 + 2");
        Assert.assertNotNull(expression);
        Assert.assertEquals(2, expression.getStack().size());
        Assert.assertEquals("1", expression.getStack().get(0));
        Assert.assertEquals("+", expression.getStack().get(1));
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("1 + 2");
        expression.doEvaluationStack();
        Assert.assertEquals(1, expression.getStack().size());
        Assert.assertEquals("3", expression.getStack().get(0));

        expression = new Expression("1 + 2 * 3");
        expression.doEvaluationStack();
        Assert.assertEquals(1, expression.getStack().size());
        Assert.assertEquals("7", expression.getStack().get(0));
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("1 + and(2, 3)");
        Assert.assertTrue(expression.isReservedFunction("and"));

        expression = new Expression("1 + or(2, 3)");
        Assert.assertTrue(expression.isReservedFunction("or"));

        expression = new Expression("1 + equals(2, 3)");
        Assert.assertTrue(expression.isReservedFunction("equals"));

        expression = new Expression("1 + not(2)");
        Assert.assertTrue(expression.isReservedFunction("not"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("1 + and(2, 3)");
        Assert.assertFalse(expression.isLogicalFunction("and"));

        expression = new Expression("and(2, 3)");
        Assert.assertTrue(expression.isLogicalFunction("and"));

        expression = new Expression("or(2, 3)");
        Assert.assertTrue(expression.isLogicalFunction("or"));

        expression = new Expression("not(2)");
        Assert.assertTrue(expression.isLogicalFunction("not"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("1 + abs(2)");
        Assert.assertTrue(expression.isMathFunction("abs"));

        expression = new Expression("1 + power(2, 2)");
        Assert.assertTrue(expression.isMathFunction("power"));

        expression = new Expression("1 + sqrt(2)");
        Assert.assertTrue(expression.isMathFunction("sqrt"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("1 + dateAdd(now(), 1, 'day')");
        Assert.assertTrue(expression.isDateFunction("dateAdd"));

        expression = new Expression("1 + dateDiff(now(), '2022-01-01', 'day')");
        Assert.assertTrue(expression.isDateFunction("dateDiff"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("1 + length(str1)");
        Assert.assertTrue(expression.isStringFunction("length"));

        expression = new Expression("1 + subString(str1, 0, 3)");
        Assert.assertTrue(expression.isStringFunction("subString"));

        expression = new Expression("1 + toLower(str1)");
        Assert.assertTrue(expression.isStringFunction("toLower"));

        expression = new Expression("1 + toUpper(str1)");
        Assert.assertTrue(expression.isStringFunction("toUpper"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("1 + and(2, 3)");
        Assert.assertNull(expression.getFunction("and"));

        expression = new Expression();
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new FunctionExpression("myFunction", FunctionExpression.CUSTOM));
        Assert.assertEquals("myFunction", expression.getFunction("myFunction").getSimbol());
    }

    @Test
    public void testPrepare() throws Exception {
        Expression expression = new Expression("1 + 2");
        expression.prepare();
        Assert.assertTrue(expression.isPrepared());

        expression = new Expression("1 + and(2, 3)");
        expression.prepare();
        Assert.assertTrue(expression.isPrepared());

        expression = new Expression("1 + not(isFunction(str1))");
        expression.prepare();
        Assert.assertTrue(expression.isPrepared());
    }

    @Test(expected = Exception.class)
    public void testPrepareWithInvalidExpression() throws Exception {
        Expression expression = new Expression("1 +");
        expression.prepare();
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression("1 + 2");
        expression.prepare();
        Object result = expression.evaluate();
        Assert.assertEquals(3, result);

        expression = new Expression("1 + and(2, 3)");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(true, result);

        expression = new Expression("1 + not(isFunction(str1))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression();
        Assert.assertNull(expression.getExternalFunctions());

        expression = new Expression();
        expression.externalFunctions = new ArrayList<>();
        Assert.assertEquals(0, expression.getExternalFunctions().size());

        expression.externalFunctions.add(new FunctionExpression("myFunction", FunctionExpression.CUSTOM, Collections.singletonList("param1")));
        Assert.assertEquals(1, expression.getExternalFunctions().size());
        Assert.assertEquals("myFunction", expression.getExternalFunctions().get(0).getSimbol());
        Assert.assertEquals("param1", expression.getExternalFunctions().get(0).getParameters().get(0));
    }
}