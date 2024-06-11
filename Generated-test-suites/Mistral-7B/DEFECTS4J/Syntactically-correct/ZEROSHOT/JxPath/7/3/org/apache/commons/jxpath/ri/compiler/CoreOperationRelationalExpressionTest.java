package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        assertInstanceOf(CoreOperationRelationalExpression.class, expr);
        assertArrayEquals(args, expr.getArguments());
        assertEquals(3, expr.getPrecedence());
        assertFalse(expr.isSymmetric());
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new Expression[] { new ConstantExpression(3), new ConstantExpression(2) });
        assertEquals(3, expr.getPrecedence());
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new Expression[] { new ConstantExpression(3), new ConstantExpression(2) });
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationGreaterThanOrEqual(new Expression[] { new ConstantExpression(3), new ConstantExpression(2) });
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationLessThan(new Expression[] { new ConstantExpression(3), new ConstantExpression(2) });
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationLessThanOrEqual(new Expression[] { new ConstantExpression(3), new ConstantExpression(2) });
        assertFalse(expr.isSymmetric());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Expression[] args1 = new Expression[] { new ConstantExpression(3), new ConstantExpression(2) };
        Expression[] args2 = new Expression[] { new ConstantExpression(2), new ConstantExpression(3) };

        CoreOperationRelationalExpression expr1 = new CoreOperationGreaterThan(args1);
        CoreOperationRelationalExpression expr2 = new CoreOperationGreaterThan(args2);
        assertFalse(expr1.equals(expr2));

        CoreOperationRelationalExpression expr3 = new CoreOperationGreaterThanOrEqual(args1);
        assertFalse(expr1.equals(expr3));

        CoreOperationRelationalExpression expr4 = new CoreOperationGreaterThanOrEqual(args2);
        assertFalse(expr2.equals(expr4));

        CoreOperationRelationalExpression expr5 = new CoreOperationGreaterThan(args1);
        CoreOperationRelationalExpression expr6 = new CoreOperationGreaterThan(args1);
        assertTrue(expr5.equals(expr6));

        CoreOperationRelationalExpression expr7 = new CoreOperationGreaterThanOrEqual(args1);
        CoreOperationRelationalExpression expr8 = new CoreOperationGreaterThanOrEqual(args1);
        assertTrue(expr7.equals(expr8));

        CoreOperationRelationalExpression expr9 = new CoreOperationLessThan(args1);
        CoreOperationRelationalExpression expr10 = new CoreOperationLessThan(args1);
        assertTrue(expr9.equals(expr10));

        CoreOperationRelationalExpression expr11 = new CoreOperationLessThanOrEqual(args1);
        CoreOperationRelationalExpression expr12 = new CoreOperationLessThanOrEqual(args1);
        assertTrue(expr11.equals(expr12));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        Expression[] args1 = new Expression[] { new ConstantExpression(3), new ConstantExpression(2) };
        Expression[] args2 = new Expression[] { new ConstantExpression(2), new ConstantExpression(3) };

        CoreOperationRelationalExpression expr1 = new CoreOperationGreaterThan(args1);
        CoreOperationRelationalExpression expr2 = new CoreOperationGreaterThan(args2);
        assertNotSame(expr1.hashCode(), expr2.hashCode());

        CoreOperationRelationalExpression expr3 = new CoreOperationGreaterThanOrEqual(args1);
        assertNotSame(expr3.hashCode(), expr1.hashCode());

        CoreOperationRelationalExpression expr4 = new CoreOperationGreaterThanOrEqual(args2);
        assertNotSame(expr4.hashCode(), expr2.hashCode());

        CoreOperationRelationalExpression expr5 = new CoreOperationGreaterThan(args1);
        CoreOperationRelationalExpression expr6 = new CoreOperationGreaterThan(args1);
        assertEquals(expr5.hashCode(), expr6.hashCode());

        CoreOperationRelationalExpression expr7 = new CoreOperationGreaterThanOrEqual(args1);
        CoreOperationRelationalExpression expr8 = new CoreOperationGreaterThanOrEqual(args1);
        assertEquals(expr7.hashCode(), expr8.hashCode());

        CoreOperationRelationalExpression expr9 = new CoreOperationLessThan(args1);
        CoreOperationRelationalExpression expr10 = new CoreOperationLessThan(args1);
        assertEquals(expr9.hashCode(), expr10.hashCode());

        CoreOperationRelationalExpression expr11 = new CoreOperationLessThanOrEqual(args1);
        CoreOperationRelationalExpression expr12 = new CoreOperationLessThanOrEqual(args1);
        assertEquals(expr11.hashCode(), expr12.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Expression[] args1 = new Expression[] { new ConstantExpression(3), new ConstantExpression(2) };

        CoreOperationRelationalExpression expr1 = new CoreOperationGreaterThan(args1);
        String expected = "(" + CoreOperation.class.getName() + " greaterThan " + Arrays.toString(args1) + ")";
        String actual = expr1.toString();
        assertEquals(expected, actual);

        CoreOperationRelationalExpression expr2 = new CoreOperationGreaterThanOrEqual(args1);
        expected = "(" + CoreOperation.class.getName() + " greaterThanOrEqual " + Arrays.toString(args1) + ")";
        actual = expr2.toString();
        assertEquals(expected, actual);

        CoreOperationRelationalExpression expr3 = new CoreOperationLessThan(args1);
        expected = "(" + CoreOperation.class.getName() + " lessThan " + Arrays.toString(args1) + ")";
        actual = expr3.toString();
        assertEquals(expected, actual);

        CoreOperationRelationalExpression expr4 = new CoreOperationLessThanOrEqual(args1);
        expected = "(" + CoreOperation.class.getName() + " lessThanOrEqual " + Arrays.toString(args1) + ")";
        actual = expr4.toString();
        assertEquals(expected, actual);
    }

}