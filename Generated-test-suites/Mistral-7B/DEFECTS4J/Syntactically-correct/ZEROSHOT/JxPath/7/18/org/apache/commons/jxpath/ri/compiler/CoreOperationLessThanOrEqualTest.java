package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThanOrEqual class.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        // Test with double values
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new Const(5.0), new Const(5.0));
        EvalContext context = new EvalContext(new InfoSet());

        assertTrue(coreOperationLessThanOrEqual.computeValue(context).booleanValue());

        // Test with different double values
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new Const(5.0), new Const(6.0));
        context = new EvalContext(new InfoSet());

        assertFalse(coreOperationLessThanOrEqual.computeValue(context).booleanValue());

        // Test with NodeInfo and double values
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new Const(new Double(5.0)), new Const(5.0));
        context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(new Object(), ""));

        assertTrue(coreOperationLessThanOrEqual.computeValue(context).booleanValue());

        // Test with double values and NodeInfo
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new Const(5.0), new Const(new NodeInfo(new Object(), "")));
        context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(new Double(6.0), ""));

        assertFalse(coreOperationLessThanOrEqual.computeValue(context).booleanValue());
    }
}