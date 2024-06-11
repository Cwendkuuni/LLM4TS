package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * Test cases for {@link CoreOperationGreaterThanOrEqual}.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Given
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = mock(EvalContext.class);
        InfoSet infoSet = mock(InfoSet.class);
        when(context.setInfoSet(infoSet)).thenReturn(context);
        when(arg1.computeValue(context)).thenReturn(5.0);
        when(arg2.computeValue(context)).thenReturn(4.0);

        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        // Then
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithFalse() {
        // Given
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = mock(EvalContext.class);
        InfoSet infoSet = mock(InfoSet.class);
        when(context.setInfoSet(infoSet)).thenReturn(context);
        when(arg1.computeValue(context)).thenReturn(4.0);
        when(arg2.computeValue(context)).thenReturn(5.0);

        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        // Then
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithInvalidArguments() {
        // Given
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = mock(EvalContext.class);
        InfoSet infoSet = mock(InfoSet.class);
        when(context.setInfoSet(infoSet)).thenReturn(context);
        when(arg1.computeValue(context)).thenThrow(new RuntimeException());

        // Then
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArguments() {
        // Given
        Expression arg1 = null;
        Expression arg2 = mock(Expression.class);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = mock(EvalContext.class);
        InfoSet infoSet = mock(InfoSet.class);
        when(context.setInfoSet(infoSet)).thenReturn(context);

        // Then
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullSecondArgument() {
        // Given
        Expression arg1 = mock(Expression.class);
        Expression arg2 = null;
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // When
        EvalContext context = mock(EvalContext.class);
        InfoSet infoSet = mock(InfoSet.class);
        when(context.setInfoSet(infoSet)).thenReturn(context);

        // Then
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        // Given
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(mock(Expression.class), mock(Expression.class));

        // When
        String result = coreOperationGreaterThanOrEqual.getSymbol();

        // Then
        assertEquals(">=", result);
    }
}