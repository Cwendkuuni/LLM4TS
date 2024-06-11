package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.TestHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for class CoreOperationNotEqual.
 */
public class CoreOperationNotEqualTest {

    private CoreOperationNotEqual coreOperationNotEqual;
    private CoreOperationCompare coreOperationCompare;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setUp() {
        coreOperationCompare = mock(CoreOperationCompare.class);
        arg1 = mock(Expression.class);
        arg2 = mock(Expression.class);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        Object value1 = TestHelper.getObjectValue("true", null);
        Object value2 = TestHelper.getObjectValue("false", null);

        when(arg1.computeValue(EvaluationContext.EMPTY)).thenReturn(value1);
        when(arg2.computeValue(EvaluationContext.EMPTY)).thenReturn(value2);

        assertFalse("computeValue should return false when arg1 equals arg2",
                coreOperationNotEqual.computeValue(EvaluationContext.EMPTY));
    }

    @Test
    public void testGetSymbol() {
        assertNotSame("getSymbol should return different symbol",
                coreOperationCompare.getSymbol(), coreOperationNotEqual.getSymbol());
        assertSame("getSymbol should return '!=' symbol",
                "!=", coreOperationNotEqual.getSymbol());
    }

    @Test
    public void testEquals() {
        CoreOperationNotEqual coreOperationNotEqual1 = new CoreOperationNotEqual(arg1, arg2);
        CoreOperationNotEqual coreOperationNotEqual2 = new CoreOperationNotEqual(arg1, arg2);
        CoreOperationNotEqual coreOperationNotEqual3 = new CoreOperationNotEqual(arg1, arg2);

        assertNotSame("equals should not return same instance for different objects",
                coreOperationNotEqual1, coreOperationNotEqual2);
        assertNotSame("equals should not return same instance for different objects",
                coreOperationNotEqual1, coreOperationNotEqual3);
        assertSame("equals should return true for same instance", coreOperationNotEqual1, coreOperationNotEqual1);
    }

    @Test
    public void testHashCode() {
        CoreOperationNotEqual coreOperationNotEqual1 = new CoreOperationNotEqual(arg1, arg2);
        CoreOperationNotEqual coreOperationNotEqual2 = new CoreOperationNotEqual(arg1, arg2);
        CoreOperationNotEqual coreOperationNotEqual3 = new CoreOperationNotEqual(arg1, arg2);

        assertNotSame("hashCode should not be same for different objects",
                coreOperationNotEqual1.hashCode(), coreOperationNotEqual2.hashCode());
        assertNotSame("hashCode should not be same for different objects",
                coreOperationNotEqual1.hashCode(), coreOperationNotEqual3.hashCode());
    }

    @Test
    public void testToString() {
        String toString1 = coreOperationNotEqual.toString();
        String toString2 = coreOperationNotEqual.toString();

        assertNull("toString should return null when object is not initialized", toString1);
        assertSame("toString should return same string for same instance", toString1, toString2);
        assertFalse("toString should not return empty string", toString1.isEmpty());
    }
}