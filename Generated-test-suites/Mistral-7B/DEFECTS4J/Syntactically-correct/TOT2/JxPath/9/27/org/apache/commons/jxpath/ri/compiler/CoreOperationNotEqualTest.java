import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;

public class CoreOperationNotEqualTest {

    @Test
    public void testNotEqual_False() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Constant(5), new Constant(5));
        assertFalse(operation.computeValue(new EvalContext()));
    }

    @Test
    public void testNotEqual_True() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Constant(5), new Constant(6));
        assertTrue(operation.computeValue(new EvalContext()));
    }

    // Add edge cases and exception handling test cases here
}