package org.apache.commons.jxpath.ri.compiler.test;

import org.junit.Test;
import org.junit.Assert;
import org.junit.experimental.runners.Enclosed;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;

public class CoreOperationNotEqualTest {
    @Test
    public void testCoreOperationNotEqualConstructor() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertNotNull(coreOperationNotEqual);
    }

    @Test
    public void testComputeValueMethod() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        Assert.assertEquals(Boolean.FALSE, coreOperationNotEqual.computeValue(context));

        arg1 = new ConstantExpression(1);
        arg2 = new ConstantExpression(1);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        Assert.assertEquals(Boolean.TRUE, coreOperationNotEqual.computeValue(context));
    }

    @Test(expected = IllegalStateException.class)
    public void testCoreOperationNotEqualWithNullInput() {
        Expression arg1 = null;
        Expression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        coreOperationNotEqual.computeValue(new EvalContext());
    }

    @Test(expected = IllegalStateException.class)
    public void testCoreOperationNotEqualWithNullInput2() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        coreOperationNotEqual.computeValue(new EvalContext());
    }

    @Test
    public void testCoreOperationNotEqualWithDifferentTypes() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new StringConstantExpression("1");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertEquals(Boolean.FALSE, coreOperationNotEqual.computeValue(context));
    }

    @Test
    public void testCoreOperationNotEqualWithEquality() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Assert.assertEquals(Boolean.TRUE, coreOperationNotEqual.computeValue(context));
    }
}