import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Iterator;

import static org.apache.commons.jxpath.ri.compiler.Expression.ZERO;
import static org.apache.commons.jxpath.ri.compiler.Expression.ONE;

/**
 * Test class for Expression.
 */
public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        AbstractExpression expression = new MyContextDependentExpression();
        Assert.assertFalse(expression.isContextDependent());
        expression.setContextDependent(true);
        Assert.assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        AbstractExpression expression = new MyContextIndependentExpression();
        EvalContext context = new EvalContext(new NodePointer(null, null, null), null);
        Object value = expression.computeValue(context);
        Assert.assertEquals(ONE, value);
    }

    @Test
    public void testCompute() {
        AbstractExpression expression = new MyContextIndependentExpression();
        EvalContext context = new EvalContext(new NodePointer(null, null, null), null);
        Object result = expression.compute(context);
        Assert.assertEquals(ONE, result);
    }

    @Test
    public void testIterate() {
        AbstractExpression expression = new MyIteratableExpression();
        EvalContext context = new EvalContext(new NodePointer(null, null, null), null);
        Iterator iterator = expression.iterate(context);
        Assert.assertTrue(iterator.hasNext());
        Object first = iterator.next();
        Assert.assertEquals(ONE, first);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        AbstractExpression expression = new MyIteratableExpression();
        EvalContext context = new EvalContext(new NodePointer(null, null, null), null);
        Iterator iterator = expression.iteratePointers(context);
        Assert.assertTrue(iterator.hasNext());
        Object first = iterator.next();
        Assert.assertTrue(first instanceof Pointer);
        Assert.assertFalse(iterator.hasNext());
    }

    private abstract static class AbstractExpression extends Expression {
    }

    private static class MyContextDependentExpression extends AbstractExpression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }
    }

    private static class MyContextIndependentExpression extends AbstractExpression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return ONE;
        }

        @Override
        public Object compute(EvalContext context) {
            return ONE;
        }
    }

    private static class MyIteratableExpression extends AbstractExpression {
        @Override
        public Iterator iterate(EvalContext context) {
            return new Iterator() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return ZERO;
                }
            };
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            return new Iterator() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return new NodePointer(null, null, null);
                }
            };
        }
    }
}