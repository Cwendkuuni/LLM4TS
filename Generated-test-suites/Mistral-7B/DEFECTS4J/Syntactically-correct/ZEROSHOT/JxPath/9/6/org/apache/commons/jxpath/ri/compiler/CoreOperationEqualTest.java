package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionUtil;

/**
 * Test class for CoreOperationEqual.
 */
public class CoreOperationEqualTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual() {

        // Test with two Boolean expressions
        Expression arg1 = ExpressionUtil.compile("true");
        Expression arg2 = ExpressionUtil.compile("true");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with two equal Number expressions
        arg1 = ExpressionUtil.compile("5");
        arg2 = ExpressionUtil.compile("5");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with two not equal Number expressions
        arg1 = ExpressionUtil.compile("5");
        arg2 = ExpressionUtil.compile("6");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Test with two String expressions
        arg1 = ExpressionUtil.compile("\"hello\"");
        arg2 = ExpressionUtil.compile("\"hello\"");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with two not equal String expressions
        arg1 = ExpressionUtil.compile("\"hello\"");
        arg2 = ExpressionUtil.compile("\"world\"");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Test with two different types expressions
        arg1 = ExpressionUtil.compile("5");
        arg2 = ExpressionUtil.compile("\"5\"");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
    }

}