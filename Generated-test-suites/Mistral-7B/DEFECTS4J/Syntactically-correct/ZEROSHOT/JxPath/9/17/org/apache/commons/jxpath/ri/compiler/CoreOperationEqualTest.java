package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.test.AbstractJxPathTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationEqual.
 */
public class CoreOperationEqualTest extends AbstractJxpathTest {

    private CoreOperationEqual op;

    @Before
    public void setUp() {
        CoreExpression arg1 = new CoreConstantExpression(1);
        CoreExpression arg2 = new CoreConstantExpression(1);
        op = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueWhenBothArgumentsAreEqual() {
        EvalContext context = createContext();
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWhenFirstArgumentIsEqualToSecond() {
        EvalContext context = createContext();
        CoreExpression arg1 = new CoreConstantExpression(1);
        CoreExpression arg2 = new CoreConstantExpression(1);
        op = new CoreOperationEqual(arg1, arg2);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWhenFirstArgumentIsNotEqualToSecond() {
        EvalContext context = createContext();
        CoreExpression arg1 = new CoreConstantExpression(1);
        CoreExpression arg2 = new CoreConstantExpression(2);
        op = new CoreOperationEqual(arg1, arg2);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @Test
    public void testComputeValueWhenFirstArgumentIsNull() {
        EvalContext context = createContext();
        CoreExpression arg1 = new CoreConstantExpression(null);
        CoreExpression arg2 = new CoreConstantExpression(1);
        op = new CoreOperationEqual(arg1, arg2);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @Test
    public void testComputeValueWhenSecondArgumentIsNull() {
        EvalContext context = createContext();
        CoreExpression arg1 = new CoreConstantExpression(1);
        CoreExpression arg2 = new CoreConstantExpression(null);
        op = new CoreOperationEqual(arg1, arg2);
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = op.getSymbol();
        assertEquals("=", symbol);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenBothArgumentsNull() {
        CoreExpression arg1 = new CoreConstantExpression(null);
        CoreExpression arg2 = new CoreConstantExpression(null);
        new CoreOperationEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenArg1IsNotNullAndArg2IsNull() {
        CoreExpression arg1 = new CoreConstantExpression(new Integer(1));
        CoreExpression arg2 = new CoreConstantExpression(null);
        new CoreOperationEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenArg1IsNullAndArg2IsNotNull() {
        CoreExpression arg1 = new CoreConstantExpression(null);
        CoreExpression arg2 = new CoreConstantExpression(new Integer(1));
        new CoreOperationEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenBothArgumentsAreNotInstanceOfCoreExpression() {
        new CoreOperationEqual(new Object(), new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenArg1IsInstanceOfCoreOperationCompare() {
        CoreOperationCompare arg1 = new CoreOperationCompare(new CoreConstantExpression(1), new CoreConstantExpression(2), CoreOperationCompare.OP_GT);
        CoreExpression arg2 = new CoreConstantExpression(1);
        new CoreOperationEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenArg2IsInstanceOfCoreOperationCompare() {
        CoreExpression arg1 = new CoreConstantExpression(1);
        CoreOperationCompare arg2 = new CoreOperationCompare(new CoreConstantExpression(1), new CoreConstantExpression(2), CoreOperationCompare.OP_GT);
        new CoreOperationEqual(arg1, arg2);
    }
}