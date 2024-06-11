package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NumberValue;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqualWithTwoNumbers() {
        Expression arg1 = new NumberValue(2.0);
        Expression arg2 = new NumberValue(3.0);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = lessThanOrEqual.computeValue(context);

        assertTrue("Expected true for less than or equal operation with 2 numbers", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqualWithTwoNegativeNumbers() {
        Expression arg1 = new NumberValue(-2.0);
        Expression arg2 = new NumberValue(-3.0);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = lessThanOrEqual.computeValue(context);

        assertTrue("Expected true for less than or equal operation with two negative numbers", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqualWithOneNumberAndOneNull() {
        Expression arg1 = new NumberValue(2.0);
        Expression arg2 = null;
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet(), new Path());
        Object result = lessThanOrEqual.computeValue(context);

        assertFalse("Expected false for less than or equal operation with one number and one null", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithInvalidArguments() {
        Expression arg1 = new NumberValue(2.0);
        Expression arg2 = new NodeInfo(new Object());
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet(), new Path());
        lessThanOrEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithMismatchedTypes() {
        Expression arg1 = new NumberValue(2.0);
        Expression arg2 = new StringValue("3");
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet(), new Path());
        lessThanOrEqual.computeValue(context);
    }
}