package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

/**
 * Tests for {@code CoreOperationLessThan} class
 */
public class CoreOperationLessThanTest {

    @Test
    public void testLessThan() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = lessThan.computeValue(context);

        assertTrue("Expected true, but got: " + result, result instanceof Boolean);
        assertEquals("Expected: true, but got: " + result, Boolean.TRUE, result);
    }

    @Test
    public void testNotLessThan() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = lessThan.computeValue(context);

        assertFalse("Expected false, but got: " + result, result instanceof Boolean);
        assertEquals("Expected: false, but got: " + result, Boolean.FALSE, result);
    }

    @Test
    public void testEqual() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = lessThan.computeValue(context);

        assertFalse("Expected false, but got: " + result, result instanceof Boolean);
        assertEquals("Expected: false, but got: " + result, Boolean.FALSE, result);
    }

    @Test
    public void testGreaterThan() {
        Expression arg1 = new LiteralExpression(2.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = lessThan.computeValue(context);

        assertFalse("Expected false, but got: " + result, result instanceof Boolean);
        assertEquals("Expected: false, but got: " + result, Boolean.FALSE, result);
    }

    @Test
    public void testLessThanWithNegativeNumbers() {
        Expression arg1 = new LiteralExpression(-1.0);
        Expression arg2 = new LiteralExpression(-2.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = lessThan.computeValue(context);

        assertTrue("Expected true, but got: " + result, result instanceof Boolean);
        assertEquals("Expected: true, but got: " + result, Boolean.TRUE, result);
    }

    @Test
    public void testNotLessThanWithNegativeNumbers() {
        Expression arg1 = new LiteralExpression(-1.0);
        Expression arg2 = new LiteralExpression(-1.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = lessThan.computeValue(context);

        assertFalse("Expected false, but got: " + result, result instanceof Boolean);
        assertEquals("Expected: false, but got: " + result, Boolean.FALSE, result);
    }
}