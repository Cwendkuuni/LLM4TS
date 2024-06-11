import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new ConstantExpression(1);
        Expression right = new ConstantExpression(2);
        CoreOperationRelationalExpression expression = new GreaterThanExpression(left, right);

        EvalContext context = new EvalContext();
        boolean result = expression.computeValue(context).booleanValue();

        assertTrue(result);
    }
}