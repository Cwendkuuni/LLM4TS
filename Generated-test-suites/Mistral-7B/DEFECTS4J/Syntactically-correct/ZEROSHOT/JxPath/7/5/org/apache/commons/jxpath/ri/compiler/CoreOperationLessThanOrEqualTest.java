package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual operation;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setUp() {
        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(2.0);
        context = new EvalContext();
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg2 = new LiteralExpression(1.5);
        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new LiteralExpression(1.5);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals("<=", symbol);
    }
}