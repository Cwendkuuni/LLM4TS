import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual.CoreOperationCompareArgs;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Tests;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

    @Test
    public void testEqual_whenBothArgumentsAreNull_thenResultIsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationNull(), new CoreOperationNull());
        EvalContext context = Tests.newMockContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testEqual_whenBothArgumentsAreSameInstance_thenResultIsTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationConstant(new Integer(1)), new CoreOperationConstant(new Integer(1)));
        EvalContext context = Tests.newMockContext();
        assertTrue(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testEqual_whenBothArgumentsAreDifferentInstancesButEqual_thenResultIsTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationConstant(new Integer(1)), new CoreOperationConstant(new Integer(1)));
        EvalContext context = Tests.newMockContext();
        assertTrue(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testEqual_whenFirstArgumentIsNullAndSecondIsNot_thenResultIsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationNull(), new CoreOperationConstant(new Integer(1)));
        EvalContext context = Tests.newMockContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testEqual_whenFirstArgumentIsNotnullAndSecondIsNull_thenResultIsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationConstant(new Integer(1)), new CoreOperationNull());
        EvalContext context = Tests.newMockContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testEqual_whenFirstArgumentIsNumberAndSecondIsString_thenResultIsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationConstant(new Integer(1)), new CoreOperationConstant("1"));
        EvalContext context = Tests.newMockContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testEqual_whenFirstArgumentIsStringAndSecondIsNumber_thenResultIsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationConstant("1"), new CoreOperationConstant(new Integer(1)));
        EvalContext context = Tests.newMockContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testEqual_whenFirstArgumentIsBooleanAndSecondIsNumber_thenResultIsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationConstant(Boolean.TRUE), new CoreOperationConstant(new Integer(1)));
        EvalContext context = Tests.newMockContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testEqual_whenFirstArgumentIsNumberAndSecondIsBoolean_thenResultIsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationConstant(new Integer(1)), new CoreOperationConstant(Boolean.TRUE));
        EvalContext context = Tests.newMockContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testEqual_whenFirstArgumentIsStringAndSecondIsString_thenResultIsTrueIfEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationConstant("1"), new CoreOperationConstant("1"));
        EvalContext context = Tests.newMockContext();
        assertTrue(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testEqual_whenFirstArgumentIsStringAndSecondIsString_thenResultIsFalseIfNotEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new CoreOperationConstant("1"), new CoreOperationConstant("2"));
        EvalContext context = Tests.newMockContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }
}