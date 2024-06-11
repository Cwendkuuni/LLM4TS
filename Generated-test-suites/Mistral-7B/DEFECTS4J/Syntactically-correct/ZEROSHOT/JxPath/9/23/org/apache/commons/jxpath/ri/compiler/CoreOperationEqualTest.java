package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.test.ExpressionTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test case for CoreOperationEqual class.
 */
public class CoreOperationEqualTest {

    @Test
    public void testComputeValueWithSameValues() {
        Expression arg1 = createDoubledExpression(5);
        Expression arg2 = createDoubledExpression(5);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = createEvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithDifferentValues() {
        Expression arg1 = createDoubledExpression(5);
        Expression arg2 = createDoubledExpression(6);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = createEvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithNullValues() {
        Expression arg1 = createNullExpression();
        Expression arg2 = createNullExpression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = createEvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithNullAndNonNullValues() {
        Expression arg1 = createNullExpression();
        Expression arg2 = createDoubledExpression(5);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = createEvalContext();
        Object result = coreOperationEqual.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = createDoubledExpression(5);
        Expression arg2 = createDoubledExpression(5);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        String symbol = coreOperationEqual.getSymbol();

        assertEquals("=", symbol);
    }

    private Expression createDoubledExpression(int value) {
        Expression arg = new LiteralExpression(value);
        return new DoubledExpression(arg);
    }

    private Expression createNullExpression() {
        return new NullExpression();
    }

    private EvalContext createEvalContext() {
        return new EvalContextWithSupport();
    }

    private static class EvalContextWithSupport extends EvalContext {
        @Override
        public Object coerceToBoolean(Object value) {
            if (value == null) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
    }

    private static class DoubledExpression extends Expression {
        private Expression expression;

        public DoubledExpression(Expression arg) {
            super();
            this.expression = arg;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return computeValue(context, 2);
        }

        private Object computeValue(EvalContext context, int times) {
            Object value = expression.computeValue(context);
            Object result;
            for (int i = 1; i < times; i++) {
                result = expression.computeValue(context);
                assertEquals(value, result);
                value = result;
            }
            return value;
        }
    }
}