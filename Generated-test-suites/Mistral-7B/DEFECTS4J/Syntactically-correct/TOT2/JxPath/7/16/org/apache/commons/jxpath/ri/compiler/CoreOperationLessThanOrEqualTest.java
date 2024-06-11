import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testEquality() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArg2() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = null;
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test
    public void testNonDoubleTypes() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression("2.0");
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testNaN() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(Double.NaN);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testInfinity() {
        Expression arg1 = new LiteralExpression(Double.POSITIVE_INFINITY);
        Expression arg2 = new LiteralExpression(Double.POSITIVE_INFINITY);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testNegativeInfinity() {
        Expression arg1 = new LiteralExpression(Double.NEGATIVE_INFINITY);
        Expression arg2 = new LiteralExpression(Double.NEGATIVE_INFINITY);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result);
    }

}