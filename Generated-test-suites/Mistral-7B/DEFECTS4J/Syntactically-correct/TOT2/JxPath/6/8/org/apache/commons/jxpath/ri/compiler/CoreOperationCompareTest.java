import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression left = new ConstantExpression(1.0);
        Expression right = new ConstantExpression(1.0);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext();

        boolean actual = coreOperationCompare.equal(context, left, right);
        boolean expected = true;

        assertEquals(expected, actual);
    }
}