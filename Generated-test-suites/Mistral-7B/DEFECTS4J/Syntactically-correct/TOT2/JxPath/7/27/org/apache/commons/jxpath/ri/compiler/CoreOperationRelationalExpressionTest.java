package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        CoreOperation[] args = new CoreOperation[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertInstanceOf(CoreOperationRelationalExpression.class, expression);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidArgument() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {null});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorLowerPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {new CoreOperationAddition()});
    }

    @Test(expected = NullPointerException.class)
    public void testEvaluateNullParent() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        expression.evaluate(null);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        assertFalse(expression.isSymmetric());
    }

}