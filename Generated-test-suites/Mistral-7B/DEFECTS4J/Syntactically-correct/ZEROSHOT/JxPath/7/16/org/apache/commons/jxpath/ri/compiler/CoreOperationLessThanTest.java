package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestUtil;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThan.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testLessThan() {
        Expression arg1 = TestUtil.createNumber(1.0);
        Expression arg2 = TestUtil.createNumber(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(new EvalContext());
        assertTrue(result);
    }

    @Test
    public void testLessThanOrEqualFalse() {
        Expression arg1 = TestUtil.createNumber(1.0);
        Expression arg2 = TestUtil.createNumber(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        Boolean result = coreOperationLessThan.computeValue(new EvalContext());
        assertFalse(result);
    }

    @Test
    public void testLessThanOrEqualTrue() {
        Expression arg1 = TestUtil.createNumber(1.0);
        Expression arg2 = TestUtil.createNumber(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        Boolean result = coreOperationLessThan.computeValue(new EvalContext());
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() {
        Expression arg1 = TestUtil.createNumber(1.0);
        Expression arg2 = null;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
    }

    @Test(expected = ClassCastException.class)
    public void testInvalidArgumentsType() {
        Expression arg1 = TestUtil.createNumber(1.0);
        Expression arg2 = TestUtil.createString("test");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
    }
}