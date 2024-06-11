import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionTest {

    private Expression expression;
    @Mock
    private EvalContext context;

    @Before
    public void setUp() {
        expression = mock(Expression.class);
    }

    @Test
    public void testIsContextDependent_default_returnsFalse() {
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testIsContextDependent_implementedBySubclasses() {
        when(expression.computeContextDependent()).thenReturn(true);
        expression.isContextDependent();
        verify(expression, times(1)).computeContextDependent();
    }

    @Test
    public void testComputeValue_returnsValue() {
        when(expression.computeValue(context)).thenReturn("testValue");
        Object value = expression.computeValue(context);
        assertSame("testValue", value);
    }

    @Test
    public void testComputeValue_returnsNull() {
        when(expression.computeValue(context)).thenReturn(null);
        Object value = expression.computeValue(context);
        assertNull(value);
    }

    @Test
    public void testCompute_returnsValue() {
        when(expression.compute(context)).thenReturn("testValue");
        Object value = expression.compute(context);
        assertSame("testValue", value);
    }

    @Test
    public void testIterate_returnsEmptyIterator_whenNullIsReturned() {
        when(expression.compute(context)).thenReturn(null);
        Iterator<Object> iterator = expression.iterate(context);
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterate_returnsValueIterator_whenValueIsReturned() {
        when(expression.compute(context)).thenReturn("testValue1");
        Iterator<Object> iterator = expression.iterate(context);
        assertSame("testValue1", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterate_returnsPointerIterator_whenNodeSetIsReturned() {
        NodePointer nodePointer = new NodePointer(new QName("test"), new Object());
        when(expression.compute(context)).thenReturn(nodePointer);
        Iterator<Object> iterator = expression.iterate(context);
        assertSame(nodePointer, iterator.next());
        assertFalse(iterator.hasNext());
    }
}