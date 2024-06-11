package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IterateIterable;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.iterators.FilterIterator;
import org.apache.commons.jxpath.ri.iterators.NodeIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.apache.commons.jxpath.ri.iterators.FilterIterator.FILTER_EQUALS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoreOperationRelationalExpressionTest {

    @Test
    void testGt() {
        Node node1 = createNode(5);
        Node node2 = createNode(10);
        Node node3 = createNode(3);

        CoreOperationRelationalExpression gt = new GreaterThanExpression(createVariableExpression("x"), createVariableExpression("y"));
        EvalContext context = createContext();
        context.setVariable("x", node1);
        context.setVariable("y", node2);

        Object result = gt.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setVariable("x", node2);
        context.setVariable("y", node3);
        result = gt.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    void testGte() {
        Node node1 = createNode(5);
        Node node2 = createNode(10);
        Node node3 = createNode(5);
        Node node4 = createNode(3);

        CoreOperationRelationalExpression gte = new GreaterThanOrEqualExpression(createVariableExpression("x"), createVariableExpression("y"));

        EvalContext context = createContext();
        context.setVariable("x", node1);
        context.setVariable("y", node2);

        Object result = gte.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        context.setVariable("x", node3);
        context.setVariable("y", node3);

        result = gte.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setVariable("x", node3);
        context.setVariable("y", node4);

        result = gte.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    void testLt() {
        Node node1 = createNode(5);
        Node node2 = createNode(10);
        Node node3 = createNode(3);

        CoreOperationRelationalExpression lt = new LessThanExpression(createVariableExpression("x"), createVariableExpression("y"));

        EvalContext context = createContext();
        context.setVariable("x", node1);
        context.setVariable("y", node2);

        Object result = lt.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context.setVariable("x", node2);
        context.setVariable("y", node3);

        result = lt.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    void testLte() {
        Node node1 = createNode(5);
        Node node2 = createNode(10);
        Node node3 = createNode(5);
        Node node4 = createNode(3);

        CoreOperationRelationalExpression lte = new LessThanOrEqualExpression(createVariableExpression("x"), createVariableExpression("y"));

        EvalContext context = createContext();
        context.setVariable("x", node1);
        context.setVariable("y", node2);

        Object result = lte.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        context.setVariable("x", node3);
        context.setVariable("y", node3);

        result = lte.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setVariable("x", node3);
        context.setVariable("y", node4);

        result = lte.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    void testContains() {
        List list = new ArrayList();
        list.add(createNode(1));
        list.add(createNode(2));
        list.add(createNode(3));

        CoreOperationRelationalExpression contains = new ContainsExpression(createVariableExpression("x"), createVariableExpression("y"));

        EvalContext context = createContext();
        context.setVariable("x", new IterateIterable(Iterators.iter(list)));
        context.setVariable("y", createNode(2));

        Object result = contains.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setVariable("y", createNode(4));

        result = contains.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    void testIteratorsMatch() {
        List list1 = new ArrayList();
        list1.add(createNode(1));
        list1.add(createNode(2));
        list1.add(createNode(3));

        Iterator iterator1 = list1.iterator();

        List list2 = new ArrayList();
        list2.add(createNode(1));
        list2.add(createNode(2));
        list2.add(createNode(3));

        Iterator iterator2 = list2.iterator();

        CoreOperationRelationalExpression match = new MatchExpression(iterator1, iterator2);

        EvalContext context = createContext();
        context.setVariable("it1", iterator1);
        context.setVariable("it2", iterator2);

        Object result = match.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        result = match.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    void testIteratorsMismatch() {
        List list1 = new ArrayList();
        list1.add(createNode(1));
        list1.add(createNode(2));
        list1.add(createNode(3));

        Iterator iterator1 = list1.iterator();

        List list2 = new ArrayList();
        list2.add(createNode(1));
        list2.add(createNode(3));
        list2.add(createNode(4));

        Iterator iterator2 = list2.iterator();

        CoreOperationRelationalExpression match = new MatchExpression(iterator1, iterator2);

        EvalContext context = createContext();
        context.setVariable("it1", iterator1);
        context.setVariable("it2", iterator2);

        Object result = match.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    void testNullLeft() {
        CoreOperationRelationalExpression gt = new GreaterThanExpression(createConstantExpression(null), createVariableExpression("y"));

        EvalContext context = createContext();

        Object result = gt.computeValue(context);
        assertNull(result);
    }

    @Test
    void testNullRight() {
        CoreOperationRelationalExpression gt = new GreaterThanExpression(createVariableExpression("x"), createConstantExpression(null));

        EvalContext context = createContext();
        context.setVariable("x", createNode(5));

        Object result = gt.computeValue(context);
        assertNull(result);
    }

    @Test
    void testDoubleComparison() {
        double value1 = 5.0;
        double value2 = 10.0;
        double value3 = 3.0;

        CoreOperationRelationalExpression gt = new GreaterThanExpression(createConstantExpression(value1), createConstantExpression(value2));

        EvalContext context = createContext();

        Object result = gt.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        context.setVariable("x", createConstantExpression(value1));
        context.setVariable("y", createConstantExpression(value3));

        result = gt.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    private CoreExpression createConstantExpression(Object value) {
        return new ConstantExpression(value);
    }

    private CoreExpression createVariableExpression(String name) {
        return new VariableExpression(name);
    }

    private CoreExpression createVariableExpression(Iterator iterator) {
        return new IteratorExpression(iterator);
    }

    private CoreExpression createGreaterThanExpression() {
        return new GreaterThanExpression(createVariableExpression("x"), createVariableExpression("y"));
    }

    private CoreExpression createGreaterThanOrEqualExpression() {
        return new GreaterThanOrEqualExpression(createVariableExpression("x"), createVariableExpression("y"));
    }

    private CoreExpression createLessThanExpression() {
        return new LessThanExpression(createVariableExpression("x"), createVariableExpression("y"));
    }

    private CoreExpression createLessThanOrEqualExpression() {
        return new LessThanOrEqualExpression(createVariableExpression("x"), createVariableExpression("y"));
    }

    private CoreExpression createContainsExpression() {
        return new ContainsExpression(createVariableExpression("x"), createVariableExpression("y"));
    }

    private CoreExpression createMatchExpression() {
        return new MatchExpression(createVariableExpression("it1"), createVariableExpression("it2"));
    }

    private EvalContext createContext() {
        return new EvalContext(new InfoSet() {
            @Override
            public Node getSingleNodePointer() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return Iterators.emptyIterator();
            }

            @Override
            public Collection getCollection(int size) {
                return Collections.emptyList();
            }

            @Override
            public void setVariable(String name, Object value) {
            }
        });
    }

    private Node createNode(int value) {
        Map attributes = new HashMap();
        attributes.put("value", value);
        return new NodeInfo(new Node(null, null, null, null, new NodeInfo[]{new NodeInfo(new NodeInfo("name", "node", null, null))}, attributes));
    }
}