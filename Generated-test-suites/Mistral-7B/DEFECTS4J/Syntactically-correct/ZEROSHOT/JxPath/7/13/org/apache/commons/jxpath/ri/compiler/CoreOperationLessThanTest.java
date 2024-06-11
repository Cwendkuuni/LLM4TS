package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Expression;

import junit.framework.TestCase;

public class CoreOperationLessThanTest extends TestCase {

    // Test constructor
    public void testCoreOperationLessThanConstructor() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertEquals("Expected 2 arguments, got " + coreOperationLessThan.args.length, 2, coreOperationLessThan.args.length);
        assertEquals("Expected arg1 to be " + arg1.getClass().getName(), arg1.getClass().getName(), coreOperationLessThan.args[0].getClass().getName());
        assertEquals("Expected arg2 to be " + arg2.getClass().getName(), arg2.getClass().getName(), coreOperationLessThan.args[1].getClass().getName());
    }

    // Test computeValue method with double values
    public void testComputeValueWithDoubleValues() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new Object[0], null);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue("Expected result to be false", result.booleanValue());

        arg1 = new ConstantExpression(1.5);
        arg2 = new ConstantExpression(2.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(new Object[0], null);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse("Expected result to be true", result.booleanValue());

        arg1 = new ConstantExpression(1.5);
        arg2 = new ConstantExpression(1.5);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(new Object[0], null);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse("Expected result to be false", result.booleanValue());

        arg1 = new ConstantExpression(1.5);
        arg2 = new ConstantExpression(1.4);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(new Object[0], null);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue("Expected result to be true", result.booleanValue());
    }

    // Test computeValue method with int values
    public void testComputeValueWithIntValues() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new Object[0], null);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue("Expected result to be false", result.booleanValue());

        arg1 = new ConstantExpression(1);
        arg2 = new ConstantExpression(1);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(new Object[0], null);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse("Expected result to be false", result.booleanValue());

        arg1 = new ConstantExpression(1);
        arg2 = new ConstantExpression(1);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(new Object[0], null);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse("Expected result to be false", result.booleanValue());

        arg1 = new ConstantExpression(1);
        arg2 = new ConstantExpression(2);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(new Object[0], null);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue("Expected result to be false", result.booleanValue());

        arg1 = new ConstantExpression(1);
        arg2 = new ConstantExpression(0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(new Object[0], null);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue("Expected result to be true", result.booleanValue());
    }
}