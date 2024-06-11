package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.SimpleNode;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operation;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setup() {
        arg1 = new SimpleNode(new NodeInfo(1.5));
        arg2 = new SimpleNode(new NodeInfo(1.0));
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueTrue() {
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueFalse() {
        EvalContext context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(1.0));
        arg1 = new SimpleNode(new NodeInfo(1.0));
        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithDifferentTypes() {
        EvalContext context = new EvalContext(new InfoSet());
        arg2 = new SimpleNode(new NodeInfo("2"));
        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArg() {
        new CoreOperationGreaterThanOrEqual(null, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArg2() {
        new CoreOperationGreaterThanOrEqual(arg1, null);
    }
}