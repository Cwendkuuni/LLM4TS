package org.apache.commons.jxpath.ri.compiler;

import junit.framework.TestCase;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.core.CoreConstant;
import org.apache.commons.jxpath.ri.core.CoreFunctionCall;
import org.apache.commons.jxpath.ri.core.CoreVariable;

public class CoreOperationNotEqualTest extends TestCase {

    // Test case for method computeValue() with arg1 and arg2 as CoreConstant
    public void testComputeValueWithConstantArgs() {
        Expression arg1 = new CoreConstant(Boolean.TRUE);
        Expression arg2 = new CoreConstant(Boolean.FALSE);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        arg1 = new CoreConstant(Boolean.FALSE);
        arg2 = new CoreConstant(Boolean.TRUE);
        operation = new CoreOperationNotEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.FALSE));
    }

    // Test case for method computeValue() with arg1 and arg2 as CoreVariable
    public void testComputeValueWithVariableArgs() {
        CoreVariable var1 = new CoreVariable("x");
        CoreVariable var2 = new CoreVariable("y");
        Expression arg1 = var1;
        Expression arg2 = var2;
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.put("x", Boolean.TRUE);
        context.put("y", Boolean.FALSE);
        Object result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        context.put("x", Boolean.FALSE);
        context.put("y", Boolean.TRUE);
        result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.FALSE));
    }

    // Test case for method computeValue() with arg1 and arg2 as CoreFunctionCall
    public void testComputeValueWithFunctionCallArgs() {
        Expression arg1 = new CoreFunctionCall(new Path("boolean(true)"), new Expression[0]);
        Expression arg2 = new CoreFunctionCall(new Path("boolean(false)"), new Expression[0]);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));

        arg1 = new CoreFunctionCall(new Path("boolean(false)"), new Expression[0]);
        arg2 = new CoreFunctionCall(new Path("boolean(true)"), new Expression[0]);
        operation = new CoreOperationNotEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.FALSE));
    }

    // Test case for method computeValue() with arg1 == arg2
    public void testComputeValueWithEqualArgs() {
        Expression arg1 = new CoreConstant(Boolean.TRUE);
        Expression arg2 = arg1;
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.FALSE));
    }

    // Test case for method getSymbol()
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new CoreConstant(Boolean.TRUE), new CoreConstant(Boolean.FALSE));
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }

    // Test case for method computeValue() with null arg1 or arg2
    public void testComputeValueWithNullArgs() {
        Expression arg1 = null;
        Expression arg2 = new CoreConstant(Boolean.FALSE);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        try {
            operation.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        arg1 = new CoreConstant(Boolean.TRUE);
        arg2 = null;
        operation = new CoreOperationNotEqual(arg1, arg2);
        context = new EvalContext();
        try {
            operation.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test case for method computeValue() with arg1 and arg2 of different types
    public void testComputeValueWithDifferentTypes() {
        Expression arg1 = new CoreConstant(Boolean.TRUE);
        Expression arg2 = new CoreFunctionCall(new Path("string('Foo')"), new Expression[0]);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        try {
            operation.computeValue(context);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    // Test case for method computeValue() with arg1 and arg2 of the same type but different values
    public void testComputeValueWithSameTypeDifferentValues() {
        Expression arg1 = new CoreConstant("1");
        Expression arg2 = new CoreConstant("2");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));
    }
}