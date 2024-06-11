package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.IteratorUtil;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"1", "2"})) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertNotNull(expression);
    }

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"1.0", "2.0"})) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object result = expression.computeValue(new EvalContext());

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithNullLeft() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"null", "2.0"})) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object result = expression.computeValue(new EvalContext());

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithNullRight() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"1.0", "null"})) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object result = expression.computeValue(new EvalContext());

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithDifferentTypes() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"1.0", "2"})) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object result = expression.computeValue(new EvalContext());

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithEqualTypesButDifferentValues() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"1.0", "2.0"})) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object result = expression.computeValue(new EvalContext());

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithEqualTypesAndValues() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"1.0", "1.0"})) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object result = expression.computeValue(new EvalContext());

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testReduce() {
        Object left = new SelfContext();
        Object right = new SelfContext();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"" + left, "" + right}));

        Object reducedLeft = expression.reduce(left);
        Object reducedRight = expression.reduce(right);

        assertNull(reducedLeft);
        assertNull(reducedRight);

        left = new Integer(1);
        right = new Integer(2);

        reducedLeft = expression.reduce(left);
        reducedRight = expression.reduce(right);

        assertEquals(left, reducedLeft);
        assertEquals(right, reducedRight);

        left = new Double(1.0);
        right = new Double(2.0);

        reducedLeft = expression.reduce(left);
        reducedRight = expression.reduce(right);

        assertEquals(left, reducedLeft);
        assertEquals(right, reducedRight);

        left = new IteratorUtil.EmptyIterator();
        right = new IteratorUtil.EmptyIterator();

        reducedLeft = expression.reduce(left);
        reducedRight = expression.reduce(right);

        assertEquals(left, reducedLeft);
        assertEquals(right, reducedRight);

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        reducedLeft = expression.reduce(left);

        assertEquals(left, reducedLeft);

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        reducedLeft = expression.reduce(left);
        reducedRight = expression.reduce(right);

        assertEquals(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        }, reducedLeft);
        assertEquals(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        }, reducedRight);
    }

    @Test
    public void testContainsMatch() {
        Iterator<Object> left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        Object right = new Integer(1);

        boolean result = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"" + left, "" + right}))
                .containsMatch(left, right);

        assertTrue(result);

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        result = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"" + left, "" + right}))
                .containsMatch(left, right);

        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator<Object> left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        Iterator<Object> right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        boolean result = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"" + left, "" + right}))
                .findMatch(left, right);

        assertTrue(result);

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        result = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"" + left, "" + right}))
                .findMatch(left, right);

        assertFalse(result);

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        left = new HashSet<Object>() {{
            add(new Integer(1));
            add(new Integer(2));
        }};

        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        result = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"" + left, "" + right}))
                .findMatch(left.iterator(), right);

        assertTrue(result);

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        result = new CoreOperationRelationalExpression(new ExpressionsFactory()
                .createExpression(new String[]{"" + left, "" + right}))
                .findMatch(left, right);

        assertFalse(result);
    }
}