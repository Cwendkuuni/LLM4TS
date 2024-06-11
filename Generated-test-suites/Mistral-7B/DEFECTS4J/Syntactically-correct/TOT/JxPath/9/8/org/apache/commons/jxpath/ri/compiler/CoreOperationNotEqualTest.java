package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.core.Context;
import org.apache.commons.jxpath.ri.expr.Expression;
import org.apache.commons.jxpath.ri.expr.Variable;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationNotEqual.
 */
public class CoreOperationNotEqualTest {

    @Test
    public void testNotEqualTrue() {
        Expression arg1 = new Variable("arg1", new Integer(5));
        Expression arg2 = new Variable("arg2", new Integer(6));
        Expression notEqual = new CoreOperationNotEqual(arg1, arg2);
        Context context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testNotEqualFalse() {
        Expression arg1 = new Variable("arg1", new Integer(5));
        Expression arg2 = new Variable("arg2", new Integer(5));
        Expression notEqual = new CoreOperationNotEqual(arg1, arg2);
        Context context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testNotEqualNullArg1() {
        Expression arg1 = new Variable("arg1", null);
        Expression arg2 = new Variable("arg2", new Integer(5));
        Expression notEqual = new CoreOperationNotEqual(arg1, arg2);
        Context context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testNotEqualNullArg2() {
        Expression arg1 = new Variable("arg1", new Integer(5));
        Expression arg2 = new Variable("arg2", null);
        Expression notEqual = new CoreOperationNotEqual(arg1, arg2);
        Context context = new EvalContext();
        notEqual.computeValue(context);
    }

    @Test
    public void testNotEqualEqualObjects() {
        Expression arg1 = new Variable("arg1", new String("test"));
        Expression arg2 = new Variable("arg2", new String("test"));
        Expression notEqual = new CoreOperationNotEqual(arg1, arg2);
        Context context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testNotEqualDifferentObjects() {
        Expression arg1 = new Variable("arg1", new String("test"));
        Expression arg2 = new Variable("arg2", new Integer(5));
        Expression notEqual = new CoreOperationNotEqual(arg1, arg2);
        Context context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}