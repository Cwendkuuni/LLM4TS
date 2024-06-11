package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CoreOperationEqual}.
 */
public class CoreOperationEqualTest {

    private CoreOperationEqual equal;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setUp() {
        arg1 = new SimpleNode(new NodeInfo(1, 1, null, "first", null));
        arg2 = new SimpleNode(new NodeInfo(1, 1, null, "second", null));
        equal = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueWithTwoIdenticalNodes() {
        EvalContext context = new EvalContext();
        Object value = equal.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue(((Boolean) value).booleanValue());
    }

    @Test
    public void testComputeValueWithTwoDifferentNodes() {
        EvalContext context = new EvalContext();
        Object value = equal.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertFalse(((Boolean) value).booleanValue());
    }

    @Test
    public void testComputeValueWithTwoIdenticalObjects() {
        EvalContext context = new EvalContext();
        Object value = equal.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue(((Boolean) value).booleanValue());
    }

    @Test
    public void testComputeValueWithTwoDifferentObjects() {
        EvalContext context = new EvalContext();
        Object value = equal.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertFalse(((Boolean) value).booleanValue());
    }

    @Test
    public void testGetSymbol() {
        String symbol = equal.getSymbol();
        assertEquals("=", symbol);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, arg2);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments2() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, null);
    }
}