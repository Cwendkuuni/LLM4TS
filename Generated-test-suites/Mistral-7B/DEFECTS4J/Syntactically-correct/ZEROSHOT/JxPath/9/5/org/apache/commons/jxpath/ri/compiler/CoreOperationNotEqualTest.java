package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.core.CoreBoolean;
import org.apache.commons.jxpath.ri.core.CoreContext;
import org.apache.commons.jxpath.ri.core.CoreNumber;
import org.apache.commons.jxpath.ri.core.CoreString;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;

public class CoreOperationNotEqualTest {

    @org.junit.Test
    public void testCoreOperationNotEqualWithBoolean() {
        Expression arg1 = new CoreBoolean(Boolean.TRUE);
        Expression arg2 = new CoreBoolean(Boolean.FALSE);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new CoreContext());
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.TRUE, ((CoreBoolean) result).getValue());

        arg1 = new CoreBoolean(Boolean.FALSE);
        arg2 = new CoreBoolean(Boolean.TRUE);

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.FALSE, ((CoreBoolean) result).getValue());
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithNumber() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(2);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new CoreContext());
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.FALSE, ((CoreBoolean) result).getValue());

        arg1 = new CoreNumber(2);
        arg2 = new CoreNumber(1);

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.TRUE, ((CoreBoolean) result).getValue());
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithString() {
        Expression arg1 = new CoreString("a");
        Expression arg2 = new CoreString("b");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new CoreContext());
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.TRUE, ((CoreBoolean) result).getValue());

        arg1 = new CoreString("b");
        arg2 = new CoreString("a");

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.FALSE, ((CoreBoolean) result).getValue());
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithNullAndBoolean() {
        Expression arg1 = ExpressionUtil.createNull();
        Expression arg2 = new CoreBoolean(Boolean.TRUE);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new CoreContext());
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.TRUE, ((CoreBoolean) result).getValue());

        arg2 = ExpressionUtil.createNull();

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.FALSE, ((CoreBoolean) result).getValue());
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithNullAndNumber() {
        Expression arg1 = ExpressionUtil.createNull();
        Expression arg2 = new CoreNumber(1);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new CoreContext());
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.TRUE, ((CoreBoolean) result).getValue());

        arg2 = ExpressionUtil.createNull();

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.FALSE, ((CoreBoolean) result).getValue());
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithNullAndString() {
        Expression arg1 = ExpressionUtil.createNull();
        Expression arg2 = new CoreString("a");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new CoreContext());
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.TRUE, ((CoreBoolean) result).getValue());

        arg2 = ExpressionUtil.createNull();

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.FALSE, ((CoreBoolean) result).getValue());
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithNullAndNull() {
        Expression arg1 = ExpressionUtil.createNull();
        Expression arg2 = ExpressionUtil.createNull();

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new CoreContext());
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.FALSE, ((CoreBoolean) result).getValue());
    }

    @org.junit.Test
    public void testCoreOperationNotEqualWithDifferentTypes() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreString("a");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new CoreContext());
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof CoreBoolean);
        assertEquals(Boolean.TRUE, ((CoreBoolean) result).getValue());
    }
}