package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.utils.TestUtils;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    /**
     * Test for greater than operator.
     */
    @Test
    public void testGT() {
        Expression left = new Const(new Integer(2));
        Expression right = new Const(new Integer(1));
        CoreOperationRelationalExpression op = new GT(new CoreOperationUnaryMinus(right), left);

        EvalContext context = TestUtils.createEvalContext();
        InfoSet infoSet = TestUtils.createInfoSet(2, 3, 1, 4);
        context.setInfoSet(infoSet);

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        infoSet = TestUtils.createInfoSet(1, 3, 1, 4);
        context.setInfoSet(infoSet);

        result = op.computeValue(context);

        assertFalse(result instanceof Boolean);
    }

    /**
     * Test for greater than or equal operator.
     */
    @Test
    public void testGTE() {
        Expression left = new Const(new Integer(2));
        Expression right = new Const(new Integer(1));
        CoreOperationRelationalExpression op = new GTE(left, right);

        EvalContext context = TestUtils.createEvalContext();
        InfoSet infoSet = TestUtils.createInfoSet(2, 3, 1, 4);
        context.setInfoSet(infoSet);

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        infoSet = TestUtils.createInfoSet(2, 3, 1, 4);
        context.setInfoSet(infoSet);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        infoSet = TestUtils.createInfoSet(1, 3, 1, 4);
        context.setInfoSet(infoSet);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test for less than operator.
     */
    @Test
    public void testLT() {
        Expression left = new Const(new Integer(2));
        Expression right = new Const(new Integer(1));
        CoreOperationRelationalExpression op = new LT(left, right);

        EvalContext context = TestUtils.createEvalContext();
        InfoSet infoSet = TestUtils.createInfoSet(2, 3, 1, 4);
        context.setInfoSet(infoSet);

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        infoSet = TestUtils.createInfoSet(1, 3, 1, 4);
        context.setInfoSet(infoSet);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test for less than or equal operator.
     */
    @Test
    public void testLTE() {
        Expression left = new Const(new Integer(2));
        Expression right = new Const(new Integer(1));
        CoreOperationRelationalExpression op = new LTE(left, right);

        EvalContext context = TestUtils.createEvalContext();
        InfoSet infoSet = TestUtils.createInfoSet(2, 3, 1, 4);
        context.setInfoSet(infoSet);

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        infoSet = TestUtils.createInfoSet(2, 3, 1, 4);
        context.setInfoSet(infoSet);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        infoSet = TestUtils.createInfoSet(2, 3, 1, 4);
        context.setInfoSet(infoSet);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test for equality operator.
     */
    @Test
    public void testEQ() {
        Expression left = new Const(new Integer(2));
        Expression right = new Const(new Integer(2));
        CoreOperationRelationalExpression op = new EQ(left, right);

        EvalContext context = TestUtils.createEvalContext();
        InfoSet infoSet = TestUtils.createInfoSet(2, 3, 1, 4);
        context.setInfoSet(infoSet);

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test for inequality operator.
     */
    @Test
    public void testNE() {
        Expression left = new Const(new Integer(2));
        Expression right = new Const(new Integer(2));
        CoreOperationRelationalExpression op = new NE(left, right);

        EvalContext context = TestUtils.createEvalContext();
        InfoSet infoSet = TestUtils.createInfoSet(2, 3, 1, 4);
        context.setInfoSet(infoSet);

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        right = new Const(new Integer(1));

        context.setInfoSet(infoSet);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test for equality operator with Iterators.
     */
    @Test
    public void testIteratorsEQ() {
        Expression left = new IteratorExpr(Arrays.asList(1, 2, 3).iterator());
        Expression right = new IteratorExpr(Arrays.asList(1, 2, 3).iterator());
        CoreOperationRelationalExpression op = new EQ(left, right);

        EvalContext context = TestUtils.createEvalContext();
        InfoSet infoSet = TestUtils.createInfoSet(1, 2, 3);
        context.setInfoSet(infoSet);

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test for inequality operator with Iterators.
     */
    @Test
    public void testIteratorsNE() {
        Expression left = new IteratorExpr(Collections.singletonList(1).iterator());
        Expression right = new IteratorExpr(Arrays.asList(1, 2, 3).iterator());
        CoreOperationRelationalExpression op = new NE(left, right);

        EvalContext context = TestUtils.createEvalContext();
        InfoSet infoSet = TestUtils.createInfoSet(1);
        context.setInfoSet(infoSet);

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test for greater than operator with Iterators.
     */
    @Test
    public void testIteratorsGT() {
        Expression left = new IteratorExpr(Arrays.asList(1, 2, 3).iterator());
        Expression right = new IteratorExpr(Arrays.asList(3, 4).iterator());
        CoreOperationRelationalExpression op = new GT(left, right);

        EvalContext context = TestUtils.createEvalContext();
        InfoSet infoSet = TestUtils.createInfoSet();
        context.setInfoSet(infoSet);

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        infoSet = TestUtils.createInfoSet(1, 2, 3, 4);
        context.setInfoSet(infoSet);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test for less than operator with Iterators.
     */
    @Test
    public void testIteratorsLT() {
        Expression left = new IteratorExpr(Arrays.asList(1, 2, 3).iterator());
        Expression right = new IteratorExpr(Arrays.asList(3, 4).iterator());
        CoreOperationRelationalExpression op = new LT(left, right);

        EvalContext context = TestUtils.createEvalContext();
        InfoSet infoSet = TestUtils.createInfoSet();
        context.setInfoSet(infoSet);

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        infoSet = TestUtils.createInfoSet(1, 2, 3, 4);
        context.setInfoSet(infoSet);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test for greater than or equal operator with Iterators.
     */
    @Test
    public void testIteratorsGTE() {
        Expression left = new IteratorExpr(Arrays.asList(1, 2, 3).iterator());
        Expression right = new IteratorExpr(Arrays.asList(3, 4).iterator());
        CoreOperationRelationalExpression op = new GTE(left, right);

        EvalContext context = TestUtils.createEvalContext();
        InfoSet infoSet = TestUtils.createInfoSet();
        context.setInfoSet(infoSet);

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        infoSet = TestUtils.createInfoSet(1, 2, 3, 4);
        context.setInfoSet(infoSet);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test for less than or equal operator with Iterators.
     */
    @Test
    public void testIteratorsLTE() {
        Expression left = new IteratorExpr(Arrays.asList(1, 2, 3).iterator());
        Expression right = new IteratorExpr(Arrays.asList(3, 4).iterator());
        CoreOperationRelationalExpression op = new LTE(left, right);

        EvalContext context = TestUtils.createEvalContext();
        InfoSet infoSet = TestUtils.createInfoSet();
        context.setInfoSet(infoSet);

        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        infoSet = TestUtils.createInfoSet(1, 2, 3, 4);
        context.setInfoSet(infoSet);

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}