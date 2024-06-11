import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.ExpressionTest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        double value1 = 5.5;
        double value2 = 4.5;
        Expression arg1 = new ExpressionTest.DummyExpression(value1);
        Expression arg2 = new ExpressionTest.DummyExpression(value2);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}