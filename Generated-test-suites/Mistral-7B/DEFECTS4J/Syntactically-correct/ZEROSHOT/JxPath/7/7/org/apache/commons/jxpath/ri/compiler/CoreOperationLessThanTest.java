package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThan
 */
public class CoreOperationLessThanTest {

    /**
     * Test method for {@link CoreOperationLessThan#CoreOperationLessThan(Expression, Expression)} .
     */
    @Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(coreOperationLessThan);
        assertEquals("<", coreOperationLessThan.getSymbol());
    }

    /**
     * Test method for {@link CoreOperationLessThan#computeValue(EvalContext)} .
     */
    @Test
    public void testComputeValue() {
        double l = 1.0;
        double r = 2.0;

        Expression arg1 = new LiteralExpression(l);
        Expression arg2 = new LiteralExpression(r);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        l = 2.0;
        r = 1.0;

        arg1 = new LiteralExpression(l);
        arg2 = new LiteralExpression(r);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        context = new EvalContext();
        result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}