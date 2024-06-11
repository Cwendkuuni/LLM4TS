package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.ParseException;
import org.junit.Test;

/**
 * Test class for CoreOperationEqual.
 */
public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() throws ParseException {
        // Test with Boolean values
        Expression arg1 = new ConstantExpression(Boolean.TRUE);
        Expression arg2 = new ConstantExpression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg2 = new ConstantExpression(Boolean.FALSE);
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));

        // Test with Number values
        arg1 = new ConstantExpression(1);
        arg2 = new ConstantExpression(1);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg2 = new ConstantExpression(2);
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));

        // Test with String values
        arg1 = new ConstantExpression("test");
        arg2 = new ConstantExpression("test");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg2 = new ConstantExpression("test2");
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));

        // Test with different types
        arg1 = new ConstantExpression(1);
        arg2 = new ConstantExpression("test");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
    }
}