package org.javex.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.date.DateUtils;
import org.javex.functions.string.ContainsFunction;
import org.javex.functions.string.EndsWithFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.StartsWithFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToStringFunction;
import org.javex.utils.JavexUtils;

public class ExpressionTest
{
    @org.junit.Test
    public void testExpressionWithReservedFunctions()
    {
        final String expression = "and(1>2,3=3)";
        final Expression e = new Expression(expression);
        e.prepare();
        assertFalse(e.evaluate() instanceof Boolean);
    }

    @org.junit.Test
    public void testExpressionWithLogicalFunctions()
    {
        final String expression = "or(and(1>2,3=3),and(3=3,1>2))";
        final Expression e = new Expression(expression);
        e.prepare();
        assertTrue(e.evaluate() instanceof Boolean);
    }

    @org.junit.Test
    public void testExpressionWithMathFunctions()
    {
        final String expression = "power(power(3,2),2)";
        final Expression e = new Expression(expression);
        e.prepare();
        assertEquals(529, e.evaluate());
    }

    @org.junit.Test
    public void testExpressionWithDateFunctions()
    {
        final String expression = "dateAdd(dateDiff(now()," + DateUtils.DAY + ")," + DateUtils.MONTH + ")";
        final Expression e = new Expression(expression);
        e.prepare();
        final Object result = e.evaluate();
        assertNotNull(result);
        assertTrue(result instanceof java.util.Date);
        final java.util.Date date = (java.util.Date) result;
        assertTrue(date.after(new java.util.Date()));
    }

    @org.junit.Test
    public void testExpressionWithStringFunctions()
    {
        final String expression = "substring(substring(toString(1),1,5),3,2) = 'or'";
        final Expression e = new Expression(expression);
        e.prepare();
        assertFalse(e.evaluate() instanceof Boolean);
    }

    @org.junit.Test
    public void testExpressionWithCustomFunctions()
    {
        final String expression = "myFunction(5, 3)";
        final Expression e = new Expression(expression);
        e.stack = new ExpressionStack();
        e.externalFunctions = new ArrayList<>();
        e.externalFunctions.add(new MyFunction(5, 3));
        e.prepare();
        assertEquals(12, e.evaluate());
    }

    @org.junit.Test
    public void testExpressionWithInvalidFunction()
    {
        final String expression = "invalidFunction(1, 2)";
        final Expression e = new Expression(expression);
        assertThrows(RuntimeException.class, () -> e.prepare());
    }

    @org.junit.Test
    public void testExpressionWithMissingBrackets()
    {
        final String expression = "1 > 2 + 3";
        final Expression e = new Expression(expression);
        assertThrows(RuntimeException.class, () -> e.prepare());
    }

    @org.junit.Test
    public void testExpressionWithMissingOperators()
    {
        final String expression = "1 > 2 = 3";
        final Expression e = new Expression(expression);
        assertThrows(RuntimeException.class, () -> e.prepare());
    }

    @org.junit.Test
    public void testExpressionWithNestedBrackets()
    {
        final String expression = "((1 > 2) and (3 > 4))";
        final Expression e = new Expression(expression);
        e.prepare();
        assertTrue(e.evaluate() instanceof Boolean);
    }

    @org.junit.Test
    public void testExpressionWithMultipleOperators()
    {
        final String expression = "(1 > 2) and (3 > 4)";
        final Expression e = new Expression(expression);
        e.prepare();
        assertTrue(e.evaluate() instanceof Boolean);
    }

    @org.junit.Test
    public void testExpressionWithMultipleInputs()
    {
        final String expression = "max(1, 2, 3)";
        final Expression e = new Expression(expression);
        e.prepare();
        assertEquals(3, e.evaluate());
    }

    private static class MyFunction implements FunctionExpression
    {
        private final int a;
        private final int b;

        public MyFunction(final int a, final int b)
        {
            this.a = a;
            this.b = b;
            this.type = FunctionExpression.CUSTOM;
        }

        @Override
        public String getSimbol()
        {
            return "myFunction";
        }

        @Override
        public Object evaluate(final Map<String, Object> context) throws Exception
        {
            return this.a * this.b;
        }
    }
}