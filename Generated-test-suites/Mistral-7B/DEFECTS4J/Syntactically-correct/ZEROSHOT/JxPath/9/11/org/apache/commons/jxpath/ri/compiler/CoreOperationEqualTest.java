package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        // Test constructor
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertNotNull(coreOperationEqual);

        // Test getSymbol method
        assertEquals("=", coreOperationEqual.getSymbol());

        // Test computeValue method with boolean values
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        context.setValue("booleanValue1", Boolean.TRUE);
        context.setValue("booleanValue2", Boolean.TRUE);
        result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        context.setValue("booleanValue1", Boolean.FALSE);
        context.setValue("booleanValue2", Boolean.TRUE);
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(Boolean.TRUE.equals(result));

        // Test computeValue method with number values
        context.setValue("numberValue1", 1.0);
        context.setValue("numberValue2", 1.0);
        result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        context.setValue("numberValue1", 1.0);
        context.setValue("numberValue2", 2.0);
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(Boolean.TRUE.equals(result));

        // Test computeValue method with string values
        context.setValue("stringValue1", "test");
        context.setValue("stringValue2", "test");
        result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        context.setValue("stringValue1", "test");
        context.setValue("stringValue2", "other");
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(Boolean.TRUE.equals(result));

        // Test computeValue method with different data types
        context.setValue("numberValue1", 1.0);
        context.setValue("stringValue2", " test");
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(Boolean.TRUE.equals(result));
    }
}