package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.expressions.Expression;

public class CoreOperationLessThanOrEqualTest {

    @org.junit.Test
    public void testCoreOperationLessThanOrEqual() {
        // Test with two double expressions and expected result is true
        Expression arg1 = new CoreConstantExpression(1.0);
        Expression arg2 = new CoreConstantExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertTrue(coreOperationLessThanOrEqual.computeValue(new EvalContext()).booleanValue());

        // Test with two double expressions and expected result is false
        arg1 = new CoreConstantExpression(2.0);
        arg2 = new CoreConstantExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertFalse(coreOperationLessThanOrEqual.computeValue(new EvalContext()).booleanValue());

        // Test with two double expressions and expected result is true
        arg1 = new CoreConstantExpression(1.0);
        arg2 = new CoreConstantExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertTrue(coreOperationLessThanOrEqual.computeValue(new EvalContext()).booleanValue());

        // Test with two double expressions and expected result is true
        arg1 = new CoreConstantExpression(Double.NEGATIVE_INFINITY);
        arg2 = new CoreConstantExpression(Double.NaN);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertFalse(coreOperationLessThanOrEqual.computeValue(new EvalContext()).booleanValue());

        // Test with two double expressions and expected result is false
        arg1 = new CoreConstantExpression(Double.NaN);
        arg2 = new CoreConstantExpression(Double.NEGATIVE_INFINITY);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertFalse(coreOperationLessThanOrEqual.computeValue(new EvalContext()).booleanValue());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualWithInvalidArgs() {
        Expression arg1 = new CoreConstantExpression(1.0);
        Expression arg2 = null;
        new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualWithMismatchedTypes() {
        Expression arg1 = new CoreConstantExpression(1.0);
        Expression arg2 = new CoreConstantStringExpression("test");
        new CoreOperationLessThanOrEqual(arg1, arg2);
    }
}