package org.javex.expression;

import org.junit.Assert;
import org.junit.Test;
import org.javex.Expression;
import org.javex.functions.FunctionExpression;
import org.javex.functions.LogicalFunction;
import org.javex.functions.MathFunction;
import org.javex.functions.DateFunction;
import org.javex.functions.StringFunction;
import org.javex.utils.JavexUtils;
import org.javex.utils.ExpressionStack;
import org.javex.utils.FunctionExpressionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    @Test
    public void testConstructor() {
        Expression expression = new Expression("(1 + 2) * 3");
        Assert.assertNotNull(expression);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidInput() {
        Expression expression = new Expression("");
    }

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("(1 + and(2, 3))");
        List externalFunctions = expression.getExternalFunctions();
        Assert.assertNotNull(externalFunctions);
        Assert.assertTrue(externalFunctions.isEmpty());

        expression = new Expression("(1 + and(myFunction(), 3))");
        FunctionExpression myFunction = new LogicalFunction("myFunction") {};
        expression.setExternalFunctions(Arrays.asList(myFunction));
        externalFunctions = expression.getExternalFunctions();
        Assert.assertNotNull(externalFunctions);
        Assert.assertEquals(1, externalFunctions.size());
        Assert.assertEquals(myFunction, externalFunctions.get(0));
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("(and(1, 2))");
        Assert.assertTrue(expression.isReservedFunction("and"));

        expression = new Expression("(or(1, 2))");
        Assert.assertTrue(expression.isReservedFunction("or"));

        expression = new Expression("(abs(1))");
        Assert.assertTrue(expression.isReservedFunction("abs"));

        expression = new Expression("(dateAdd(1, " + JavexUtils.DATE_FORMAT_DATE + " '2022-01-01', 1))");
        Assert.assertTrue(expression.isReservedFunction("dateAdd"));

        Assert.assertFalse(expression.isReservedFunction("notFunction"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("(and(1, 2))");
        Assert.assertTrue(expression.isLogicalFunction("and"));

        Assert.assertFalse(expression.isLogicalFunction("abs"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("(abs(1))");
        Assert.assertTrue(expression.isMathFunction("abs"));

        Assert.assertFalse(expression.isMathFunction("and"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("(dateAdd(1, " + JavexUtils.DATE_FORMAT_DATE + " '2022-01-01', 1))");
        Assert.assertTrue(expression.isDateFunction("dateAdd"));

        Assert.assertFalse(expression.isDateFunction("abs"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("(length(1))");
        Assert.assertTrue(expression.isStringFunction("length"));

        Assert.assertFalse(expression.isStringFunction("abs"));
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("(and(1, 2))");
        Assert.assertTrue(expression.isFunction("and"));

        Assert.assertFalse(expression.isFunction("abs"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("(and(1, 2))");
        FunctionExpression andFunction = new LogicalFunction("and") {};
        expression.setReservedFunctions(new HashMap<>());
        expression.setReservedFunctions(Expression.putFunction(andFunction, expression.getReservedFunctions()));
        FunctionExpression returnedFunction = expression.getFunction("and");
        Assert.assertEquals(andFunction, returnedFunction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFunctionWithInvalidInput() {
        Expression expression = new Expression("(and(1, 2))");
        expression.getFunction("notFunction");
    }

    @Test
    public void testEvaluateWithSimpleExpressions() {
        Expression expression = new Expression("(1 + 2)");
        expression.prepare();
        Object result = expression.evaluate();
        Assert.assertEquals(3, result);

        expression = new Expression("(2 * 3)");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(6, result);

        expression = new Expression("(5 / 2)");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(2.5, result);

        expression = new Expression("(sqrt(16))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(4.0, result);

        expression = new Expression("(1 + (2 * 3))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(7, result);
    }

    @Test
    public void testEvaluateWithLogicalExpressions() {
        Expression expression = new Expression("(1 > 0 and 2 > 0)");
        expression.prepare();
        Object result = expression.evaluate();
        Assert.assertEquals(true, result);

        expression = new Expression("(1 < 0 or 2 < 0)");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(false, result);

        expression = new Expression("((1 > 0) and (2 > 0))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(true, result);

        expression = new Expression("((1 < 0) or (2 < 0))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(true, result);

        expression = new Expression("((1 > 0) and (2 < 0))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(false, result);
    }

    @Test
    public void testEvaluateWithMathExpressions() {
        Expression expression = new Expression("(abs(-5))");
        expression.prepare();
        Object result = expression.evaluate();
        Assert.assertEquals(5, result);

        expression = new Expression("(pow(2, 3))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(8.0, result);

        expression = new Expression("(pow(2, 3) + sqrt(16))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(11.0, result, 0.001);
    }

    @Test
    public void testEvaluateWithDateExpressions() {
        Expression expression = new Expression("(dateAdd(now(), 1))");
        expression.prepare();
        Object result = expression.evaluate();
        Assert.assertTrue(result instanceof java.util.Date);
        java.util.Date expectedDate = new java.util.GregorianCalendar(2022, 0, 1).getTime();
        Assert.assertEquals(expectedDate.getTime(), result.getClass().getMethod("getTime").invoke(result));

        expression = new Expression("(dateDiff(now(), " + JavexUtils.DATE_FORMAT_DATE + " '2022-01-01', 'YYYY'))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(2022, result.intValue());

        expression = new Expression("(dateAdd(now(), 1, 'MM'))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(2, result.intValue());
    }

    @Test
    public void testEvaluateWithStringExpressions() {
        Expression expression = new Expression("(length(\"abc\"))");
        expression.prepare();
        Object result = expression.evaluate();
        Assert.assertEquals(3, result);

        expression = new Expression("(substring(\"abc\", 1, 1))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals('a', result);

        expression = new Expression("(substring(\"abc\", -1, 1))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals('c', result);

        expression = new Expression("(toLowerCase(\"ABC\"))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals("abc", result);

        expression = new Expression("(toUpperCase(\"abc\"))");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals("ABC", result);
    }

    @Test
    public void testEvaluateWithComplexExpressions() {
        Expression expression = new Expression("(1 + (2 * (3 - 1)))");
        expression.prepare();
        Object result = expression.evaluate();
        Assert.assertEquals(6, result);

        expression = new Expression("((1 > 0) and (2 > 0)) and 1 + 2");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(4, result);

        expression = new Expression("(abs(-5) + sqrt(16) * 2) / 3");
        expression.prepare();
        result = expression.evaluate();
        Assert.assertEquals(3.80952380952381, result, 0.0001);

        expression = new Expression("(pow(2, 3) + sqrt(16)) and \"abc\"");
        expression.prepare();
        try {
            result = expression.evaluate();
            Assert.fail();
        } catch (ClassCastException e) {
            // Expected
        }

        expression = new Expression("(pow(2, 3) + sqrt(16)) and toLowerCase(\"ABC\")");
        expression.prepare();
        try {
            result = expression.evaluate();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Error in expression: Operand mismatch: FunctionExpression [functionName=toLowerCase, functionType=STRING, parameters=[], operands=[java.lang.String@29ac36e2]] and FunctionExpression [functionName=pow, functionType=MATH, parameters=[java.lang.Double@15db9742, java.lang.Integer@254e2822], operands=[java.lang.Double@18b4e1fc]]", e.getMessage());
        }
    }
}