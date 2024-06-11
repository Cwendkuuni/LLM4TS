package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() throws Exception {
        Expression left = ExpressionFactory.createNodeTest(NodeTest.GT, "1");
        Expression right = ExpressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[]{left, right});

        JXPathContext jxpathContext = new JXPathContext(new Object[]{1.0f});
        Object result = op.compile(jxpathContext).evaluate(jxpathContext);

        assertFalse(result instanceof Boolean);

        left = ExpressionFactory.createNumber(2.0);
        right = ExpressionFactory.createNumber(1.0);

        op = new CoreOperationGreaterThan(new Expression[]{left, right});

        jxpathContext = new JXPathContext(new Object[]{1.0f, 2.0f});
        result = op.compile(jxpathContext).evaluate(jxpathContext);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqual() throws Exception {
        Expression left = ExpressionFactory.createNumber(1.0);
        Expression right = ExpressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression op = new CoreOperationGreaterThanOrEqual(new Expression[]{left, right});

        JXPathContext jxpathContext = new JXPathContext(new Object[]{1.0f, 2.0f});
        Object result = op.compile(jxpathContext).evaluate(jxpathContext);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = ExpressionFactory.createNumber(2.0);
        right = ExpressionFactory.createNumber(2.0);

        op = new CoreOperationGreaterThanOrEqual(new Expression[]{left, right});

        jxpathContext = new JXPathContext(new Object[]{1.0f, 2.0f});
        result = op.compile(jxpathContext).evaluate(jxpathContext);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = ExpressionFactory.createNumber(2.0);
        right = ExpressionFactory.createNumber(1.0);

        op = new CoreOperationGreaterThanOrEqual(new Expression[]{left, right});

        jxpathContext = new JXPathContext(new Object[]{1.0f, 2.0f});
        result = op.compile(jxpathContext).evaluate(jxpathContext);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() throws Exception {
        Expression left = ExpressionFactory.createNumber(1.0);
        Expression right = ExpressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression op = new CoreOperationLessThan(new Expression[]{left, right});

        JXPathContext jxpathContext = new JXPathContext(new Object[]{1.0f, 2.0f});
        Object result = op.compile(jxpathContext).evaluate(jxpathContext);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = ExpressionFactory.createNumber(2.0);
        right = ExpressionFactory.createNumber(1.0);

        op = new CoreOperationLessThan(new Expression[]{left, right});

        jxpathContext = new JXPathContext(new Object[]{1.0f, 2.0f});
        result = op.compile(jxpathContext).evaluate(jxpathContext);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() throws Exception {
        Expression left = ExpressionFactory.createNumber(1.0);
        Expression right = ExpressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression op = new CoreOperationLessThanOrEqual(new Expression[]{left, right});

        JXPathContext jxpathContext = new JXPathContext(new Object[]{1.0f, 2.0f});
        Object result = op.compile(jxpathContext).evaluate(jxpathContext);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = ExpressionFactory.createNumber(1.0);
        right = ExpressionFactory.createNumber(1.0);

        op = new CoreOperationLessThanOrEqual(new Expression[]{left, right});

        jxpathContext = new JXPathContext(new Object[]{1.0f, 1.0f});
        result = op.compile(jxpathContext).evaluate(jxpathContext);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = ExpressionFactory.createNumber(2.0);
        right = ExpressionFactory.createNumber(1.0);

        op = new CoreOperationLessThanOrEqual(new Expression[]{left, right});

        jxpathContext = new JXPathContext(new Object[]{1.0f, 2.0f});
        result = op.compile(jxpathContext).evaluate(jxpathContext);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() throws Exception {
        Expression left = ExpressionFactory.createNumber(1.0);
        Expression right = null;
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[]{left, right});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments2() throws Exception {
        Expression left = null;
        Expression right = ExpressionFactory.createNumber(1.0);
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[]{left, right});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments3() throws Exception {
        Expression left = ExpressionFactory.createNodeTest(NodeTest.GT, "1");
        Expression right = null;
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[]{left, right});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments4() throws Exception {
        Expression left = null;
        Expression right = ExpressionFactory.createNodeTest(NodeTest.GT, "1");
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[]{left, right});
    }

    @Test
    public void testToString() throws Exception {
        Expression left = ExpressionFactory.createNumber(1.0);
        Expression right = ExpressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[]{left, right});

        String expected = "> [1.0, 2.0]";
        assertEquals(expected, op.toString());
    }

}