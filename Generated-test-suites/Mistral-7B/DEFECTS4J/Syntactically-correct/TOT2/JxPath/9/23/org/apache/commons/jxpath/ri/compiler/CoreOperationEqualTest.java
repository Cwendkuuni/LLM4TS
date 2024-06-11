import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqualConstructor() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        // No exception is thrown, so test passed
    }

    @Test
    public void testComputeValueWithEqualValues() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueWithEqualValuesTypeInt() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueWithEqualValuesDifferentTypes() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueWithNotEqualValues() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result.equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValueWithNotEqualValuesButSameDataTypes() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result.equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValueWithNaNValues() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(Double.NaN);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result.equals(Boolean.FALSE));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(1));
        assertEquals(coreOperationEqual.getSymbol(), "=");
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullContext() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(null);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullArg1() {
        EvalContext context = new EvalContext();
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullArg2() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullArg1AndNullArg2() {
        EvalContext context = new EvalContext();
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(context);
    }
}