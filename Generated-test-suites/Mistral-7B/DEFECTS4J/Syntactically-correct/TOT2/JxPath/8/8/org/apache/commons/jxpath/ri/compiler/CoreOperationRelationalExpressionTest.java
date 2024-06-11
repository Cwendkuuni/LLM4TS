import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new SelfContext(true), new SelfContext(true));
        EvalContext context = new EvalContext(new HashSet<>());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        expr = new CoreOperationRelationalExpression(new Integer(5), new Integer(5));
        context = new EvalContext(new HashSet<>());
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}