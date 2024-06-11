package org.apache.commons.jxpath.ri.compiler;

import junit.framework.TestCase;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateHelper;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.util.Simpleiter;

public class CoreOperationRelationalExpressionTest extends TestCase {

    public void testGreater() {
        Node left = NodeTest.createNode("5", "a");
        Node right = NodeTest.createNode("6", "b");
        EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionNode(left), new CoreExpressionNode(right));
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    public void testGreaterEqual() {
        Node left = NodeTest.createNode("5", "a");
        Node right = NodeTest.createNode("5", "b");
        EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNode(left), new CoreExpressionNode(right));
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    public void testLess() {
        Node left = NodeTest.createNode("5", "a");
        Node right = NodeTest.createNode("6", "b");
        EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });

        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreExpressionNode(left), new CoreExpressionNode(right));
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    public void testLessEqual() {
        Node left = NodeTest.createNode("5", "a");
        Node right = NodeTest.createNode("5", "b");
        EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });

        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreExpressionNode(left), new CoreExpressionNode(right));
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    public void testNaN() {
        Node left = NodeTest.createNode("NaN", "a");
        Node right = NodeTest.createNode("NaN", "b");
        EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionNode(left), new CoreExpressionNode(right));
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    public void testIterators() {
        Node left = NodeTest.createDocumentNode("root", new Object[] { "a", "b", "c", "d", "e" });
        Iterator iterLeft = IterateHelper.iterate(left);

        Node right = NodeTest.createDocumentNode("root", new Object[] { "1", "2", "3", "4", "5" });
        Iterator iterRight = IterateHelper.iterate(right);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionNode(left), new CoreExpressionNode(right));

        assertFalse(expression.computeValue(new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") })) instanceof Boolean);

        while (iterLeft.hasNext()) {
            assertTrue(iterRight.hasNext());
            Object leftValue = iterLeft.next();
            Object rightValue = iterRight.next();

            EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });
            Object result = expression.computeValue(context);

            assertFalse(result instanceof Boolean);

            while (iterRight.hasNext()) {
                rightValue = iterRight.next();
                context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });
                result = expression.computeValue(context);

                assertTrue(result instanceof Boolean);
                assertFalse(result);
            }

            iterRight = IterateHelper.iterate(right);
            leftValue = iterLeft.next();
        }

        iterLeft = IterateHelper.iterate(left);
        while (iterLeft.hasNext()) {
            Object leftValue = iterLeft.next();
            Object rightValue = Iterators.getNext(IterateHelper.iterate(right));

            EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });
            Object result = expression.computeValue(context);

            assertTrue(result instanceof Boolean);
            assertTrue(result);
        }
    }

    public void testIteratorsContainsMatch() {
        Node left = NodeTest.createDocumentNode("root", new Object[] { "a", "b", "c", "d", "e" });
        Iterator iterLeft = IterateHelper.iterate(left);

        Node right = NodeTest.createDocumentNode("root", new Object[] { "b", "1", "2", "3", "a" });
        Iterator iterRight = IterateHelper.iterate(right);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNode(left), new CoreExpressionNode(right));

        while (iterLeft.hasNext()) {
            Object leftValue = iterLeft.next();
            Object rightValue = iterRight.next();

            EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });
            Object result = expression.computeValue(context);

            assertTrue(result instanceof Boolean);
            assertTrue(result);
        }

        while (iterRight.hasNext()) {
            Object leftValue = iterLeft.next();
            Object rightValue = iterRight.next();

            EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });
            Object result = expression.computeValue(context);

            assertTrue(result instanceof Boolean);
            assertFalse(result);
        }

        iterLeft = IterateHelper.iterate(left);
        Object leftValue = iterLeft.next();
        Object rightValue = Iterators.getNext(IterateHelper.iterate(right));

        EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue(result);
    }

    public void testIteratorsFindMatch() {
        Node left = NodeTest.createDocumentNode("root", new Object[] { "a", "b", "c", "d" });
        Iterator iterLeft = IterateHelper.iterate(left);

        Node right = NodeTest.createDocumentNode("root", new Object[] { "b", "c", "d", "e" });
        Iterator iterRight = IterateHelper.iterate(right);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNode(left), new CoreExpressionNode(right));

        while (iterLeft.hasNext()) {
            Object leftValue = iterLeft.next();
            Object rightValue = iterRight.next();

            EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });
            Object result = expression.computeValue(context);

            assertTrue(result instanceof Boolean);
            assertFalse(result);
        }

        while (iterRight.hasNext()) {
            Object leftValue = iterLeft.next();
            Object rightValue = iterRight.next();

            EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });
            Object result = expression.computeValue(context);

            assertTrue(result instanceof Boolean);
            assertTrue(result);

            leftValue = iterLeft.next();
            rightValue = iterRight.next();

            context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });
            result = expression.computeValue(context);

            assertTrue(result instanceof Boolean);
            assertFalse(result);
        }

        iterLeft = IterateHelper.iterate(left);
        Object leftValue = iterLeft.next();

        iterRight = IterateHelper.iterate(right);
        Object rightValue = Iterators.getNext(IterateHelper.iterate(right));

        EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue(result);

        leftValue = iterLeft.next();
        while (iterRight.hasNext()) {
            rightValue = iterRight.next();

            context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });
            result = expression.computeValue(context);

            assertTrue(result instanceof Boolean);
            assertFalse(result);
        }

        iterRight = Simpleiter.createIterator(right, true);
        Object nextRight = iterRight.next();
        while (nextRight != null) {
            iterLeft = Simpleiter.createIterator(left, false);
            Object nextLeft = iterLeft.next();

            EvalContext context = new EvalContext(new Path("/"), new Node[] { NodeTest.createDocumentNode("root") });
            Object result = expression.computeValue(context);

            assertTrue(result instanceof Boolean);
            assertFalse(result);

            nextLeft = iterLeft.next();
            nextRight = iterRight.next();
        }
    }

}