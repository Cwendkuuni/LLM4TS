package org.javex;

import static org.junit.Assert.*;
import org.javex.utils.*;
import org.javex.functions.*;
import org.junit.Test;

import java.util.*;

public class ExpressionTest {

    @Test
    public void testIsReservedFunction() {
        Expression e = new Expression("");
        assertFalse(e.isReservedFunction(""));
        assertTrue(e.isReservedFunction("and"));
        assertTrue(e.isReservedFunction("or"));
        // Test other reserved functions here
    }

    @Test
    public void testIsLogicalFunction() {
        Expression e = new Expression("");
        assertFalse(e.isLogicalFunction(""));
        assertTrue(e.isLogicalFunction("and"));
        assertTrue(e.isLogicalFunction("or"));
        // Test other logical functions here
    }

    @Test
    public void testIsMathFunction() {
        Expression e = new Expression("");
        assertFalse(e.isMathFunction(""));
        assertTrue(e.isMathFunction("abs"));
        assertTrue(e.isMathFunction("sqrt"));
        // Test other math functions here
    }

    @Test
    public void testIsDateFunction() {
        Expression e = new Expression("");
        assertFalse(e.isDateFunction(""));
        assertTrue(e.isDateFunction("dateadd"));
        assertTrue(e.isDateFunction("datediff"));
        // Test other date functions here
    }

    @Test
    public void testIsStringFunction() {
        Expression e = new Expression("");
        assertFalse(e.isStringFunction(""));
        assertTrue(e.isStringFunction("length"));
        assertTrue(e.isStringFunction("substring"));
        // Test other string functions here
    }

    @Test
    public void testGetFunction() {
        Expression e = new Expression("");
        FunctionExpression function = e.getFunction("and");
        assertNotNull(function);
        assertEquals(FunctionType.LOGICAL, function.getType());
        // Test other functions here
    }

    @Test
    public void testGetExternalFunction() {
        Expression e = new Expression("");
        FunctionExpression function = e.getExternalFunction("customFunction");
        assertNull(function);
        // Add customFunction to the externalFunctions list and test again
    }

    @Test
    public void testIsFunction() {
        Expression e = new Expression("");
        String expression = "customFunction(1, 2, 3)";
        e.setExpression(expression);
        Stack<String> stack = new Stack<>();
        e.tokenize();
        for (String token : e.getStack()) {
            stack.push(token);
        }
        assertTrue(e.isFunction("customFunction"));
        // Test other functions here
    }

    @Test
    public void testPrepare() {
        Expression e = new Expression("");
        e.setExpression("1 + 2 * 3");
        e.prepare();
        // Test the stack here
    }

    @Test(expected = Exception.class)
    public void testEvaluateWithException() {
        Expression e = new Expression("");
        e.setExpression("invalidExpression");
        e.prepare();
        e.evaluate();
    }

    @Test
    public void testEvaluate() {
        Expression e = new Expression("");
        e.setExpression("1 + 2 * 3");
        e.prepare();
        Object result = e.evaluate();
        assertEquals(6.0, result);
    }
}