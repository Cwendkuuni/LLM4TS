package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateIterable;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.NodeContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    private NodeContext nodeContext;
    private EvalContext evalContext;

    @Before
    public void setUp() {
        nodeContext = new NodeContext();
        evalContext = new EvalContext(nodeContext);
    }

    @Test
    public void testGt() {
        Expression left = new NumberLiteralExpression(1.0, evalContext);
        Expression right = new NumberLiteralExpression(2.0, evalContext);
        CoreOperationRelationalExpression expression = new GtExpression(left, right);

        assertTrue(expression.computeValue(evalContext));
    }

    @Test
    public void testLt() {
        Expression left = new NumberLiteralExpression(1.0, evalContext);
        Expression right = new NumberLiteralExpression(2.0, evalContext);
        CoreOperationRelationalExpression expression = new LtExpression(left, right);

        assertFalse(expression.computeValue(evalContext));
    }

    @Test
    public void testGte() {
        Expression left = new NumberLiteralExpression(1.0, evalContext);
        Expression right = new NumberLiteralExpression(2.0, evalContext);
        CoreOperationRelationalExpression expression = new GteExpression(left, right);

        assertFalse(expression.computeValue(evalContext));
    }

    @Test
    public void testLte() {
        Expression left = new NumberLiteralExpression(1.0, evalContext);
        Expression right = new NumberLiteralExpression(2.0, evalContext);
        CoreOperationRelationalExpression expression = new LteExpression(left, right);

        assertTrue(expression.computeValue(evalContext));
    }

    @Test
    public void testGtWithIterators() {
        Iterator leftIter = Iterators.emptyIterator();
        Iterator rightIter = Iterators.singletonIterator(new NumberLiteralExpression(2.0, evalContext));
        CoreOperationRelationalExpression expression = new GtExpression(
                new IteratorToExpression(leftIter), new IteratorToExpression(rightIter));

        assertFalse(expression.computeValue(evalContext));
    }

    @Test
    public void testLtWithIterators() {
        Iterator leftIter = Iterators.singletonIterator(new NumberLiteralExpression(1.0, evalContext));
        Iterator rightIter = Iterators.singletonIterator(new NumberLiteralExpression(2.0, evalContext));
        CoreOperationRelationalExpression expression = new LtExpression(
                new IteratorToExpression(leftIter), new IteratorToExpression(rightIter));

        assertFalse(expression.computeValue(evalContext));
    }

    @Test
    public void testGteWithIterators() {
        Iterator leftIter = Iterators.singletonIterator(new NumberLiteralExpression(1.0, evalContext));
        Iterator rightIter = Iterators.singletonIterator(new NumberLiteralExpression(2.0, evalContext));
        CoreOperationRelationalExpression expression = new GteExpression(
                new IteratorToExpression(leftIter), new IteratorToExpression(rightIter));

        assertFalse(expression.computeValue(evalContext));
    }

    @Test
    public void testLteWithIterators() {
        Iterator leftIter = Iterators.singletonIterator(new NumberLiteralExpression(1.0, evalContext));
        Iterator rightIter = Iterators.singletonIterator(new NumberLiteralExpression(2.0, evalContext));
        CoreOperationRelationalExpression expression = new LteExpression(
                new IteratorToExpression(leftIter), new IteratorToExpression(rightIter));

        assertTrue(expression.computeValue(evalContext));
    }

    @Test
    public void testEqualsWithIterators() {
        Iterator leftIter = Iterators.singletonIterator(new NumberLiteralExpression(1.0, evalContext));
        Iterator rightIter = Iterators.singletonIterator(new NumberLiteralExpression(1.0, evalContext));
        CoreOperationRelationalExpression expression = new EqualsExpression(
                new IteratorToExpression(leftIter), new IteratorToExpression(rightIter));

        assertTrue(expression.computeValue(evalContext));
    }

    @Test
    public void testNotEqualsWithIterators() {
        Iterator leftIter = Iterators.singletonIterator(new NumberLiteralExpression(1.0, evalContext));
        Iterator rightIter = Iterators.singletonIterator(new NumberLiteralExpression(2.0, evalContext));
        CoreOperationRelationalExpression expression = new NotEqualsExpression(
                new IteratorToExpression(leftIter), new IteratorToExpression(rightIter));

        assertFalse(expression.computeValue(evalContext));
    }

    @Test
    public void testGtWithNulls() {
        Expression left = new NumberLiteralExpression(1.0, evalContext);
        Expression right = new NumberLiteralExpression(null, evalContext);
        CoreOperationRelationalExpression expression = new GtExpression(left, right);

        assertNull(expression.computeValue(evalContext));
    }

    @Test
    public void testLtWithNulls() {
        Expression left = new NumberLiteralExpression(null, evalContext);
        Expression right = new NumberLiteralExpression(2.0, evalContext);
        CoreOperationRelationalExpression expression = new LtExpression(left, right);

        assertNull(expression.computeValue(evalContext));
    }

    @Test
    public void testGteWithNulls() {
        Expression left = new NumberLiteralExpression(null, evalContext);
        Expression right = new NumberLiteralExpression(2.0, evalContext);
        CoreOperationRelationalExpression expression = new GteExpression(left, right);

        assertFalse(expression.computeValue(evalContext));
    }

    @Test
    public void testLteWithNulls() {
        Expression left = new NumberLiteralExpression(1.0, evalContext);
        Expression right = new NumberLiteralExpression(null, evalContext);
        CoreOperationRelationalExpression expression = new LteExpression(left, right);

        assertFalse(expression.computeValue(evalContext));
    }

    @Test
    public void testEqualsWithNulls() {
        Expression left = new NumberLiteralExpression(null, evalContext);
        Expression right = new NumberLiteralExpression(null, evalContext);
        CoreOperationRelationalExpression expression = new EqualsExpression(left, right);

        assertTrue(expression.computeValue(evalContext));
    }

    @Test
    public void testNotEqualsWithNulls() {
        Expression left = new NumberLiteralExpression(null, evalContext);
        Expression right = new NumberLiteralExpression(1.0, evalContext);
        CoreOperationRelationalExpression expression = new NotEqualsExpression(left, right);

        assertTrue(expression.computeValue(evalContext));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComparison() {
        Expression left = new NumberLiteralExpression(1.0, evalContext);
        Expression right = null;
        CoreOperationRelationalExpression expression = new LtExpression(left, right);
    }
}