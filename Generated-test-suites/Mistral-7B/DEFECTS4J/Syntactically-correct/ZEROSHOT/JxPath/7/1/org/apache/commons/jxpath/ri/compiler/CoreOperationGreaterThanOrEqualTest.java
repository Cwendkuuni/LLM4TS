package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = ExpressionFactory.createNumber(1.5);
        Expression arg2 = ExpressionFactory.createNumber(2.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createNumber(3.0);
        arg2 = ExpressionFactory.createNumber(2.0);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createNumber(1.5);
        arg2 = ExpressionFactory.createNumber(1.5);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createNumber(Double.NaN);
        arg2 = ExpressionFactory.createNumber(1.5);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createNumber(1.5);
        arg2 = ExpressionFactory.createNumber(Double.NaN);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createNumber(Double.NaN);
        arg2 = ExpressionFactory.createNumber(Double.NaN);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createString("3");
        arg2 = ExpressionFactory.createString("2");
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createString("3");
        arg2 = ExpressionFactory.createString("3");
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createString("3");
        arg2 = ExpressionFactory.createNumber(3.0);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createNumber(3.0);
        arg2 = ExpressionFactory.createString("3");
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = ExpressionFactory.createNumber(1.5);
        Expression arg2 = ExpressionFactory.createNumber(2.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        String symbol = coreOperationGreaterThanOrEqual.getSymbol();

        assertEquals(">=", symbol);
    }
}