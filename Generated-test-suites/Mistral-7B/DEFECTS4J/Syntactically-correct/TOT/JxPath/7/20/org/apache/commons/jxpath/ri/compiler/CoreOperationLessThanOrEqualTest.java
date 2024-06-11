package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.node.DoubleNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqualWithBothNumbersGreaterThan() {
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(
                new DoubleNode(5.0), new DoubleNode(10.0));
        EvalContext context = new EvalContext(new InfoSet());
        Assert.assertFalse(expr.computeValue(context).booleanValue());
    }

    @Test
    public void testLessThanOrEqualWithFirstNumberSmallerThanSecond() {
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(
                new DoubleNode(3.0), new DoubleNode(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Assert.assertTrue(expr.computeValue(context).booleanValue());
    }

    @Test
    public void testLessThanOrEqualWithFirstNumberEqualToSecond() {
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(
                new DoubleNode(5.0), new DoubleNode(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        Assert.assertTrue(expr.computeValue(context).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithInvalidArgType() {
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(
                new StringNode("Apache Commons JXPath"), new DoubleNode(3.5));
        EvalContext context = new EvalContext(new InfoSet());
        expr.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithArgNull() {
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(
                null, new DoubleNode(3.5));
        EvalContext context = new EvalContext(new InfoSet());
        expr.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithSecondArgNull() {
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(
                new DoubleNode(3.5), null);
        EvalContext context = new EvalContext(new InfoSet());
        expr.computeValue(context);
    }
}