import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testGreaterThan() throws EvaluationException {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new ConstantExpression(new JXPathContext(10.5), "10.5"),
                new ConstantExpression(new JXPathContext(5.3), "5.3"));

        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Boolean result = (Boolean) expression.computeValue(context);

        assertTrue(result);
    }

    @Test
    public void testGreaterThanWithEqual() throws EvaluationException {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new ConstantExpression(new JXPathContext(10.5), "10.5"),
                new ConstantExpression(new JXPathContext(10.5), "10.5"));

        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testGreaterThanWithLess() throws EvaluationException {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new ConstantExpression(new JXPathContext(10.5), "10.5"),
                new ConstantExpression(new JXPathContext(5.3), "5.3"));

        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        assertFalse(expression.computeValue(context));
    }

    @Test(expected = EvaluationException.class)
    public void testGreaterThanWithInvalidTypes() throws EvaluationException {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new ConstantExpression(new JXPathContext("10.5"), "10.5"),
                new ConstantExpression(new JXPathContext("String"), "String"));

        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        expression.computeValue(context);
    }

    @Test
    public void testGreaterThanWithNull() throws EvaluationException {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new ConstantExpression(new JXPathContext(Double.NaN), "Double.NaN"),
                new ConstantExpression(new JXPathContext(null), "null"));

        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        assertFalse(expression.computeValue(context));
    }
}