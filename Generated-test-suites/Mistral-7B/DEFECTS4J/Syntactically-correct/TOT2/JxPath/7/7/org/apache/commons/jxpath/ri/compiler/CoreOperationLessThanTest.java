import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanTest {

    @Test
    public void testLessThan() {
        Expression arg1 = new Const(1.0);
        Expression arg2 = new Const(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new Const(1.0);
        arg2 = new Const(1.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new Const(1.0);
        arg2 = new Const(0.5);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    // TODO: Add edge cases and exception handling test cases here
}