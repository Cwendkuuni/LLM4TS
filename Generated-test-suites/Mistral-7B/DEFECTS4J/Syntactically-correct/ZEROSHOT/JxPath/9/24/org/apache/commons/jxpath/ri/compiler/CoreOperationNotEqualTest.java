package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.core.CoreConstant;
import org.apache.commons.jxpath.ri.core.CoreOperationEquals;
import org.apache.commons.jxpath.ri.core.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.core.CoreVariable;

public class CoreOperationNotEqualTest {

    @org.junit.Test
    public void testCoreOperationNotEqual_whenBothArgsAreConstants_thenReturnsExpectedValue() {
        Expression arg1 = new CoreConstant(Boolean.TRUE);
        Expression arg2 = new CoreConstant(Boolean.FALSE);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenBothArgsAreVariables_thenReturnsExpectedValue() {
        Expression arg1 = new CoreVariable("x");
        Expression arg2 = new CoreVariable("y");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        context.set("x", Boolean.TRUE);
        context.set("y", Boolean.FALSE);

        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenOneArgIsConstantAndAnotherIsVariable_thenReturnsExpectedValue() {
        Expression arg1 = new CoreConstant(Boolean.TRUE);
        Expression arg2 = new CoreVariable("x");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        context.set("x", Boolean.FALSE);

        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenOneArgIsVariableAndAnotherIsConstant_thenReturnsExpectedValue() {
        Expression arg1 = new CoreVariable("x");
        Expression arg2 = new CoreConstant(Boolean.TRUE);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        context.set("x", Boolean.FALSE);

        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testCoreOperationNotEqual_whenBothArgsAreExpressions_thenReturnsExpectedValue() {
        Expression arg1 = new CoreOperationEquals(new CoreVariable("x"), new CoreConstant(1));
        Expression arg2 = new CoreOperationEquals(new CoreVariable("y"), new CoreConstant(2));

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        context.set("x", 1);
        context.set("y", 2);

        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context.set("x", 2);
        context.set("y", 1);

        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}