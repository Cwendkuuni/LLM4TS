package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.StringConstantExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.ConstantExpression;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = new ConstantExpression(5.0);
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvaluationContext context = new EvaluationContext();
        Assert.assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValue() {
        Expression arg1 = new ConstantExpression(5.0);
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvaluationContext context = new EvaluationContext();
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(5.0));
        String symbol = operation.getSymbol();
        Assert.assertEquals(">=", symbol);
    }

    // Edge cases
    @Test
    public void testCoreOperationGreaterThanOrEqualWithEqualValues() {
        Expression arg1 = new ConstantExpression(5.0);
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvaluationContext context = new EvaluationContext();
        Assert.assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithDifferentSignValues() {
        Expression arg1 = new ConstantExpression(-5.0);
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvaluationContext context = new EvaluationContext();
        Assert.assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithNullContext() {
        Expression arg1 = new ConstantExpression(5.0);
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvaluationContext context = null;
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithNonNumericArguments() {
        Expression arg1 = new StringConstantExpression("5");
        Expression arg2 = new StringConstantExpression("abc");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvaluationContext context = new EvaluationContext();
        operation.computeValue(context);
    }

    // Exception handling
    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullContext() {
        Expression arg1 = new ConstantExpression(5.0);
        Expression arg2 = new ConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvaluationContext context = null;
        operation.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithNonNumericArguments() {
        Expression arg1 = new StringConstantExpression("5");
        Expression arg2 = new StringConstantExpression("abc");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvaluationContext context = new EvaluationContext();
        operation.computeValue(context);
    }
}