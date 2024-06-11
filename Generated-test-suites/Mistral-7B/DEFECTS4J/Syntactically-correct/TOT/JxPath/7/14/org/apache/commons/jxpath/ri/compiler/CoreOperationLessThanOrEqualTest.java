import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.core.Context;
import org.apache.commons.jxpath.ri.core.CoreExpression;
import org.apache.commons.jxpath.ri.core.CoreOperationRelationalExpression;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new Context(new EvalContext());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testGreaterThan() {
        Expression arg1 = new ConstantExpression(2.0);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new Context(new EvalContext());

        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testEqual() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new Context(new EvalContext());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testNaN() {
        Expression arg1 = new ConstantExpression(Double.NaN);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new Context(new EvalContext());

        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testNegativeNumber() {
        Expression arg1 = new ConstantExpression(-1.0);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new Context(new EvalContext());

        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongType() {
        Expression arg1 = new ConstantExpression("abc");
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArg() {
        Expression arg1 = new ConstantExpression(null);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
    }
}