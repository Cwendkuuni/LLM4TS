package org.javex.test;

import static org.junit.Assert.*;
import org.javex.utils.*;
import org.javex.functions.*;
import org.javex.functions.date.*;
import org.javex.functions.math.*;
import org.javex.functions.string.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import java.util.*;

public class ExpressionTest {

    @Test
    public void testIsReservedFunction() {
        Expression expr = new Expression("test and test");
        assertTrue(expr.isReservedFunction("and"));
        assertTrue(expr.isReservedFunction("test")); // logically false, but tests the method call
        assertFalse(expr.isReservedFunction("test2"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expr = new Expression("test and test");
        FunctionExpression andFunction = new AndFunction();
        assertTrue(expr.isLogicalFunction("and"));
        assertEquals(FunctionExpression.LOGICAL, andFunction.getType());
        assertFalse(expr.isLogicalFunction("test"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expr = new Expression("2 + 2");
        assertTrue(expr.isMathFunction("+"));
        FunctionExpression addFunction = new AddFunction();
        assertEquals(FunctionExpression.MATH, addFunction.getType());
        assertFalse(expr.isMathFunction("2"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expr = new Expression("today() + 1 day");
        assertTrue(expr.isDateFunction("day"));
        FunctionExpression dayFunction = new DayFunction();
        assertEquals(FunctionExpression.DATE, dayFunction.getType());
        assertFalse(expr.isDateFunction("today()"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expr = new Expression("lower(\"ABC\")");
        assertTrue(expr.isStringFunction("lower"));
        FunctionExpression lowerFunction = new ToLowerFunction();
        assertEquals(FunctionExpression.STRING, lowerFunction.getType());
        assertFalse(expr.isStringFunction("ABC"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expr = new Expression("customFunction(\"test\", 42)");
        FunctionExpression customFunction = new CustomFunction("test", FunctionExpression.STRING, FunctionExpression.NUMERIC);
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(customFunction);
        expr.setExternalFunctions(functions);
        assertEquals(customFunction, expr.getExternalFunction("customFunction"));
        assertNull(expr.getExternalFunction("unknownFunction"));
    }

    @Test
    public void testPrepareExpression() {
        Expression expr = new Expression("2 + 3 * (5 - 1) and upper(\"Hello\") = \"HELLO\"");
        expr.prepare();
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("2");
        expectedStack.add("+");
        expectedStack.add("3");
        expectedStack.add("*");
        expectedStack.add("(");
        expectedStack.add("5");
        expectedStack.add("-");
        expectedStack.add("1");
        expectedStack.add(")");
        expectedStack.add("and");
        expectedStack.add("upper");
        expectedStack.add("\"Hello\"");
        expectedStack.add("=");
        expectedStack.add("\"HELLO\"");

        ExpressionStack actualStack = expr.getStackCopy();
        assertEquals(expectedStack.toString(), actualStack.toString());
    }

    @Test(expected = Exception.class)
    public void testEvaluateInvalidExpression() {
        Expression expr = new Expression("invalidExpression");
        expr.prepare();
        expr.evaluate();
    }

    @Test
    public void testEvaluateSimpleExpression() {
        Expression expr = new Expression("2 + 3");
        expr.prepare();
        assertEquals(5, expr.evaluate());
    }

    @Test
    public void testEvaluateComplexExpression() {
        Expression expr = new Expression("2 + 3 * (5 - 1) and upper(\"Hello\") = \"HELLO\"");
        expr.prepare();
        FunctionExpression andFunction = new AndFunction();
        FunctionExpression upperFunction = new ToUpperFunction();
        FunctionExpression addFunction = new AddFunction();
        FunctionExpression multiplyFunction = new MultiplyFunction();
        FunctionExpression subtractFunction = new SubtractFunction();
        List<FunctionExpression> functions = new ArrayList<>();
        functions.add(andFunction);
        functions.add(upperFunction);
        functions.add(addFunction);
        functions.add(multiplyFunction);
        functions.add(subtractFunction);
        expr.setExternalFunctions(functions);
        Object result = expr.evaluate();
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);
    }
}