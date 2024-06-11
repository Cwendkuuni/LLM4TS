import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.ITERATOR_TYPE;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(5), new ConstantExpression(7));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationRelationalExpression(new ConstantExpression(7), new ConstantExpression(5));
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationRelationalExpression(new ConstantExpression(5), new ConstantExpression(5));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationRelationalExpression(new ConstantExpression(5), new ConstantExpression(5.0));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithNullArguments() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null, new ConstantExpression(5));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        expression = new CoreOperationRelationalExpression(new ConstantExpression(5), null);
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueWithNonBooleanResults() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(5), new ConstantExpression(7.0));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueWithDifferentTypes() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ConstantExpression(5), new ConstantExpression("5"));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        expression = new CoreOperationRelationalExpression(new ConstantExpression("5"), new ConstantExpression(5));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        expression = new CoreOperationRelationalExpression(new ConstantExpression(5), new ConstantExpression(null));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        expression = new CoreOperationRelationalExpression(new ConstantExpression(null), new ConstantExpression(5));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueWithEmptyCollections() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ListExpression(Collections.emptyList()), new ListExpression(Collections.emptyList()));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithLargeCollections() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ListExpression(new Object[10000]), new ListExpression(new Object[10000]));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithEmptyIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new IteratorExpression(Collections.emptyIterator()), new ConstantExpression(5));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        expression = new CoreOperationRelationalExpression(new ConstantExpression(5), new IteratorExpression(Collections.emptyIterator()));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }
}