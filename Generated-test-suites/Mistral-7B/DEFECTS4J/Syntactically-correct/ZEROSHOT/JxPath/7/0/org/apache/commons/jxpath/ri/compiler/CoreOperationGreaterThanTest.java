package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.InfoSetFactory;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;

/**
 * Test class for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        Expression arg1 = ExpressionFactory.createNumber(1.0);
        Expression arg2 = ExpressionFactory.createNumber(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertNotNull(coreOperationGreaterThan);
        assertEquals(CoreOperationRelationalExpression.class, coreOperationGreaterThan.getClass());
        assertEquals(2, coreOperationGreaterThan.getArgs().length);
    }

    /**
     * Test computeValue method with double values.
     */
    @org.junit.Test
    public void testComputeValueDoubleDouble() {
        Expression arg1 = ExpressionFactory.createNumber(1.0);
        Expression arg2 = ExpressionFactory.createNumber(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(InfoSetFactory.createInfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createNumber(2.0);
        arg2 = ExpressionFactory.createNumber(1.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(InfoSetFactory.createInfoSet());
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createNumber(2.0);
        arg2 = ExpressionFactory.createNumber(2.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(InfoSetFactory.createInfoSet());
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with int values.
     */
    @org.junit.Test
    public void testComputeValueIntInt() {
        Expression arg1 = ExpressionFactory.createNumber(1);
        Expression arg2 = ExpressionFactory.createNumber(2);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(InfoSetFactory.createInfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createNumber(2);
        arg2 = ExpressionFactory.createNumber(1);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(InfoSetFactory.createInfoSet());
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createNumber(2);
        arg2 = ExpressionFactory.createNumber(2);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(InfoSetFactory.createInfoSet());
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test getSymbol method.
     */
    @org.junit.Test
    public void testGetSymbol() {
        Expression arg1 = ExpressionFactory.createNumber(1.0);
        Expression arg2 = ExpressionFactory.createNumber(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(">", coreOperationGreaterThan.getSymbol());
    }
}