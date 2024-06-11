package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationLessThanTest {

    @org.junit.Test
    public void testCoreOperationLessThanWithTwoNumberArguments() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(3.5);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(5.0);

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        Boolean result = (Boolean) coreOperationLessThan.computeValue(mock(EvalContext.class));

        assertEquals(false, result);

        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(3.5);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(3.0);

        result = (Boolean) coreOperationLessThan.computeValue(mock(EvalContext.class));

        assertEquals(true, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanWithInvalidArgumentType() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.computeValue(mock(EvalContext.class))).thenReturn("Hello");
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(5.0);

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        coreOperationLessThan.computeValue(mock(EvalContext.class));
    }
}