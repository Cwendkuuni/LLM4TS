import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan_Expert1() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreNumberExpression(10.5), new CoreNumberExpression(5.0));

        EvalContext context = new EvalContext();

        Object result = coreOperationGreaterThan.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThan_Expert2() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreNumberExpression(10.0), new CoreNumberExpression(10.0));

        EvalContext context = new EvalContext();

        Object result = coreOperationGreaterThan.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThan_Expert3_Exception() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreStringExpression("a"), new CoreStringExpression("b"));

        EvalContext context = new EvalContext();

        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected an exception when comparing non-numeric values");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }

    @Test
    public void testCoreOperationGreaterThan_Expert3() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreNumberExpression(10.5), new CoreNumberExpression(5.0));

        EvalContext context = new EvalContext();

        Object result = coreOperationGreaterThan.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }
}