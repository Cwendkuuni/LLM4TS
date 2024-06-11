package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_whenArg1EqualsArg2_thenReturnTrue() {
        // Given
        Expression arg1 = new Const(1);
        Expression arg2 = new Const(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result == Boolean.TRUE);
    }

    @Test
    public void testCoreOperationEqual_whenArg1NotEqualsArg2_thenReturnFalse() {
        // Given
        Expression arg1 = new Const(1);
        Expression arg2 = new Const(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertFalse(result == Boolean.TRUE);
        assertTrue(result == Boolean.FALSE);
    }

    @Test
    public void testCoreOperationEqual_whenArg1Null_thenThrowNPE() {
        // Given
        Expression arg1 = new Const(null);
        Expression arg2 = new Const(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Then
        try {
            coreOperationEqual.computeValue(new EvalContext());
            fail("NPE expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCoreOperationEqual_whenArg2Null_thenThrowNPE() {
        // Given
        Expression arg1 = new Const(1);
        Expression arg2 = new Const(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // When & Then
        coreOperationEqual.computeValue(new EvalContext());
    }
}