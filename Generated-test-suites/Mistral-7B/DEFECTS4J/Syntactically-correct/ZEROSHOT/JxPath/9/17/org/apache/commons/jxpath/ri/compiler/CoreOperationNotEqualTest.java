package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Variable;

public class CoreOperationNotEqualTest {

    @org.junit.Before
    public void setUp() {
        // Initialize test variables
        Expression exprArg1 = new Variable("arg1");
        Expression exprArg2 = new Variable("arg2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(exprArg1, exprArg2);
    }

    @org.junit.Test
    public void testComputeValue_WhenArg1IsEqualArg2_ThenReturnFalse() {
        EvalContext context = new EvalContext();
        Object arg1Value = "value1";
        Object arg2Value = "value1";
        context.set("arg1", arg1Value);
        context.set("arg2", arg2Value);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Variable("arg1"), new Variable("arg2"));
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(context);

        assertFalse("Expected false, but got " + result, result);
    }

    @org.junit.Test
    public void testComputeValue_WhenArg1IsNotEqualArg2_ThenReturnTrue() {
        EvalContext context = new EvalContext();
        Object arg1Value = "value1";
        Object arg2Value = "value2";
        context.set("arg1", arg1Value);
        context.set("arg2", arg2Value);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Variable("arg1"), new Variable("arg2"));
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(context);

        assertTrue("Expected true, but got " + result, result);
    }

    @org.junit.Test
    public void testComputeValue_WhenArg1IsNull_ThenReturnTrueIfArg2IsNotNull() {
        EvalContext context = new EvalContext();
        Object arg1Value = null;
        Object arg2Value = "value2";
        context.set("arg1", arg1Value);
        context.set("arg2", arg2Value);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Variable("arg1"), new Variable("arg2"));
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(context);

        assertTrue("Expected true, but got " + result, result);
    }

    @org.junit.Test
    public void testComputeValue_WhenArg1IsNotNull_ThenReturnFalseIfArg2IsNull() {
        EvalContext context = new EvalContext();
        Object arg1Value = "value1";
        Object arg2Value = null;
        context.set("arg1", arg1Value);
        context.set("arg2", arg2Value);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Variable("arg1"), new Variable("arg2"));
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(context);

        assertFalse("Expected false, but got " + result, result);
    }

    @org.junit.Test
    public void testGetSymbol_ThenReturnNotEqualSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Variable("arg1"), new Variable("arg2"));
        String symbol = coreOperationNotEqual.getSymbol();

        assertEquals("!=" , symbol);
    }
}