package org.javex.expression;

import static org.junit.Assert.*;
import org.javex.utils.JavexUtils;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.LengthFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.ContainsFunction;
import org.javex.functions.string.EndsWithFunction;
import org.javex.functions.string.StartsWithFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression("1 + 2 * (3 + 4) / (5 + 6)") {
            @Override
            protected List externalFunctions() {
                return Arrays.asList(new AndFunction(), new OrFunction(), new AbsFunction(),
                        new DivideFunction(), new MultiplyFunction(), new PowerFunction(),
                        new SubtractFunction(), new DateAddFunction(), new DateDiffFunction(),
                        new LengthFunction(), new ContainsFunction(), new EndsWithFunction(),
                        new StartsWithFunction(), new SubStringFunction(), new ToLowerFunction(),
                        new ToUpperFunction());
            }
        };
    }

    @Test
    public void testExpressionConstruction() {
        assertNotNull(expression);
        assertEquals("1 + 2 * (3 + 4) / (5 + 6)", expression.expression);
        assertTrue(expression.stack.isEmpty());
    }

    @Test
    public void testIsReservedFunction() {
        assertTrue(expression.isReservedFunction("and"));
        assertTrue(expression.isReservedFunction("between"));
        // Add more tests for all reserved functions

        assertFalse(expression.isReservedFunction("myFunction"));
    }

    @Test
    public void testIsLogicalFunction() {
        assertTrue(expression.isLogicalFunction("and"));
        assertTrue(expression.isLogicalFunction("or"));
        // Add more tests for all logical functions

        assertFalse(expression.isLogicalFunction("abs"));
    }

    @Test
    public void testIsMathFunction() {
        assertTrue(expression.isMathFunction("abs"));
        assertTrue(expression.isMathFunction("divide"));
        // Add more tests for all math functions

        assertFalse(expression.isMathFunction("and"));
    }

    @Test
    public void testIsDateFunction() {
        assertTrue(expression.isDateFunction("dateadd"));
        assertTrue(expression.isDateFunction("datediff"));
        // Add more tests for all date functions

        assertFalse(expression.isDateFunction("and"));
    }

    @Test
    public void testIsStringFunction() {
        assertTrue(expression.isStringFunction("contains"));
        assertTrue(expression.isStringFunction("endsWith"));
        // Add more tests for all string functions

        assertFalse(expression.isStringFunction("and"));
    }

    @Test
    public void testGetExternalFunction() {
        FunctionExpression andFunction = expression.getExternalFunction("and");
        assertEquals(AndFunction.class, andFunction.getClass());

        FunctionExpression nonExistentFunction = expression.getExternalFunction("nonExistentFunction");
        assertNull(nonExistentFunction);
    }

    @Test
    public void testIsFunction() {
        assertTrue(expression.isFunction("and"));
        assertTrue(expression.isFunction("abs"));
        // Add more tests for all functions

        assertFalse(expression.isFunction("123"));
    }

    @Test
    public void testPrepare() {
        expression.prepare();
        assertFalse(expression.stack.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrepareWithInvalidExpression() {
        Expression invalidExpression = new Expression("") {
            @Override
            protected List externalFunctions() {
                return new ArrayList<>();
            }
        };
        invalidExpression.prepare();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrepareWithInvalidExpression2() {
        Expression invalidExpression = new Expression("1 +") {
            @Override
            protected List externalFunctions() {
                return new ArrayList<>();
            }
        };
        invalidExpression.prepare();
    }

    @Test
    public void testEvaluate() throws Exception {
        expression.prepare();
        assertEquals(6.0, expression.evaluate());
    }

    @Test(expected = IllegalStateException.class)
    public void testEvaluateWithoutPreparing() {
        expression.evaluate();
    }

    @Test
    public void testGetStackCopy() {
        expression.prepare();
        ExpressionStack stackCopy = expression.getStackCopy();
        assertEquals(expression.stack.size(), stackCopy.size());
        for (int i = 0; i < stackCopy.size(); i++) {
            assertEquals(expression.stack.get(i), stackCopy.get(i));
        }
    }
}