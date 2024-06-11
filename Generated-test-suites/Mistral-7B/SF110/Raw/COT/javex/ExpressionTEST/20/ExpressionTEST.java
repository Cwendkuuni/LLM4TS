package org.javex.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.utils.JavexUtils;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        assertNull(expression.getExternalFunctions());

        // Add some external functions
        expression.setExternalFunctions(Arrays.asList(new FunctionExpression("testFunc", "testType"), new FunctionExpression("testFunc2", "testType2")));

        List externalFunctions = expression.getExternalFunctions();
        assertEquals(2, externalFunctions.size());
        FunctionExpression testFunc = (FunctionExpression) externalFunctions.get(0);
        FunctionExpression testFunc2 = (FunctionExpression) externalFunctions.get(1);
        assertEquals("testFunc", testFunc.getSimbol());
        assertEquals("testType", testFunc.getType().name());
        assertEquals("testFunc2", testFunc2.getSimbol());
        assertEquals("testType2", testFunc2.getType().name());
    }

    @Test
    public void testConstructor() {
        Expression expression = new Expression("test");
        assertFalse(expression.isPrepared());
        assertEquals(0, expression.stack.size());
        assertEquals(null, expression.externalFunctions);
        assertEquals("test", expression.expression);

        // Test with empty string
        Expression emptyExpression = new Expression("");
        assertEquals(0, emptyExpression.stack.size());
        assertFalse(emptyExpression.isPrepared());
        assertEquals(null, emptyExpression.externalFunctions);
        assertEquals("", emptyExpression.expression);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("1 + 2 * (3 + 4)");
        expression.doEvaluationStack();

        ExpressionStack stack = expression.getStackCopy();
        assertEquals(7, stack.size());
        assertEquals("3", stack.get(0));
        assertEquals("4", stack.get(1));
        assertEquals("+", stack.get(2));
        assertEquals("2", stack.get(3));
        assertEquals("*", stack.get(4));
        assertEquals("1", stack.get(5));
        assertEquals("+", stack.get(6));

        Object result = expression.evaluate();
        assertEquals(7, result.intValue());
    }

    @Test(expected = Exception.class)
    public void testDoEvaluationStackWithException() {
        Expression expression = new Expression("1 + ( 2 * 3 ) / 0");
        expression.doEvaluationStack();

        try {
            expression.evaluate();
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("and(1, 2)");
        assertTrue(expression.isReservedFunction("and"));

        expression = new Expression("or(1, 2)");
        assertTrue(expression.isReservedFunction("or"));

        expression = new Expression("isFunction(1)");
        assertTrue(expression.isReservedFunction("isFunction"));

        expression = new Expression("isNull(1)");
        assertTrue(expression.isReservedFunction("isNull"));

        expression = new Expression("not(1)");
        assertTrue(expression.isReservedFunction("not"));

        expression = new Expression("abs(1)");
        assertTrue(expression.isReservedFunction("abs"));

        expression = new Expression("divide(1, 2)");
        assertTrue(expression.isReservedFunction("divide"));

        expression = new Expression("length(1)");
        assertTrue(expression.isReservedFunction("length"));

        // Test with non-reserved function
        expression = new Expression("testFunc(1)");
        assertFalse(expression.isReservedFunction("testFunc"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("and(1, 2)");
        assertTrue(expression.isLogicalFunction("and"));

        expression = new Expression("or(1, 2)");
        assertTrue(expression.isLogicalFunction("or"));

        expression = new Expression("not(1)");
        assertTrue(expression.isLogicalFunction("not"));

        expression = new Expression("notChar(1)");
        assertTrue(expression.isLogicalFunction("notChar"));

        expression = new Expression("testFunc(1)");
        assertFalse(expression.isLogicalFunction("testFunc"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("abs(1)");
        assertTrue(expression.isMathFunction("abs"));

        expression = new Expression("divide(1, 2)");
        assertTrue(expression.isMathFunction("divide"));

        expression = new Expression("length(1)");
        assertTrue(expression.isMathFunction("length"));

        expression = new Expression("subString(1, 2, 3)");
        assertTrue(expression.isMathFunction("subString"));

        expression = new Expression("testFunc(1)");
        assertFalse(expression.isMathFunction("testFunc"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("dateAdd(1, 2, " + JavexUtils.DATE_FORMAT + " '2022-01-01', '1 day')");
        assertTrue(expression.isDateFunction("dateAdd"));

        expression = new Expression("dateDiff(1, 2, " + JavexUtils.DATE_FORMAT + " '2022-01-01', '1 day')");
        assertTrue(expression.isDateFunction("dateDiff"));

        expression = new Expression("testFunc(1)");
        assertFalse(expression.isDateFunction("testFunc"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("length(1)");
        assertTrue(expression.isStringFunction("length"));

        expression = new Expression("substring(1, 2, 3)");
        assertTrue(expression.isStringFunction("substring"));

        expression = new Expression("toUpper(1)");
        assertTrue(expression.isStringFunction("toUpper"));

        expression = new Expression("toLower(1)");
        assertTrue(expression.isStringFunction("toLower"));

        expression = new Expression("testFunc(1)");
        assertFalse(expression.isStringFunction("testFunc"));
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("1 + 2 * (3 + 4)");
        assertFalse(expression.isPrepared());

        expression.prepare();
        assertTrue(expression.isPrepared());

        Object result = expression.evaluate();
        assertEquals(7, result.intValue());
    }
}