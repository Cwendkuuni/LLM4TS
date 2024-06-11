import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(1),
                new LiteralExpression(1)
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(1));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testNotEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(1),
                new LiteralExpression(2)
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(1));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualCollection() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(Arrays.asList(1, 2)),
                new LiteralExpression(Arrays.asList(1, 2))
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(Arrays.asList(1, 2)));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testNotEqualCollection() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(Arrays.asList(1, 2)),
                new LiteralExpression(Arrays.asList(1, 3))
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(Arrays.asList(1, 2)));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualCollectionWithNull() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(Arrays.asList(1, 2)),
                new LiteralExpression(null)
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(Arrays.asList(1, 2)));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualTwoPointers() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new VariableExpression("x"),
                new VariableExpression("x")
        );

        EvalContext context = mock(EvalContext.class);
        Pointer x = mock(Pointer.class);
        when(context.getSingleNodePointer()).thenReturn(x);
        when(x.getValue()).thenReturn(new LiteralExpression("abc"));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testNotEqualTwoPointers() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new VariableExpression("x"),
                new VariableExpression("y")
        );

        EvalContext context = mock(EvalContext.class);
        Pointer x = mock(Pointer.class);
        when(context.getSingleNodePointer()).thenReturn(x);
        when(x.getValue()).thenReturn(new LiteralExpression("abc"));

        Pointer y = mock(Pointer.class);
        when(context.getSingleNodePointer()).thenReturn(y);
        when(y.getValue()).thenReturn(new LiteralExpression("def"));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualIterators() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new IteratorExpression(Arrays.asList(1, 2).iterator()),
                new IteratorExpression(Arrays.asList(1, 2).iterator())
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(Arrays.asList(1, 2).iterator()));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testNotEqualIterators() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new IteratorExpression(Arrays.asList(1, 2).iterator()),
                new IteratorExpression(Arrays.asList(1, 3).iterator())
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(Arrays.asList(1, 2).iterator()));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualIteratorsWithNull() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new IteratorExpression(Arrays.asList(1, 2).iterator()),
                new LiteralExpression(null)
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(Arrays.asList(1, 2).iterator()));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(Arrays.asList(1, 2)),
                new LiteralExpression(1)
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(Arrays.asList(1, 2).iterator()));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testContainsFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(Arrays.asList(1, 2)),
                new LiteralExpression(3)
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(Arrays.asList(1, 2).iterator()));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(Arrays.asList(1, 2)),
                new LiteralExpression(Arrays.asList(1, 2, 3))
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(Arrays.asList(1, 2).iterator()));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatchFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(Arrays.asList(1, 2)),
                new LiteralExpression(Collections.singletonList(3))
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(Arrays.asList(1, 2).iterator()));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatchWithNull() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(Arrays.asList(1, 2)),
                new LiteralExpression(null)
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new LiteralExpression(Arrays.asList(1, 2).iterator()));

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testCompareInitialContexts() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new InitialContext(),
                new InitialContext()
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new InitialContext());

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testCompareSelfContexts() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new SelfContext(),
                new SelfContext()
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new SelfContext());

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testCompareInitialContextsWithSelfContext() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new InitialContext(),
                new SelfContext()
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new SelfContext());

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testCompareSelfContextsWithInitialContext() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new SelfContext(),
                new InitialContext()
        );

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(new InitialContext());

        boolean result = coreOperationCompare.evaluate(context);
        Assert.assertFalse(result);
    }
}