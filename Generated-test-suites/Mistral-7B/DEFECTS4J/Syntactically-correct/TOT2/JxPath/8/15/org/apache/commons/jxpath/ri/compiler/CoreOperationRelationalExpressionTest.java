import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.SimpleContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object left = Boolean.TRUE;
        Object right = Boolean.TRUE;
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new Expression[]{new ConstantExpression(left), new ConstantExpression(right)});

        Boolean result = expression.computeValue(new SimpleContext());
        assertTrue(result);

        left = Boolean.FALSE;
        right = Boolean.TRUE;
        result = expression.computeValue(new SimpleContext());
        assertFalse(result);

        left = Boolean.TRUE;
        right = Boolean.FALSE;
        result = expression.computeValue(new SimpleContext());
        assertFalse(result);

        left = Boolean.FALSE;
        right = Boolean.FALSE;
        result = expression.computeValue(new SimpleContext());
        assertFalse(result);
    }
}