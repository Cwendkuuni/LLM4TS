package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Before;
import org.junit.Test;

/**
 * Test file for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    private NodeInfo node1;
    private NodeInfo node2;
    private NodeInfo node3;
    private InfoSet infoSet;

    @Before
    public void setUp() {
        node1 = new NodeInfo("1.0", null);
        node2 = new NodeInfo("2.0", null);
        node3 = new NodeInfo("3.0", null);
        infoSet = new InfoSet();
        infoSet.setRootNode(node3);
    }

    @Test
    public void testComputeValueWithTwoNumbersGreaterThan() {
        Expression arg1 = new Literal(node1);
        Expression arg2 = new Literal(node2);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithTwoNumbersNotGreaterThan() {
        Expression arg1 = new Literal(node1);
        Expression arg2 = new Literal(node2);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueWithTwoNumbersFirstGreaterThanSecond() {
        Expression arg1 = new Literal(node2);
        Expression arg2 = new Literal(node1);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithTwoNumbersFirstLessThanSecond() {
        Expression arg1 = new Literal(node1);
        Expression arg2 = new Literal(node2);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueWithNumberAndString() {
        Expression arg1 = new Literal(node1);
        Expression arg2 = new Literal(node2.getNodeName());
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithMoreThanTwoArguments() {
        Expression arg1 = new Literal(node1);
        Expression arg2 = new Literal(node2);
        Expression arg3 = new Literal(node3);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2, arg3);
    }

    @Test
    public void testSymbol() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(null, null);
        String result = coreOperationGreaterThan.getSymbol();
        assertEquals(">", result);
    }
}