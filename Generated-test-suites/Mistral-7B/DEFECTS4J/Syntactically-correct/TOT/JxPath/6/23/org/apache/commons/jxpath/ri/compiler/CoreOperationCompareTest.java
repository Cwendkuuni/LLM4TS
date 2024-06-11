package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare_BooleanExpression() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                Mockito.mock(Expression.class),
                Mockito.mock(Expression.class)
        );
        EvaluationContext context = Mockito.mock(EvaluationContext.class);
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        Mockito.when(left.compute(context)).thenReturn(Boolean.TRUE);
        Mockito.when(right.compute(context)).thenReturn(Boolean.TRUE);

        assertTrue(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.TRUE);

        Mockito.when(left.compute(context)).thenReturn(Boolean.FALSE);
        Mockito.when(right.compute(context)).thenReturn(Boolean.TRUE);

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(left.compute(context)).thenReturn(Boolean.TRUE);
        Mockito.when(right.compute(context)).thenReturn(Boolean.FALSE);

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);
    }

    @Test
    public void testCoreOperationCompare_NumberExpression() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                Mockito.mock(Expression.class),
                Mockito.mock(Expression.class)
        );
        EvaluationContext context = Mockito.mock(EvaluationContext.class);
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        Mockito.when(left.compute(context)).thenReturn(Double.valueOf(1.0));
        Mockito.when(right.compute(context)).thenReturn(Double.valueOf(1.0));

        assertTrue(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.TRUE);

        Mockito.when(left.compute(context)).thenReturn(Double.valueOf(1.0));
        Mockito.when(right.compute(context)).thenReturn(Double.valueOf(2.0));

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(left.compute(context)).thenReturn(Double.valueOf(2.0));
        Mockito.when(right.compute(context)).thenReturn(Double.valueOf(1.0));

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(left.compute(context)).thenReturn(Double.valueOf(null));
        Mockito.when(right.compute(context)).thenReturn(Double.valueOf(1.0));

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(left.compute(context)).thenReturn(Double.valueOf(1.0));
        Mockito.when(right.compute(context)).thenReturn(Double.valueOf(null));

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);
    }

    @Test
    public void testCoreOperationCompare_StringExpression() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                Mockito.mock(Expression.class),
                Mockito.mock(Expression.class)
        );
        EvaluationContext context = Mockito.mock(EvaluationContext.class);
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        Mockito.when(left.compute(context)).thenReturn("hello");
        Mockito.when(right.compute(context)).thenReturn("hello");

        assertTrue(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.TRUE);

        Mockito.when(left.compute(context)).thenReturn("hello");
        Mockito.when(right.compute(context)).thenReturn("world");

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(left.compute(context)).thenReturn("hello");
        Mockito.when(right.compute(context)).thenReturn("hello world");

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(left.compute(context)).thenReturn("null");
        Mockito.when(right.compute(context)).thenReturn("hello");

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(left.compute(context)).thenReturn("hello");
        Mockito.when(right.compute(context)).thenReturn("null");

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);
    }

    @Test
    public void testCoreOperationCompare_CollectionExpression() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                Mockito.mock(Expression.class),
                Mockito.mock(Expression.class)
        );
        EvaluationContext context = Mockito.mock(EvaluationContext.class);
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        Mockito.when(left.compute(context)).thenReturn(createList(1, 2, 3));
        Mockito.when(right.compute(context)).thenReturn(createList(1, 2, 3));

        assertTrue(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.TRUE);

        Mockito.when(left.compute(context)).thenReturn(createList(1, 2, 3));
        Mockito.when(right.compute(context)).thenReturn(createList(1, 2, 4));

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(left.compute(context)).thenReturn(createList(1, 2));
        Mockito.when(right.compute(context)).thenReturn(createList(1, 2, 3));

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(left.compute(context)).thenReturn(createList(null));
        Mockito.when(right.compute(context)).thenReturn(createList(1, 2, 3));

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(left.compute(context)).thenReturn(createList(1, 2, 3));
        Mockito.when(right.compute(context)).thenReturn(createList(null));

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);
    }

    private List createList(Object... values) {
        return new ArrayList<>(Collections.asList(values));
    }

    @Test
    public void testCoreOperationCompare_IterableExpression() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                Mockito.mock(Expression.class),
                Mockito.mock(Expression.class)
        );
        EvaluationContext context = Mockito.mock(EvaluationContext.class);
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);
        Mockito.when(left.compute(context)).thenReturn(leftIterator);
        Mockito.when(right.compute(context)).thenReturn(rightIterator);
        Mockito.when(leftIterator.hasNext()).thenReturn(true);
        Mockito.when(rightIterator.hasNext()).thenReturn(true);
        Mockito.when(leftIterator.next()).thenReturn(1);
        Mockito.when(rightIterator.next()).thenReturn(1);

        assertTrue(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.TRUE);

        Mockito.when(leftIterator.hasNext()).thenReturn(true);
        Mockito.when(rightIterator.hasNext()).thenReturn(false);

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(leftIterator.hasNext()).thenReturn(false);
        Mockito.when(rightIterator.hasNext()).thenReturn(true);

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(leftIterator.hasNext()).thenReturn(false);
        Mockito.when(rightIterator.hasNext()).thenReturn(false);

        assertTrue(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.TRUE);

        Mockito.when(leftIterator.hasNext()).thenReturn(true);
        Mockito.when(rightIterator.next()).thenReturn(null);

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);

        Mockito.when(leftIterator.next()).thenReturn(null);
        Mockito.when(rightIterator.hasNext()).thenReturn(true);

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);
    }

    @Test
    public void testCoreOperationCompare_NullExpression() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                Mockito.mock(Expression.class),
                Mockito.mock(Expression.class)
        );
        EvaluationContext context = Mockito.mock(EvaluationContext.class);
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        Mockito.when(left.compute(context)).thenReturn(null);
        Mockito.when(right.compute(context)).thenReturn(null);

        assertTrue(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.TRUE);
    }

    @Test
    public void testCoreOperationCompare_PointerExpression() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                Mockito.mock(Expression.class),
                Mockito.mock(Expression.class)
        );
        EvaluationContext context = Mockito.mock(EvaluationContext.class);
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        Pointer leftPointer = Mockito.mock(Pointer.class);
        Pointer rightPointer = Mockito.mock(Pointer.class);
        Map<String, Object> nodeMap = new HashMap<>();
        nodeMap.put("node1", leftPointer);
        nodeMap.put("node2", rightPointer);
        Mockito.when(left.compute(context)).thenReturn(leftPointer);
        Mockito.when(right.compute(context)).thenReturn(rightPointer);
        Mockito.when(leftPointer.getValue()).thenReturn("node1");
        Mockito.when(rightPointer.getValue()).thenReturn("node1");

        assertTrue(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.TRUE);
    }

    @Test
    public void testCoreOperationCompare_DifferentPointerExpression() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                Mockito.mock(Expression.class),
                Mockito.mock(Expression.class)
        );
        EvaluationContext context = Mockito.mock(EvaluationContext.class);
        Expression left = Mockito.mock(Expression.class);
        Expression right = Mockito.mock(Expression.class);
        Pointer leftPointer = Mockito.mock(Pointer.class);
        Pointer rightPointer = Mockito.mock(Pointer.class);
        Map<String, Object> nodeMap = new HashMap<>();
        nodeMap.put("node1", leftPointer);
        nodeMap.put("node2", rightPointer);
        Mockito.when(left.compute(context)).thenReturn(leftPointer);
        Mockito.when(right.compute(context)).thenReturn(rightPointer);
        Mockito.when(leftPointer.getValue()).thenReturn("node1");
        Mockito.when(rightPointer.getValue()).thenReturn("node2");

        assertFalse(coreOperationCompare.equal(context, left, right));
        Mockito.verify(context, Mockito.times(1)).setResult(Boolean.FALSE);
    }
}