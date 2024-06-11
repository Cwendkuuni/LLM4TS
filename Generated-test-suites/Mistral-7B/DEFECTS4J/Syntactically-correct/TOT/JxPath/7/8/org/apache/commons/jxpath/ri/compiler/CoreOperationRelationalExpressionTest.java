package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.TestCase;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.IntegerValue;

public class CoreOperationRelationalExpressionTest extends TestCase {

    public void testGreaterThan() {
        Expression left = new CoreOperationAdd(new IntegerValue(5), new IntegerValue(2));
        Expression right = new IntegerValue(7);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(left, right);

        JXPathContext ctx = new JXPathContext();
        Object result = expression.compile(ctx).evaluate(null);

        assertTrue("Expected true, got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    public void testGreaterThanWithInvalidArguments() {
        Expression left = new CoreOperationGreaterThan(new IntegerValue(2), new CoreOperationAdd(new IntegerValue(5), new IntegerValue(2)));

        try {
            left.compile(new JXPathContext());
            fail("Expected compilation error");
        } catch (Exception e) {
            // expected
        }
    }

    public void testGreaterThanOrEqual() {
        Expression left = new CoreOperationAdd(new IntegerValue(5), new IntegerValue(2));
        Expression right = new IntegerValue(7);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(left, right);

        JXPathContext ctx = new JXPathContext();
        Object result = expression.compile(ctx).evaluate(null);

        assertTrue("Expected true, got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());

        left = new CoreOperationAdd(new IntegerValue(5), new IntegerValue(2));
        right = new IntegerValue(5);
        expression = new CoreOperationGreaterThanOrEqual(left, right);

        result = expression.compile(ctx).evaluate(null);

        assertTrue("Expected true, got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    public void testLessThan() {
        Expression left = new CoreOperationAdd(new IntegerValue(5), new IntegerValue(2));
        Expression right = new IntegerValue(3);
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(left, right);

        JXPathContext ctx = new JXPathContext();
        Object result = expression.compile(ctx).evaluate(null);

        assertFalse("Expected false, got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    public void testLessThanOrEqual() {
        Expression left = new CoreOperationAdd(new IntegerValue(5), new IntegerValue(2));
        Expression right = new IntegerValue(5);
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(left, right);

        JXPathContext ctx = new JXPathContext();
        Object result = expression.compile(ctx).evaluate(null);

        assertTrue("Expected true, got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());

        left = new CoreOperationAdd(new IntegerValue(5), new IntegerValue(2));
        right = new IntegerValue(3);
        expression = new CoreOperationLessThanOrEqual(left, right);

        result = expression.compile(ctx).evaluate(null);

        assertFalse("Expected false, got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    public void testEdgeCases() {
        Expression left = new IntegerValue(-1);
        Expression right = new IntegerValue(0);
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(left, right);

        JXPathContext ctx = new JXPathContext();
        Object result = expression.compile(ctx).evaluate(null);

        assertTrue("Expected true, got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());

        left = new IntegerValue(0);
        right = new IntegerValue(-1);
        expression = new CoreOperationGreaterThan(left, right);

        result = expression.compile(ctx).evaluate(null);

        assertFalse("Expected false, got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());

        left = new IntegerValue(0);
        right = new IntegerValue(0);
        expression = new CoreOperationGreaterThanOrEqual(left, right);

        result = expression.compile(ctx).evaluate(null);

        assertTrue("Expected true, got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());

        left = new IntegerValue(Integer.MAX_VALUE);
        right = new IntegerValue(Integer.MAX_VALUE);
        expression = new CoreOperationLessThan(left, right);

        result = expression.compile(ctx).evaluate(null);

        assertFalse("Expected false, got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());

        left = new IntegerValue(Integer.MIN_VALUE);
        right = new IntegerValue(Integer.MIN_VALUE);
        expression = new CoreOperationGreaterThanOrEqual(left, right);

        result = expression.compile(ctx).evaluate(null);

        assertTrue("Expected true, got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    public void testErrorScenarios() {
        Expression left = new CoreOperationGreaterThanOrEqual(new IntegerValue(5), new IntegerValue(2));
        Expression right = null;

        try {
            left.compile(new JXPathContext());
            fail("Expected compilation error");
        } catch (Exception e) {
            // expected
        }

        left = new CoreOperationGreaterThanOrEqual(new IntegerValue(5), new IntegerValue(2));
        left.setLeftExpression(null);

        try {
            left.compile(new JXPathContext());
            fail("Expected compilation error");
        } catch (Exception e) {
            // expected
        }
    }

}