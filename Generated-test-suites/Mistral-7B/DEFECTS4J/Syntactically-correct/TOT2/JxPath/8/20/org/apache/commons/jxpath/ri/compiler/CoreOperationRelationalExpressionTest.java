import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new LiteralExpression(true);
        Expression right = new LiteralExpression(true);
        CoreOperationRelationalExpression expression = new GTExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}