package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateContext;
import org.apache.commons.jxpath.ri.NodeContext;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.axes.IteratorIterable;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Tests for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreGreaterThanExpression(new CoreConstantExpression(5));
        Object result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        Expression left = new CoreConstantExpression(3);
        Expression right = new CoreConstantExpression(5);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new CoreConstantExpression(5);
        right = new CoreConstantExpression(5);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new CoreConstantExpression(5.1);
        right = new CoreConstantExpression(5);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new CoreConstantExpression(5);
        right = new CoreConstantExpression(5.1);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new CoreConstantExpression(1.2);
        right = new CoreConstantExpression(1.1);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CoreConstantExpression(1.2);
        right = new CoreConstantExpression(1.2);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreGreaterThanOrEqualExpression(new CoreConstantExpression(5));
        Object result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        Expression left = new CoreConstantExpression(3);
        Expression right = new CoreConstantExpression(5);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new CoreConstantExpression(5);
        right = new CoreConstantExpression(5);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CoreConstantExpression(5.1);
        right = new CoreConstantExpression(5);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CoreConstantExpression(5);
        right = new CoreConstantExpression(5.1);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CoreConstantExpression(1.2);
        right = new CoreConstantExpression(1.1);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CoreConstantExpression(1.2);
        right = new CoreConstantExpression(1.2);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new CoreLessThanExpression(new CoreConstantExpression(5));
        Object result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        Expression left = new CoreConstantExpression(5);
        Expression right = new CoreConstantExpression(3);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CoreConstantExpression(5);
        right = new CoreConstantExpression(5);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new CoreConstantExpression(5.1);
        right = new CoreConstantExpression(5);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new CoreConstantExpression(5);
        right = new CoreConstantExpression(5.1);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new CoreConstantExpression(1.2);
        right = new CoreConstantExpression(1.1);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CoreConstantExpression(1.2);
        right = new CoreConstantExpression(1.2);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreLessThanOrEqualExpression(new CoreConstantExpression(5));
        Object result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        Expression left = new CoreConstantExpression(3);
        Expression right = new CoreConstantExpression(5);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CoreConstantExpression(5);
        right = new CoreConstantExpression(5);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CoreConstantExpression(5.1);
        right = new CoreConstantExpression(5);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CoreConstantExpression(5);
        right = new CoreConstantExpression(5.1);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CoreConstantExpression(1.2);
        right = new CoreConstantExpression(1.1);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new CoreConstantExpression(1.2);
        right = new CoreConstantExpression(1.2);
        expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = expression.computeValue(new EvalContext(new NodeContext(new Object())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComparisonWithIterators() {
        CoreOperationRelationalExpression expression = new CoreGreaterThanExpression(new CoreConstantExpression(5));

        Collection numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Iterator it = Iterators.iterate(numbers.iterator());
        Expression left = new CoreVariableExpression("x");
        Expression right = new CoreConstantExpression(5);
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});

        EvalContext context = new EvalContext(new IterateContext(it));
        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        while (it.hasNext()) {
            Object next = it.next();
            context = new EvalContext(new IterateContext(it));
            left = new CoreVariableExpression("x");
            right = new CoreConstantExpression(next);
            op = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
            result = op.computeValue(context);
            assertTrue(result instanceof Boolean);
            assertTrue((Boolean) result);
        }

        context = new EvalContext(new IterateContext(it));
        left = new CoreVariableExpression("x");
        right = new CoreConstantExpression(6);
        op = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComparisonWithEmptyIterators() {
        CoreOperationRelationalExpression expression = new CoreGreaterThanExpression(new CoreConstantExpression(5));

        Iterator leftIter = Iterators.emptyIterator();
        Iterator rightIter = Iterators.emptyIterator();

        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{
                new CoreVariableExpression("left"),
                new CoreVariableExpression("right")
        });

        Expression left = new CoreVariableExpression("left");
        Expression right = new CoreVariableExpression("right");

        EvalContext context = new EvalContext(new IterateContext(leftIter));
        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = new EvalContext(new IterateContext(rightIter));
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = new EvalContext(new IterateContext(leftIter));
        left = new CoreVariableExpression("left");
        right = new CoreConstantExpression(5);
        op = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        context = new EvalContext(new IterateContext(rightIter));
        left = new CoreVariableExpression("left");
        right = new CoreConstantExpression(5);
        op = new CoreOperationRelationalExpression(new CoreOperationRelationalExpression[]{left, right});
        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
    }
}