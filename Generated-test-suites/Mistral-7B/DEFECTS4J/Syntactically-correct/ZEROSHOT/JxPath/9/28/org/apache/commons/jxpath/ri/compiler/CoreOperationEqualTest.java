package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.test.ExpressionTestBase;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CoreOperationEqual}.
 */
public class CoreOperationEqualTest extends ExpressionTestBase {

    @Test
    public void testOperationEqual() {
        Expression arg1 = new LiteralExpression(1L);
        Expression arg2 = new LiteralExpression(1L);
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = operationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testOperationNotEqual() {
        Expression arg1 = new LiteralExpression(1L);
        Expression arg2 = new LiteralExpression(2L);
        CoreOperationEqual operationNotEqual = new CoreOperationEqual(arg1, arg2);
        Object result = operationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testOperationEqualWithNull() {
        Expression arg1 = new LiteralExpression(1L);
        Expression arg2 = new NullExpression();
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = operationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testOperationEqualWithSameNull() {
        Expression arg1 = new NullExpression();
        Expression arg2 = new NullExpression();
        CoreOperationEqual operationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = operationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testOperationCompareWithDifferentTypes() {
        Expression arg1 = new LiteralExpression("Hello");
        Expression arg2 = new LiteralExpression(1L);
        CoreOperationCompare operationCompare = new CoreOperationCompare(new CoreOperationEqual(arg1, new LiteralExpression("Hello")), Operator.EQ);
        operationCompare.setLeft(new CoreOperationCompare(arg1, new LiteralExpression("World"), Operator.NE));
        Object result = operationCompare.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOperationEqualWithInvalidArgTypes() {
        Expression arg1 = new LiteralExpression(1L);
        Expression arg2 = new ArithmeticExpression();
        new CoreOperationEqual(arg1, arg2);
    }
}