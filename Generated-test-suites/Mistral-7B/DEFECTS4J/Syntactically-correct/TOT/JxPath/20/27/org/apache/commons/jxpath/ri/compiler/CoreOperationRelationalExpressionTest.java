package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Iter;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.SimpleNode;
import org.apache.commons.jxpath.ri.node.ElementNode;
import org.apache.commons.jxpath.ri.node.TextNode;
import org.apache.commons.jxpath.ri.util.JXPathContext;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link CoreOperationRelationalExpression}.
 *
 * @since JXPath 1.3
 */
public class CoreOperationRelationalExpressionTest {

    private EvalContext context;
    private Path path;
    private SimpleNode node;

    @Before
    public void setup() {
        context = new JXPathContext();
        path = new Path(new String[] {"//test"});
        node = new ElementNode("test", new String[] {}, new Object[] {});
    }

    /**
     * Tests the &gt; method.
     */
    @Test
    public void testGreaterThan() {
        Expression left = new NumberLiteralExpression(1.0d);
        Expression right = new NumberLiteralExpression(2.0d);
        CoreOperationRelationalExpression op = new GreaterThanExpression(left, right);

        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new NumberLiteralExpression(2.0d);
        right = new NumberLiteralExpression(1.0d);
        op = new GreaterThanExpression(left, right);

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new NumberLiteralExpression(1.0d);
        right = new NumberLiteralExpression(1.0d);
        op = new GreaterThanExpression(left, right);

        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Tests the &gt;= method.
     */
    @Test
    public void testGreaterThanOrEqual() {
        Expression left = new NumberLiteralExpression(1.0d);
        Expression right = new NumberLiteralExpression(2.0d);
        CoreOperationRelationalExpression op = new GreaterThanOrEqualExpression(left, right);

        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new NumberLiteralExpression(2.0d);
        op = new GreaterThanOrEqualExpression(left, right);

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new NumberLiteralExpression(2.0d);
        right = new NumberLiteralExpression(2.0d);
        op = new GreaterThanOrEqualExpression(left, right);

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Tests the &lt; method.
     */
    @Test
    public void testLessThan() {
        Expression left = new NumberLiteralExpression(1.0d);
        Expression right = new NumberLiteralExpression(2.0d);
        CoreOperationRelationalExpression op = new LessThanExpression(left, right);

        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new NumberLiteralExpression(2.0d);
        right = new NumberLiteralExpression(1.0d);
        op = new LessThanExpression(left, right);

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new NumberLiteralExpression(1.0d);
        right = new NumberLiteralExpression(1.0d);
        op = new LessThanExpression(left, right);

        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Tests the &lt;= method.
     */
    @Test
    public void testLessThanOrEqual() {
        Expression left = new NumberLiteralExpression(1.0d);
        Expression right = new NumberLiteralExpression(2.0d);
        CoreOperationRelationalExpression op = new LessThanOrEqualExpression(left, right);

        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new NumberLiteralExpression(2.0d);
        op = new LessThanOrEqualExpression(left, right);

        result = op.computeValue(context);
        assertFalse(result instanceof Boolean);

        left = new NumberLiteralExpression(1.0d);
        right = new NumberLiteralExpression(1.0d);
        op = new LessThanOrEqualExpression(left, right);

        result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Tests the compare method.
     */
    @Test
    public void testCompare() {
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(
                new NumberLiteralExpression(1.0d),
                new NumberLiteralExpression(2.0d)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                assertTrue(compare != 0);
                return compare > 0;
            }
        };

        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Tests the computeValue method with iterators.
     */
    @Test
    public void testComputeValueWithIterators() {
        Iterator it1 = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };

        Iterator it2 = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };

        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(
                new IteratorValueExpression(it1),
                new IteratorValueExpression(it2)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                assertTrue(compare != 0);
                return compare > 0;
            }
        };

        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Tests the computeValue method with collections.
     */
    @Test
    public void testComputeValueWithCollections() {
        Collection coll1 = new ArrayList();
        coll1.add(new Object());
        Collection coll2 = new ArrayList();
        coll2.add(new Object());

        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(
                new CollectionValueExpression(coll1),
                new CollectionValueExpression(coll2)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                assertTrue(compare != 0);
                return compare > 0;
            }
        };

        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Tests the computeValue method with self contexts.
     */
    @Test
    public void testComputeValueWithSelfContexts() {
        NodeInfo nodeInfo = new NodeInfo(new ElementNode("test", new String[] {}, new Object[] {}),
                new Object[] { path, null, null }, new Object[] {});
        SimpleNode selfNode = new SelfContext(path, context, null, nodeInfo);

        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(
                new SelfContextExpression(path, context, null),
                new SelfContextExpression(path, context, null)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                assertTrue(compare != 0);
                return compare > 0;
            }
        };

        Object result = op.computeValue(context);
        assertNull(result);
    }

    /**
     * Tests the computeValue method with an empty path.
     */
    @Test
    public void testComputeValueWithEmptyPath() {
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(
                new NumberLiteralExpression(1.0d),
                new NumberLiteralExpression(2.0d));

        Object result = op.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Tests the computeValue method with null left operand.
     */
    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullLeftOperand() {
        Expression right = new NumberLiteralExpression(2.0d);
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(
                null, right);

        op.computeValue(context);
    }

    /**
     * Tests the computeValue method with null right operand.
     */
    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullRightOperand() {
        Expression left = new NumberLiteralExpression(1.0d);
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(
                left, null);

        op.computeValue(context);
    }

    /**
     * Tests the getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(
                new NumberLiteralExpression(1.0d),
                new NumberLiteralExpression(2.0d));

        int precedence = op.getPrecedence();
        assertEquals(RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    /**
     * Tests the isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(
                new NumberLiteralExpression(1.0d),
                new NumberLiteralExpression(2.0d));

        assertFalse(op.isSymmetric());
    }
}