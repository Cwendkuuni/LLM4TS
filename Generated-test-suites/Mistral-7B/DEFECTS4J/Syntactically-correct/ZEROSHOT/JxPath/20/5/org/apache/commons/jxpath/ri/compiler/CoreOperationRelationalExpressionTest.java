package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}.
     */
    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationRelationalExpression() {
        Expression a = new LiteralExpression(1.0);
        Expression b = new LiteralExpression(2.0);
        Expression c = new LiteralExpression(1.0);
        Expression d = new LiteralExpression(1.0);
        Expression e = new LiteralExpression(null);
        Expression[] args = { a, b };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNotNull(expression);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)}.
     */
    @org.junit.Test
    public void testComputeValue() {
        Expression a = new LiteralExpression(1.0);
        Expression b = new LiteralExpression(2.0);
        CoreOperationRelationalExpression expression = new GtExpression(new CoreOperationRelationalExpression[] { a, b });
        EvalContext context = new EvalContext(new SelfContext(), new InitialContext());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        a = new LiteralExpression(1.0);
        b = new LiteralExpression(1.0);
        expression = new GeExpression(new CoreOperationRelationalExpression[] { a, b });
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);

        result = expression.computeValue(context);
        assertTrue((boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()}.
     */
    @org.junit.Test
    public void testGetPrecedence() {
        Expression a = new LiteralExpression(1.0);
        Expression b = new LiteralExpression(2.0);
        CoreOperationRelationalExpression expression = new GtExpression(new CoreOperationRelationalExpression[] { a, b });
        int precedence = expression.getPrecedence();
        assertEquals(RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()}.
     */
    @org.junit.Test
    public void testIsSymmetric() {
        Expression a = new LiteralExpression(1.0);
        Expression b = new LiteralExpression(2.0);
        CoreOperationRelationalExpression expression = new GtExpression(new CoreOperationRelationalExpression[] { a, b });
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)}.
     */
    @org.junit.Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new GtExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(1.0), new LiteralExpression(2.0) });
        int compare = expression.compare(1.0, 2.0);
        assertTrue(expression.evaluateCompare(compare));

        expression = new LeExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(1.0), new LiteralExpression(2.0) });
        compare = expression.compare(1.0, 2.0);
        assertFalse(expression.evaluateCompare(compare));

        expression = new GeExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(1.0), new LiteralExpression(2.0) });
        compare = expression.compare(1.0, 2.0);
        assertFalse(expression.evaluateCompare(compare));

        expression = new LeExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(1.0), new LiteralExpression(2.0) });
        compare = expression.compare(1.0, 2.0);
        assertTrue(expression.evaluateCompare(compare));

        expression = new EqExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(1.0), new LiteralExpression(1.0) });
        compare = expression.compare(1.0, 1.0);
        assertTrue(expression.evaluateCompare(compare));

        expression = new EqExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(1.0), new LiteralExpression(2.0) });
        compare = expression.compare(1.0, 2.0);
        assertFalse(expression.evaluateCompare(compare));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)}.
     */
    @org.junit.Test
    public void testCompute() {
        Expression a = new LiteralExpression(1.0);
        Expression b = new LiteralExpression(2.0);
        Expression c = new LiteralExpression(1.0);
        Expression d = new LiteralExpression(1.0);
        Expression e = new LiteralExpression(null);

        CoreOperationRelationalExpression expression = new GtExpression(new CoreOperationRelationalExpression[] { a, b });
        Object result = expression.compute(a, b);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        expression = new GeExpression(new CoreOperationRelationalExpression[] { a, b });
        result = expression.compute(a, b);
        assertFalse((boolean) result);

        expression = new GtExpression(new CoreOperationRelationalExpression[] { c, d });
        result = expression.compute(c, d);
        assertTrue((boolean) result);

        expression = new GeExpression(new CoreOperationRelationalExpression[] { c, d });
        result = expression.compute(c, d);
        assertTrue((boolean) result);

        expression = new LtExpression(new CoreOperationRelationalExpression[] { c, d });
        result = expression.compute(c, d);
        assertFalse((boolean) result);

        expression = new LeExpression(new CoreOperationRelationalExpression[] { c, d });
        result = expression.compute(c, d);
        assertFalse((boolean) result);

        expression = new LtExpression(new CoreOperationRelationalExpression[] { a, e });
        result = expression.compute(a, e);
        assertFalse((boolean) result);

        expression = new LeExpression(new CoreOperationRelationalExpression[] { a, e });
        result = expression.compute(a, e);
        assertFalse((boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#reduce(Object)}.
     */
    @org.junit.Test
    public void testReduce() {
        Expression a = new LiteralExpression(1.0);
        Expression b = new SelfContext();
        Expression c = new InitialContext();
        Expression d = new LiteralExpression(new LinkedList<>());
        Expression e = new IteratorExpression(new LinkedList<Object>());

        Object result = expression.reduce(a);
        assertNotNull(result);
        assertEquals(a, result);

        result = expression.reduce(b);
        assertNotNull(result);
        assertTrue(result instanceof SelfContext);

        result = expression.reduce(c);
        assertNotNull(result);
        assertTrue(result instanceof InitialContext);

        result = expression.reduce(d);
        assertNotNull(result);
        assertTrue(result instanceof Iterator);

        result = expression.reduce(e);
        assertNotNull(result);
        assertTrue(result instanceof Iterator);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#containsMatch(Iterator, Object)}.
     */
    @org.junit.Test
    public void testContainsMatch() {
        List list = new LinkedList<>();
        list.add(1.0);
        list.add(2.0);
        list.add(3.0);
        Iterator iterator = list.iterator();

        Expression a = new LiteralExpression(1.0);
        Expression b = new LiteralExpression(2.0);
        CoreOperationRelationalExpression expression = new GtExpression(new CoreOperationRelationalExpression[] { a, b });

        boolean result = expression.containsMatch(iterator, a);
        assertFalse(result);

        result = expression.containsMatch(iterator, b);
        assertFalse(result);

        result = expression.containsMatch(iterator, new LiteralExpression(3.0));
        assertTrue(result);

        expression = new EqExpression(new CoreOperationRelationalExpression[] { a, b });
        result = expression.containsMatch(iterator, a);
        assertTrue(result);

        result = expression.containsMatch(iterator, b);
        assertTrue(result);

        result = expression.containsMatch(iterator, new LiteralExpression(3.0));
        assertFalse(result);

        expression = new LtExpression(new CoreOperationRelationalExpression[] { a, b });
        result = expression.containsMatch(iterator, a);
        assertTrue(result);

        result = expression.containsMatch(iterator, b);
        assertFalse(result);

        result = expression.containsMatch(iterator, new LiteralExpression(1.0));
        assertTrue(result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#findMatch(Iterator, Iterator)}.
     */
    @org.junit.Test
    public void testFindMatch() {
        List listA = new LinkedList<>();
        listA.add(1.0);
        listA.add(2.0);
        listA.add(3.0);
        Iterator iteratorA = listA.iterator();

        List listB = new LinkedList<>();
        listB.add(2.0);
        listB.add(3.0);
        listB.add(4.0);
        Iterator iteratorB = listB.iterator();

        CoreOperationRelationalExpression expression = new EqExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(2.0), new LiteralExpression(2.0) });
        boolean result = expression.findMatch(iteratorA, iteratorB);
        assertTrue(result);

        expression = new GtExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(1.0), new LiteralExpression(2.0) });
        result = expression.findMatch(iteratorA, iteratorB);
        assertFalse(result);

        expression = new LtExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(3.0), new LiteralExpression(2.0) });
        result = expression.findMatch(iteratorA, iteratorB);
        assertFalse(result);

        expression = new GeExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(2.0), new LiteralExpression(2.0) });
        result = expression.findMatch(iteratorA, iteratorB);
        assertTrue(result);

        expression = new LeExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(2.0), new LiteralExpression(2.0) });
        result = expression.findMatch(iteratorA, iteratorB);
        assertTrue(result);

        expression = new GtExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(3.0), new LiteralExpression(2.0) });
        result = expression.findMatch(iteratorA, iteratorB);
        assertFalse(result);

        expression = new LtExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(2.0), new LiteralExpression(3.0) });
        result = expression.findMatch(iteratorA, iteratorB);
        assertFalse(result);

        expression = new GeExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(2.0), new LiteralExpression(3.0) });
        result = expression.findMatch(iteratorA, iteratorB);
        assertFalse(result);

        expression = new LeExpression(new CoreOperationRelationalExpression[] { new LiteralExpression(2.0), new LiteralExpression(3.0) });
        result = expression.findMatch(iteratorA, iteratorB);
        assertFalse(result);
    }

    private static int RELATIONAL_EXPR_PRECEDENCE = 2;

    private static void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Expected: <" + expected + ">. But was: <" + actual + ">.");
        }
    }
}