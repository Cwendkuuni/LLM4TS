package org.javex.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression("(1 + 2) * 3 + 4");
    }

    @Test
    public void testExpressionConstructor() {
        assertNotNull(expression);
        assertEquals("(1+2)*3+4", expression.getExpression());
        assertEquals(Collections.emptyList(), expression.getExternalFunctions());
    }

    @Test
    public void testIsLogicalFunction() {
        assertFalse(expression.isLogicalFunction("+"));
        assertFalse(expression.isLogicalFunction("*"));
        assertFalse(expression.isLogicalFunction("("));
        assertFalse(expression.isLogicalFunction(")"));
        assertFalse(expression.isLogicalFunction("1"));
        assertFalse(expression.isLogicalFunction("3"));
        assertFalse(expression.isLogicalFunction("4"));
        assertFalse(expression.isLogicalFunction("5"));
        assertTrue(expression.isLogicalFunction("and"));
        assertTrue(expression.isLogicalFunction("or"));
    }

    @Test
    public void testIsMathFunction() {
        assertFalse(expression.isMathFunction("+"));
        assertFalse(expression.isMathFunction("*"));
        assertFalse(expression.isMathFunction("("));
        assertFalse(expression.isMathFunction(")"));
        assertFalse(expression.isMathFunction("1"));
        assertFalse(expression.isMathFunction("3"));
        assertFalse(expression.isMathFunction("4"));
        assertFalse(expression.isMathFunction("5"));
        assertFalse(expression.isMathFunction("and"));
        assertFalse(expression.isMathFunction("or"));
        assertTrue(expression.isMathFunction("Abs"));
        assertTrue(expression.isMathFunction("Divide"));
        assertTrue(expression.isMathFunction("Length"));
        assertTrue(expression.isMathFunction("Multiply"));
        assertTrue(expression.isMathFunction("Plus"));
        assertTrue(expression.isMathFunction("Power"));
        assertTrue(expression.isMathFunction("Sqrt"));
        assertTrue(expression.isMathFunction("Subtract"));
    }

    @Test
    public void testIsDateFunction() {
        assertFalse(expression.isDateFunction("+"));
        assertFalse(expression.isDateFunction("*"));
        assertFalse(expression.isDateFunction("("));
        assertFalse(expression.isDateFunction(")"));
        assertFalse(expression.isDateFunction("1"));
        assertFalse(expression.isDateFunction("3"));
        assertFalse(expression.isDateFunction("4"));
        assertFalse(expression.isDateFunction("5"));
        assertFalse(expression.isDateFunction("and"));
        assertFalse(expression.isDateFunction("or"));
        assertFalse(expression.isDateFunction("Abs"));
        assertFalse(expression.isDateFunction("Divide"));
        assertFalse(expression.isDateFunction("Length"));
        assertFalse(expression.isDateFunction("Multiply"));
        assertFalse(expression.isDateFunction("Plus"));
        assertFalse(expression.isDateFunction("Power"));
        assertFalse(expression.isDateFunction("Sqrt"));
        assertFalse(expression.isDateFunction("Subtract"));
        assertTrue(expression.isDateFunction("DateAdd"));
        assertTrue(expression.isDateFunction("DateDiff"));
    }

    @Test
    public void testIsStringFunction() {
        assertFalse(expression.isStringFunction("+"));
        assertFalse(expression.isStringFunction("*"));
        assertFalse(expression.isStringFunction("("));
        assertFalse(expression.isStringFunction(")"));
        assertFalse(expression.isStringFunction("1"));
        assertFalse(expression.isStringFunction("3"));
        assertFalse(expression.isStringFunction("4"));
        assertFalse(expression.isStringFunction("5"));
        assertFalse(expression.isStringFunction("and"));
        assertFalse(expression.isStringFunction("or"));
        assertFalse(expression.isStringFunction("Abs"));
        assertFalse(expression.isStringFunction("Divide"));
        assertFalse(expression.isStringFunction("Length"));
        assertFalse(expression.isStringFunction("Multiply"));
        assertFalse(expression.isStringFunction("Plus"));
        assertFalse(expression.isStringFunction("Power"));
        assertFalse(expression.isStringFunction("Sqrt"));
        assertFalse(expression.isStringFunction("Subtract"));
        assertFalse(expression.isStringFunction("DateAdd"));
        assertFalse(expression.isStringFunction("DateDiff"));
        assertTrue(expression.isStringFunction("Contains"));
        assertTrue(expression.isStringFunction("EndsWith"));
        assertTrue(expression.isStringFunction("StartsWith"));
        assertTrue(expression.isStringFunction("Matches"));
        assertTrue(expression.isStringFunction("Length"));
        assertTrue(expression.isStringFunction("SubString"));
        assertTrue(expression.isStringFunction("ToLower"));
        assertTrue(expression.isStringFunction("ToUpper"));
    }

    @Test
    public void testIsFunction() {
        assertFalse(expression.isFunction("+"));
        assertFalse(expression.isFunction("*"));
        assertFalse(expression.isFunction("1"));
        assertFalse(expression.isFunction("3"));
        assertFalse(expression.isFunction("and"));
        assertFalse(expression.isFunction("or"));
        assertTrue(expression.isFunction("("));
        assertTrue(expression.isFunction(")"));
        assertTrue(expression.isFunction("Abs"));
        assertTrue(expression.isFunction("DateAdd"));
        assertTrue(expression.isFunction("DateDiff"));
        assertTrue(expression.isFunction("Contains"));
        assertTrue(expression.isFunction("EndsWith"));
        assertTrue(expression.isFunction("Length"));
        assertTrue(expression.isFunction("Multiply"));
        assertTrue(expression.isFunction("Plus"));
        assertTrue(expression.isFunction("Power"));
        assertTrue(expression.isFunction("Sqrt"));
        assertTrue(expression.isFunction("Subtract"));
        assertTrue(expression.isFunction("StartsWith"));
        assertTrue(expression.isFunction("Matches"));
        assertTrue(expression.isFunction("InList"));
    }

    @Test
    public void testGetFunction() {
        FunctionExpression result = expression.getFunction("Abs");
        assertNotNull(result);
        assertEquals(FunctionExpression.MATH, result.getType());
        assertEquals("Abs", result.getSymbol());

        result = expression.getFunction("and");
        assertNotNull(result);
        assertEquals(FunctionExpression.LOGICAL, result.getType());
        assertEquals("and", result.getSymbol());

        result = expression.getFunction("Plus");
        assertNotNull(result);
        assertEquals(FunctionExpression.MATH, result.getType());
        assertEquals("Plus", result.getSymbol());
    }

    @Test
    public void testIsReservedFunction() {
        assertFalse(expression.isReservedFunction("Abs"));
        assertFalse(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("Plus"));
        assertTrue(expression.isReservedFunction("("));
        assertTrue(expression.isReservedFunction(")"));
        assertTrue(expression.isReservedFunction("not"));
        assertTrue(expression.isReservedFunction("or"));
    }

    @Test
    public void testPrepare() {
        expression.prepare();
        assertTrue(expression.isPrepared());

        Expression expressionCopy = new Expression(expression.getExpression());
        expressionCopy.prepare();
        assertNotSame(expression.getStack(), expressionCopy.getStack());
    }

    @Test(expected = Exception.class)
    public void testEvaluateException() {
        expression.evaluate();
    }

    @Test
    public void testEvaluate() {
        Object result = expression.evaluate();
        assertEquals(13.0, result, 0.001);
    }

    @Test
    public void testGetExternalFunctions() {
        List<FunctionExpression> functions = Arrays.asList(
                new FunctionExpression("MyFunction", FunctionExpression.USER_DEFINED),
                new FunctionExpression("AnotherFunction", FunctionExpression.USER_DEFINED));
        Expression expressionWithFunctions = new Expression("MyFunction(1) + AnotherFunction(2)") {
            {
                this.externalFunctions = functions;
            }
        };

        List<FunctionExpression> resultFunctions = expressionWithFunctions.getExternalFunctions();
        assertEquals(2, resultFunctions.size());
        assertNotSame(functions, resultFunctions);
    }

    @Test
    public void testIsExternalFunction() {
        List<FunctionExpression> functions = Arrays.asList(
                new FunctionExpression("MyFunction", FunctionExpression.USER_DEFINED),
                new FunctionExpression("AnotherFunction", FunctionExpression.USER_DEFINED));
        Expression expressionWithFunctions = new Expression("MyFunction(1) + AnotherFunction(2)") {
            {
                this.externalFunctions = functions;
            }
        };

        assertTrue(expressionWithFunctions.isExternalFunction("MyFunction"));
        assertTrue(expressionWithFunctions.isExternalFunction("AnotherFunction"));
        assertFalse(expressionWithFunctions.isExternalFunction("Abs"));
    }
}