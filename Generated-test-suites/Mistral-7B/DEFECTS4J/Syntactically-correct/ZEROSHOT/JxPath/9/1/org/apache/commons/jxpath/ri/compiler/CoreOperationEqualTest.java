package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqualDifferentTypes() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression("one");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationEqualDifferentValues() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationEqualNulls() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqualNullAndOther() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression("one");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertFalse(result instanceof Boolean);
    }
}