package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationGreaterThanTest {

    @org.junit.Test
    public void testCoreOperationGreaterThanWithBothArgsDoubleTypeAndBothValuesGreaterThan() {
        // Given
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new Double(5.0));
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(new Double(3.0));

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        // When
        Object result = coreOperationGreaterThan.computeValue(mock(EvalContext.class));

        // Then
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testCoreOperationGreaterThanWithBothArgsDoubleTypeAndFirstValueLessThanSecondValue() {
        // Given
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new Double(3.0));
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(new Double(5.0));

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        // When
        Object result = coreOperationGreaterThan.computeValue(mock(EvalContext.class));

        // Then
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testCoreOperationGreaterThanWithBothArgsDoubleTypeAndBothValuesEqual() {
        // Given
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new Double(5.0));
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(new Double(5.0));

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        // When
        Object result = coreOperationGreaterThan.computeValue(mock(EvalContext.class));

        // Then
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanWithOneOrBothArgsNotDoubleType() {
        // Given
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        when(arg1.computeValue(mock(EvalContext.class))).thenReturn(new Integer(5));
        when(arg2.computeValue(mock(EvalContext.class))).thenReturn(new Double(3.0));

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        // When and Then
        coreOperationGreaterThan.computeValue(mock(EvalContext.class));
    }
}