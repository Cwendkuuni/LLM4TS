package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));

        EvalContext context = new EvalContext();

        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(2), new ConstantExpression(1));

        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));

        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));

        String symbol = coreOperationNotEqual.getSymbol();
        assertEquals("!=", symbol);
    }
}