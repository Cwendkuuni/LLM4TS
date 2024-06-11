package org.javex.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.javex.Expression;
import org.javex.functions.FunctionExpression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testExpressionConstruction() {
        final String expression = "1 + 2 * (3 + 4)";
        final Expression e = new Expression(expression);
        assertEquals(expression, e.getExpression());
        assertTrue(Arrays.equals(Arrays.asList("1", "+", "2", "*", "(", "3", "+", "4", ")").toArray(), e.getStack().toArray()));
    }

    @Test
    public void testIsReservedFunction() {
        final Expression e = new Expression("1 + and(2, 3)");
        assertTrue(e.isReservedFunction("and"));
    }

    @Test
    public void testIsLogicalFunction() {
        final Expression e = new Expression("1 + and(2, 3)");
        assertTrue(e.isLogicalFunction("and"));
    }

    @Test
    public void testIsMathFunction() {
        final Expression e = new Expression("1 + 2 + sqrt(4)");
        assertTrue(e.isMathFunction("sqrt"));
    }

    @Test
    public void testIsDateFunction() {
        final Expression e = new Expression("dateDiff(day, '2022-01-01', '2022-02-01')");
        assertTrue(e.isDateFunction("dateDiff"));
    }

    @Test
    public void testIsStringFunction() {
        final Expression e = new Expression("len(myString)");
        assertTrue(e.isStringFunction("len"));
    }

    @Test
    public void testGetFunction() {
        final Map<String, FunctionExpression> reservedFunctions = new HashMap<>();
        Expression.putFunction(new AndFunction(), reservedFunctions);
        Expression.putFunction(new OrFunction(), reservedFunctions);
        final Expression e = new Expression("1 + and(2, 3)");
        FunctionExpression function = e.getFunction("and");
        assertSame(new AndFunction(), function);
    }

    @Test
    public void testGetExternalFunction() {
        final Expression e = new Expression("1 + myFunction(2, 3)");
        FunctionExpression function = e.getExternalFunction("myFunction");
        assertNull(function);
    }

    @Test
    public void testIsFunction() {
        final Expression e = new Expression("1 + and(2, 3)");
        assertTrue(e.isFunction("and"));
    }

    @Test
    public void testIsFunctionWithInvalidSymbol() {
        final Expression e = new Expression("1 + and(2, 3)");
        assertFalse(e.isFunction("z"));
    }

    @Test
    public void testPrepare() {
        final Expression e = new Expression("1 + 2 * (3 + 4)");
        e.prepare();
        assertTrue(Arrays.equals(Arrays.asList("1", "+", "2", "*", "(", "3", "+", "4", ")").toArray(), e.getStack().toArray()));
    }

    @Test
    public void testPrepareWithEmptyExpression() {
        final Expression e = new Expression("");
        e.prepare();
        assertTrue(Arrays.equals(new Object[0], e.getStack().toArray()));
    }

    @Test
    public void testPrepareWithQuotes() {
        final Expression e = new Expression("\"Hello World\"");
        e.prepare();
        assertTrue(Arrays.equals(new Object[]{"\"Hello World\""}, e.getStack().toArray()));
    }

    @Test
    public void testPrepareWithQuotesAndOperators() {
        final Expression e = new Expression("\"Hello World\" + 1");
        e.prepare();
        assertTrue(Arrays.equals(new Object[]{"\"Hello World\"", "+", "1"}, e.getStack().toArray()));
    }

    @Test
    public void testEvaluateSimpleMathExpression() throws Exception {
        final Expression e = new Expression("1 + 2 * 3");
        e.prepare();
        assertEquals(7.0, e.evaluate());
    }

    @Test
    public void testEvaluateSimpleMathExpressionWithExternalFunction() throws Exception {
        final Expression e = new Expression("1 + myFunction(2, 3)");
        e.prepare();
        e.setExternalFunctions(Collections.singletonList(new AddFunction()));
        assertEquals(6.0, e.evaluate());
    }

    @Test
    public void testEvaluateLogicalExpression() throws Exception {
        final Expression e = new Expression("true and false");
        e.prepare();
        assertEquals(false, e.evaluate());
    }

    @Test
    public void testEvaluateLogicalExpressionWithExternalFunction() throws Exception {
        final Expression e = new Expression("true and myFunction(false, false)");
        e.prepare();
        e.setExternalFunctions(Arrays.asList(new AndFunction(), new BooleanFunction() {
            @Override
            public boolean evaluate(boolean arg) {
                return arg;
            }
        }));
        assertEquals(false, e.evaluate());
    }

    @Test
    public void testEvaluateExpressionWithInvalidSymbol() throws Exception {
        final Expression e = new Expression("1 + z(2, 3)");
        e.prepare();
        try {
            e.evaluate();
            fail("Expected EvaluationException");
        } catch (Exception ex) {
            assertEquals("Function z not found", ex.getMessage());
        }
    }

    @Test
    public void testEvaluateExpressionWithInvalidFunctionArgument() throws Exception {
        final Expression e = new Expression("isNull(null, 'Hello World')");
        e.prepare();
        try {
            e.evaluate();
            fail("Expected EvaluationException");
        } catch (Exception ex) {
            assertEquals("Type mismatch: 'null' is not a valid argument for the isNull function", ex.getMessage());
        }
    }

    @Test
    public void testEvaluateExpressionWithUndefinedFunction() throws Exception {
        final Expression e = new Expression("myFunction(2, 3)");
        e.prepare();
        try {
            e.evaluate();
            fail("Expected EvaluationException");
        } catch (Exception ex) {
            assertEquals("Function myFunction not found", ex.getMessage());
        }
    }

    @Test
    public void testEvaluateComplexExpression() throws Exception {
        final Expression e = new Expression("((5 + 2) * 3) - (2 * 4) + 1");
        e.prepare();
        assertEquals(2.0, e.evaluate());
    }

    @Test
    public void testEvaluateComplexExpressionWithParentheses() throws Exception {
        final Expression e = new Expression("((5 + 2) * (3 + 4))");
        e.prepare();
        assertEquals(25.0, e.evaluate());
    }

    @Test
    public void testEvaluateComplexExpressionWithExternalFunctions() throws Exception {
        final List<FunctionExpression> functions = new ArrayList<>();
        functions.add(new AddFunction());
        functions.add(new MultiplyFunction());
        final Expression e = new Expression("((5 + 2) * (3 + 4))");
        e.prepare();
        e.setExternalFunctions(functions);
        assertEquals(25.0, e.evaluate());
    }

    @Test
    public void testEvaluateComplexExpressionWithStrings() throws Exception {
        final Expression e = new Expression("\"Hello World\" + ' ' + \"Test\" + ' ' + 1");
        e.prepare();
        assertEquals("Hello World Test 1", e.evaluate());
    }

    @Test
    public void testEvaluateComplexExpressionWithDateFunctions() throws Exception {
        final Expression e = new Expression("fromDate(2022, 1, 1) + dateDiff(day, fromDate(2022, 1, 1), fromDate(2022, 2, 1))");
        e.prepare();
        assertEquals(31.0, e.evaluate());
    }
}

class AddFunction implements FunctionExpression {
    @Override
    public String getSimbol() {
        return "+";
    }

    @Override
    public int getType() {
        return FunctionExpression.MATH;
    }

    @Override
    public Object evaluate(List args) {
        final Object arg1 = args.get(0);
        final Object arg2 = args.get(1);
        return arg1 instanceof Number ? ((Number) arg1).doubleValue() + ((Number) arg2).doubleValue() : "";
    }
}

class BooleanFunction implements FunctionExpression {
    @Override
    public String getSimbol() {
        return "";
    }

    @Override
    public int getType() {
        return FunctionExpression.LOGICAL;
    }

    @Override
    public Object evaluate(List args) {
        final Object arg = args.get(0);
        return arg instanceof Boolean ? (Boolean) arg : false;
    }
}

class FromDateFunction implements FunctionExpression {
    @Override
    public String getSimbol() {
        return "fromDate";
    }

    @Override
    public int getType() {
        return FunctionExpression.DATE;
    }

    @Override
    public Object evaluate(List args) {
        final int year = ((Number) args.get(0)).intValue();
        final int month = ((Number) args.get(1)).intValue();
        final int day = ((Number) args.get(2)).intValue();
        return new org.joda.time.DateTime(year, month, day, 0, 0, 0, 0);
    }
}

class DateAddFunction implements FunctionExpression {
    @Override
    public String getSimbol() {
        return "dateAdd";
    }

    @Override
    public int getType() {
        return FunctionExpression.DATE;
    }

    @Override
    public Object evaluate(List args) {
        final Object arg1 = args.get(0);
        final Object arg2 = args.get(1);
        final int unit = ((Number) args.get(2)).intValue();
        final Object date = arg1 instanceof org.joda.time.DateTime ? (org.joda.time.DateTime) arg1 : new org.joda.time.DateTime(arg1.toString());
        final long amount = unit == org.javex.functions.date.DateFunction.DAY ? ((Number) arg2).longValue() * 24 * 60 * 60 * 1000 : ((Number) arg2).longValue();
        return date.plus(amount);
    }
}

class DateDiffFunction implements FunctionExpression {
    @Override
    public String getSimbol() {
        return "dateDiff";
    }

    @Override
    public int getType() {
        return FunctionExpression.DATE;
    }

    @Override
    public Object evaluate(List args) {
        final Object arg1 = args.get(0);
        final Object arg2 = args.get(1);
        final int unit = ((Number) args.get(2)).intValue();
        final Object date1 = arg1 instanceof org.joda.time.DateTime ? (org.joda.time.DateTime) arg1 : new org.joda.time.DateTime(arg1.toString());
        final Object date2 = arg2 instanceof org.joda.time.DateTime ? (org.joda.time.DateTime) arg2 : new org.joda.time.DateTime(arg2.toString());
        final long amount = unit == org.javex.functions.date.DateFunction.DAY ? date1.getDaysDifference(date2) * 24 * 60 * 60 * 1000 : 0;
        return amount;
    }
}

class LengthFunction implements FunctionExpression {
    @Override
    public String getSimbol() {
        return "len";
    }

    @Override
    public int getType() {
        return FunctionExpression.STRING;
    }

    @Override
    public Object evaluate(List args) {
        final Object arg = args.get(0);
        return arg instanceof String ? ((String) arg).length() : 0;
    }
}

class SubStringFunction implements FunctionExpression {
    @Override
    public String getSimbol() {
        return "substr";
    }

    @Override
    public int getType() {
        return FunctionExpression.STRING;
    }

    @Override
    public Object evaluate(List args) {
        final Object arg1 = args.get(0);
        final Number arg2 = (Number) args.get(1);
        final Number arg3 = (Number) args.get(2);
        final String str = arg1 instanceof String ? (String) arg1 : arg1.toString();
        return arg3.intValue() < 0 ? str.substring(str.length() + arg2.intValue()) : str.substring(arg2.intValue(), arg2.intValue() + arg3.intValue());
    }
}

class ToLowerFunction implements FunctionExpression {
    @Override
    public String getSimbol() {
        return "tolower";
    }

    @Override
    public int getType() {
        return FunctionExpression.STRING;
    }

    @Override
    public Object evaluate(List args) {
        final Object arg = args.get(0);
        return arg instanceof String ? ((String) arg).toLowerCase() : arg;
    }
}

class ToUpperFunction implements FunctionExpression {
    @Override
    public String getSimbol() {
        return "toupper";
    }

    @Override
    public int getType() {
        return FunctionExpression.STRING;
    }

    @Override
    public Object evaluate(List args) {
        final Object arg = args.get(0);
        return arg instanceof String ? ((String) arg).toUpperCase() : arg;
    }
}