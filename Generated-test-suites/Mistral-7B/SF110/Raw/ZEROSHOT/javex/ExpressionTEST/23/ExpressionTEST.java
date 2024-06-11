package org.javex.expression;

import static org.junit.Assert.*;
import org.javex.utils.JavexUtils;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.date.DateUtils;
import org.javex.functions.string.ContainsFunction;
import org.javex.functions.string.EndsWithFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.StartsWithFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

    private Expression expression;
    private static final String EXPRESSION_1 = "1+2*3";
    private static final String EXPRESSION_2 = "(3+2)*4";
    private static final String EXPRESSION_3 = "2>3 and 4>5";
    private static final String EXPRESSION_4 = "2>3 or 4>5";
    private static final String EXPRESSION_5 = "2>3 and (4>5 or 5>4)";
    private static final String EXPRESSION_6 = "abs(-3)";
    private static final String EXPRESSION_7 = "sqrt(16)";
    private static final String EXPRESSION_8 = "1+2+3";
    private static final String EXPRESSION_9 = "1+2*(3+4)";
    private static final String EXPRESSION_10 = "1+2*(3+4)+5";
    private static final String EXPRESSION_11 = "dateAdd(now(), '1 day')";
    private static final String EXPRESSION_12 = "length('abcdef')";
    private static final String EXPRESSION_13 = "toLower('abcDEF')";

    @Before
    public void setUp() {
        Map<FunctionExpression, FunctionExpression> reservedFunctions = new TreeMap<>();
        putFunction(new AndFunction(), reservedFunctions);
        putFunction(new OrFunction(), reservedFunctions);
        putFunction(new AbsFunction(), reservedFunctions);
        putFunction(new DivideFunction(), reservedFunctions);
        putFunction(new MultiplyFunction(), reservedFunctions);
        putFunction(new PowerFunction(), reservedFunctions);
        putFunction(new SqrtFunction(), reservedFunctions);
        putFunction(new SubtractFunction(), reservedFunctions);
        putFunction(new DateAddFunction(), reservedFunctions);
        putFunction(new LengthFunction(), reservedFunctions);
        putFunction(new ToLowerFunction(), reservedFunctions);

        Expression.reservedFunctions = reservedFunctions;
    }

    @Test
    public void testExpressionEvaluation() throws Exception {
        expression = new Expression(EXPRESSION_1);
        expression.prepare();
        double result = (double) expression.evaluate();
        assertEquals(7.0, result, 0.0);

        expression.setExpression(EXPRESSION_2);
        expression.prepare();
        result = (double) expression.evaluate();
        assertEquals(13.0, result, 0.0);

        expression.setExpression(EXPRESSION_3);
        expression.prepare();
        boolean logicalResult = (boolean) expression.evaluate();
        assertTrue(logicalResult);

        expression.setExpression(EXPRESSION_4);
        expression.prepare();
        logicalResult = (boolean) expression.evaluate();
        assertFalse(logicalResult);

        expression.setExpression(EXPRESSION_5);
        expression.prepare();
        logicalResult = (boolean) expression.evaluate();
        assertTrue(logicalResult);

        expression.setExpression(EXPRESSION_6);
        expression.prepare();
        double absResult = (double) expression.evaluate();
        assertEquals(3.0, absResult, 0.0);

        expression.setExpression(EXPRESSION_7);
        expression.prepare();
        double sqrtResult = (double) expression.evaluate();
        assertEquals(4.0, sqrtResult, 0.0);

        expression.setExpression(EXPRESSION_8);
        expression.prepare();
        result = (double) expression.evaluate();
        assertEquals(6.0, result, 0.0);

        expression.setExpression(EXPRESSION_9);
        expression.prepare();
        result = (double) expression.evaluate();
        assertEquals(10.0, result, 0.0);

        expression.setExpression(EXPRESSION_10);
        expression.prepare();
        result = (double) expression.evaluate();
        assertEquals(14.0, result, 0.0);

        expression.setExpression(EXPRESSION_11);
        expression.prepare();
        DateUtils dateUtils = new DateUtils();
        java.util.Date now = new java.util.Date();
        java.util.Date resultDate = (java.util.Date) expression.evaluate();
        assertTrue(dateUtils.isSameDay(now, resultDate));

        expression.setExpression(EXPRESSION_12);
        expression.prepare();
        int lengthResult = (int) expression.evaluate();
        assertEquals(6, lengthResult);

        expression.setExpression(EXPRESSION_13);
        expression.prepare();
        String toLowerResult = (String) expression.evaluate();
        assertEquals("abcdef", toLowerResult);
    }

    private static void putFunction(FunctionExpression f, Map<FunctionExpression, FunctionExpression> map) {
        map.put(f, f);
    }
}