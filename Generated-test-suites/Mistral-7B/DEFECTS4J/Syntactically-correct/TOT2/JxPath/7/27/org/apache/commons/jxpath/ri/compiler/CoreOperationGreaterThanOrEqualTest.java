// CoreOperationGreaterThanOrEqualTest.java

package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CoreOperationGreaterThanOrEqualTest {

    // Constructor test
    @Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNotNull(coreOperationGreaterThanOrEqual);
    }

    // ComputeValue: True test
    @Test
    public void testComputeValueTrue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(5.0);
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result);
    }

    // ComputeValue: False test
    @Test
    public void testComputeValueFalse() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(5.0);
        Expression arg2 = new ConstantExpression(6.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result);
    }

    // GetSymbol test
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(5.0));
        String symbol = coreOperationGreaterThanOrEqual.getSymbol();
        assertEquals(">=", symbol);
    }

    // ComputeValue: Null arguments test
    @Test(expected = NullPointerException.class)
    public void testComputeValueNullArg1() {
        EvalContext context = new EvalContext();
        Expression arg1 = null;
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueNullArg2() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(5.0);
        Expression arg2 = null;
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    // ComputeValue: NaN test
    @Test
    public void testComputeValueNaN() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(Double.NaN);
        Expression arg2 = new ConstantExpression(Double.NaN);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result);
    }

    // ComputeValue: Infinite and finite test
    @Test
    public void testCompareInfiniteAndFinite() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(Double.POSITIVE_INFINITY);
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result);
    }

    // ComputeValue: Different data types test
    @Test
    public void testCompareDifferentDataTypes() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(5);
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Boolean result = (Boolean) coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result);
    }
}

//