import static org.junit.Assert.*;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        Expression left = new ConstantExpression(new Integer(5));
        Expression right = new ConstantExpression(new Integer(10));
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new CoreExpressionList(left, right));
        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}