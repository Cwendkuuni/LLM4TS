package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionInstance() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        assertNotNull(coreOperationRelationalExpression);
        assertEquals(3, coreOperationRelationalExpression.getPrecedence());
        assertFalse(coreOperationRelationalExpression.isSymmetric());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionNullArguments() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionEmptyArguments() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]);
    }

    @Test
    public void testCoreOperationRelationalExpressionGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        assertEquals(3, coreOperationRelationalExpression.getPrecedence());
    }

    @Test
    public void testCoreOperationRelationalExpressionIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        assertFalse(coreOperationRelationalExpression.isSymmetric());
    }
}