package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.junit.Test;

/**
 * Test file for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    /**
     * Test method for CoreOperationGreaterThan.CoreOperationGreaterThan(Expression arg1, Expression arg2) constructor.
     */
    @Test
    public void testCoreOperationGreaterThanConstructor() {
        Expression arg1 = TestUtils.newNumberExpression(1.0);
        Expression arg2 = TestUtils.newNumberExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals("Expected 2 arguments", 2, coreOperationGreaterThan.getNumArgs());
        assertNotNull("Expected non-null arguments", coreOperationGreaterThan.args);
    }

    /**
     * Test method for CoreOperationGreaterThan.computeValue(EvalContext).
     */
    @Test
    public void testComputeValueTrue() {
        Expression arg1 = TestUtils.newNumberExpression(3.0);
        Expression arg2 = TestUtils.newNumberExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = TestUtils.newEvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertTrue("Expected Boolean.TRUE", value instanceof Boolean);
        assertTrue("Expected true value", ((Boolean) value).booleanValue());
    }

    /**
     * Test method for CoreOperationGreaterThan.computeValue(EvalContext).
     */
    @Test
    public void testComputeValueFalse() {
        Expression arg1 = TestUtils.newNumberExpression(2.0);
        Expression arg2 = TestUtils.newNumberExpression(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = TestUtils.newEvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertTrue("Expected Boolean.FALSE", value instanceof Boolean);
        assertFalse("Expected false value", ((Boolean) value).booleanValue());
    }

    /**
     * Test method for CoreOperationGreaterThan.computeValue(EvalContext).
     */
    @Test
    public void testComputeValueSameValues() {
        Expression arg1 = TestUtils.newNumberExpression(2.0);
        Expression arg2 = TestUtils.newNumberExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = TestUtils.newEvalContext();
        Object value = coreOperationGreaterThan.computeValue(context);
        assertFalse("Expected false value", value instanceof Boolean);
    }

    /**
     * Test method for CoreOperationGreaterThan.getSymbol().
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = TestUtils.newNumberExpression(1.0);
        Expression arg2 = TestUtils.newNumberExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        String symbol = coreOperationGreaterThan.getSymbol();
        assertEquals("Expected '>' symbol", ">", symbol);
    }
}