package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.core.CoreExpression;
import org.apache.commons.jxpath.ri.core.CoreOperationCompare;
import org.apache.commons.jxpath.ri.util.TestUtils;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testEqual() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreExpression("1"),
                new CoreExpression("1")
        );

        EvalContext context = TestUtils.newEvalContext();
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testNotEqual() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreExpression("1"),
                new CoreExpression("2")
        );

        EvalContext context = TestUtils.newEvalContext();
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLeftNull() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreExpression(null),
                new CoreExpression("1")
        );

        EvalContext context = TestUtils.newEvalContext();
        Object result = operation.computeValue(context);

        Assert.assertNull(result);
    }

    @Test
    public void testRightNull() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreExpression("1"),
                new CoreExpression(null)
        );

        EvalContext context = TestUtils.newEvalContext();
        Object result = operation.computeValue(context);

        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLeftAndRightNull() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreExpression(null),
                new CoreExpression(null)
        );

        EvalContext context = TestUtils.newEvalContext();
        Object result = operation.computeValue(context);
    }

    @Test
    public void testTypeMismatch() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreExpression("1"),
                new CoreExpression("hello")
        );

        EvalContext context = TestUtils.newEvalContext();
        Object result = operation.computeValue(context);

        Assert.assertFalse(result instanceof Boolean);
    }
}