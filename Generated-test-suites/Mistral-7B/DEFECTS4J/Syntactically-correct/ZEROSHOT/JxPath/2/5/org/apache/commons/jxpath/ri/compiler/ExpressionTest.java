import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = Mockito.spy(new MyExpression());
        assertFalse(expression.isContextDependent());

        Mockito.doReturn(true).when(expression).computeContextDependent();
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new MyExpression();
        EvalContext context = Mockito.mock(EvalContext.class);

        Object result = expression.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCompute() {
        Expression expression = new MyExpression();
        EvalContext context = Mockito.mock(EvalContext.class);

        Object result = expression.compute(context);
        assertEquals(0, result.hashCode());
    }

    @Test
    public void testIterate() {
        Expression expression = new MyExpression();
        EvalContext context = Mockito.mock(EvalContext.class);

        Iterator<Object> iterator = expression.iterate(context);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new MyExpression();
        EvalContext context = Mockito.mock(EvalContext.class);
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(context.getRootContext()).thenReturn(Mockito.mock(EvalContext.class));
        Mockito.when(context.getRootContext().getCurrentNodePointer()).thenReturn(nodePointer);
        Mockito.when(nodePointer.getLocale()).thenReturn(Locale.ENGLISH);

        Iterator<Pointer> iterator = expression.iteratePointers(context);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPointerIterator() {
        Expression expression = new MyExpression();
        EvalContext context = Mockito.mock(EvalContext.class);
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        QName qname = new QName(null, "test");

        Mockito.when(context.getRootContext()).thenReturn(Mockito.mock(EvalContext.class));
        Mockito.when(context.getRootContext().getCurrentNodePointer()).thenReturn(nodePointer);
        Mockito.when(nodePointer.getLocale()).thenReturn(Locale.ENGLISH);

        Iterator<Pointer> pointerIterator = new Expression.PointerIterator(
                ValueUtils.iterate(0), qname, Locale.ENGLISH);

        assertTrue(pointerIterator.hasNext());
        Pointer pointer = pointerIterator.next();
        assertEquals(0, pointer.getValue().hashCode());
        assertSame(qname, pointer.getQName());
        assertSame(Locale.ENGLISH, pointer.getLocale());

        assertFalse(pointerIterator.hasNext());
    }

    @Test
    public void testValueIterator() {
        Expression expression = new MyExpression();
        EvalContext context = Mockito.mock(EvalContext.class);
        Object value = new Object();

        Iterator<Object> valueIterator = new Expression.ValueIterator(
                ValueUtils.iterate(value));

        assertTrue(valueIterator.hasNext());
        assertEquals(value, valueIterator.next());

        assertFalse(valueIterator.hasNext());
    }

    private static class MyExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return 0;
        }

        @Override
        public Object compute(EvalContext context) {
            return 0;
        }
    }
}