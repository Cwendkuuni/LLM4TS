import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.jaxp.JaxpExpression;
import org.apache.commons.jxpath.ri.parser.StaticContext;
import org.junit.Test;
import org.junit.Assert.*;

import java.math.BigDecimal;

public class CoreOperationGreaterThanTest {

    @Test
    public void testGreaterThanDoublePositive() {
        Expression arg1 = new LiteralExpression(new Double(5.5));
        Expression arg2 = new LiteralExpression(new Double(2.0));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new StaticContext(), new InfoSet());
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanDoubleNegative() {
        Expression arg1 = new LiteralExpression(new Double(-5.5));
        Expression arg2 = new LiteralExpression(new Double(-2.0));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new StaticContext(), new InfoSet());
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanDoubleZero() {
        Expression arg1 = new LiteralExpression(new Double(0.0));
        Expression arg2 = new LiteralExpression(new Double(-2.0));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new StaticContext(), new InfoSet());
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanDoubleZeroZero() {
        Expression arg1 = new LiteralExpression(new Double(0.0));
        Expression arg2 = new LiteralExpression(new Double(0.0));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new StaticContext(), new InfoSet());
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanDecimalPositive() {
        Expression arg1 = new LiteralExpression(new BigDecimal("5.5"));
        Expression arg2 = new LiteralExpression(new BigDecimal("2.0"));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new StaticContext(), new InfoSet());
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanDecimalNegative() {
        Expression arg1 = new LiteralExpression(new BigDecimal("-5.5"));
        Expression arg2 = new LiteralExpression(new BigDecimal("-2.0"));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new StaticContext(), new InfoSet());
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = ClassCastException.class)
    public void testGreaterThanString() {
        Expression arg1 = new LiteralExpression("5.5");
        Expression arg2 = new LiteralExpression("2.0");
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new StaticContext(), new InfoSet());
        op.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testGreaterThanNull() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(new Double(2.0));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);

        EvaluationContext context = new EvaluationContext(new StaticContext(), new InfoSet());
        op.computeValue(context);
    }
}