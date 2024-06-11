package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IteratingIterator;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.Nodes;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;

/**
 * Test class for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    private static final CoreOperationRelationalExpression GREATER = new CoreOperationRelationalExpression(">", new CoreOperation[] {});
    private static final CoreOperationRelationalExpression GREATER_EQUAL = new CoreOperationRelationalExpression(">=", new CoreOperation[] {});
    private static final CoreOperationRelationalExpression LESS = new CoreOperationRelationalExpression("<", new CoreOperation[] {});
    private static final CoreOperationRelationalExpression LESS_EQUAL = new CoreOperationRelationalExpression("<=", new CoreOperation[] {});

    @org.junit.Test
    public void testGivenTwoNumericValues_WhenComparingWithGreaterThanOperator_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root"), Nodes.newElementNode("child1", "5"), Nodes.newElementNode("child2", "7")));
        context.setCurrentNode("root");

        Object leftValue = GREATER.computeValue(context);
        Object rightValue = Nodes.newNumberNode("6", Nodes.newNumber("6.0"), Nodes.newNumber("6.0"));
        Object result = GREATER.computeValue(context);

        assertTrue(leftValue instanceof Number);
        assertTrue(rightValue instanceof Number);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testGivenTwoNumericValues_WhenComparingWithGreaterOrEqualOperator_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root"), Nodes.newElementNode("child1", "5"), Nodes.newElementNode("child2", "5")));
        context.setCurrentNode("root");

        Object leftValue = GREATER_EQUAL.computeValue(context);
        Object rightValue = Nodes.newNumberNode("5", Nodes.newNumber("5.0"), Nodes.newNumber("5.0"));
        Object result = GREATER_EQUAL.computeValue(context);

        assertTrue(leftValue instanceof Number);
        assertTrue(rightValue instanceof Number);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testGivenTwoNumericValues_WhenComparingWithLessThanOperator_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root"), Nodes.newElementNode("child1", "5"), Nodes.newElementNode("child2", "7")));
        context.setCurrentNode("root");

        Object leftValue = LESS.computeValue(context);
        Object rightValue = Nodes.newNumberNode("7", Nodes.newNumber("7.0"), Nodes.newNumber("7.0"));
        Object result = LESS.computeValue(context);

        assertTrue(leftValue instanceof Number);
        assertTrue(rightValue instanceof Number);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testGivenTwoNumericValues_WhenComparingWithLessOrEqualOperator_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root"), Nodes.newElementNode("child1", "5"), Nodes.newElementNode("child2", "5")));
        context.setCurrentNode("root");

        Object leftValue = LESS_EQUAL.computeValue(context);
        Object rightValue = Nodes.newNumberNode("5", Nodes.newNumber("5.0"), Nodes.newNumber("5.0"));
        Object result = LESS_EQUAL.computeValue(context);

        assertTrue(leftValue instanceof Number);
        assertTrue(rightValue instanceof Number);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testGivenTwoStrings_WhenComparingWithGreaterThanOperator_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root"), Nodes.newElementNode("child1", "apple"), Nodes.newElementNode("child2", "banana")));
        context.setCurrentNode("root");

        Object leftValue = GREATER.computeValue(context);
        Object rightValue = Nodes.newStringNode("banana");
        Object result = GREATER.computeValue(context);

        assertTrue(leftValue instanceof String);
        assertTrue(rightValue instanceof String);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testGivenTwoStrings_WhenComparingWithGreaterOrEqualOperator_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root"), Nodes.newElementNode("child1", "apple"), Nodes.newElementNode("child2", "banana")));
        context.setCurrentNode("root");

        Object leftValue = GREATER_EQUAL.computeValue(context);
        Object rightValue = Nodes.newStringNode("apple");
        Object result = GREATER_EQUAL.computeValue(context);

        assertTrue(leftValue instanceof String);
        assertTrue(rightValue instanceof String);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testGivenTwoStrings_WhenComparingWithLessThanOperator_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root"), Nodes.newElementNode("child1", "apple"), Nodes.newElementNode("child2", "banana")));
        context.setCurrentNode("root");

        Object leftValue = LESS.computeValue(context);
        Object rightValue = Nodes.newStringNode("banana");
        Object result = LESS.computeValue(context);

        assertTrue(leftValue instanceof String);
        assertTrue(rightValue instanceof String);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testGivenTwoStrings_WhenComparingWithLessOrEqualOperator_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root"), Nodes.newElementNode("child1", "apple"), Nodes.newElementNode("child2", "apple")));
        context.setCurrentNode("root");

        Object leftValue = LESS_EQUAL.computeValue(context);
        Object rightValue = Nodes.newStringNode("apple");
        Object result = LESS_EQUAL.computeValue(context);

        assertTrue(leftValue instanceof String);
        assertTrue(rightValue instanceof String);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testGivenTwoNumbersWithNaN_WhenComparing_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root")));
        context.setCurrentNode("root");

        Object leftValue = LESS.computeValue(context);
        Object rightValue = Nodes.newNumberNode("NaN", Nodes.newNumber("NaN"), Nodes.newNumber("NaN"));
        Object result = LESS.computeValue(context);

        assertTrue(leftValue instanceof Number);
        assertTrue(rightValue instanceof Number);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testGivenTwoCollections_WhenComparingIterators_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root")));
        context.setCurrentNode("root");

        Iterator<Number> leftIterator = Iterators.iterate(1, 2, 3);
        Iterator<Number> rightIterator = Iterators.iterate(1, 2, 4);

        Object leftValue = GREATER.computeValue(context);
        Object rightValue = context.createIterator(Collections.singletonList(rightIterator));
        Object result = GREATER.computeValue(context);

        assertTrue(leftValue instanceof Boolean);
        assertTrue(rightValue instanceof IteratingIterator);
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @org.junit.Test
    public void testGivenTwoCollections_WhenComparingWithSets_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root"), Nodes.newElementNode("child1", "a"), Nodes.newElementNode("child2", "b"), Nodes.newElementNode("child3", "c")));
        context.setCurrentNode("root");

        Iterator<Node> leftIterator = context.getDescendants();
        HashSet<Node> leftSet = new HashSet<>();
        while (leftIterator.hasNext()) {
            leftSet.add(leftIterator.next());
        }
        HashSet<Node> rightSet = new HashSet<>(Arrays.asList(Nodes.newElementNode("d"), Nodes.newElementNode("e"), Nodes.newElementNode("f")));

        Object leftValue = LESS.computeValue(context);
        Object rightValue = context.createIterator(Iterators.iterate(rightSet.iterator()));
        Object result = LESS.computeValue(context);

        assertTrue(leftValue instanceof Boolean);
        assertTrue(rightValue instanceof IteratingIterator);
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @org.junit.Test
    public void testGivenTwoEmptyCollections_WhenComparing_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root")));
        context.setCurrentNode("root");

        Object leftValue = LESS.computeValue(context);
        Object rightValue = Nodes.newDocumentNode(Nodes.newElementNode("document"));
        Object result = LESS.computeValue(context);

        assertTrue(leftValue instanceof Boolean);
        assertTrue(rightValue instanceof Node);
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @org.junit.Test
    public void testGivenTwoIteratorsWithEmptyIterator_WhenComparing_ThenCorrectResult() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root")));
        context.setCurrentNode("root");

        Iterator<Number> leftIterator = Iterators.emptyIterator();
        Iterator<Number> rightIterator = Iterators.emptyIterator();

        Object leftValue = LESS.computeValue(context);
        Object rightValue = context.createIterator(Iterators.iterate(rightIterator));
        Object result = LESS.computeValue(context);

        assertTrue(leftValue instanceof Boolean);
        assertTrue(rightValue instanceof IteratingIterator);
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGivenNullLeftArgument_WhenComparing_ThenExceptionIsThrown() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root")));
        context.setCurrentNode("root");

        Object leftValue = LESS.computeValue(context);
        Object rightValue = Nodes.newNumberNode("2");
        Object result = LESS.computeValue(context);

        assertNull(leftValue);
        assertTrue(rightValue instanceof Number);
        assertNull(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGivenNullRightArgument_WhenComparing_ThenExceptionIsThrown() {
        EvalContext context = new EvalContext();
        context.setCurrentNode(Nodes.newDocumentNode(Nodes.newElementNode("root")));
        context.setCurrentNode("root");

        Object leftValue = Nodes.newNumberNode("2");
        Object rightValue = null;
        Object result = LESS.computeValue(context);

        assertTrue(leftValue instanceof Number);
        assertNull(rightValue);
        assertNull(result);
    }
}