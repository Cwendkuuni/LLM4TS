package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.iter.Iterators;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression e = new CoreOperationRelationalExpression(args);
        assertNotNull(e);
        assertEquals(CoreOperationRelationalExpression.class, e.getClass());
        assertFalse(e.isSymmetric());
        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, e.getPrecedence());
    }

    @Test
    public void testComputeValue() {
        Expression left = new LiteralExpression(1);
        Expression right = new LiteralExpression(2);
        CoreOperationRelationalExpression e = new GreaterExpression(new Expression[2], left, right);

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Object getSingleNodePointer() {
                return null;
            }
        });

        Object result = e.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new LiteralExpression(2);
        right = new LiteralExpression(1);
        e = new GreaterExpression(new Expression[2], left, right);

        context = new EvalContext(new InfoSet() {
            @Override
            public Object getSingleNodePointer() {
                return null;
            }
        });

        result = e.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new LiteralExpression(2);
        right = new LiteralExpression(2);
        e = new GreaterExpression(new Expression[2], left, right);

        context = new EvalContext(new InfoSet() {
            @Override
            public Object getSingleNodePointer() {
                return null;
            }
        });

        result = e.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new LiteralExpression(1.0);
        right = new LiteralExpression(2.0);
        e = new GreaterExpression(new Expression[2], left, right);

        context = new EvalContext(new InfoSet() {
            @Override
            public Object getSingleNodePointer() {
                return null;
            }
        });

        result = e.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new LiteralExpression(2.0);
        right = new LiteralExpression(1.0);
        e = new GreaterExpression(new Expression[2], left, right);

        context = new EvalContext(new InfoSet() {
            @Override
            public Object getSingleNodePointer() {
                return null;
            }
        });

        result = e.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression e = new GreaterExpression(new Expression[2], new LiteralExpression(1), new LiteralExpression(2));
        assertTrue(e.evaluateCompare(-1));
        assertFalse(e.evaluateCompare(0));
        assertFalse(e.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        Object left = new Integer(1);
        Object right = new Integer(2);
        CoreOperationRelationalExpression e = new CoreOperationRelationalExpression(new Expression[2], new LiteralExpression(left), new LiteralExpression(right)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertFalse(e.compute(left, right));

        left = new Integer(2);
        right = new Integer(1);
        e = new CoreOperationRelationalExpression(new Expression[2], new LiteralExpression(left), new LiteralExpression(right)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertFalse(e.compute(left, right));

        left = new Integer(2);
        right = new Integer(2);
        e = new CoreOperationRelationalExpression(new Expression[2], new LiteralExpression(left), new LiteralExpression(right)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        assertTrue(e.compute(left, right));

        left = new Double(1.0);
        right = new Double(2.0);
        e = new CoreOperationRelationalExpression(new Expression[2], new LiteralExpression(left), new LiteralExpression(right)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertFalse(e.compute(left, right));

        left = new Double(2.0);
        right = new Double(1.0);
        e = new CoreOperationRelationalExpression(new Expression[2], new LiteralExpression(left), new LiteralExpression(right)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertFalse(e.compute(left, right));

        left = new Double(2.0);
        right = new Double(2.0);
        e = new CoreOperationRelationalExpression(new Expression[2], new LiteralExpression(left), new LiteralExpression(right)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        assertTrue(e.compute(left, right));
    }

    @Test
    public void testReduce() {
        Object obj = new Integer(1);
        Object reduced = new CoreOperationRelationalExpression(new Expression[2], new LiteralExpression(obj), new LiteralExpression(obj)) {
        }.reduce(obj);

        assertEquals(obj, reduced);

        obj = new SelfContext();
        reduced = new CoreOperationRelationalExpression(new Expression[2], new LiteralExpression(obj), new LiteralExpression(obj)) {
        }.reduce(obj);
        assertEquals(null, reduced);

        obj = new HashSet<>();
        reduced = new CoreOperationRelationalExpression(new Expression[2], new LiteralExpression(obj), new LiteralExpression(obj)) {
        }.reduce(obj);
        assertNull(reduced);

        List<Object> list = Arrays.asList(1, 2, 3);
        Iterator<Object> iterator = list.iterator();
        reduced = new CoreOperationRelationalExpression(new Expression[2], new LiteralExpression(iterator), new LiteralExpression(iterator)) {
        }.reduce(iterator);
        assertEquals(iterator, reduced);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression e = new CoreOperationRelationalExpression(new Expression[2], new LiteralExpression(1), new LiteralExpression(2)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        Iterator<Object> it = Collections.singletonList(1).iterator();
        assertFalse(e.containsMatch(it, 2));

        it = Collections.singletonList(2).iterator();
        assertFalse(e.containsMatch(it, 1));

        it = Iterators.iter(1, 2, 3);
        assertFalse(e.containsMatch(it, 1));
        assertFalse(e.containsMatch(it, 2));
        assertFalse(e.containsMatch(it, 3));

        it = Iterators.iter(1, 2, 2);
        assertFalse(e.containsMatch(it, 1));
        assertTrue(e.containsMatch(it, 2));
        assertFalse(e.containsMatch(it, 3));

        it = Iterators.iter(1, 2, 1.0);
        assertFalse(e.containsMatch(it, 1.0));
        assertFalse(e.containsMatch(it, 2.0));
        assertTrue(e.containsMatch(it, 1));

        it = Iterators.iter(1.0, 2.0, 1.0);
        assertFalse(e.containsMatch(it, 1.0));
        assertTrue(e.containsMatch(it, 2.0));
        assertFalse(e.containsMatch(it, 1));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression e = new CoreOperationRelationalExpression(new Expression[2], new LiteralExpression(1), new LiteralExpression(2)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        Iterator<Object> leftIt = Collections.singletonList(1).iterator();
        Iterator<Object> rightIt = Collections.singletonList(2).iterator();
        HashSet<Object> left = new HashSet<>(Arrays.asList(1, 1, 2, 2));
        assertFalse(e.findMatch(leftIt, rightIt));

        leftIt = Collections.singletonList(2).iterator();
        assertFalse(e.findMatch(leftIt, rightIt));

        leftIt = Iterators.iter(1, 2, 3);
        rightIt = Iterators.iter(3, 2, 1);
        HashSet<Object> left2 = new HashSet<>(Arrays.asList(1, 1, 2, 2));
        assertFalse(e.findMatch(leftIt, rightIt));

        leftIt = Iterators.iter(1, 2, 2);
        rightIt = Iterators.iter(3, 2, 1);
        HashSet<Object> left3 = new HashSet<>(Arrays.asList(1, 1, 2, 2));
        assertTrue(e.findMatch(leftIt, rightIt));

        leftIt = Iterators.iter(1.0, 2.0, 1.0);
        rightIt = Iterators.iter(3.0, 2.0, 1.0);
        HashSet<Object> left4 = new HashSet<>(Arrays.asList(1.0, 1.0, 2.0, 2.0));
        assertFalse(e.findMatch(leftIt, rightIt));

        leftIt = Iterators.iter(1.0, 2.0, 2.0);
        rightIt = Iterators.iter(3.0, 2.0, 1.0);
        HashSet<Object> left5 = new HashSet<>(Arrays.asList(1.0, 1.0, 2.0, 2.0));
        assertTrue(e.findMatch(leftIt, rightIt));
    }

}