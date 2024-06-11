import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.info.InfoSet;
import junit.framework.TestCase;

public class CoreOperationGreaterThanTest extends TestCase {

    public void testCoreOperationGreaterThan() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(5.0);
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new Double(3.0);
            }
        };

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        boolean result = coreOperationGreaterThan.computeValue(context).booleanValue();
        assertTrue(result);
    }
}