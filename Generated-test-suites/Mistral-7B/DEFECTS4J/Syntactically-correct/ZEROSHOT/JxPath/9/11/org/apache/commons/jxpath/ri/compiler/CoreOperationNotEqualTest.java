package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.NodesetExpression;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new NodesetExpression("//node()[1]");
        Expression arg2 = new NodesetExpression("//node()[2]");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        assertNotNull(coreOperationNotEqual);
    }

    @Test
    public void testComputeValueTrue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new NodesetExpression("//node()[1]");
        Expression arg2 = new NodesetExpression("//node()[2]");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object value = coreOperationNotEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue(((Boolean) value).booleanValue());
    }

    @Test
    public void testComputeValueFalse() {
        EvalContext context = new EvalContext();
        Expression arg1 = new NodesetExpression("//node()[1]");
        Expression arg2 = new NodesetExpression("//node()[1]");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object value = coreOperationNotEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertFalse(((Boolean) value).booleanValue());
    }

    @Test
    public void testComputeValueNull() {
        EvalContext context = new EvalContext();
        Expression arg1 = new NodesetExpression("//node()[1]");
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object value = coreOperationNotEqual.computeValue(context);
        assertNull(value);
    }
}