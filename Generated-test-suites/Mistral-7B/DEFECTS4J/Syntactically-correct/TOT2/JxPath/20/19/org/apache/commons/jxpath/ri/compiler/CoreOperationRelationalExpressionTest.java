// CoreOperationRelationalExpressionTest.java

package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.LiteralExpression;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new LiteralExpression(10);
        Expression right = new LiteralExpression(5);
        CoreOperationRelationalExpression operation = new GreaterThanExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext(new Object[]{});

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        left = new LiteralExpression(15);
        right = new LiteralExpression(5);
        operation = new GreaterThanExpression(new Expression[]{left, right});

        result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithNullLeft() {
        Expression left = null;
        Expression right = new LiteralExpression(5);
        CoreOperationRelationalExpression operation = new GreaterThanExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext(new Object[]{});

        Object result = operation.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testComputeValueWithNullRight() {
        Expression left = new LiteralExpression(10);
        Expression right = null;
        CoreOperationRelationalExpression operation = new GreaterThanExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext(new Object[]{});

        Object result = operation.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testComputeValueWithSameObjects() {
        Expression left = new LiteralExpression(10);
        Expression right = new LiteralExpression(10);
        CoreOperationRelationalExpression operation = new GreaterThanExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext(new Object[]{});

        Object result = operation.computeValue(context);

        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueWithDifferentObjectsSameValue() {
        Expression left = new LiteralExpression("10");
        Expression right = new LiteralExpression(10);
        CoreOperationRelationalExpression operation = new GreaterThanExpression(new Expression[]{left, right});
        EvalContext context = new EvalContext(new Object[]{});

        Object result = operation.computeValue(context);

        assertFalse(result instanceof Boolean);
    }

    // Add test cases for other methods here

}

//