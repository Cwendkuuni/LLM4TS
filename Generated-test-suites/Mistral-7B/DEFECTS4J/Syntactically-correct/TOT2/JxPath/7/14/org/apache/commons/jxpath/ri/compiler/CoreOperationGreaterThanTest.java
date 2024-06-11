 package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.LiteralExpression;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    // Test case for method: CoreOperationGreaterThan(Expression arg1, Expression arg2)
    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertTrue(operation.computeValue(context).booleanValue());
    }

    // Test case for method: computeValue(EvalContext context)
    @Test
    public void testComputeValueTrue() {
        Expression arg1 = new LiteralExpression(5.5);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertTrue(operation.computeValue(context).booleanValue());
    }

    // Test case for method: computeValue(EvalContext context) with edge case - equal values
    @Test
    public void testComputeValueEqual() {
        Expression arg1 = new LiteralExpression(5.5);
        Expression arg2 = new LiteralExpression(5.5);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertFalse(operation.computeValue(context).booleanValue());
    }

    // Test case for method: computeValue(EvalContext context) with error case - non-numeric values
    @Test(expected = ClassCastException.class)
    public void testComputeValueNonNumeric() {
        Expression arg1 = new LiteralExpression("5.5");
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }

    // Test case for method: computeValue(EvalContext context) with edge case - very large numbers
    @Test
    public void testComputeValueLargeNumbers() {
        Expression arg1 = new LiteralExpression(Double.POSITIVE_INFINITY);
        Expression arg2 = new LiteralExpression(Double.POSITIVE_INFINITY.times(1.00001));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertTrue(operation.computeValue(context).booleanValue());
    }

    // Test case for method: computeValue(EvalContext context) with error case - null values
    @Test(expected = NullPointerException.class)
    public void testComputeValueNull() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }

    // Test case for method: computeValue(EvalContext context) with edge case - very small numbers
    @Test
    public void testComputeValueSmallNumbers() {
        Expression arg1 = new LiteralExpression(Double.MIN_VALUE);
        Expression arg2 = new LiteralExpression(Double.MIN_VALUE.times(1.00001));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertTrue(operation.computeValue(context).booleanValue());
    }

    // Test case for method: computeValue(EvalContext context) with error case - NaN values
    @Test
    public void testComputeValueNaN() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertFalse(operation.computeValue(context).booleanValue());
    }

    // Test case for method: getSymbol()
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new LiteralExpression(1.0), new LiteralExpression(2.0));
        String symbol = operation.getSymbol();
        assertEquals(">", symbol);
    }
}