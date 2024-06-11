import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new ConstantExpression(2.5);
        Expression arg2 = new ConstantExpression(3.0);
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        Boolean result = coreOperation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new ConstantExpression(2.5);
        arg2 = new ConstantExpression(2.5);
        coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}