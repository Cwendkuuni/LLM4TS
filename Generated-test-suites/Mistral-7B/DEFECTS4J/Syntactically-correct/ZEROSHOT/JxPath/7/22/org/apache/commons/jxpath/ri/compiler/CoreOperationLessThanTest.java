package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationLessThanTest {

    @org.junit.Test
    public void testCoreOperationLessThanWithValidArguments() {
        // given
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(1.0);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(2.0);

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        // when
        Object result = coreOperationLessThan.computeValue(mock(EvalContext.class));

        // then
        assertEquals(Boolean.FALSE, result);

        // given
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(1.0);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(1.0);

        // when
        result = coreOperationLessThan.computeValue(mock(EvalContext.class));

        // then
        assertEquals(Boolean.FALSE, result);

        // given
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(1.0);
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(0.5);

        // when
        result = coreOperationLessThan.computeValue(mock(EvalContext.class));

        // then
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanWithInvalidArguments() {
        // given
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        // when
        when(arg1.computeValue(mock(EvalContext.class))).thenThrow(new IllegalStateException());

        // then
        coreOperationLessThan.computeValue(mock(EvalContext.class));
    }
}