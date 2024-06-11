package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestUtil;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThan.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        // Test case 1: l > r
        Expression arg1 = TestUtil.createNumberExpression(1.5);
        Expression arg2 = TestUtil.createNumberExpression(1.0);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = TestUtil.createContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test case 2: l <= r
        arg1 = TestUtil.createNumberExpression(1.0);
        arg2 = TestUtil.createNumberExpression(1.5);
        operation = new CoreOperationGreaterThan(arg1, arg2);

        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        // Test case 3: l == r
        arg1 = TestUtil.createNumberExpression(1.5);
        arg2 = TestUtil.createNumberExpression(1.5);
        operation = new CoreOperationGreaterThan(arg1, arg2);

        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        // Test case 4: l < r
        arg1 = TestUtil.createNumberExpression(1.0);
        arg2 = TestUtil.createNumberExpression(1.5);
        operation = new CoreOperationGreaterThan(arg1, arg2);

        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        // Test case 5: l = NaN and r = NaN
        arg1 = TestUtil.createNumberExpression(Double.NaN);
        arg2 = TestUtil.createNumberExpression(Double.NaN);
        operation = new CoreOperationGreaterThan(arg1, arg2);

        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        // Test case 6: l = NaN and r != NaN
        arg1 = TestUtil.createNumberExpression(Double.NaN);
        arg2 = TestUtil.createNumberExpression(1.5);
        operation = new CoreOperationGreaterThan(arg1, arg2);

        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        // Test case 7: l != NaN and r = NaN
        arg1 = TestUtil.createNumberExpression(1.5);
        arg2 = TestUtil.createNumberExpression(Double.NaN);
        operation = new CoreOperationGreaterThan(arg1, arg2);

        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        // Test case 8: l > NaN
        arg1 = TestUtil.createNumberExpression(Double.NaN);
        arg2 = TestUtil.createNumberExpression(1.5);
        operation = new CoreOperationGreaterThan(arg1, arg2);

        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        // Test case 9: l < NaN
        arg1 = TestUtil.createNumberExpression(1.5);
        arg2 = TestUtil.createNumberExpression(Double.NaN);
        operation = new CoreOperationGreaterThan(arg1, arg2);

        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        // Test case 10: l = r
        arg1 = TestUtil.createNumberExpression(1.5);
        arg2 = TestUtil.createNumberExpression(1.5);
        operation = new CoreOperationGreaterThan(arg1, arg2);

        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }
}