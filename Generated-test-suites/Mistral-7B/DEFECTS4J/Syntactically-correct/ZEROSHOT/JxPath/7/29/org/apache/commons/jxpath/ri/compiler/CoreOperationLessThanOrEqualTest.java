package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(2.0);
        arg2 = new LiteralExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(3.0);
        arg2 = new LiteralExpression(2.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(3.0);
        arg2 = new LiteralExpression(3.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(Double.NaN);
        arg2 = new LiteralExpression(Double.NaN);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(Double.NaN);
        arg2 = new LiteralExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(Double.NaN);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        String symbol = coreOperationLessThanOrEqual.getSymbol();
        assertEquals("<=", symbol);
    }
}