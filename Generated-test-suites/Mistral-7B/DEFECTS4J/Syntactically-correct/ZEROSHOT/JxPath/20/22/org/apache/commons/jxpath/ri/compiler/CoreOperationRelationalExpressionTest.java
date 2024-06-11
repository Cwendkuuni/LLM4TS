package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.core.Expression;
import org.apache.commons.jxpath.ri.iter.CollectionIterator;
import org.apache.commons.jxpath.ri.iter.IteratorIterator;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.apache.commons.jxpath.ri.iter.SimpleIterator;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Expression left = new LiteralExpression("5");
        Expression right = new LiteralExpression("10");
        CoreOperationRelationalExpression expression = new GreaterThanExpression(left, right);
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression left = new LiteralExpression("5");
        Expression right = new LiteralExpression("5");
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(left, right);
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        Expression left = new LiteralExpression("1");
        Expression right = new LiteralExpression("5");
        CoreOperationRelationalExpression expression = new LessThanExpression(left, right);
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        Expression left = new LiteralExpression("1");
        Expression right = new LiteralExpression("5");
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(left, right);
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testDoubleGreaterThan() {
        Expression left = new LiteralExpression(3.14);
        Expression right = new LiteralExpression(2.71828);
        CoreOperationRelationalExpression expression = new GreaterThanExpression(left, right);
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testDoubleGreaterThanOrEqual() {
        Expression left = new LiteralExpression(3.14);
        Expression right = new LiteralExpression(3.14);
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(left, right);
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testDoubleLessThan() {
        Expression left = new LiteralExpression(2.71828);
        Expression right = new LiteralExpression(3.14);
        CoreOperationRelationalExpression expression = new LessThanExpression(left, right);
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testDoubleLessThanOrEqual() {
        Expression left = new LiteralExpression(2.71828);
        Expression right = new LiteralExpression(3.14);
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(left, right);
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanIterators() {
        Expression left = new IteratorExpression(new CollectionIterator(new Iterator[] { new SingletonIterator("A"), new SingletonIterator("B") }));
        Expression right = new IteratorExpression(new SingletonIterator("C"));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(left, right);
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqualIterators() {
        Expression left = new IteratorExpression(new CollectionIterator(new Iterator[] { new SingletonIterator("A"), new SingletonIterator("B") }));
        Expression right = new IteratorExpression(new CollectionIterator(new Iterator[] { new SingletonIterator("A"), new SingletonIterator("B") }));
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(left, right);
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanIterators() {
        Expression left = new IteratorExpression(new CollectionIterator(new Iterator[] { new SingletonIterator("A"), new SingletonIterator("B") }));
        Expression right = new IteratorExpression(new SingletonIterator("C"));
        CoreOperationRelationalExpression expression = new LessThanExpression(left, right);
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualIterators() {
        Expression left = new IteratorExpression(new CollectionIterator(new Iterator[] { new SingletonIterator("A"), new SingletonIterator("B") }));
        Expression right = new IteratorExpression(new SingletonIterator("C"));
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(left, right);
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testSelfContextGreaterThan() {
        Expression left = new SelfContext();
        Expression right = new LiteralExpression("B");
        CoreOperationRelationalExpression expression = new GreaterThanExpression(left, right);
        EvalContext context = new EvalContext();
        context.setCurrentNode(new InitialContext());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testSelfContextGreaterThanOrEqual() {
        Expression left = new SelfContext();
        Expression right = new LiteralExpression("B");
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(left, right);
        EvalContext context = new EvalContext();
        context.setCurrentNode(new InitialContext());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testSelfContextLessThan() {
        Expression left = new SelfContext();
        Expression right = new LiteralExpression("B");
        CoreOperationRelationalExpression expression = new LessThanExpression(left, right);
        EvalContext context = new EvalContext();
        context.setCurrentNode(new InitialContext());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testSelfContextLessThanOrEqual() {
        Expression left = new SelfContext();
        Expression right = new LiteralExpression("B");
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(left, right);
        EvalContext context = new EvalContext();
        context.setCurrentNode(new InitialContext());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testSelfContextGreaterThanSelfContext() {
        Expression left = new SelfContext();
        Expression right = new SelfContext();
        CoreOperationRelationalExpression expression = new GreaterThanExpression(left, right);
        EvalContext context = new EvalContext();
        context.setCurrentNode(new InitialContext());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testSelfContextGreaterThanOrEqualSelfContext() {
        Expression left = new SelfContext();
        Expression right = new SelfContext();
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(left, right);
        EvalContext context = new EvalContext();
        context.setCurrentNode(new InitialContext());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testSelfContextLessThanSelfContext() {
        Expression left = new SelfContext();
        Expression right = new SelfContext();
        CoreOperationRelationalExpression expression = new LessThanExpression(left, right);
        EvalContext context = new EvalContext();
        context.setCurrentNode(new InitialContext());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testSelfContextLessThanOrEqualSelfContext() {
        Expression left = new SelfContext();
        Expression right = new SelfContext();
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(left, right);
        EvalContext context = new EvalContext();
        context.setCurrentNode(new InitialContext());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}