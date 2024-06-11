import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression() {};
        Expression arg2 = new Expression() {};
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertNotNull(coreOperationEqual);
    }

    @Test
    public void testComputeValueTrue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression() {};
        Expression arg2 = new Expression() {};
        Object result = new CoreOperationEqual(arg1, arg2).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueFalse() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression() {};
        Expression arg2 = new Expression() {};
        Object result = new CoreOperationEqual(arg1, arg2).computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression() {}, new Expression() {});
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}