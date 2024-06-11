package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        assertEquals("Unexpected expression type.", CoreOperationRelationalExpression.class, expr.getClass());
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        int precedence = expr.getPrecedence();
        assertEquals("Unexpected precedence.", 3, precedence);
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        boolean symmetric = expr.isSymmetric();
        assertEquals("Unexpected symmetry.", false, symmetric);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        CoreOperationRelationalExpression expr1 = new CoreOperationRelationalExpression(new Expression[0]);
        CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[0]);
        assertEquals("Unexpected equality.", expr1, expr2);

        Expression arg1 = new Expression() {
            @Override
            public int getPrecedence() {
                return 0;
            }

            @Override
            public String toString() {
                return "arg1";
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public int getPrecedence() {
                return 0;
            }

            @Override
            public String toString() {
                return "arg2";
            }
        };

        Expression arg3 = new Expression() {
            @Override
            public int getPrecedence() {
                return 0;
            }

            @Override
            public String toString() {
                return "arg3";
            }
        };

        CoreOperationRelationalExpression expr3 = new CoreOperationRelationalExpression(new Expression[] {arg1, arg2});
        CoreOperationRelationalExpression expr4 = new CoreOperationRelationalExpression(new Expression[] {arg2, arg1});

        assertEquals("Unexpected inequality when arguments are the same in both expressions.", expr3, expr4);
        assertFalse("Unexpected equality when arguments are different.", expr3, expr4);
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() throws CloneNotSupportedException {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        CoreOperationRelationalExpression clonedExpr = (CoreOperationRelationalExpression) expr.clone();
        assertEquals("Unexpected cloned expression type.", CoreOperationRelationalExpression.class, clonedExpr.getClass());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        String str = expr.toString();
        assertTrue("UnexpectedtoString representation.", str.contains("CoreOperationRelationalExpression"));
    }
}