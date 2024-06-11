package org.javex.test;

import static org.junit.Assert.*;

import org.javex.utils.*;
import org.javex.functions.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.FunctionExpression;
import org.junit.Test;

import java.util.*;

public class ExpressionTest {

    @Test
    public void testIsReservedFunction() {
        Expression exp = new Expression("");
        assertTrue(exp.isReservedFunction("and"));
        assertTrue(exp.isReservedFunction("or"));
        assertTrue(exp.isReservedFunction("not"));
        // Add more tests for all reserved functions
    }

    @Test
    public void testIsLogicalFunction() {
        Expression exp = new Expression("");
        assertFalse(exp.isLogicalFunction("abs"));
        assertFalse(exp.isLogicalFunction("sqrt"));
        // Add more tests for all logical functions
        FunctionExpression and = new LogicalAndFunction();
        FunctionExpression or = new LogicalOrFunction();
        assertTrue(exp.isLogicalFunction(and.getSimbol()));
        assertTrue(exp.isLogicalFunction(or.getSimbol()));
    }

    @Test
    public void testGetFunction() {
        Expression exp = new Expression("");
        FunctionExpression and = new LogicalAndFunction();
        FunctionExpression or = new LogicalOrFunction();
        FunctionExpression add = new MathAddFunction();
        FunctionExpression sub = new MathSubtractFunction();

        assertEquals(and, exp.getFunction(and.getSimbol()));
        assertEquals(or, exp.getFunction(or.getSimbol()));
        assertEquals(add, exp.getFunction(add.getSimbol()));
        assertEquals(sub, exp.getFunction(sub.getSimbol()));
        // Add more tests for all functions
        assertNull(exp.getFunction("nonExistentFunction"));
    }

    @Test
    public void testEvaluateSimpleExpressions() throws Exception {
        Expression exp = new Expression("1 + 2");
        ExpressionStack stack = exp.getStackCopy();
        Object result = exp.evaluate();
        assertEquals(3.0, result);

        exp = new Expression("1 + 2 * 3");
        stack = exp.getStackCopy();
        result = exp.evaluate();
        assertEquals(7.0, result);

        exp = new Expression("(1 + 2) * 3");
        stack = exp.getStackCopy();
        result = exp.evaluate();
        assertEquals(9.0, result);

        exp = new Expression("true and false");
        stack = exp.getStackCopy();
        result = exp.evaluate();
        assertFalse(result);

        exp = new Expression("true or false");
        stack = exp.getStackCopy();
        result = exp.evaluate();
        assertTrue(result);

        // Add more tests for different types of expressions
    }

    @Test
    public void testPrepare() throws Exception {
        Expression exp = new Expression("1 + 2 * (3 + 4)");
        exp.prepare();
        ExpressionStack stack = exp.getStackCopy();
        Object result = exp.evaluate();
        assertEquals(10.0, result);
    }

    @Test(expected = Exception.class)
    public void testInvalidExpression() throws Exception {
        Expression exp = new Expression("invalidExpression");
        exp.prepare();
        exp.evaluate();
        // This test should throw an Exception
    }
}