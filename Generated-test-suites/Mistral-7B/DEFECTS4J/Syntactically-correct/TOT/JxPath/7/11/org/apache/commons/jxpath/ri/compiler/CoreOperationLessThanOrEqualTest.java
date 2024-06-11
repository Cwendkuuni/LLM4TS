import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.expression.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqualWithDoubleValues() {
        Expression arg1 = new Const(5.0);
        Expression arg2 = new Const(10.0);
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setJXPathContext(new JXPathContext(new InfoSet()));
        context.setCurrentNode(new NodeInfo(new Object(), null, null, null));

        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext();
        arg1 = new Const(3.0);
        arg2 = new Const(8.0);
        op = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        arg1 = new Const(3.0);
        arg2 = new Const(3.0);
        op = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        arg1 = new Const(0.0);
        arg2 = new Const(0.0);
        op = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanOrEqualWithNullValues() {
        Expression arg1 = new Const(Double.NaN);
        Expression arg2 = new Const(Double.NaN);
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setJXPathContext(new JXPathContext(new InfoSet()));
        context.setCurrentNode(new NodeInfo(new Object(), null, null, null));

        Object result = op.computeValue(context);
        assertNull(result);

        arg1 = new Const(Double.MIN_VALUE);
        arg2 = new Const(Double.MIN_VALUE);
        op = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new Const(Double.MIN_VALUE);
        arg2 = new Const(Double.MAX_VALUE);
        op = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testLessThanOrEqualWithMixedTypes() {
        Expression arg1 = new Const(5.0);
        Expression arg2 = new Const("10");
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setJXPathContext(new JXPathContext(new InfoSet()));
        context.setCurrentNode(new NodeInfo(new Object(), null, null, null));

        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }
}