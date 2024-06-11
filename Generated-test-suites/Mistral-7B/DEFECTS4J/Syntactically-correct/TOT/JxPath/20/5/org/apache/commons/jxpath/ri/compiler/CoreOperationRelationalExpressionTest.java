package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ExpConstantNode(5.0), new ExpConstantNode(3.0));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(node.getFirstChild()).thenReturn(Mockito.mock(Node.class));
        Mockito.when(node.getFirstChild().getNextSibling()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator = Collections.singletonList(node).iterator();
            iterator.next();
            return iterator;
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanWithNaN() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ExpConstantNode(Double.NaN), new ExpConstantNode(3.0));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator = Collections.singletonList(node).iterator();
            iterator.next();
            return iterator;
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node);

        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanEqual() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new ExpConstantNode(5.0), new ExpConstantNode(3.0));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator = Collections.singletonList(node).iterator();
            iterator.next();
            return iterator;
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanEqualWithNaN() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new ExpConstantNode(Double.NaN), new ExpConstantNode(3.0));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator = Collections.singletonList(node).iterator();
            iterator.next();
            return iterator;
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new ExpConstantNode(3.0), new ExpConstantNode(5.0));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator = Collections.singletonList(node).iterator();
            iterator.next();
            return iterator;
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanWithNaN() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new ExpConstantNode(Double.NaN), new ExpConstantNode(3.0));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator = Collections.singletonList(node).iterator();
            iterator.next();
            return iterator;
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node);

        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new ExpConstantNode(3.0), new ExpConstantNode(5.0));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator = Collections.singletonList(node).iterator();
            iterator.next();
            return iterator;
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithNaN() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new ExpConstantNode(Double.NaN), new ExpConstantNode(3.0));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator = Collections.singletonList(node).iterator();
            iterator.next();
            return iterator;
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testContains() {
        CoreOperationRelationalExpression expression = new ContainsExpression(new ExpConstantNode("apple"), new ExpConstantNode("fruit basket"));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator = InfoSetUtil.createIterable("apple", "orange", "banana", "apple", "apple").iterator();
            return iterator;
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testContainsWithNull() {
        CoreOperationRelationalExpression expression = new ContainsExpression(new ExpConstantNode("apple"), new ExpConstantNode(null));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator = InfoSetUtil.createIterable("apple", "orange", "banana", "apple", "apple").iterator();
            return iterator;
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node);

        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testContainsWithEmptyString() {
        CoreOperationRelationalExpression expression = new ContainsExpression(new ExpConstantNode("apple"), new ExpConstantNode(""));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator = InfoSetUtil.createIterable("apple", "orange", "banana", "apple", "apple").iterator();
            return iterator;
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node);

        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testIntersection() {
        CoreOperationRelationalExpression expression = new ContainsAllExpression(new ExpConstantNode("apple"), new ExpConstantNode("fruit basket"));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node1);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator1 = InfoSetUtil.createIterable("apple", "orange", "banana").iterator();
            Iterator iterator2 = InfoSetUtil.createIterable("apple", "pear", "grape").iterator();
            return new IntersectionIterator(iterator1, iterator2);
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node2);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testIntersectionEmpty() {
        CoreOperationRelationalExpression expression = new ContainsAllExpression(new ExpConstantNode("apple"), new ExpConstantNode("car"));

        EvalContext context = Mockito.mock(EvalContext.class);
        Node node1 = Mockito.mock(Node.class);
        Node node2 = Mockito.mock(Node.class);
        Mockito.when(context.getCurrentNode()).thenReturn(node1);
        Mockito.when(context.getDocument()).thenReturn(null);

        Mockito.when(context.getIteratorFromNode(ArgumentMatchers.anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            Iterator iterator1 = InfoSetUtil.createIterable("apple", "orange", "banana").iterator();
            Iterator iterator2 = InfoSetUtil.createIterable("car", "truck", "bus").iterator();
            return new IntersectionIterator(iterator1, iterator2);
        });

        Mockito.when(context.getSingleNodePointer()).thenReturn(node2);

        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }
}