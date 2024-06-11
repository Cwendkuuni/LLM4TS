import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual_DoubleValues_EqualOrGreater() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));

        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(4.0));
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_DoubleValues_LessThan() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new ConstantExpression(4.0), new ConstantExpression(5.0));
        EvalContext context = new EvalContext(new InfoSet());
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqual_MixedTypes() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression("5.0"));
        EvalContext context = new EvalContext(new InfoSet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqual_NullArguments() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(null, new ConstantExpression(5.0));
        EvalContext context = new EvalContext(new InfoSet());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_NaN() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_NegativeNumbers() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new ConstantExpression(-3.0), new ConstantExpression(-2.0));
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_ZeroAndNegativeNumber() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new ConstantExpression(-3.0), new ConstantExpression(0.0));
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));
    }
}