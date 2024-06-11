package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.functions.Functions;
import org.apache.commons.jxpath.ri.functions.SimpleFunctionCall;
import org.apache.commons.jxpath.ri.functions.SimpleFunctionCallImpl;
import org.apache.commons.jxpath.ri.functions.SimpleFunctionCallImpl.FunctionType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Objects;

/**
 * Test class for CoreOperationNotEqual.
 */
public class CoreOperationNotEqualTest {

    private CoreOperationNotEqual operationNotEqual;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setUp() {
        arg1 = new SimpleExpression("5");
        arg2 = new SimpleExpression("6");
        operationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        EvaluationContext context = new EvaluationContext();
        Object result = operationNotEqual.computeValue(context);
        assertTrue(Objects.equals(Boolean.FALSE, result));
    }

    @Test
    public void testComputeValueWithSameValues() {
        arg1 = new SimpleExpression("5");
        arg2 = new SimpleExpression("5");
        operationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvaluationContext context = new EvaluationContext();
        Object result = operationNotEqual.computeValue(context);
        assertTrue(Objects.equals(Boolean.TRUE, result));
    }

    @Test
    public void testComputeValueWithNullArg1() {
        arg1 = new SimpleExpression(null);
        arg2 = new SimpleExpression("5");
        operationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvaluationContext context = new EvaluationContext();
        Object result = operationNotEqual.computeValue(context);
        assertTrue(Objects.equals(Boolean.TRUE, result));
    }

    @Test
    public void testComputeValueWithNullArg2() {
        arg1 = new SimpleExpression("5");
        arg2 = new SimpleExpression(null);
        operationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvaluationContext context = new EvaluationContext();
        Object result = operationNotEqual.computeValue(context);
        assertTrue(Objects.equals(Boolean.FALSE, result));
    }

    @Test
    public void testGetSymbol() {
        String symbol = operationNotEqual.getSymbol();
        assertEquals("!=", symbol);
    }

    @Test
    public void testGetFunctionCall() {
        SimpleFunctionCall functionCall = operationNotEqual.getFunctionCall();
        assertNull(functionCall);
    }

    @Test
    public void testPerformEqualityComparisonWithDifferentValues() {
        FunctionType functionType = FunctionType.BOOLEAN_BINARY;
        SimpleFunctionCallImpl functionCall = new SimpleFunctionCallImpl("not equals", functionType);
        Expression arg1 = new SimpleExpression("5");
        Expression arg2 = new SimpleExpression("6");
        Expression notEqualsExpression = new CoreOperationNotEqual(arg1, arg2);
        assertEquals(functionCall, notEqualsExpression.getFunctionCall());
    }
}