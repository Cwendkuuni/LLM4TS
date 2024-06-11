import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        double value1 = 5.5;
        double value2 = 3.0;
        Expression arg1 = new ConstantExpression(value1);
        Expression arg2 = new ConstantExpression(value2);
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());
        Boolean result = (Boolean) expression.computeValue(context);

        assertEquals(true, result);
    }
}