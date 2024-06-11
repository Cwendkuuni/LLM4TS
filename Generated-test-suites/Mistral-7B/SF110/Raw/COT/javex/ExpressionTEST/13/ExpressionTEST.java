package org.javex.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.javex.Expression;
import org.javex.utils.JavexUtils;
import org.javex.functions.FunctionExpression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubstractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.date.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToUpperFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.utils.ExpressionStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        List<FunctionExpression> externalFunctions = expression.getExternalFunctions();
        assertTrue(externalFunctions.isEmpty());

        expression = new Expression("test with external functions (and, or)");
        expression.setExternalFunctions(Arrays.asList(new AndFunction(), new OrFunction()));
        externalFunctions = expression.getExternalFunctions();
        assertEquals(2, externalFunctions.size());
        assertTrue(externalFunctions.contains(new AndFunction()));
        assertTrue(externalFunctions.contains(new OrFunction()));
    }

    @Test
    public void testConstructor() {
        Expression expression = new Expression("test");
        assertNotNull(expression);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isFunction("test"));
        FunctionExpression and = new AndFunction();
        expression.setExternalFunctions(Collections.singletonList(and));
        assertTrue(expression.isFunction(and.getSimbol()));
    }

    @Test
    public void testPrepare() throws Exception {
        Expression expression = new Expression("1 + 2 * (3 + 4)");
        expression.prepare();
        ExpressionStack stack = expression.getStackCopy();
        List<String> expected = Arrays.asList("1", "2", "3", "4", "+", "*");
        assertEquals(expected, Arrays.asList(stack.get(0), stack.get(1), stack.get(2), stack.get(3), stack.get(4), stack.get(5)));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("test");
        assertNull(expression.getFunction("test"));

        expression = new Expression("test with functions (and, or)");
        expression.setExternalFunctions(Arrays.asList(new AndFunction(), new OrFunction()));
        FunctionExpression and = expression.getFunction("and");
        FunctionExpression or = expression.getFunction("or");
        assertEquals(AndFunction.class, and.getClass());
        assertEquals(OrFunction.class, or.getClass());
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isReservedFunction("test"));

        expression = new Expression("test with functions (and, or, >)");
        Map<String, FunctionExpression> reservedFunctions = new HashMap<>();
        Expression.putFunction(new AndFunction(), reservedFunctions);
        Expression.putFunction(new OrFunction(), reservedFunctions);
        Expression.putFunction(new GreaterThanFunction(), reservedFunctions);
        assertTrue(expression.isReservedFunction(">"));
        assertFalse(expression.isReservedFunction("and"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isLogicalFunction("test"));

        expression = new Expression("test with functions (and, or)");
        expression.setExternalFunctions(Arrays.asList(new AndFunction(), new OrFunction()));
        assertTrue(expression.isLogicalFunction("and"));
        assertTrue(expression.isLogicalFunction("or"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isMathFunction("test"));

        expression = new Expression("test with functions (abs, div, pow, sqrt, sub)")
                .setExternalFunctions(Arrays.asList(new AbsFunction(), new DivideFunction(), new PowerFunction(), new SqrtFunction(), new SubstractFunction()));
        assertTrue(expression.isMathFunction("abs"));
        assertTrue(expression.isMathFunction("div"));
        assertTrue(expression.isMathFunction("pow"));
        assertTrue(expression.isMathFunction("sqrt"));
        assertTrue(expression.isMathFunction("sub"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isDateFunction("test"));

        expression = new Expression("test with functions (dateadd, datadiff, length)")
                .setExternalFunctions(Arrays.asList(new DateAddFunction(), new DateDiffFunction(), new LengthFunction()));
        assertTrue(expression.isDateFunction("dateadd"));
        assertTrue(expression.isDateFunction("datadiff"));
        assertTrue(expression.isDateFunction("length"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("test");
        assertFalse(expression.isStringFunction("test"));

        expression = new Expression("test with functions (substring, tolower, toupper)")
                .setExternalFunctions(Arrays.asList(new SubStringFunction(), new ToLowerFunction(), new ToUpperFunction()));
        assertTrue(expression.isStringFunction("substring"));
        assertTrue(expression.isStringFunction("tolower"));
        assertTrue(expression.isStringFunction("toupper"));
    }

    @Test
    public void testEvaluateException() throws Exception {
        Expression expression = new Expression("test with functions (abs, div, pow, sqrt, sub)");
        expression.setExternalFunctions(Arrays.asList(new AbsFunction(), new DivideFunction(), new PowerFunction(), new SqrtFunction(), new SubstractFunction()));

        try {
            expression.evaluate();
            fail("Expected ArithmeticException or IllegalArgumentException");
        } catch (Exception e) {
            // check exceptions
        }
    }

    @Test
    public void testEvaluateSimpleCase() throws Exception {
        Expression expression = new Expression("1 + 1");
        Object result = expression.evaluate();
        assertEquals(new Integer(2), result);
    }

    @Test
    public void testEvaluateMathFunctions() throws Exception {
        Expression expression = new Expression("2 * pow(3, 2) + abs(5) - sqrt(16)")
                .setExternalFunctions(Arrays.asList(new PowerFunction(), new AbsFunction(), new SqrtFunction()));
        Object result = expression.evaluate();
        assertEquals(new Double(11.0), result);
    }

    @Test
    public void testEvaluateDateFunctions() throws Exception {
        Expression expression = new Expression("dateadd(day(now()), 7)")
                .setExternalFunctions(Arrays.asList(new DateAddFunction(), new DateDiffFunction(), new LengthFunction()));
        Object result = expression.evaluate();
        Date expectedDate = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        assertEquals(expectedDate, result);
    }

    @Test
    public void testEvaluateStringFunctions() throws Exception {
        Expression expression = new Expression("tolower(substring(concat('abcdef', '1234'), 3, 2))")
                .setExternalFunctions(Arrays.asList(new SubStringFunction(), new ConcatFunction(), new ToLowerFunction()));
        Object result = expression.evaluate();
        assertEquals("ef", result);
    }

    @Test
    public void testEvaluateLogicalFunctions() throws Exception {
        Expression expression = new Expression("and(true, true)")
                .setExternalFunctions(Arrays.asList(new AndFunction()));
        Object result = expression.evaluate();
        assertTrue(result instanceof Boolean && ((Boolean) result).booleanValue());

        expression = new Expression("or(true, false)")
                .setExternalFunctions(Arrays.asList(new OrFunction()));
        result = expression.evaluate();
        assertTrue(result instanceof Boolean && ((Boolean) result).booleanValue());

        expression = new Expression("not(true)")
                .setExternalFunctions(Arrays.asList(new NotFunction()));
        result = expression.evaluate();
        assertFalse(result instanceof Boolean || ((Boolean) result).booleanValue());
    }

    @Test
    public void testEvaluateComplexCase() throws Exception {
        Expression expression = new Expression("and(or(and(isnull(1), isempty('test')), isdate('2023-01-31')), isnull(''))")
                .setExternalFunctions(Arrays.asList(new AndFunction(), new OrFunction(), new IsNullFunction(), new IsEmptyFunction(), new IsDateFunction(), new NotFunction(), new IsFunction(), new LengthFunction(), new SubStringFunction()));
        Object result = expression.evaluate();
        assertTrue(result instanceof Boolean && ((Boolean) result).booleanValue());
    }
}