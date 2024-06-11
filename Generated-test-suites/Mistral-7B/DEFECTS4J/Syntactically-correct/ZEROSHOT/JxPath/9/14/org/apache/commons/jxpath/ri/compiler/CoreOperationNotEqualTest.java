package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationNotEqualTest {

    @Mock
    private EvalContext context;

    @Mock
    private Expression arg1;

    @Mock
    private Expression arg2;

    @InjectMocks
    private CoreOperationNotEqual coreOperationNotEqual;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCoreOperationNotEqualConstructor() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertSame(arg1, coreOperationNotEqual.getArg1());
        assertSame(arg2, coreOperationNotEqual.getArg2());
    }

    @Test
    public void testComputeValueFalse() {
        Object leftValue = new SimpleNode("leftValue");
        Object rightValue = new SimpleNode("rightValue");

        doReturn(leftValue).when(arg1).computeValue(context);
        doReturn(rightValue).when(arg2).computeValue(context);
        doReturn(Boolean.TRUE).when(context).isXPathContext();

        assertFalse(coreOperationNotEqual.computeValue(context).booleanValue());
        verify(context, times(1)).isXPathContext();
        verify(context, times(1)).pushContext(any(SimpleNode.class));
        verify(arg1, times(1)).computeValue(context);
        verify(arg2, times(1)).computeValue(context);
    }

    @Test
    public void testComputeValueTrue() {
        Object leftValue = new SimpleNode("leftValue");
        Object rightValue = new SimpleNode("rightValue");

        doReturn(leftValue).when(arg1).computeValue(context);
        doReturn(rightValue).when(arg2).computeValue(context);
        doReturn(Boolean.FALSE).when(context).isXPathContext();

        assertTrue(coreOperationNotEqual.computeValue(context).booleanValue());
        verify(context, times(1)).isXPathContext();
        verify(context, times(1)).pushContext(any(SimpleNode.class));
        verify(arg1, times(1)).computeValue(context);
        verify(arg2, times(1)).computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }

    @Test
    public void testNullArg1() {
        coreOperationNotEqual = new CoreOperationNotEqual(null, arg2);
        assertNull(coreOperationNotEqual.getArg1());
    }

    @Test
    public void testNullArg2() {
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, null);
        assertNull(coreOperationNotEqual.getArg2());
    }
}