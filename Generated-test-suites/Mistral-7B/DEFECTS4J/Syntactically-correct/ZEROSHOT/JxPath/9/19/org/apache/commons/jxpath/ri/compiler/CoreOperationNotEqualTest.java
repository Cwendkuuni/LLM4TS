package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        // Test with null arguments
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, null);
        Assert.assertNull(coreOperationNotEqual.getSymbol());

        // Test with null context
        EvalContext context = null;
        CoreOperationNotEqual coreOperationNotEqual1 = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
        Assert.assertNull(coreOperationNotEqual1.computeValue(context));

        // Test with equal context and equal arguments
        coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
        context = new EvalContext();
        Assert.assertFalse(coreOperationNotEqual.computeValue(context).booleanValue());

        // Test with equal context and not equal arguments
        coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
        Assert.assertTrue(coreOperationNotEqual.computeValue(context).booleanValue());

        // Test with not equal context and equal arguments
        coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
        context.setVariable("var", new ConstantExpression(2));
        Assert.assertTrue(coreOperationNotEqual.computeValue(context).booleanValue());

        // Test with not equal context and not equal arguments
        coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
        context.setVariable("var", new ConstantExpression(1));
        Assert.assertFalse(coreOperationNotEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualWithComplexExpressions() {
        Expression arg1 = new VariableExpression("arg1");
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setVariable("arg1", new ConstantExpression(2));
        Assert.assertTrue(coreOperationNotEqual.computeValue(context).booleanValue());

        arg1 = new VariableExpression("arg1");
        arg2 = new VariableExpression("arg2");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        context.setVariable("arg1", new ConstantExpression(1));
        context.setVariable("arg2", new ConstantExpression(2));
        Assert.assertTrue(coreOperationNotEqual.computeValue(context).booleanValue());

        context.setVariable("arg2", new ConstantExpression(1));
        Assert.assertFalse(coreOperationNotEqual.computeValue(context).booleanValue());
    }
}