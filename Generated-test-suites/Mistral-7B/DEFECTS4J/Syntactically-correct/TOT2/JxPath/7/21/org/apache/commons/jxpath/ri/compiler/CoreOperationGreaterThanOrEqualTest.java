package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new CoreNumber(5.5);
        Expression arg2 = new CoreNumber(5.5);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperation.computeValue(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testComputeValueWithNonNumericValues() {
        Expression arg1 = new CoreNumber(5.5);
        Expression arg2 = new CoreString("abc");
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertFalse(coreOperation.computeValue(context));
    }

    @Test(expected = RuntimeException.class)
    public void testComputeValueWithNullArg1() {
        Expression arg1 = null;
        Expression arg2 = new CoreNumber(5.5);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperation.computeValue(context); // should throw an exception
    }

    @Test
    public void testComputeValueWithStringArg1() {
        Expression arg1 = new CoreString("5.5");
        Expression arg2 = new CoreNumber(5.5);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperation.computeValue(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testComputeValueWithBooleanArg1() {
        Expression arg1 = new CoreBoolean(true);
        Expression arg2 = new CoreNumber(5.5);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertFalse(coreOperation.computeValue(context));
    }

    @Test
    public void testComputeValueWithVeryLargeValues() {
        Expression arg1 = new CoreNumber(Double.MAX_VALUE);
        Expression arg2 = new CoreNumber(Double.MAX_VALUE + 1);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertFalse(coreOperation.computeValue(context));
    }

    @Test
    public void testComputeValueWithVerySmallValues() {
        Expression arg1 = new CoreNumber(Double.MIN_VALUE);
        Expression arg2 = new CoreNumber(Double.MIN_VALUE + 1);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertTrue(coreOperation.computeValue(context));
    }

    @Test
    public void testComputeValueWithDifferentDataTypes() {
        Expression arg1 = new CoreNumber(5.5);
        Expression arg2 = new CoreString("5.5");
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertFalse(coreOperation.computeValue(context));
    }

    @Test
    public void testComputeValueWithNaN() {
        Expression arg1 = new CoreNumber(Double.NaN);
        Expression arg2 = new CoreNumber(5.5);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertFalse(coreOperation.computeValue(context));
    }

    @Test
    public void testComputeValueWithInfinity() {
        Expression arg1 = new CoreNumber(Double.POSITIVE_INFINITY);
        Expression arg2 = new CoreNumber(5.5);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertTrue(coreOperation.computeValue(context));
    }
}