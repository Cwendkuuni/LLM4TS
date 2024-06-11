package org.javex.expression;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.javex.Expression;
import org.javex.functions.FunctionExpression;
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
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.javex.utils.JavexUtils;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testBasicFunctionality() {
        final Expression expression = new Expression("1 + 2 * 3");
        expression.prepare();
        assertEquals(6.0, expression.evaluate());
    }

    @Test
    public void testReservedFunctions() {
        final Expression expression = new Expression("1 > 2 and 3 > 4");
        expression.prepare();
        assertFalse(expression.evaluate());

        expression = new Expression("1 < 2 or 3 > 4");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("1 and 2 and 3");
        expression.prepare();
        assertFalse(expression.evaluate());

        expression = new Expression("1 or 2 or 3");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("not 1 = 2");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("not 1 < 2");
        expression.prepare();
        assertFalse(expression.evaluate());

        expression = new Expression("not 1 > 2");
        expression.prepare();
        assertFalse(expression.evaluate());
    }

    @Test
    public void testLogicalFunctions() {
        final Expression expression = new Expression("and(1 > 2, 3 > 4)");
        expression.prepare();
        assertFalse(expression.evaluate());

        expression = new Expression("or(1 > 2, 3 > 4)");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("not(and(1 > 2, 3 > 4))");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("not(or(1 > 2, 3 > 4))");
        expression.prepare();
        assertFalse(expression.evaluate());
    }

    @Test
    public void testMathFunctions() {
        final Expression expression = new Expression("abs(-1)");
        expression.prepare();
        assertEquals(1.0, expression.evaluate());

        expression = new Expression("sqrt(16)");
        expression.prepare();
        assertEquals(4.0, expression.evaluate());

        expression = new Expression("power(2, 3)");
        expression.prepare();
        assertEquals(8.0, expression.evaluate());

        expression = new Expression("power(2, -1)");
        expression.prepare();
        assertEquals(0.5, expression.evaluate());

        expression = new Expression("divide(10, 2)");
        expression.prepare();
        assertEquals(5.0, expression.evaluate());

        expression = new Expression("mult(2, 3) + 1");
        expression.prepare();
        assertEquals(7.0, expression.evaluate());

        expression = new Expression("sub(5, 2, 1)");
        expression.prepare();
        assertEquals(3.0, expression.evaluate());

        expression = new Expression("sub(5, mult(2, 3), 1) + 1");
        expression.prepare();
        assertEquals(2.0, expression.evaluate());
    }

    @Test
    public void testDateFunctions() {
        final Expression expression = new Expression("dateadd(day, '2022-01-01', 1) > date('2022-01-03')");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("datediff(day, '2022-01-01', '2022-01-03') < 3");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("length(substring('abcdef', 2, 3)) = 3");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("tolower(substring('abcdef', 2, 3)) = 'def'");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("length(tolower(substring('abcdef', 2, 3))) = 3");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("toUpper(substring('abcdef', 2, 3)) = 'DEF'");
        expression.prepare();
        assertFalse(expression.evaluate());
    }

    @Test
    public void testStringFunctions() {
        final Expression expression = new Expression("length(concat('abc', 'def')) = 6");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("toUpper(substring(concat('abc', 'def'), 2, 3)) = 'DEF'");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("tolower(substring(concat('abc', 'def'), 2, 3)) = 'def'");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("substring(concat('abc', 'def'), 1, 3) = 'abc'");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("substring(concat('abc', 'def'), 5, 3) = 'def'");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("substring(concat('abc', 'def'), 4, 1) = 'e'");
        expression.prepare();
        assertTrue(expression.evaluate());
    }

    @Test
    public void testExternalFunctions() {
        final Expression expression = new Expression("myFunction(1, 2, 3)");
        expression.prepared = false;
        expression.externalFunctions = Arrays.asList(new MyFunction());
        expression.prepare();
        assertEquals(6.0, expression.evaluate(), 0.01);
    }

    @Test(expected = RuntimeException.class)
    public void testUndefinedFunction() {
        final Expression expression = new Expression("undefinedFunction(1, 2, 3)");
        expression.prepare();
        fail("Expected a RuntimeException");
    }

    @Test
    public void testPredefinedFunctions() {
        final Expression expression = new Expression("and(1 > 2, 3 > 4)");
        expression.prepare();
        assertFalse(expression.evaluate());

        expression = new Expression("or(1 > 2, 3 > 4)");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("not(and(1 > 2, 3 > 4))");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("not(or(1 > 2, 3 > 4))");
        expression.prepare();
        assertFalse(expression.evaluate());

        expression = new Expression("isNull(null)");
        expression.prepare();
        assertTrue(expression.evaluate());

        expression = new Expression("isNull(1)");
        expression.prepare();
        assertFalse(expression.evaluate());

        expression = new Expression("isFunction(myFunction)");
        expression.prepare();
        assertTrue(expression.evaluate());
    }

    @Test
    public void testPredefinedFunctionsWithExternal() {
        final Expression expression = new Expression("myFunction(and(1 > 2, 3 > 4))");
        expression.prepared = false;
        expression.externalFunctions = Arrays.asList(new MyFunction());
        expression.prepare();
        assertEquals(5.0, expression.evaluate(), 0.01);
    }

    @Test
    public void testPredefinedFunctionsWithUndefined() {
        final Expression expression = new Expression("undefinedFunction(and(1 > 2, 3 > 4))");
        expression.prepare();
        assertFalse(expression.evaluate());
    }

    @Test
    public void testPredefinedFunctionsWithPredefined() {
        final Expression expression = new Expression("not(or(not(1 > 2), not(3 > 4))))");
        expression.prepare();
        assertTrue(expression.evaluate());
    }

    @Test
    public void testPredefinedFunctionsWithExternalAndPredefined() {
        final Expression expression = new Expression("myFunction(or(not(1 > 2), not(3 > 4))))");
        expression.prepared = false;
        expression.externalFunctions = Arrays.asList(new MyFunction());
        expression.prepare();
        assertEquals(4.0, expression.evaluate(), 0.01);
    }

    @Test
    public void testPredefinedFunctionsWithExternalAndUndefined() {
        final Expression expression = new Expression("myFunction(undefinedFunction(1 > 2, 3 > 4))");
        expression.prepare();
        assertFalse(expression.evaluate());
    }

    @Test
    public void testPredefinedFunctionsWithUndefinedAndPredefined() {
        final Expression expression = new Expression("not(undefinedFunction(or(not(1 > 2), not(3 > 4))))");
        expression.prepare();
        assertFalse(expression.evaluate());
    }

    @Test
    public void testPredefinedFunctionsWithUndefinedAndUndefined() {
        final Expression expression = new Expression("not(undefinedFunction(undefinedFunction(1 > 2, 3 > 4))))");
        expression.prepare();
        assertFalse(expression.evaluate());
    }

    @Test
    public void testPredefinedFunctionsWithExternalAndPredefinedAndUndefined() {
        final Expression expression = new Expression("myFunction(not(or(not(1 > 2), not(3 > 4)))) or undefinedFunction(1)");
        expression.prepared = false;
        expression.externalFunctions = Arrays.asList(new MyFunction());
        expression.prepare();
        assertFalse(expression.evaluate());
    }

    @Test
    public void testEmptyString() {
        final Expression expression = new Expression("");
        expression.prepare();
        assertTrue(expression.evaluate());
    }

    @Test
    public void testEmptyStack() {
        final Expression expression = new Expression("");
        expression.stack = new ExpressionStack();
        expression.prepare();
        assertTrue(expression.evaluate());
    }

    @Test
    public void testNullExpression() {
        final Expression expression = new Expression(null);
        expression.prepare();
        assertFalse(expression.evaluate());
    }

    private static class MyFunction implements FunctionExpression {
        @Override
        public String getSimbol() {
            return "myFunction";
        }

        @Override
        public FunctionType getType() {
            return FunctionType.MATH;
        }

        @Override
        public Object evaluate(List args) {
            final double a = ((Number) args.get(0)).doubleValue();
            final double b = ((Number) args.get(1)).doubleValue();
            final double c = ((Number) args.get(2)).doubleValue();
            return a + b + c;
        }
    }

    private static class GenericFunctionExpression implements FunctionExpression {
        private final String symbol;

        public GenericFunctionExpression(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String getSimbol() {
            return symbol;
        }

        @Override
        public FunctionType getType() {
            return null;
        }

        @Override
        public Object evaluate(List args) {
            throw new UnsupportedOperationException("This function does not have an implementation");
        }
    }
}