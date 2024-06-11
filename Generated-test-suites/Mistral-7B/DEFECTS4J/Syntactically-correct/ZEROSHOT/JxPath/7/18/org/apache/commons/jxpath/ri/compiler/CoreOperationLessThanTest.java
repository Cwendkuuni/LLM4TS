package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.expression.Expression;

/**
 * Test file for CoreOperationLessThan class.
 */
public class CoreOperationLessThanTest {

    /**
     * Test method for CoreOperationLessThan construction with two arguments.
     */
    @org.junit.Test
    public void testCoreOperationLessThanConstruction() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        Expression[] expectedArgs = { arg1, arg2 };
        assertEquals(expectedArgs.length, coreOperationLessThan.args.length);
        for (int i = 0; i < expectedArgs.length; i++) {
            assertEquals(expectedArgs[i], coreOperationLessThan.args[i]);
        }
    }

    /**
     * Test method for CoreOperationLessThan computation with double values.
     */
    @org.junit.Test
    public void testCoreOperationLessThanDoubleValues() {
        EvalContext context = mock(EvalContext.class);
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        when(arg1.computeValue(context)).thenReturn(3.0);
        when(arg2.computeValue(context)).thenReturn(5.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        Boolean expectedResult = Boolean.FALSE;
        assertEquals(expectedResult, result);

        when(arg1.computeValue(context)).thenReturn(1.0);
        when(arg2.computeValue(context)).thenReturn(5.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        expectedResult = Boolean.FALSE;
        assertEquals(expectedResult, result);

        when(arg1.computeValue(context)).thenReturn(1.0);
        when(arg2.computeValue(context)).thenReturn(1.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        expectedResult = Boolean.TRUE;
        assertEquals(expectedResult, result);

        when(arg1.computeValue(context)).thenReturn(5.0);
        when(arg2.computeValue(context)).thenReturn(3.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        expectedResult = Boolean.TRUE;
        assertEquals(expectedResult, result);
    }

    /**
     * Test method for CoreOperationLessThan getSymbol method.
     */
    @org.junit.Test
    public void testCoreOperationLessThanGetSymbol() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(mock(Expression.class),
                mock(Expression.class));
        String expectedSymbol = "<";
        String resultSymbol = coreOperationLessThan.getSymbol();
        assertEquals(expectedSymbol, resultSymbol);
    }

    /**
     * Test method for CoreOperationLessThan equals method.
     */
    @org.junit.Test
    public void testCoreOperationLessThanEquals() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationLessThan coreOperationLessThan1 = new CoreOperationLessThan(arg1, arg2);
        CoreOperationLessThan coreOperationLessThan2 = new CoreOperationLessThan(arg1, arg2);
        CoreOperationRelationalExpression coreOperationRelationalExpression = mock(CoreOperationRelationalExpression.class);
        assertEquals(coreOperationLessThan1, coreOperationLessThan1);
        assertEquals(coreOperationLessThan1, coreOperationLessThan2);
        assertNotEquals(coreOperationLessThan1, coreOperationRelationalExpression);
    }

    /**
     * Test method for CoreOperationLessThan hashCode method.
     */
    @org.junit.Test
    public void testCoreOperationLessThanHashCode() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationLessThan coreOperationLessThan1 = new CoreOperationLessThan(arg1, arg2);
        Expression arg1Clone = mock(Expression.class);
        Expression arg2Clone = mock(Expression.class);
        when(arg1Clone.hashCode()).thenReturn(arg1.hashCode());
        when(arg2Clone.hashCode()).thenReturn(arg2.hashCode());
        CoreOperationLessThan coreOperationLessThan2 = new CoreOperationLessThan(arg1Clone, arg2Clone);
        int expectedHashCode = coreOperationLessThan1.hashCode();
        int resultHashCode = coreOperationLessThan2.hashCode();
        assertEquals(expectedHashCode, resultHashCode);
    }
}