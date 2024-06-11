package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqualBoolean() {
        Expression left = new LiteralExpression(true);
        Expression right = new LiteralExpression(true);
        CoreOperationCompare operation = new EqualsOperation(left, right);
        EvalContext context = new EvalContext();
        assertTrue(operation.evaluateToBoolean(context));
    }

    @Test
    public void testEqualNumber() {
        Expression left = new LiteralExpression(1d);
        Expression right = new LiteralExpression(1d);
        CoreOperationCompare operation = new EqualsOperation(left, right);
        EvalContext context = new EvalContext();
        assertTrue(operation.evaluateToBoolean(context));
    }

    @Test
    public void testEqualString() {
        Expression left = new LiteralExpression("test");
        Expression right = new LiteralExpression("test");
        CoreOperationCompare operation = new EqualsOperation(left, right);
        EvalContext context = new EvalContext();
        assertTrue(operation.evaluateToBoolean(context));
    }

    @Test
    public void testEqualObjects() {
        Expression left = new VariableExpression("a");
        Expression right = new VariableExpression("a");
        CoreOperationCompare operation = new EqualsOperation(left, right);
        JXPathContext jxpathContext = new JXPathContext();
        jxpathContext.setVariables(Collections.singletonMap("a", new Pointer(new Object[]{"test"}, 0)));
        assertTrue(operation.evaluateToBoolean(jxpathContext));
    }

    @Test
    public void testUnequalBoolean() {
        Expression left = new LiteralExpression(true);
        Expression right = new LiteralExpression(false);
        CoreOperationCompare operation = new EqualsOperation(left, right);
        EvalContext context = new EvalContext();
        assertFalse(operation.evaluateToBoolean(context));
    }

    @Test
    public void testUnequalNumber() {
        Expression left = new LiteralExpression(1d);
        Expression right = new LiteralExpression(2d);
        CoreOperationCompare operation = new EqualsOperation(left, right);
        EvalContext context = new EvalContext();
        assertFalse(operation.evaluateToBoolean(context));
    }

    @Test
    public void testUnequalString() {
        Expression left = new LiteralExpression("test");
        Expression right = new LiteralExpression("notTest");
        CoreOperationCompare operation = new EqualsOperation(left, right);
        EvalContext context = new EvalContext();
        assertFalse(operation.evaluateToBoolean(context));
    }

    @Test
    public void testUnequalObjects() {
        Expression left = new VariableExpression("a");
        Expression right = new VariableExpression("b");
        CoreOperationCompare operation = new EqualsOperation(left, right);
        JXPathContext jxpathContext = new JXPathContext();
        jxpathContext.setVariables(
            Collections.singletonMap("a", new Pointer(new Object[]{"test"}, 0))
        );
        assertFalse(operation.evaluateToBoolean(jxpathContext));
    }

    @Test
    public void testEqualCollections() {
        Expression left = new LiteralExpression(new ArrayList<>(Arrays.asList(1, 2, 3)));
        Expression right = new LiteralExpression(new ArrayList<>(Arrays.asList(1, 2, 3)));
        CoreOperationCompare operation = new EqualsOperation(left, right);
        EvalContext context = new EvalContext();
        assertTrue(operation.evaluateToBoolean(context));
    }

    @Test
    public void testUnequalCollectionsDifferentSize() {
        Expression left = new LiteralExpression(new ArrayList<>(Arrays.asList(1, 2, 3)));
        Expression right = new LiteralExpression(new ArrayList<>(Arrays.asList(1, 2)));
        CoreOperationCompare operation = new EqualsOperation(left, right);
        EvalContext context = new EvalContext();
        assertFalse(operation.evaluateToBoolean(context));
    }

    @Test
    public void testUnequalCollectionsDifferentElements() {
        Expression left = new LiteralExpression(new ArrayList<>(Arrays.asList(1, 2, 3)));
        Expression right = new LiteralExpression(new ArrayList<>(Arrays.asList(1, 4, 3)));
        CoreOperationCompare operation = new EqualsOperation(left, right);
        EvalContext context = new EvalContext();
        assertFalse(operation.evaluateToBoolean(context));
    }

    @Test
    public void testContainsIterators() {
        Expression left = new VariableExpression("list1");
        Expression right = new LiteralExpression(5);
        CoreOperationCompare operation = new ContainsOperation(left, right);
        JXPathContext jxpathContext = new JXPathContext();
        jxpathContext.setVariables(
            Collections.singletonMap("list1", new IteratorExpression(new LiteralExpression(new ArrayList<>(Arrays.asList(1, 2, 3, 5)))))
        );
        assertTrue(operation.evaluateToBoolean(jxpathContext));
    }

    @Test
    public void testContainsIteratorsDifferentOrder() {
        Expression left = new VariableExpression("list1");
        Expression right = new LiteralExpression(3);
        CoreOperationCompare operation = new ContainsOperation(left, right);
        JXPathContext jxpathContext = new JXPathContext();
        jxpathContext.setVariables(
            Collections.singletonMap("list1", new IteratorExpression(new LiteralExpression(new ArrayList<>(Arrays.asList(3, 2, 1)))))
        );
        assertTrue(operation.evaluateToBoolean(jxpathContext));
    }

    @Test
    public void testContainsIteratorsEmptyList() {
        Expression left = new VariableExpression("list1");
        Expression right = new LiteralExpression(5);
        CoreOperationCompare operation = new ContainsOperation(left, right);
        JXPathContext jxpathContext = new JXPathContext();
        jxpathContext.setVariables(
            Collections.singletonMap("list1", new IteratorExpression(new LiteralExpression(Collections.emptyList())))
        );
        assertFalse(operation.evaluateToBoolean(jxpathContext));
    }

    @Test
    public void testContainsIteratorsNullIterator() {
        Expression left = new VariableExpression("list1");
        Expression right = new LiteralExpression(5);
        CoreOperationCompare operation = new ContainsOperation(left, right);
        JXPathContext jxpathContext = new JXPathContext();
        jxpathContext.setVariables(
            Collections.singletonMap("list1", new IteratorExpression(null))
        );
        assertFalse(operation.evaluateToBoolean(jxpathContext));
    }

    @Test
    public void testContainsIteratorsSameIterator() {
        Expression left = new VariableExpression("list1");
        Expression right = new VariableExpression("list1");
        CoreOperationCompare operation = new ContainsOperation(left, right);
        JXPathContext jxpathContext = new JXPathContext();
        jxpathContext.setVariables(
            Collections.singletonMap("list1", new IteratorExpression(new LiteralExpression(new ArrayList<>(Arrays.asList(1, 2, 3)))))
        );
        assertTrue(operation.evaluateToBoolean(jxpathContext));
    }

    @Test(expected = AssertionError.class)
    public void testErrorIteratorsDifferentTypes() {
        Expression left = new VariableExpression("list1");
        Expression right = new LiteralExpression(new String("5"));
        CoreOperationCompare operation = new ContainsOperation(left, right);
        JXPathContext jxpathContext = new JXPathContext();
        jxpathContext.setVariables(
            Collections.singletonMap("list1", new IteratorExpression(new LiteralExpression(new ArrayList<>(Arrays.asList(1, 2, 3)))))
        );
    }
}