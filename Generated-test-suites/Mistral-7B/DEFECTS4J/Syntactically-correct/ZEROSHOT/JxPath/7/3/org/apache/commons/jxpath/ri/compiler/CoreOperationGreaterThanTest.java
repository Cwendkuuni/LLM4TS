package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;

public class CoreOperationGreaterThanTest extends TestCase {

    public void testCoreOperationGreaterThan() {
        // Test with valid numbers
        Expression arg1 = new Constant(1.0);
        Expression arg2 = new Constant(0.5);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet(), null);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertTrue((Boolean) result);

        // Test with invalid numbers
        arg1 = new Constant("abc");
        arg2 = new Constant(1.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet(), null);
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertFalse((Boolean) result);

        // Test with same number
        arg1 = new Constant(5.0);
        arg2 = new Constant(5.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet(), null);
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertFalse((Boolean) result);

        // Test with NaN and infinite numbers
        arg1 = new Constant(Double.NaN);
        arg2 = new Constant(Double.POSITIVE_INFINITY);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet(), null);
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertFalse((Boolean) result);

        arg1 = new Constant(Double.POSITIVE_INFINITY);
        arg2 = new Constant(Double.POSITIVE_INFINITY);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet(), null);
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertFalse((Boolean) result);

        arg1 = new Constant(Double.POSITIVE_INFINITY);
        arg2 = new Constant(Double.NaN);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet(), null);
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertTrue((Boolean) result);

        // Test with negative numbers
        arg1 = new Constant(-1.0);
        arg2 = new Constant(-2.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet(), null);
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertTrue((Boolean) result);

        arg1 = new Constant(-1.0);
        arg2 = new Constant(1.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet(), null);
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        Assert.assertFalse((Boolean) result);
    }

    public void testSymbol() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(null, null);
        String symbol = coreOperationGreaterThan.getSymbol();
        assertEquals(">", symbol);
    }
}