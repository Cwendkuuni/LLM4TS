package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Value;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

/**
 * Test file for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    private Expression arg1;
    private Expression arg2;
    private CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual;

    @Before
    public void setup() {
        arg1 = Mockito.spy(new CoreNumberExpression(new ConstantValue(1.5)));
        arg2 = Mockito.spy(new CoreNumberExpression(new ConstantValue(1.0)));
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueTrue() {
        EvalContext context = mock(EvalContext.class);
        InfoSet infoSet = Mockito.mock(InfoSet.class);
        NodeInfo nodeInfo = Mockito.mock(NodeInfo.class);
        Mockito.when(context.getNodeInfo(ArgumentMatchers.anyInt())).thenReturn(nodeInfo);
        Mockito.when(context.getInfoSet()).thenReturn(infoSet);
        Mockito.when(infoSet.getNode(ArgumentMatchers.anyInt())).thenReturn(nodeInfo);
        Mockito.when(arg1.computeValue(context)).thenReturn(new DoubleValue(5.0));
        Mockito.when(arg2.computeValue(context)).thenReturn(new DoubleValue(3.0));

        Value result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertEquals(Boolean.TRUE, result.getValue());
    }

    @Test
    public void testComputeValueFalse() {
        EvalContext context = mock(EvalContext.class);
        InfoSet infoSet = Mockito.mock(InfoSet.class);
        NodeInfo nodeInfo = Mockito.mock(NodeInfo.class);
        Mockito.when(context.getNodeInfo(ArgumentMatchers.anyInt())).thenReturn(nodeInfo);
        Mockito.when(context.getInfoSet()).thenReturn(infoSet);
        Mockito.when(infoSet.getNode(ArgumentMatchers.anyInt())).thenReturn(nodeInfo);
        Mockito.when(arg1.computeValue(context)).thenReturn(new DoubleValue(3.0));
        Mockito.when(arg2.computeValue(context)).thenReturn(new DoubleValue(5.0));

        Value result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertFalse(result.getValue() instanceof Boolean);
        assertEquals(Boolean.FALSE, result.getValue());
    }

    @Test
    public void testComputeValueFalseWithSameValues() {
        EvalContext context = mock(EvalContext.class);
        InfoSet infoSet = Mockito.mock(InfoSet.class);
        NodeInfo nodeInfo = Mockito.mock(NodeInfo.class);
        Mockito.when(context.getNodeInfo(ArgumentMatchers.anyInt())).thenReturn(nodeInfo);
        Mockito.when(context.getInfoSet()).thenReturn(infoSet);
        Mockito.when(infoSet.getNode(ArgumentMatchers.anyInt())).thenReturn(nodeInfo);
        Mockito.when(arg1.computeValue(context)).thenReturn(new DoubleValue(3.0));
        Mockito.when(arg2.computeValue(context)).thenReturn(new DoubleValue(3.0));

        Value result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertFalse(result.getValue() instanceof Boolean);
        assertEquals(Boolean.FALSE, result.getValue());
    }

    @Test
    public void testSymbol() {
        String symbol = coreOperationGreaterThanOrEqual.getSymbol();

        assertEquals(">=", symbol);
    }
}