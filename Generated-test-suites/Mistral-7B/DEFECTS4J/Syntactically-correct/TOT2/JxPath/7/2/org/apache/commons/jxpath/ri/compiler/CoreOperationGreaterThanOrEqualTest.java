import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.SimpleContext;
import org.apache.commons.jxpath.ri.IntegrationTest;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = new LiteralExpression(1.5);
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        SimpleContext context = new SimpleContext();
        EvalContext evalContext = new EvalContext(context);

        Boolean result = (Boolean) coreOperation.computeValue(evalContext);
        assertTrue(result);

        arg2 = new LiteralExpression(2.0);
        result = (Boolean) coreOperation.computeValue(evalContext);
        assertTrue(result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(2.0);
        coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        result = (Boolean) coreOperation.computeValue(evalContext);
        assertFalse(result);
    }
}