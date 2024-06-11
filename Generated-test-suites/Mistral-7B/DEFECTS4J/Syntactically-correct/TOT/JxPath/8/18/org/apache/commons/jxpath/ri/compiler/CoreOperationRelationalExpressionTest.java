package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.NodeIterators;
import org.apache.commons.jxpath.ri.axes.Axis;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("a"), new NumericLiteralExpression(5.0));
        InfoSet infoSet = createInfoSet();
        EvalContext context = new EvalContext(Axis.SELF, infoSet);

        // Positive test
        Object left = new Double(3.0);
        Object right = new Double(5.0);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Edge case: equal values should not match
        left = new Double(5.0);
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        // Negative test: left value is smaller
        left = new Double(2.0);
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new VariableExpression("a"), new NumericLiteralExpression(5.0));
        InfoSet infoSet = createInfoSet();
        EvalContext context = new EvalContext(Axis.SELF, infoSet);

        // Positive test
        Object left = new Double(5.0);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Edge case: equal values should match
        left = new Double(5.0);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Negative test: left value is smaller
        left = new Double(2.0);
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new VariableExpression("a"), new NumericLiteralExpression(5.0));
        InfoSet infoSet = createInfoSet();
        EvalContext context = new EvalContext(Axis.SELF, infoSet);

        // Positive test
        Object left = new Double(2.0);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Edge case: equal values should not match
        left = new Double(5.0);
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        // Negative test: left value is greater
        left = new Double(6.0);
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new VariableExpression("a"), new NumericLiteralExpression(5.0));
        InfoSet infoSet = createInfoSet();
        EvalContext context = new EvalContext(Axis.SELF, infoSet);

        // Positive test
        Object left = new Double(5.0);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Edge case: equal values should match
        left = new Double(5.0);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Negative test: left value is greater
        left = new Double(6.0);
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCompareIterators() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("a"), new VariableExpression("b"));
        InfoSet infoSet = createInfoSet();
        EvalContext context = new EvalContext(Axis.SELF, infoSet);

        Iterator leftIterator = NodeIterators.emptyIterator();
        Iterator rightIterator = Iterators.singletonIterator(new Node("B"));

        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        leftIterator = Iterators.singletonIterator(new Node("A"));
        rightIterator = NodeIterators.emptyIterator();

        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        leftIterator = Iterators.singletonIterator(new Node("A"));
        rightIterator = Iterators.singletonIterator(new Node("A"));

        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        leftIterator = Iterators.singletonIterator(new Node("A"));
        rightIterator = Iterators.singletonIterator(new Node("B"));

        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        leftIterator = Iterators.singletonIterator(new Node("B"));
        rightIterator = Iterators.singletonIterator(new Node("B"));

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        leftIterator = Iterators.singletonIterator(new Node("B"));
        rightIterator = Iterators.singletonIterator(new Node("A"));

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCompareNullValues() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("a"), new NullLiteralExpression());
        InfoSet infoSet = createInfoSet();
        EvalContext context = new EvalContext(Axis.SELF, infoSet);

        Object left = null;
        Object right = new String("B");

        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new String("A");
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new String("B");
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new String("B");
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        left = new String("A");
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    private InfoSet createInfoSet() {
        EvalContext context = new EvalContext(Axis.SELF, new InfoSet());
        context.setCurrentNode(new Node("root"));
        return context.getInfoSet();
    }

    private static class Node {
        private final String name;

        public Node(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}