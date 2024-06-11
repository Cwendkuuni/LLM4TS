package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.IteratorUtil;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.apache.commons.jxpath.ri.nodes.NodeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(5), new CoreConstantExpression(4));
        EvalContext context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(1));
        Object result = expression.computeValue(context);
        Assertions.assertTrue(result instanceof Boolean);
        Assertions.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanWithSelfContext() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(5), new CoreSelfExpression());
        EvalContext context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(5));
        Object result = expression.computeValue(context);
        Assertions.assertTrue(result instanceof Boolean);
        Assertions.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanWithInitialContext() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(5), new CoreInitialContextExpression());
        EvalContext context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(1));
        context.setCurrentNodePointer(new NodeInfo(5));
        Object result = expression.computeValue(context);
        Assertions.assertTrue(result instanceof Boolean);
        Assertions.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanWithDoubleValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(5.5), new CoreConstantExpression(4.5));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        Assertions.assertTrue(result instanceof Boolean);
        Assertions.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanWithNaN() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(Double.NaN), new CoreConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        Assertions.assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanWithIterators() {
        List<Object> list1 = Arrays.asList(1, 2, 3, 4);
        List<Object> list2 = Arrays.asList(1, 3, 5, 6);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreListExpression(list1.iterator()), new CoreListExpression(list2.iterator()));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        Assertions.assertTrue(result instanceof Boolean);
        Assertions.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanWithSingletonIterators() {
        Object value1 = new Integer(5);
        Object value2 = new Integer(4);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreSingletonIteratorExpression(new SingletonIterator(value1)), new CoreSingletonIteratorExpression(new SingletonIterator(value2)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        Assertions.assertTrue(result instanceof Boolean);
        Assertions.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanWithCollections() {
        Collection collection1 = new ArrayList();
        collection1.add(new Integer(5));
        Collection collection2 = new ArrayList();
        collection2.add(new Integer(4));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreCollectionExpression(collection1.iterator()), new CoreCollectionExpression(collection2.iterator()));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        Assertions.assertTrue(result instanceof Boolean);
        Assertions.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(5), new CoreConstantExpression(4));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        Assertions.assertTrue(result instanceof Boolean);
        Assertions.assertEquals(Boolean.TRUE, result);
    }

    // Add other test methods for the remaining methods in CoreOperationRelationalExpression class

    @Test
    public void testLessThan() {
        // ...
    }

    @Test
    public void testLessThanOrEqual() {
        // ...
    }
}