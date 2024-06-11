import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

    @Test
    public void testNotEqual() {
        Expression arg1 = new Const(1);
        Expression arg2 = new Const(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.FALSE));
    }

    // Edge case 1: Checking null arguments
    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, new Const(1));
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments2() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Const(1), null);
    }

    // Edge case 2: Checking equal expressions
    @Test
    public void testEqualExpressions() {
        Expression arg1 = new Const(1);
        Expression arg2 = arg1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationNotEqual.computeValue(context).equals(Boolean.FALSE));
        assertTrue(coreOperationNotEqual.computeValue(context).equals(Boolean.TRUE));
    }
}