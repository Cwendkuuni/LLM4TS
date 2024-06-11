package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestUtil;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Test with double values
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Constant(5.0), new Constant(4.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InfoSet());
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(4.0),
                new Constant(5.0));
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test with int values
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(5),
                new Constant(4));
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InfoSet());
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(4),
                new Constant(5));
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test with null values
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(5.0),
                new Constant(null));
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(null),
                new Constant(5.0));
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test with different data types
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(5.0),
                new Constant(new Integer(4)));
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(new Integer(4)),
                new Constant(5.0));
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test with NaN values
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(Double.NaN),
                new Constant(Double.NaN));
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(Double.NaN),
                new Constant(5.0));
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(5.0),
                new Constant(Double.NaN));
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test with positive and negative infinity
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(Double.POSITIVE_INFINITY),
                new Constant(Double.POSITIVE_INFINITY));
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(Double.POSITIVE_INFINITY),
                new Constant(Double.NEGATIVE_INFINITY));
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(Double.NEGATIVE_INFINITY),
                new Constant(Double.POSITIVE_INFINITY));
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Constant(Double.NEGATIVE_INFINITY),
                new Constant(Double.NEGATIVE_INFINITY));
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Constant(5.0), new Constant(4.0));
        assertEquals(">=", coreOperationGreaterThanOrEqual.getSymbol());
    }
}