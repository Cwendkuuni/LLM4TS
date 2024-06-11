package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.RuntimeTestCase;

public class CoreOperationGreaterThanTest extends RuntimeTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCoreOperationGreaterThan() {
        // Test with double values
        Expression arg1 = new CoreNumber(new Double(5.5));
        Expression arg2 = new CoreNumber(new Double(3.3));
        CoreOperationGreaterThan coreOp = new CoreOperationGreaterThan(arg1, arg2);
        assertTrue(coreOp.computeValue(new EvalContext()).booleanValue());

        arg1 = new CoreNumber(new Double(3.3));
        arg2 = new CoreNumber(new Double(5.5));
        coreOp = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOp.computeValue(new EvalContext()).booleanValue());

        arg1 = new CoreNumber(new Double(5.5));
        arg2 = new CoreNumber(new Double(5.5));
        coreOp = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOp.computeValue(new EvalContext()).booleanValue());

        arg1 = new CoreNumber(new Double(NaN));
        arg2 = new CoreNumber(new Double(3.3));
        coreOp = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOp.computeValue(new EvalContext()).booleanValue());

        arg1 = new CoreNumber(new Double(3.3));
        arg2 = new CoreNumber(new Double(NaN));
        coreOp = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOp.computeValue(new EvalContext()).booleanValue());

        arg1 = new CoreNumber(new Double(1.1));
        arg2 = new CoreNumber(new Double(1.1));
        coreOp = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOp.computeValue(new EvalContext()).booleanValue());

        // Test with invalid input
        arg1 = new CoreInvalidNode();
        arg2 = new CoreNumber(new Double(3.3));
        coreOp = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOp.computeValue(new EvalContext()).booleanValue());
        assertEquals("CoreOperationGreaterThan: invalid input", coreOp.getErrorMessage(), coreOp.getErrorMessage());

        arg1 = new CoreNumber(new Double(3.3));
        arg2 = new CoreInvalidNode();
        coreOp = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOp.computeValue(new EvalContext()).booleanValue());
        assertEquals("CoreOperationGreaterThan: invalid input", coreOp.getErrorMessage(), coreOp.getErrorMessage());
    }

}