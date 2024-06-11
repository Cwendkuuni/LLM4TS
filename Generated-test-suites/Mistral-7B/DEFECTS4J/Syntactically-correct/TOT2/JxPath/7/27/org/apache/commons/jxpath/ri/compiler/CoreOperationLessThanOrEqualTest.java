import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.info.InfoSet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        InfoSet infoSet = new InfoSet();
        Object result = coreOperationLessThanOrEqual.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }
}