import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testComputeValue() {
        Expression left = new ConstantExpression(1);
        Expression right = new ConstantExpression(2);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[] { left, right });
        EvalContext context = new EvalContext(new SelfContext());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        // Test with true condition
        left = new ConstantExpression(2);
        right = new ConstantExpression(2);
        expr = new CoreOperationRelationalExpression(new Expression[] { left, right });
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}