package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Expression left = new NumberValueExpression(1);
        Expression right = new NumberValueExpression(2);
        CoreOperationRelationalExpression op = new GreaterThanExpression(left, right);
        EvaluationContext context = new EvaluationContext(new Node(new Object()));
        Object result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        left = new NumberValueExpression(2);
        right = new NumberValueExpression(1);
        op = new GreaterThanExpression(left, right);
        context = new EvaluationContext(new Node(new Object()));
        result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression left = new NumberValueExpression(1);
        Expression right = new NumberValueExpression(1);
        CoreOperationRelationalExpression op = new GreaterThanOrEqualExpression(left, right);
        EvaluationContext context = new EvaluationContext(new Node(new Object()));
        Object result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        left = new NumberValueExpression(2);
        right = new NumberValueExpression(1);
        op = new GreaterThanOrEqualExpression(left, right);
        context = new EvaluationContext(new Node(new Object()));
        result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        left = new NumberValueExpression(2);
        right = new NumberValueExpression(2);
        op = new GreaterThanOrEqualExpression(left, right);
        context = new EvaluationContext(new Node(new Object()));
        result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThan() {
        Expression left = new NumberValueExpression(1);
        Expression right = new NumberValueExpression(2);
        CoreOperationRelationalExpression op = new LessThanExpression(left, right);
        EvaluationContext context = new EvaluationContext(new Node(new Object()));
        Object result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        left = new NumberValueExpression(2);
        right = new NumberValueExpression(1);
        op = new LessThanExpression(left, right);
        context = new EvaluationContext(new Node(new Object()));
        result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanOrEqual() {
        Expression left = new NumberValueExpression(1);
        Expression right = new NumberValueExpression(1);
        CoreOperationRelationalExpression op = new LessThanOrEqualExpression(left, right);
        EvaluationContext context = new EvaluationContext(new Node(new Object()));
        Object result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        left = new NumberValueExpression(2);
        right = new NumberValueExpression(1);
        op = new LessThanOrEqualExpression(left, right);
        context = new EvaluationContext(new Node(new Object()));
        result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        left = new NumberValueExpression(2);
        right = new NumberValueExpression(2);
        op = new LessThanOrEqualExpression(left, right);
        context = new EvaluationContext(new Node(new Object()));
        result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testContain() {
        Expression left = new NumberValueExpression(1);
        Expression right = new NumberValueExpression(new Integer[]{1, 2, 3});
        CoreOperationRelationalExpression op = new ContainExpression(left, right);
        EvaluationContext context = new EvaluationContext(new Node(new Object()));
        Object result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        left = new NumberValueExpression(2);
        right = new NumberValueExpression(new Integer[]{1, 2, 3});
        op = new ContainExpression(left, right);
        context = new EvaluationContext(new Node(new Object()));
        result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        left = new NumberValueExpression(new Integer[]{1, 2, 3});
        right = new NumberValueExpression(new Integer[]{1, 2, 3});
        op = new ContainExpression(left, right);
        context = new EvaluationContext(new Node(new Object()));
        result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        left = new NumberValueExpression(new Integer[]{1, 2, 3});
        right = new NumberValueExpression(new Integer[]{4, 5, 6});
        op = new ContainExpression(left, right);
        context = new EvaluationContext(new Node(new Object()));
        result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testIterators() {
        Expression left = new Iterate(new Iterators.EmptyIterator());
        Expression right = new Iterate(new Iterators.EmptyIterator());
        CoreOperationRelationalExpression op = new ContainExpression(left, right);
        EvaluationContext context = new EvaluationContext(new Node(new Object()));
        Object result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        left = new Iterate(new Iterators.IntegerIterator(1));
        right = new Iterate(new Iterators.IntegerIterator(1, 3));
        op = new ContainExpression(left, right);
        context = new EvaluationContext(new Node(new Object()));
        result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        left = new Iterate(new Iterators.IntegerIterator(1));
        right = new Iterate(new Iterators.IntegerIterator(3, 1));
        op = new ContainExpression(left, right);
        context = new EvaluationContext(new Node(new Object()));
        result = op.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }
}