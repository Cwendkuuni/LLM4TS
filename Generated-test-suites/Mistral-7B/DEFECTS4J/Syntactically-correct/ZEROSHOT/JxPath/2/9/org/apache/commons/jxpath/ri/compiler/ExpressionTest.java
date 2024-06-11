import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionTest {

    @Mock
    private EvalContext mockEvalContext;

    private Expression testExpression;

    @Before
    public void setup() {
        testExpression = Mockito.spy(new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        });
    }

    @Test
    public void testIsContextDependent() {
        assertFalse(testExpression.isContextDependent());
    }

    @Test
    public void testComputeContextDependent() {
        testExpression = Mockito.spy(new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }
        });

        Mockito.doReturn(true).when(testExpression).computeContextDependent();
        assertTrue(testExpression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Mockito.when(mockEvalContext.getCurrentNodePointer()).thenReturn(new NodePointer(new QName("root"), null, null, Locale.ENGLISH));
        Object value = testExpression.computeValue(mockEvalContext);
        assertNull(value);
    }

    @Test
    public void testComputeValueWithContextDependentExpression() {
        testExpression = Mockito.spy(new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return "context-dependent value";
            }
        });

        Mockito.when(mockEvalContext.getCurrentNodePointer()).thenReturn(new NodePointer(new QName("root"), null, null, Locale.ENGLISH));
        Object value = testExpression.computeValue(mockEvalContext);
        assertEquals("context-dependent value", value);

        Mockito.verify(testExpression, Mockito.times(1)).computeContextDependent();
    }

    @Test
    public void testComputeWithContextDependentExpression() {
        testExpression = Mockito.spy(new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }

            @Override
            public Object compute(EvalContext context) {
                return "context-dependent value";
            }
        });

        Mockito.when(mockEvalContext.getCurrentNodePointer()).thenReturn(new NodePointer(new QName("root"), null, null, Locale.ENGLISH));
        Object value = testExpression.compute(mockEvalContext);
        assertEquals("context-dependent value", value);

        Mockito.verify(testExpression, Mockito.times(1)).computeContextDependent();
    }

    @Test
    public void testIterateWithContextDependentExpression() {
        testExpression = Mockito.spy(new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }

            @Override
            public Iterator iterate(EvalContext context) {
                return new ValueIterator(Collections.singletonList("context-dependent value").iterator());
            }
        });

        Mockito.when(mockEvalContext.getCurrentNodePointer()).thenReturn(new NodePointer(new QName("root"), null, null, Locale.ENGLISH));
        Iterator iterator = testExpression.iterate(mockEvalContext);
        assertEquals("context-dependent value", iterator.next().toString());

        Mockito.verify(testExpression, Mockito.times(1)).computeContextDependent();
    }

    @Test
    public void testIteratePointers() {
        testExpression = Mockito.spy(new AbstractExpression() {
            @Override
            public Iterator iteratePointers(EvalContext context) {
                return new PointerIterator(Collections.singletonList(new NodePointer(new QName("root"), null, null, Locale.ENGLISH)).iterator(),
                        new QName(null, "value"),
                        Locale.ENGLISH);
            }
        });

        Iterator iterator = testExpression.iteratePointers(mockEvalContext);
        assertTrue(iterator.hasNext());
        assertEquals("root", iterator.next().getLocalName());

        Mockito.verifyZeroInteractions(testExpression);
    }
}

class AbstractExpression extends Expression {
}