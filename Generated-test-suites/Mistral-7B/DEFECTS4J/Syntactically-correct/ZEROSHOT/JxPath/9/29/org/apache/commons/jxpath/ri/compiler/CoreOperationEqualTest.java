package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));

        EvalContext context = new EvalContext(new Object[]{1, 1}, new Class[]{int.class, int.class});
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new Object[]{1, 2}, new Class[]{int.class, int.class});
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        coreOperationEqual = new CoreOperationEqual(new ConstantExpression("abcd"), new ConstantExpression("abcd"));
        context = new EvalContext(new Object[]{"abcd", "abcd"}, new Class[]{String.class, String.class});
        result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        coreOperationEqual = new CoreOperationEqual(new ConstantExpression(null), new ConstantExpression(Integer.valueOf(1)));
        context = new EvalContext(new Object[]{null, 1}, new Class[]{Object.class, Number.class});
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        coreOperationEqual = new CoreOperationEqual(new ConstantExpression("abcd"), new ConstantExpression(1234L));
        context = new EvalContext(new Object[]{"abcd", 1234L}, new Class[]{String.class, Number.class});
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualNull() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(null), new ConstantExpression(null));

        EvalContext context = new EvalContext(new Object[]{null, null}, new Class[]{Object.class, Object.class});
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualDifferentTypes() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression("abcd"));

        EvalContext context = new EvalContext(new Object[]{1, "abcd"}, new Class[]{int.class, String.class});
        coreOperationEqual.computeValue(context);
    }
}