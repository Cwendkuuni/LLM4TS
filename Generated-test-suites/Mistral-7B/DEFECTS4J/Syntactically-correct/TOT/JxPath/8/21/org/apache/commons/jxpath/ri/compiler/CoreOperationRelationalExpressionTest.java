package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionArgs;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTestBase;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateIterable;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.IteratorsTestHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest extends CoreOperationTestBase {

    @Test
    public void testGreaterThan() {
        final CoreOperationRelationalExpressionArgs args = new CoreOperationRelationalExpressionArgs(
                new Expression[] { new ConstExpression(1), new ConstExpression(2) });
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(args);
        EqualEvalContext context = new EqualEvalContext();
        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1, 2, 3, 1, 2, 3 }));
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1, 2, 3, 1, 2, 3, 3 }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1.1, 2.1, 3.1, 1.1, 2.1, 3.1 }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1.1, 2.1, 3.1, 1.1, 2.1, 3.1, 3.1 }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { Iterators.EMPTY_ITERATOR }));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { new ConstExpression(-1) }));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqual() {
        final CoreOperationRelationalExpressionArgs args = new CoreOperationRelationalExpressionArgs(
                new Expression[] { new ConstExpression(1), new ConstExpression(2) });
        CoreOperationRelationalExpression op = new CoreOperationGreaterThanOrEqual(args);
        EqualEvalContext context = new EqualEvalContext();
        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1, 2, 3, 1, 2, 3 }));
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1, 2, 3, 1, 2, 3, 3 }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1.1, 2.1, 3.1, 1.1, 2.1, 3.1 }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1.1, 2.1, 3.1, 1.1, 2.1, 3.1, 3.1 }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { Iterators.EMPTY_ITERATOR }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { new ConstExpression(-1) }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        final CoreOperationRelationalExpressionArgs args = new CoreOperationRelationalExpressionArgs(
                new Expression[] { new ConstExpression(1), new ConstExpression(2) });
        CoreOperationRelationalExpression op = new CoreOperationLessThan(args);
        EqualEvalContext context = new EqualEvalContext();
        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1, 2, 3, 1, 2, 3 }));
        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1, 2, 3, 1, 2, 3, 3 }));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1.1, 2.1, 3.1, 1.1, 2.1, 3.1 }));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1.1, 2.1, 3.1, 1.1, 2.1, 3.1, 3.1 }));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { Iterators.EMPTY_ITERATOR }));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { new ConstExpression(-1) }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        final CoreOperationRelationalExpressionArgs args = new CoreOperationRelationalExpressionArgs(
                new Expression[] { new ConstExpression(1), new ConstExpression(2) });
        CoreOperationRelationalExpression op = new CoreOperationLessThanOrEqual(args);
        EqualEvalContext context = new EqualEvalContext();
        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1, 2, 3, 1, 2, 3 }));
        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1, 2, 3, 1, 2, 3, 3 }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1.1, 2.1, 3.1, 1.1, 2.1, 3.1 }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(
                new Object[] { 1.1, 2.1, 3.1, 1.1, 2.1, 3.1, 3.1 }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { Iterators.EMPTY_ITERATOR }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { new ConstExpression(-1) }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testIteratorsComparison() {
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArgs(
                new Expression[] { new VariableExpression("x"), new VariableExpression("y") }));

        Collection<Object> nodes = new ArrayList<>();
        nodes.add(new ConstExpression(1));
        nodes.add(new ConstExpression(2));
        nodes.add(new ConstExpression(3));

        IterateIterable xIter = Iterators.forCollection(nodes);
        IterateIterable yIter = Iterators.forCollection(nodes);

        EqualEvalContext context = new EqualEvalContext();
        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { xIter, yIter }));

        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        nodes.remove(1);
        xIter = Iterators.forCollection(nodes);

        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { xIter, yIter }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testIteratorsComparisonWithSameIterator() {
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArgs(
                new Expression[] { new VariableExpression("x"), new VariableExpression("x") }));

        Collection<Object> nodes = new ArrayList<>();
        nodes.add(new ConstExpression(1));
        nodes.add(new ConstExpression(2));
        nodes.add(new ConstExpression(3));

        IterateIterable xIter = Iterators.forCollection(nodes);

        EqualEvalContext context = new EqualEvalContext();
        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { xIter, xIter }));

        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testIteratorsComparisonWithDifferentIterators() {
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new CoreOperationRelationalExpressionArgs(
                new Expression[] { new VariableExpression("x"), new VariableExpression("y") }));

        Collection<Object> xNodes = new ArrayList<>();
        xNodes.add(new ConstExpression(1));
        xNodes.add(new ConstExpression(2));
        xNodes.add(new ConstExpression(3));

        IterateIterable xIter = Iterators.forCollection(xNodes);

        Collection<Object> yNodes = new ArrayList<>();
        yNodes.add(new ConstExpression(2));
        yNodes.add(new ConstExpression(3));
        yNodes.add(new ConstExpression(4));

        IterateIterable yIter = Iterators.forCollection(yNodes);

        EqualEvalContext context = new EqualEvalContext();
        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { xIter, yIter }));

        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        xNodes.remove(1);
        xIter = Iterators.forCollection(xNodes);

        context.setNodes(IteratorsTestHelper.createIterable(new Object[] { xIter, yIter }));
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
    }
}