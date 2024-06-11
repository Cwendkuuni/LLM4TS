import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.iter.Iterators;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new ConstantExpression(1);
        Expression right = new ConstantExpression(2);
        CoreOperationRelationalExpression coreOperation = new GreaterThanExpression(new String[]{"args"}, new Expression[]{left, right});

        EvalContext context = new EvalContext(new SelfContext());

        boolean result = coreOperation.computeValue(context).equals(Boolean.TRUE);
        Assert.assertTrue(result);
    }
}