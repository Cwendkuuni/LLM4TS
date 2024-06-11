import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.jxpath.JXPathContext.newContext;
import static org.apache.commons.jxpath.ri.axes.JXPathConstants.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new LiteralExpression("1"),
                new LiteralExpression("1")
        );
        Object result;

        try {
            result = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{1, 1})
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testNotEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new LiteralExpression("1"),
                new LiteralExpression("2")
        );
        Object result;

        try {
            result = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{1, 2})
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testEqualStrings() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new LiteralExpression("one"),
                new LiteralExpression("one")
        );
        Object result;

        try {
            result = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{"one", "one"})
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testNotEqualStrings() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new LiteralExpression("one"),
                new LiteralExpression("two")
        );
        Object result;

        try {
            result = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{"one", "two"})
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testEqualCollections() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new CollectionExpression(new ArrayList<>(Arrays.asList(1, 2))),
                new CollectionExpression(new ArrayList<>(Arrays.asList(1, 2)))
        );
        Object result;

        try {
            result = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{
                            Arrays.asList(1, 2),
                            Arrays.asList(1, 2)
                    })
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testNotEqualCollections() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new CollectionExpression(new ArrayList<>(Arrays.asList(1, 2))),
                new CollectionExpression(new ArrayList<>(Arrays.asList(1, 3)))
        );
        Object result;

        try {
            result = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{
                            Arrays.asList(1, 2),
                            Arrays.asList(1, 3)
                    })
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testEqualCollectionsSameInstance() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new CollectionExpression(new ArrayList<>(Arrays.asList(1, 2))),
                new CollectionExpression(new ArrayList<>(Arrays.asList(1, 2)))
        );
        Object result1, result2;

        try {
            result1 = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{
                            new ArrayList<>(Arrays.asList(1, 2)),
                            new ArrayList<>(Arrays.asList(1, 2))
                    })
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        try {
            result2 = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{
                            new ArrayList<>(Arrays.asList(1, 2)),
                            Collections.singletonList(result1)
                    })
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        assertTrue(result1 instanceof Boolean);
        assertTrue((Boolean) result1);
        assertTrue(result2 instanceof Boolean);
        assertTrue((Boolean) result2);
    }

    @Test
    public void testNotEqualIterators() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new CollectionExpression(Arrays.asList(1, 2).iterator()),
                new CollectionExpression(Arrays.asList(1, 3).iterator())
        );
        Object result;

        try {
            result = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{null, null})
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        try {
            result = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{
                            Arrays.asList(1, 2).iterator(),
                            Arrays.asList(1, 3).iterator()
                    })
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testEqualIteratorsSameInstance() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new CollectionExpression(Arrays.asList(1, 2).iterator()),
                new CollectionExpression(Arrays.asList(1, 2).iterator())
        );
        Object result1, result2;

        try {
            result1 = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{null, null})
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        try {
            result1 = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{
                            Arrays.asList(1, 2).iterator(),
                            Arrays.asList(1, 2).iterator()
                    })
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        try {
            result2 = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{result1, result1})
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        assertTrue(result1 instanceof Boolean);
        assertTrue((Boolean) result1);
        assertTrue(result2 instanceof Boolean);
        assertTrue((Boolean) result2);
    }

    @Test
    public void testEqualPointerAndNumber() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new PointerExpression("$this/number[1]", newEvaluationContext()),
                new LiteralExpression("1")
        );
        Object result;

        try {
            result = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{Integer.valueOf(1)})
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testNotEqualPointerAndNumber() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new PointerExpression("$this/number[1]", newEvaluationContext()),
                new LiteralExpression("2")
        );
        Object result;

        try {
            result = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{Integer.valueOf(1)})
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidComparison() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new LiteralExpression("1"),
                new LiteralExpression("string")
        );
        Object result;

        try {
            result = compare.compile(newEvaluationContext()).evaluate(
                    newJXPathContext(new Object[]{"1", "string"})
            );
        } catch (EvaluationException e) {
            throw new RuntimeException(e);
        }
    }

    private JXPathContext newJXPathContext(Object[] args) {
        return newContext(args);
    }

    private EvaluationContext newEvaluationContext() {
        return new EvaluationContext(new InitialContext());
    }
}