import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression arg1 = new LiteralExpression(2.5);
        Expression arg2 = new LiteralExpression(3.0);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        Boolean result = operation.computeValue(context).booleanValue();
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(3.0);
        arg2 = new LiteralExpression(3.0);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = operation.computeValue(context).booleanValue();
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(2.5);
        arg2 = new LiteralExpression(2.5);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = operation.computeValue(context).booleanValue();
        assertEquals(Boolean.TRUE, result);
    }
}