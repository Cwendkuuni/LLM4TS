package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.SimpleNode;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest {

    /**
     * Test method for CoreOperationGreaterThanOrEqual.computeValue(EvalContext).
     */
    @Test
    public void testComputeValue() {
        // Test case 1: l >= r
        EvalContext context = new EvalContext();
        InfoSet is = new InfoSet();
        SimpleNode arg1 = new SimpleNode(null, 5.0, null, null);
        SimpleNode arg2 = new SimpleNode(null, 5.0, null, null);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test case 2: l < r
        arg1 = new SimpleNode(null, 4.0, null, null);
        arg2 = new SimpleNode(null, 5.0, null, null);
        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test case 3: l == r
        arg1 = new SimpleNode(null, 5.0, null, null);
        arg2 = new SimpleNode(null, 5.0, null, null);
        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test method for CoreOperationGreaterThanOrEqual.getSymbol().
     */
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new SimpleNode(null), new SimpleNode(null));
        String symbol = expression.getSymbol();
        assertEquals(">=", symbol);
    }
}