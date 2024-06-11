package org.javex.test;

import org.junit.Test;
import org.javex.Expression;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("expression with external functions");
        List<Expression> externalFunctions = expression.getExternalFunctions();
        // Add asserts here to check the contents of externalFunctions
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("1+2");
        // Add asserts here to check the state of the Expression object
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("1+2");
        expression.prepare(); // Call prepare() method before evaluation
        Object result = expression.evaluate();
        // Add asserts here to check the result of the evaluation
    }

    @Test
    public void testEmptyExpression() {
        Expression expression = new Expression("");
        try {
            expression.prepare();
            expression.evaluate();
            fail("Expected Exception");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Empty expression"));
        }
    }

    @Test
    public void testInvalidFunction() {
        Expression expression = new Expression("invalidFunction(1,2)");
        try {
            expression.prepare();
            expression.evaluate();
            fail("Expected Exception");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Invalid function 'invalidFunction'"));
        }
    }

    @Test
    public void testMissingParentheses() {
        Expression expression = new Expression("1+2*3");
        try {
            expression.prepare();
            expression.evaluate();
            fail("Expected Exception");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Missing parentheses"));
        }
    }

    @Test
    public void testInvalidOperator() {
        Expression expression = new Expression("1+^2");
        try {
            expression.prepare();
            expression.evaluate();
            fail("Expected Exception");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Invalid operator '^'"));
        }
    }

    @Test
    public void testArithmeticOverflowUnderflow() {
        Expression expression = new Expression("1E+308+1E-308");
        try {
            expression.prepare();
            expression.evaluate();
            fail("Expected Exception");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Arithmetic overflow or underflow"));
        }
    }

    @Test
    public void testLogicalErrors() {
        Expression expression = new Expression("true and 5=5 and not 3=3");
        try {
            expression.prepare();
            expression.evaluate();
            fail("Expected Exception");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Logical error"));
        }
    }

    @Test
    public void testStringManipulationErrors() {
        Expression expression = new Expression("substr(abc,5,3) and length(abc)<5");
        try {
            expression.prepare();
            expression.evaluate();
            fail("Expected Exception");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("String manipulation error"));
        }
    }

    @Test
    public void testDateManipulationErrors() {
        Expression expression = new Expression("dateAdd(date '2022-01-01',interval '1 year') and dateDiff(date '2022-01-01',date '2022-01-02') < 0");
        try {
            expression.prepare();
            expression.evaluate();
            fail("Expected Exception");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Date manipulation error"));
        }
    }
}