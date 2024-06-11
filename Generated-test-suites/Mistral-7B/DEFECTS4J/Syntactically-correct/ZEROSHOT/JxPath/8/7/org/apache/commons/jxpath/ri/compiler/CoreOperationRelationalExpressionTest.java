package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import org.junit.Test;

/**
 * Test class for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNotNull(expression);
    }

    /**
     * Test computeValue method.
     */
    @Test
    public void testComputeValue() {
        Expression left = new Const(1);
        Expression right = new Const(2);
        CoreOperationRelationalExpression expression = new GtExpression(new Expression[2], new ComparisonOperator('>', false));
        EvalContext context = new EvalContext(new InitialContext(new Object[0]));

        Object result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Const(2);
        right = new Const(1);
        expression = new GtExpression(new Expression[2], new ComparisonOperator('>', false));
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Const(2);
        right = new Const(2);
        expression = new GteExpression(new Expression[2], new ComparisonOperator('>=', false));
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Const(2);
        right = new Const(1);
        expression = new GteExpression(new Expression[2], new ComparisonOperator('>=', false));
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Const(1);
        right = new Const(1);
        expression = new EqExpression(new Expression[2], new ComparisonOperator('=', false));
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Const(1);
        right = new Const(2);
        expression = new NeExpression(new Expression[2], new ComparisonOperator('!=', false));
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Const(1.0);
        right = new Const(2.0);
        expression = new GtExpression(new Expression[2], new ComparisonOperator('>', false));
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Const(2.0);
        right = new Const(1.0);
        expression = new GtExpression(new Expression[2], new ComparisonOperator('>', false));
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Const(2.0);
        right = new Const(2.0);
        expression = new GteExpression(new Expression[2], new ComparisonOperator('>=', false));
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Const(2.0);
        right = new Const(1.0);
        expression = new GteExpression(new Expression[2], new ComparisonOperator('>=', false));
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));

        left = new Const(1.0);
        right = new Const(1.0);
        expression = new EqExpression(new Expression[2], new ComparisonOperator('=', false));
        result = expression.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        left = new Const(1.0);
        right = new Const(2.0);
        expression = new NeExpression(new Expression[2], new ComparisonOperator('!=', false));
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new GtExpression(new Expression[2], new ComparisonOperator('>', false));
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);

        expression = new NeExpression(new Expression[2], new ComparisonOperator('!=', false));
        precedence = expression.getPrecedence();
        assertEquals(3, precedence);

        expression = new GteExpression(new Expression[2], new ComparisonOperator('>=', false));
        precedence = expression.getPrecedence();
        assertEquals(3, precedence);

        expression = new LtExpression(new Expression[2], new ComparisonOperator('<', false));
        precedence = expression.getPrecedence();
        assertEquals(3, precedence);

        expression = new LeExpression(new Expression[2], new ComparisonOperator('<=', false));
        precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new GtExpression(new Expression[2], new ComparisonOperator('>', false));
        assertFalse(expression.isSymmetric());

        expression = new GteExpression(new Expression[2], new ComparisonOperator('>=', false));
        assertFalse(expression.isSymmetric());

        expression = new LtExpression(new Expression[2], new ComparisonOperator('<', false));
        assertFalse(expression.isSymmetric());

        expression = new LeExpression(new Expression[2], new ComparisonOperator('<=', false));
        assertFalse(expression.isSymmetric());

        expression = new EqExpression(new Expression[2], new ComparisonOperator('=', false));
        assertTrue(expression.isSymmetric());

        expression = new NeExpression(new Expression[2], new ComparisonOperator('!=', false));
        assertTrue(expression.isSymmetric());
    }

    /**
     * Test evaluateCompare method.
     */
    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new GtExpression(new Expression[2], new ComparisonOperator('>', false));
        assertTrue(expression.evaluateCompare(-1) == -1);
        assertTrue(expression.evaluateCompare(0) == -1);
        assertTrue(expression.evaluateCompare(1) == 1);

        expression = new GteExpression(new Expression[2], new ComparisonOperator('>=', false));
        assertTrue(expression.evaluateCompare(-1) == -1);
        assertTrue(expression.evaluateCompare(0) == 0);
        assertTrue(expression.evaluateCompare(1) == 1);

        expression = new LtExpression(new Expression[2], new ComparisonOperator('<', false));
        assertTrue(expression.evaluateCompare(-1) == -1);
        assertTrue(expression.evaluateCompare(0) == -1);
        assertTrue(expression.evaluateCompare(1) == -1);

        expression = new LeExpression(new Expression[2], new ComparisonOperator('<=', false));
        assertTrue(expression.evaluateCompare(-1) == -1);
        assertTrue(expression.evaluateCompare(0) == 0);
        assertTrue(expression.evaluateCompare(1) == 0);

        expression = new EqExpression(new Expression[2], new ComparisonOperator('=', false));
        assertTrue(expression.evaluateCompare(0) == 0);
        assertTrue(expression.evaluateCompare(1) == 0);

        expression = new NeExpression(new Expression[2], new ComparisonOperator('!=', false));
        assertTrue(expression.evaluateCompare(0) == -1);
        assertTrue(expression.evaluateCompare(1) == -1);
    }

    /**
     * Test reduce method.
     */
    @Test
    public void testReduce() {
        Object obj = new SelfContext();
        Object result = new CoreOperationRelationalExpression(new Expression[0]).reduce(obj);
        assertNull(result);

        obj = new InitialContext(new Object[0]);
        result = new CoreOperationRelationalExpression(new Expression[0]).reduce(obj);
        assertNotNull(result);

        obj = new Const(new Integer(1));
        result = new CoreOperationRelationalExpression(new Expression[0]).reduce(obj);
        assertNotNull(result);

        obj = new IteratorList(Arrays.asList(new Const(new Integer(1)), new Const(new Integer(2))).iterator());
        result = new CoreOperationRelationalExpression(new Expression[0]).reduce(obj);
        assertNotNull(result);
    }

    /**
     * Test containsMatch method.
     */
    @Test
    public void testContainsMatch() {
        Iterator it = Arrays.asList(new Const(new Integer(1)), new Const(new Integer(2))).iterator();
        Object value = new Const(new Integer(1));
        boolean result = new CoreOperationRelationalExpression(new Expression[0]).containsMatch(it, value);
        assertTrue(result);

        it = Arrays.asList(new Const(new Integer(1)), new Const(new Integer(2))).iterator();
        value = new Const(new Integer(3));
        result = new CoreOperationRelationalExpression(new Expression[0]).containsMatch(it, value);
        assertFalse(result);

        it = Arrays.asList(new Const(new Integer(1)), new Const(new Integer(2))).iterator();
        value = new Const(new Integer(1));
        result = new EqExpression(new Expression[2], new ComparisonOperator('=', false)).containsMatch(it, value);
        assertTrue(result);

        it = Arrays.asList(new Const(new Integer(1)), new Const(new Integer(2))).iterator();
        value = new Const(new Integer(1));
        result = new NeExpression(new Expression[2], new ComparisonOperator('!=', false)).containsMatch(it, value);
        assertFalse(result);
    }

    /**
     * Test findMatch method.
     */
    @Test
    public void testFindMatch() {
        Iterator lit = Arrays.asList(new Const(new Integer(1)), new Const(new Integer(2))).iterator();
        Iterator rit = Arrays.asList(new Const(new Integer(1)), new Const(new Integer(1))).iterator();
        boolean result = new CoreOperationRelationalExpression(new Expression[0]).findMatch(lit, rit);
        assertTrue(result);

        lit = Arrays.asList(new Const(new Integer(1)), new Const(new Integer(2))).iterator();
        rit = Arrays.asList(new Const(new Integer(1)), new Const(new Integer(2))).iterator();
        result = new CoreOperationRelationalExpression(new Expression[0]).findMatch(lit, rit);
        assertTrue(result);

        lit = Arrays.asList(new Const(new Integer(1)), new Const(new Integer(2))).iterator();
        rit = Arrays.asList(new Const(new Integer(2)), new Const(new Integer(1))).iterator();
        result = new CoreOperationRelationalExpression(new Expression[0]).findMatch(lit, rit);
        assertFalse(result);

        lit = Arrays.asList(new Const(new Integer(1)), new Const(new Integer(2))).iterator();
        rit = Arrays.asList(new Const(new Integer(2)), new Const(new Integer(3))).iterator();
        result = new CoreOperationRelationalExpression(new Expression[0]).findMatch(lit, rit);
        assertFalse(result);
    }

}