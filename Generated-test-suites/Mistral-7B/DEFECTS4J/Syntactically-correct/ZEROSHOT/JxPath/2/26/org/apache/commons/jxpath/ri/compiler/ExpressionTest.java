import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.apache.commons.jxpath.ri.compiler.Expression.ZERO;
import static org.apache.commons.jxpath.ri.compiler.Expression.ONE;
import static org.apache.commons.jxpath.ri.compiler.Expression.NOT_A_NUMBER;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new FooExpression();
        Assert.assertFalse(expression.isContextDependent());

        Expression contextDependentExpression = new BarExpression();
        Assert.assertTrue(contextDependentExpression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new FooExpression();
        Object value = expression.computeValue(new EvalContext());
        Assert.assertEquals(ONE, value);

        Expression contextDependentExpression = new BarExpression();
        Object contextDependentValue = contextDependentExpression.computeValue(new EvalContext());
        Assert.assertNotNull(contextDependentValue);
    }

    @Test
    public void testCompute() {
        Expression expression = new FooExpression();
        Object result = expression.compute(new EvalContext());
        Assert.assertEquals(ONE, result);

        Expression contextDependentExpression = new BarExpression();
        Object contextDependentResult = contextDependentExpression.compute(new EvalContext());
        Assert.assertNotNull(contextDependentResult);
    }

    @Test
    public void testIterate() {
        Expression expression = new FooExpression();
        Iterator iterator = expression.iterate(new EvalContext());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(ONE, iterator.next());

        Expression contextDependentExpression = new BarExpression();
        Iterator contextDependentIterator = contextDependentExpression.iterate(new EvalContext());
        Assert.assertTrue(contextDependentIterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new FooExpression();
        Iterator pointerIterator = expression.iteratePointers(new EvalContext());
        Assert.assertTrue(pointerIterator.hasNext());
        Assert.assertNotNull(pointerIterator.next());

        Expression contextDependentExpression = new BarExpression();
        Iterator contextDependentPointerIterator = contextDependentExpression.iteratePointers(new EvalContext());
        Assert.assertTrue(contextDependentPointerIterator.hasNext());
    }

    private static class FooExpression extends Expression {
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

    private static class BarExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            // This method should not be called because isContextDependent() returns true
            return NOT_A_NUMBER;
        }

        @Override
        public Object compute(EvalContext context) {
            // This method should not be called because isContextDependent() returns true
            return NOT_A_NUMBER;
        }
    }
}