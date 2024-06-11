package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestHelper;
import org.junit.Test;

/**
 * Test for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(3.0)));

        EvalContext context = TestHelper.createEvalContext(new InfoSet());
        Object result = operation.computeValue(context);
        assertTrue("Expected true but got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());

        context = TestHelper.createEvalContext(new InfoSet());
        operation = new CoreOperationGreaterThan(
                new ConstantExpression(new Double(3.0)),
                new ConstantExpression(new Double(5.0)));

        result = operation.computeValue(context);
        assertFalse("Expected false but got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());

        context = TestHelper.createEvalContext(new InfoSet());
        operation = new CoreOperationGreaterThan(
                new ConstantExpression(new Double.NaN()),
                new ConstantExpression(new Double.NaN()));

        result = operation.computeValue(context);
        assertFalse("Expected false but got: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());
    }
}