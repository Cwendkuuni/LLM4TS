package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    /**
     * Test method for CoreOperationLessThanOrEqual.computeValue(EvalContext).
     */
    @Test
    public void testComputeValue() {
        // Test with double values
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOpLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOpLessThanOrEqual.computeValue(context);
        assertTrue("Expected true, but got: " + result, result instanceof Boolean);
        assertEquals("Expected: true. Got: " + result, Boolean.TRUE, result);

        arg1 = new LiteralExpression(2.0);
        arg2 = new LiteralExpression(1.0);
        coreOpLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOpLessThanOrEqual.computeValue(context);
        assertTrue("Expected true, but got: " + result, result instanceof Boolean);
        assertEquals("Expected: true. Got: " + result, Boolean.TRUE, result);

        arg1 = new LiteralExpression(2.0);
        arg2 = new LiteralExpression(2.0);
        coreOpLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOpLessThanOrEqual.computeValue(context);
        assertFalse("Expected false, but got: " + result, result instanceof Boolean);

        arg1 = new LiteralExpression(3.0);
        arg2 = new LiteralExpression(2.0);
        coreOpLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOpLessThanOrEqual.computeValue(context);
        assertFalse("Expected false, but got: " + result, result instanceof Boolean);

        // Test with different types of expressions
        arg1 = new LiteralExpression(1.0);
        Expression arg3 = new LiteralExpression("2");
        CoreOperationLessThanOrEqual coreOpLessThanOrEqualString = new CoreOperationLessThanOrEqual(arg1, arg3);
        result = coreOpLessThanOrEqualString.computeValue(context);
        assertFalse("Expected false, but got: " + result, result instanceof Boolean);
    }
}