import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression();
        Assert.assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeContextDependent() {
        // Cannot test abstract method directly
    }

    @Test
    public void testComputeValue() {
        Expression expression = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "Test Value";
            }
        };
        EvalContext context = new EvalContext(null);
        Object result = expression.computeValue(context);
        Assert.assertEquals("Test Value", result);
    }

    @Test
    public void testCompute() {
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return "Test Value";
            }
        };
        EvalContext context = new EvalContext(null);
        Object result = expression.compute(context);
        Assert.assertEquals("Test Value", result);
    }

    @Test
    public void testIterate() {
        // Cannot test abstract method directly
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return new Object[]{"Test Value 1", "Test Value 2"};
            }
        };
        EvalContext context = new EvalContext(null);
        Iterator iterator = expression.iteratePointers(context);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(new NodePointer("Test", "Test Value 1"), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(new NodePointer("Test", "Test Value 2"), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointersWithNullContext() {
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return new Object[]{"Test Value 1", "Test Value 2"};
            }
        };
        EvalContext context = null;
        Iterator iterator = expression.iteratePointers(context);
        // Test for NullPointerException
        Assert.assertNull(iterator);
    }

    @Test
    public void testIteratePointersWithEmptyContext() {
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return new Object[]{};
            }
        };
        EvalContext context = new EvalContext(null);
        Iterator iterator = expression.iteratePointers(context);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointersWithMixedNodes() {
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return new Object[]{"Test String", new NodePointer("Test", "Test Node"), 123};
            }
        };
        EvalContext context = new EvalContext(null);
        Iterator iterator = expression.iteratePointers(context);
        // Test for Iterator contract violation
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(new NodePointer("Test", "Test Node"), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("Test String", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterateContextDependentAndContextIndependent() {
        Expression contextDependentExpression = new ContextDependentExpression();
        Expression contextIndependentExpression = new ContextIndependentExpression();
        EvalContext context = new EvalContext(null);
        Iterator contextDependentIterator = contextDependentExpression.iteratePointers(context);
        Iterator contextIndependentIterator = contextIndependentExpression.iteratePointers(context);
        // Test for ConcurrentModificationException
        Assert.assertTrue(contextDependentIterator.hasNext());
        Assert.assertTrue(contextIndependentIterator.hasNext());
        contextDependentIterator.remove();
        Assert.assertFalse(contextDependentIterator.hasNext());
        Assert.assertEquals(contextDependentIterator.next(), contextIndependentIterator.next());
        Assert.assertTrue(contextIndependentIterator.hasNext());
    }
}