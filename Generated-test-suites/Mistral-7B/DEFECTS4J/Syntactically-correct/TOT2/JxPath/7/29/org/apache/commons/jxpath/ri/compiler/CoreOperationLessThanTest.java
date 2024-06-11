package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Literal;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThanConstruction() {
        Expression arg1 = new Literal(1.0);
        Expression arg2 = new Literal(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(coreOperationLessThan);
    }

    @Test
    public void testComputeValueLessThan() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Literal(1.0);
        Expression arg2 = new Literal(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertTrue(coreOperationLessThan.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testGetSymbolLessThan() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new Literal(1.0), new Literal(2.0));
        assertEquals("<", coreOperationLessThan.getSymbol());
    }

    @Test
    public void testEdgeCaseLessThanNegativeNumbers() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Literal(-1.0);
        Expression arg2 = new Literal(-2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertTrue(coreOperationLessThan.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testEdgeCaseLessThanEqualNumbers() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Literal(1.0);
        Expression arg2 = new Literal(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertFalse(coreOperationLessThan.computeValue(context).equals(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionNullInput() {
        EvalContext context = new EvalContext();
        Expression arg1 = null;
        Expression arg2 = new Literal(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
    }

    // Add more test cases as needed
}