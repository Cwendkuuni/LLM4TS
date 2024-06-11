package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.BooleanExpression;
import org.apache.commons.jxpath.ri.expression.NumberExpression;
import org.apache.commons.jxpath.ri.expression.StringExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertNotNull(coreOperationEqual);
    }

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    @Test
    public void testArgumentsOfDifferentTypes() {
        Expression arg1 = new NumberExpression(1);
        Expression arg2 = new StringExpression("true");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentsNull() {
        Expression arg1 = null;
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationEqual.computeValue(context); // Should throw an exception
    }

    @Test
    public void testArgumentsEqualNull() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(null);
        arg2.setValue(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testArgumentsEqualBooleanValues() {
        Expression arg1 = new BooleanExpression(true);
        Expression arg2 = new BooleanExpression(true);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testArgumentsEqualNumberValues() {
        Expression arg1 = new NumberExpression(1.0);
        Expression arg2 = new NumberExpression(1.0);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationEqual.computeValue(context).booleanValue());
    }

    @Test
    public void testArgumentsEqualStringValues() {
        Expression arg1 = new StringExpression("hello");
        Expression arg2 = new StringExpression("hello");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationEqual.computeValue(context).booleanValue());
    }
}