package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionInstance() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertInstanceOf(CoreOperationRelationalExpression.class, expression);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testCoreOperationRelationalExpressionInstanceWithArgs() {
        Expression left = new LiteralExpression(1);
        Expression right = new LiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{left, right});
        assertInstanceOf(CoreOperationRelationalExpression.class, expression);
        assertEquals(left, expression.getLeftExpression());
        assertEquals(right, expression.getRightExpression());
    }

    @Test
    public void testOperatorGreaterThan() {
        Expression left = new LiteralExpression(1);
        Expression right = new LiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{left, right});
        assertTrue(expression.isTrue(new Context(new Object[0], new Object[0])));
        assertFalse(expression.isTrue(new Context(new Object[]{new Integer(1)}, new Object[0])));
        assertFalse(expression.isTrue(new Context(new Object[0], new Object[]{new Integer(1)})));
    }

    @Test
    public void testOperatorGreaterThanOrEqual() {
        Expression left = new LiteralExpression(1);
        Expression right = new LiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new Expression[]{left, right});
        assertTrue(expression.isTrue(new Context(new Object[]{new Integer(2)}, new Object[0])));
        assertTrue(expression.isTrue(new Context(new Object[]{new Integer(1)}, new Object[0])));
        assertTrue(expression.isTrue(new Context(new Object[0], new Object[0])));
        assertFalse(expression.isTrue(new Context(new Object[]{new Integer(0)}, new Object[0])));
        assertFalse(expression.isTrue(new Context(new Object[]{new Integer(1)}, new Object[]{new Integer(2)})));
    }

    @Test
    public void testOperatorLessThan() {
        Expression left = new LiteralExpression(1);
        Expression right = new LiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new Expression[]{left, right});
        assertFalse(expression.isTrue(new Context(new Object[]{new Integer(2)}, new Object[0])));
        assertFalse(expression.isTrue(new Context(new Object[]{new Integer(1)}, new Object[0])));
        assertTrue(expression.isTrue(new Context(new Object[0], new Object[0])));
        assertTrue(expression.isTrue(new Context(new Object[]{new Integer(0)}, new Object[0])));
        assertFalse(expression.isTrue(new Context(new Object[]{new Integer(1)}, new Object[]{new Integer(2)})));
    }

    @Test
    public void testOperatorLessThanOrEqual() {
        Expression left = new LiteralExpression(1);
        Expression right = new LiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new Expression[]{left, right});
        assertTrue(expression.isTrue(new Context(new Object[]{new Integer(1)}, new Object[0])));
        assertTrue(expression.isTrue(new Context(new Object[]{new Integer(0)}, new Object[0])));
        assertTrue(expression.isTrue(new Context(new Object[0], new Object[0])));
        assertTrue(expression.isTrue(new Context(new Object[]{new Integer(1)}, new Object[]{new Integer(1)})));
        assertFalse(expression.isTrue(new Context(new Object[]{new Integer(2)}, new Object[0])));
        assertFalse(expression.isTrue(new Context(new Object[]{new Integer(1)}, new Object[]{new Integer(2)})));
    }

}