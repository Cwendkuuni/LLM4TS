import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

    private EvalContext context;

    @Before
    public void setup() {
        context = Mockito.mock(EvalContext.class);
    }

    @Test
    public void testIsContextDependentDefaultValue() {
        Expression expression = new AbstractExpression() {};
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testIsContextDependentContextDependentExpression() {
        Expression expression = new ContextDependentExpression() {};
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeValueContextIndependentExpression() {
        Expression expression = new ContextIndependentExpression() {};
        Object result = expression.computeValue(context);
        assertNotNull(result);
    }

    @Test
    public void testComputeValueContextDependentExpression() {
        Expression expression = new ContextDependentExpression() {};
        Object result = expression.computeValue(context);
        assertNotNull(result);
    }

    @Test
    public void testIterateContextIndependentExpression() {
        Expression expression = new ContextIndependentExpression() {};
        Iterator iterator = expression.iterate(context);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterateContextDependentExpression() {
        Expression expression = new ContextDependentExpression() {};
        Iterator iterator = expression.iterate(context);
        assertNotNull(iterator);
        List list = new LinkedList();
        list.add(1);
        list.add(2);
        Mockito.when(expression.compute(context)).thenReturn(list);
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointersContextIndependentExpression() {
        Expression expression = new ContextIndependentExpression() {};
        Iterator iterator = expression.iteratePointers(context);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointersContextDependentExpression() {
        Expression expression = new ContextDependentExpression() {};
        Iterator iterator = expression.iteratePointers(context);
        assertNotNull(iterator);
        NodePointer nodePointer = new NodePointer(new QName("test"), Mockito.mock(Object.class), Locale.US);
        Mockito.when(expression.compute(context)).thenReturn(nodePointer);
        assertEquals(nodePointer, iterator.next());
        assertFalse(iterator.hasNext());
    }

    private static class AbstractExpression extends Expression {

    }

    private static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }
    }

    private static class ContextIndependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return 1;
        }

        @Override
        public Object compute(EvalContext context) {
            return 1;
        }
    }
}