import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanTest {

    @Test
    public void testLessThan() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();

        Object result = lessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg2 = new LiteralExpression(1.0);
        result = lessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(2.0);
        result = lessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanWithNaN() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();

        Object result = lessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanWithNegativeNumbers() {
        Expression arg1 = new LiteralExpression(-1.0);
        Expression arg2 = new LiteralExpression(-2.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();

        Object result = lessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(-2.0);
        arg2 = new LiteralExpression(-1.0);
        lessThan = new CoreOperationLessThan(arg1, arg2);
        result = lessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testLessThanWithNonNumericTypes() {
        Expression arg1 = new LiteralExpression("foo");
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();

        lessThan.computeValue(context); // This line should throw ClassCastException
    }
}