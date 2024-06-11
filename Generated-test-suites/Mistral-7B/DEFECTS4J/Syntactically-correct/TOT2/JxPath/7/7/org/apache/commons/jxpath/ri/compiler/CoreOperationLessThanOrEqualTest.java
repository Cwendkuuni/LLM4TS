import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(2.0), new CoreConstantExpression(3.0));
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testLessThanOrEqualWithZero() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(0.0), new CoreConstantExpression(0.0));
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testLessThanOrEqualWithNegativeNumbers() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(-2.0), new CoreConstantExpression(-3.0));
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(expression.computeValue(context).booleanValue());
    }

    @Test
    public void testLessThanOrEqualWithNaN() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(Double.NaN), new CoreConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        assertFalse(expression.computeValue(context).booleanValue());
    }

    @Test(expected = ClassCastException.class)
    public void testLessThanOrEqualWithNonNumericArguments() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new CoreStringConstantExpression("2"), new CoreStringConstantExpression("3"));
        EvalContext context = new EvalContext(new InfoSet());
        expression.computeValue(context);
    }
}