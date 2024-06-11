import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1.0), new ConstantExpression(2.0)});
        Object result = expression.computeValue(new EvalContext());
        assertEquals(Boolean.TRUE, result);
    }
}