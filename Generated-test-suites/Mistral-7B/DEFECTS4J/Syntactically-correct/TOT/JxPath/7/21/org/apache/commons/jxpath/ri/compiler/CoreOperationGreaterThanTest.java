import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testGreaterThan() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new LiteralExpression(new Double(5.0)),
                new LiteralExpression(new Double(3.0)));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new LiteralExpression(new Double(5.0)),
                new LiteralExpression(new Double(5.0)));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                null,
                new LiteralExpression(new Double(3.0)));

        EvalContext context = new EvalContext(new InfoSet());
        expression.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments2() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new LiteralExpression(new Double(3.0)),
                null);

        EvalContext context = new EvalContext(new InfoSet());
        expression.computeValue(context);
    }
}