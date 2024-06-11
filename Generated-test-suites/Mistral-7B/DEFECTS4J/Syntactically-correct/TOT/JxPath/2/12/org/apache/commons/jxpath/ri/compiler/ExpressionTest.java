import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Node;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Test for {@link Expression} class.
 */
public class ExpressionTest {

    private Expression expression;
    private EvaluationContext context;

    @Before
    public void setup() {
        expression = new SomeExpression();
        context = new EvaluationContext();
    }

    @Test
    public void testContextIndependentExpression() {
        // Test context-independent expression
        Expression contextIndependentExpression = new ConstantExpression(1);
        Object result1 = contextIndependentExpression.computeValue(context);
        Object result2 = contextIndependentExpression.computeValue(new EvaluationContext(new Node("root")));
        assertEquals(1.0, result1.doubleValue());
        assertEquals(1.0, result2.doubleValue());
    }

    @Test
    public void testContextDependentExpression() {
        // Test context-dependent expression
        Expression contextDependentExpression = new ContextDependentExpression();
        Object result1 = contextDependentExpression.computeValue(context);
        context.setCurrentNode(new Node("root"));
        Object result2 = contextDependentExpression.computeValue(context);
        assertNotSame(result1, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullContext() {
        expression.computeValue(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNullIterable() {
        Iterator iter = null;
        expression.iterate(new EvaluationContext()).iterator();
    }

    @Test
    public void testIterate() {
        // Test iterate method
        Expression iterateExpression = new IterateExpression();
        Iterator iterator = iterateExpression.iterate(context);
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        Object next = iterator.next();
        assertTrue(next instanceof Node);
        iterator.hasNext(); // check hasNext() method works correctly after next() call
        iterator.remove(); // check remove() method throws UnsupportedOperationException
    }

    @Test
    public void testIteratePointers() {
        // Test iteratePointers method
        Expression iteratePointersExpression = new IteratePointersExpression();
        Iterator iterator = iteratePointersExpression.iteratePointers(context);
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        Object next = iterator.next();
        assertTrue(next instanceof Pointer);
        iterator.hasNext(); // check hasNext() method works correctly after next() call
        iterator.remove(); // check remove() method throws UnsupportedOperationException
    }

    private static class ConstantExpression extends Expression {
        private final double value;

        public ConstantExpression(double value) {
            this.value = value;
        }

        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvaluationContext context) {
            return value;
        }
    }

    private static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvaluationContext context) {
            return context.getCurrentNode().getValue();
        }
    }

    private static class IterateExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Iterator iterate(EvaluationContext context) {
            return context.getCurrentNode().getChildren().iterator();
        }
    }

    private static class IteratePointersExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Iterator iteratePointers(EvaluationContext context) {
            return context.getCurrentNode().getChildren().iterator();
        }
    }

    private static class Node {
        private final String name;
        private final double value;

        public Node(String name) {
            this(name, 0.0);
        }

        public Node(String name, double value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public double getValue() {
            return value;
        }

        public Iterator getChildren() {
            return Collections.emptyList().iterator();
        }
    }
}