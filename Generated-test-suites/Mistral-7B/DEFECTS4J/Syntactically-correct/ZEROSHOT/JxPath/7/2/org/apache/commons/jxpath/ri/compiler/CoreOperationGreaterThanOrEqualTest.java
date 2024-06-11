package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

/**
 * Tests for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Test with valid arguments of double type
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(new Double(4.0));
        CoreOperationGreaterThanOrEqual coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        Boolean result = (Boolean) coreOp.computeValue(context);
        assertTrue(result.booleanValue());

        // Test with valid arguments of double type, but arg1 < arg2
        arg1 = new LiteralExpression(new Double(4.0));
        arg2 = new LiteralExpression(new Double(5.0));
        coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = (Boolean) coreOp.computeValue(context);
        assertFalse(result.booleanValue());

        // Test with valid arguments of different types, but arg1 can be casted to double and is greater than or equal to arg2
        arg1 = new LiteralExpression(new Integer(5));
        arg2 = new LiteralExpression(new Double(4.5));
        coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = (Boolean) coreOp.computeValue(context);
        assertTrue(result.booleanValue());

        // Test with valid arguments of different types, but arg1 cannot be casted to double and is less than arg2
        arg1 = new LiteralExpression(new Integer(4));
        arg2 = new LiteralExpression(new Double(5.5));
        coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = (Boolean) coreOp.computeValue(context);
        assertFalse(result.booleanValue());

        // Test with null arguments
        arg1 = new LiteralExpression(null);
        arg2 = new LiteralExpression(new Double(4.0));
        coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = (Boolean) coreOp.computeValue(context);
        assertFalse(result.booleanValue());

        arg1 = new LiteralExpression(new Double(4.0));
        arg2 = new LiteralExpression(null);
        coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = (Boolean) coreOp.computeValue(context);
        assertFalse(result.booleanValue());

        // Test with invalid arguments
        arg1 = new LiteralExpression(new Object());
        arg2 = new LiteralExpression(new Double(4.0));
        coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = (Boolean) coreOp.computeValue(context);
        assertFalse(result.booleanValue());

        arg1 = new LiteralExpression(new Double(4.0));
        arg2 = new LiteralExpression(new Object());
        coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = (Boolean) coreOp.computeValue(context);
        assertFalse(result.booleanValue());
    }
}