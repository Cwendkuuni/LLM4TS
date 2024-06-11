import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new ConstantExpression(new Integer(5));
        Expression right = new ConstantExpression(new Integer(10));
        CoreOperationRelationalExpression expr = new GreaterThanExpression(left, right);
        EvalContext context = new EvalContext();
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}