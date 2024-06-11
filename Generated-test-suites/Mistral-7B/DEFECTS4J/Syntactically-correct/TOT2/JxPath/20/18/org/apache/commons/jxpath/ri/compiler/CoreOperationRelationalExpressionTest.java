package org.junit.tests;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.core.SimpleNode;
import org.apache.commons.jxpath.ri.expressions.IteratorExpression;
import org.apache.commons.jxpath.ri.iterators.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    // Basic test case
    @Test
    public void testComputeValue() {
        Expression left = new SimpleNode(1);
        Expression right = new SimpleNode(2);
        CoreOperationRelationalExpression expression = new SpecificCoreOperationRelationalExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    // Edge cases
    @Test
    public void testNullComparison() {
        Expression left = new SimpleNode(null);
        Expression right = new SimpleNode(null);
        CoreOperationRelationalExpression expression = new SpecificCoreOperationRelationalExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result); // null is considered equal to null
    }

    @Test
    public void testComparisonDifferentTypes() {
        Expression left = new SimpleNode(1);
        Expression right = new SimpleNode("1");
        CoreOperationRelationalExpression expression = new SpecificCoreOperationRelationalExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testEmptyCollectionsComparison() {
        Expression left = new SimpleNode(new HashSet<>());
        Expression right = new SimpleNode(new HashSet<>());
        CoreOperationRelationalExpression expression = new SpecificCoreOperationRelationalExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testIteratorsWithDifferentSizes() {
        Iterator it1 = new Iterator() {
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count++ < 2;
            }

            @Override
            public Object next() {
                return count == 0 ? 1 : 2;
            }
        };

        Iterator it2 = new Iterator() {
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count++ < 3;
            }

            @Override
            public Object next() {
                return count == 0 ? 1 : (count == 1 ? 3 : 4);
            }
        };

        Expression left = new IteratorExpression(it1);
        Expression right = new IteratorExpression(it2);
        CoreOperationRelationalExpression expression = new SpecificCoreOperationRelationalExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    // Exception handling
    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        Expression left = null;
        Expression right = new SimpleNode(1);
        CoreOperationRelationalExpression expression = new SpecificCoreOperationRelationalExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext();
        expression.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testDifferentTypes() {
        Expression left = new SimpleNode(1);
        Expression right = new SimpleNode("1");
        CoreOperationRelationalExpression expression = new SpecificCoreOperationRelationalExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext();
        expression.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareResultOutOfRange() {
        Expression left = new SimpleNode(1);
        Expression right = new SimpleNode(2);
        CoreOperationRelationalExpression expression = new SpecificCoreOperationRelationalExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext();
        expression.evaluateCompare(2);
    }
}