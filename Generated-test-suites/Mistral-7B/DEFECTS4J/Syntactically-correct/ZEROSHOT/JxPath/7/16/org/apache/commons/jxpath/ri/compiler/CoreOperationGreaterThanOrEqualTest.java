package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.ExpressionTestBase;
import org.apache.commons.jxpath.ri.test.TestHelper;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest extends ExpressionTestBase {

    private CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    /**
     * Test computeValue method with valid arguments.
     */
    @Test
    public void testComputeValueWithValidArguments() {
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse("Expected false, but got " + result, result instanceof Boolean
                && !((Boolean) result).booleanValue());

        arg1 = new LiteralExpression(2.0);
        context = new EvalContext();
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue("Expected true, but got " + result, result instanceof Boolean
                && ((Boolean) result).booleanValue());

        arg1 = new LiteralExpression(3.0);
        context = new EvalContext();
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue("Expected true, but got " + result, result instanceof Boolean
                && ((Boolean) result).booleanValue());
    }

    /**
     * Test computeValue method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithInvalidArguments() {
        EvalContext context = new EvalContext();
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(null, new LiteralExpression(2.0));
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    /**
     * Test getSymbol method.
     */
    @Test
    public void testGetSymbol() {
        String symbol = coreOperationGreaterThanOrEqual.getSymbol();
        assertEquals("Expected '>=', but got " + symbol, ">=", symbol);
    }
}