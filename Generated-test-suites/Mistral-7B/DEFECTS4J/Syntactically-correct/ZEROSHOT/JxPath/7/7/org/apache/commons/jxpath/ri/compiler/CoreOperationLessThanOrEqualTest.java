package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtils;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.jaxp.DoubleValue;

public class CoreOperationLessThanOrEqualTest {

    @org.junit.Test
    public void testCoreOperationLessThanOrEqual() {
        // Test with valid arguments
        Expression arg1 = ExpressionFactory.createNumber(1.5);
        Expression arg2 = ExpressionFactory.createNumber(2.5);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = TestUtils.createMockInfoSet();
        EvalContext context = TestUtils.createMockEvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createNumber(1.5);
        arg2 = ExpressionFactory.createNumber(1.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createNumber(0.5);
        arg2 = ExpressionFactory.createNumber(1.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createNumber(1.5);
        arg2 = ExpressionFactory.createNumber(0.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test with invalid arguments
        arg1 = null;
        arg2 = ExpressionFactory.createNumber(1.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        try {
            coreOperationLessThanOrEqual.computeValue(context);
            fail("Expected a null pointer exception");
        } catch (NullPointerException e) {
            // expected
        }

        arg1 = ExpressionFactory.createNumber(1.5);
        arg2 = null;
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        try {
            coreOperationLessThanOrEqual.computeValue(context);
            fail("Expected a null pointer exception");
        } catch (NullPointerException e) {
            // expected
        }

        arg1 = ExpressionFactory.createNumber(1.5);
        arg2 = ExpressionFactory.createNumber(1.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        arg1 = ExpressionFactory.createString("1");
        coreOperationLessThanOrEqual.setArgs(arg1, arg2);
        try {
            coreOperationLessThanOrEqual.computeValue(context);
            fail("Expected a ClassCastException");
        } catch (ClassCastException e) {
            // expected
        }

        arg1 = ExpressionFactory.createNumber(1.5);
        arg2 = ExpressionFactory.createExpression("1");
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        try {
            coreOperationLessThanOrEqual.computeValue(context);
            fail("Expected a RuntimeException");
        } catch (RuntimeException e) {
            // expected
        }

        arg1 = ExpressionFactory.createExpression("1");
        arg2 = ExpressionFactory.createNumber(1.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        try {
            coreOperationLessThanOrEqual.computeValue(context);
            fail("Expected a RuntimeException");
        } catch (RuntimeException e) {
            // expected
        }

        arg1 = ExpressionFactory.createExpression("1");
        arg2 = ExpressionFactory.createExpression("1");
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        try {
            coreOperationLessThanOrEqual.computeValue(context);
            fail("Expected a RuntimeException");
        } catch (RuntimeException e) {
            // expected
        }
    }
}