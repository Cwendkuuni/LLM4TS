package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}
     * with arguments where the first expression is greater than the second expression.
     */
    @Test
    public void testGreaterThan() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(3);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreOperation[] { left, right });
        assertTrue(expression.evaluate(new Object[] {}));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}
     * with arguments where the first expression is not greater than the second expression.
     */
    @Test
    public void testNotGreaterThan() {
        Expression left = new CoreNumberExpression(3);
        Expression right = new CoreNumberExpression(5);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreOperation[] { left, right });
        assertFalse(expression.evaluate(new Object[] {}));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}
     * with arguments where the first expression is equal to the second expression.
     */
    @Test
    public void testEqual() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(5);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreOperation[] { left, right }); // Using CoreOperationGreaterThanExpression instead of CoreOperationEqualExpression for testing equality
        assertTrue(expression.evaluate(new Object[] {}));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}
     * with arguments where the first expression is less than the second expression.
     */
    @Test
    public void testLessThan() {
        Expression left = new CoreNumberExpression(3);
        Expression right = new CoreNumberExpression(5);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanExpression(new CoreOperation[] { left, right });
        assertFalse(expression.evaluate(new Object[] {}));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}
     * with arguments where the first expression is not less than the second expression.
     */
    @Test
    public void testNotLessThan() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(3);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanExpression(new CoreOperation[] { left, right });
        assertFalse(expression.evaluate(new Object[] {}));

        // Test with swapped arguments
        CoreOperationRelationalExpression expressionSwapped = new CoreOperationLessThanExpression(new CoreOperation[] { right, left });
        assertTrue(expressionSwapped.evaluate(new Object[] {}));
    }
}