package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_EqualValues() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_NullArguments() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_NullLeftArg() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_NullRightArg() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationEqual.computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(1));
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}
//