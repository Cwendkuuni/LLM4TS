package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.Op;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThanExpression() {
        Expression left = new VariableExpression("$left");
        Expression right = new NumberExpression(5);
        CoreOperationRelationalExpression gtExpression = new CoreOperationRelationalExpression(new CoreOperationBinary(Op.GT, left, right)) {
        };

        Object result = null;
        try {
            result = gtExpression.compileToExpression(new JXPathContext());
            JXPathContext context = new JXPathContext();
            context.setValues(new Object[]{"$left", 6});
            assertEquals(true, ((Boolean) result.getValue(context)).booleanValue());
            context.setValues(new Object[]{"$left", 5});
            assertEquals(false, ((Boolean) result.getValue(context)).booleanValue());
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testGreaterThanOrEqualExpression() {
        Expression left = new VariableExpression("$left");
        Expression right = new NumberExpression(5);
        CoreOperationRelationalExpression gteExpression = new CoreOperationRelationalExpression(new CoreOperationBinary(Op.GTE, left, right)) {
        };

        Object result = null;
        try {
            result = gteExpression.compileToExpression(new JXPathContext());
            JXPathContext context = new JXPathContext();
            context.setValues(new Object[]{"$left", 5});
            assertEquals(false, ((Boolean) result.getValue(context)).booleanValue());
            context.setValues(new Object[]{"$left", 6});
            assertEquals(true, ((Boolean) result.getValue(context)).booleanValue());
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testLessThanExpression() {
        Expression left = new VariableExpression("$left");
        Expression right = new NumberExpression(5);
        CoreOperationRelationalExpression ltExpression = new CoreOperationRelationalExpression(new CoreOperationBinary(Op.LT, left, right)) {
        };

        Object result = null;
        try {
            result = ltExpression.compileToExpression(new JXPathContext());
            JXPathContext context = new JXPathContext();
            context.setValues(new Object[]{"$left", 5});
            assertEquals(true, ((Boolean) result.getValue(context)).booleanValue());
            context.setValues(new Object[]{"$left", 6});
            assertEquals(false, ((Boolean) result.getValue(context)).booleanValue());
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testLessThanOrEqualExpression() {
        Expression left = new VariableExpression("$left");
        Expression right = new NumberExpression(5);
        CoreOperationRelationalExpression lteExpression = new CoreOperationRelationalExpression(new CoreOperationBinary(Op.LTE, left, right)) {
        };

        Object result = null;
        try {
            result = lteExpression.compileToExpression(new JXPathContext());
            JXPathContext context = new JXPathContext();
            context.setValues(new Object[]{"$left", 5});
            assertEquals(true, ((Boolean) result.getValue(context)).booleanValue());
            context.setValues(new Object[]{"$left", 6});
            assertEquals(true, ((Boolean) result.getValue(context)).booleanValue());
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }
}