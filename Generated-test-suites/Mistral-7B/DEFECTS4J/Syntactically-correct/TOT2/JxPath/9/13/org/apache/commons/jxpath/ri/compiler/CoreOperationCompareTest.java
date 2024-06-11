import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return 5;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return 5;
            }
        };
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        boolean result = operation.equal(context, arg1, arg2);
        assertTrue(result);
    }
}