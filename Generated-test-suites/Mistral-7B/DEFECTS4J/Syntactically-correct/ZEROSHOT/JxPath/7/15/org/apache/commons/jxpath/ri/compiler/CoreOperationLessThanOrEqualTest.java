package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.core.Expression;
import org.apache.commons.jxpath.ri.core.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.core.CoreOperationLessThanOrEqual;

/**
 * Tests for {@link CoreOperationLessThanOrEqual} class.
 */
public class CoreOperationLessThanOrEqualTest {

    @SuppressWarnings("unchecked")
    private Expression createLessThanOrEqualExpression(Expression arg1, Expression arg2) {
        return new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @org.junit.Test
    public void testLessThanOrEqualTrue() {
        InfoSet infoSet = new InfoSet() {
            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Object getItem(int index) {
                throw new UnsupportedOperationException();
            }

            @Override
            public InfoSet getItem(String name) {
                if (CoreOperationLessThanOrEqualTest.class.getName().equals(name)) {
                    return this;
                }
                return null;
            }
        };

        Expression arg1 = new LiteralExpression(3.5);
        Expression arg2 = new LiteralExpression(3.5);

        CoreOperationLessThanOrEqual lessThanOrEqual = (CoreOperationLessThanOrEqual) createLessThanOrEqualExpression(arg1, arg2);

        EvalContext context = new EvalContext() {
            @Override
            public InfoSet getCurrentInfoSet() {
                return infoSet;
            }
        };

        Object result = lessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testLessThanOrEqualFalse() {
        InfoSet infoSet = new InfoSet() {
            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Object getItem(int index) {
                throw new UnsupportedOperationException();
            }

            @Override
            public InfoSet getItem(String name) {
                if (CoreOperationLessThanOrEqualTest.class.getName().equals(name)) {
                    return this;
                }
                return null;
            }
        };

        Expression arg1 = new LiteralExpression(3.5);
        Expression arg2 = new LiteralExpression(4.5);

        CoreOperationLessThanOrEqual lessThanOrEqual = (CoreOperationLessThanOrEqual) createLessThanOrEqualExpression(arg1, arg2);

        EvalContext context = new EvalContext() {
            @Override
            public InfoSet getCurrentInfoSet() {
                return infoSet;
            }
        };

        Object result = lessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testLessThanOrEqualMixedTypes() {
        InfoSet infoSet = new InfoSet() {
            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Object getItem(int index) {
                throw new UnsupportedOperationException();
           }

            @Override
            public InfoSet getItem(String name) {
                if (CoreOperationLessThanOrEqualTest.class.getName().equals(name)) {
                    return this;
                }
                return null;
            }
        };

        Expression arg1 = new LiteralExpression(3.5);
        Expression arg2 = new LiteralExpression(4);

        CoreOperationLessThanOrEqual lessThanOrEqual = (CoreOperationLessThanOrEqual) createLessThanOrEqualExpression(arg1, arg2);

        EvalContext context = new EvalContext() {
            @Override
            public InfoSet getCurrentInfoSet() {
                return infoSet;
            }
        };

        Object result = lessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertEquals(Class.forName("java.lang.ClassCastException"), result.getClass());
    }
}