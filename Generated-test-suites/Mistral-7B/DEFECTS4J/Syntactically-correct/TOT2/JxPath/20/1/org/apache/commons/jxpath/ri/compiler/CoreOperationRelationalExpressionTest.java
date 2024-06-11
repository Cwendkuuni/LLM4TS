import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.core.Context;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new ConstantExpression(true);
        Expression right = new ConstantExpression(false);
        CoreOperationRelationalExpression op = new GreaterExpression(new AndExpression(left, new ConstantExpression(1)), right);

        EvalContext context = new EvaluationContext(new Context(new InitialContext(), null), null);
        Object result = op.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }
}