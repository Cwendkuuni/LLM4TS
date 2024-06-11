package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationGreaterThan.
 */
public class CoreOperationGreaterThanTest extends TestBase {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan expr = new CoreOperationGreaterThan(new Constant(5.0), new Constant(3.0));
        EvalContext ctx = new EvalContext(new InfoSet());
        Object result = expr.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGreaterThan(new Constant(3.0), new Constant(5.0));
        result = expr.computeValue(ctx);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        expr = new CoreOperationGreaterThan(new Constant("5"), new Constant("3"));
        result = expr.computeValue(ctx);
        assertNull(result);

        expr = new CoreOperationGreaterThan(new Constant("3.5"), new Constant("5.5"));
        result = expr.computeValue(ctx);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        expr = new CoreOperationGreaterThan(new Constant("5.5"), new Constant("3.5"));
        result = expr.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGreaterThan(new Constant(Double.NEGATIVE_INFINITY), new Constant(Double.NEGATIVE_INFINITY));
        result = expr.computeValue(ctx);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        expr = new CoreOperationGreaterThan(new Constant(Double.NEGATIVE_INFINITY), new Constant(Double.POSITIVE_INFINITY));
        result = expr.computeValue(ctx);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        expr = new CoreOperationGreaterThan(new Constant(Double.POSITIVE_INFINITY), new Constant(Double.NEGATIVE_INFINITY));
        result = expr.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}