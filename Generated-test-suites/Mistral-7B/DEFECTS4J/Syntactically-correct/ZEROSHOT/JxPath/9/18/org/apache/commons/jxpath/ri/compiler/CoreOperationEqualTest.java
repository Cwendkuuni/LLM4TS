package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.evaluator.EvalContext;
import org.apache.commons.jxpath.ri.model.Value;
import org.apache.commons.jxpath.ri.util.Objects;
import org.junit.Test;

/**
 * Test case for {@link CoreOperationEqual}.
 */
public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_whenBothArgsAreBooleanTrue_thenReturnTrue() {
        // Given
        Expression arg1 = new ConstantExpression(new Value(Boolean.TRUE));
        Expression arg2 = new ConstantExpression(new Value(Boolean.TRUE));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenBothArgsAreBooleanFalse_thenReturnFalse() {
        // Given
        Expression arg1 = new ConstantExpression(new Value(Boolean.FALSE));
        Expression arg2 = new ConstantExpression(new Value(Boolean.FALSE));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgIsBooleanTrueAndSecondArgIsBooleanFalse_thenReturnFalse() {
        // Given
        Expression arg1 = new ConstantExpression(new Value(Boolean.TRUE));
        Expression arg2 = new ConstantExpression(new Value(Boolean.FALSE));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgIsBooleanFalseAndSecondArgIsBooleanTrue_thenReturnFalse() {
        // Given
        Expression arg1 = new ConstantExpression(new Value(Boolean.FALSE));
        Expression arg2 = new ConstantExpression(new Value(Boolean.TRUE));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenBothArgsAreDifferentTypes_thenReturnFalse() {
        // Given
        Expression arg1 = new ConstantExpression(new Value(Integer.valueOf(1)));
        Expression arg2 = new ConstantExpression(new Value("Hello"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenBothArgsAreSameInstance_thenReturnTrue() {
        // Given
        Object value = new Object();
        Expression arg1 = new ConstantExpression(new Value(value));
        Expression arg2 = new ConstantExpression(new Value(value));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgIsNullAndSecondArgIsNull_thenReturnTrue() {
        // Given
        Expression arg1 = new ConstantExpression(Value.NULL);
        Expression arg2 = new ConstantExpression(Value.NULL);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgIsNullAndSecondArgIsNotNull_thenReturnFalse() {
        // Given
        Expression arg1 = new ConstantExpression(Value.NULL);
        Expression arg2 = new ConstantExpression(new Value("Hello"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgIsNotNullAndSecondArgIsNull_thenReturnFalse() {
        // Given
        Expression arg1 = new ConstantExpression(new Value(1));
        Expression arg2 = new ConstantExpression(Value.NULL);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgIsStringAndSecondArgIsInteger_thenReturnFalse() {
        // Given
        Expression arg1 = new ConstantExpression(new Value("Hello"));
        Expression arg2 = new ConstantExpression(new Value(1));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgIsIntegerAndSecondArgIsString_thenReturnFalse() {
        // Given
        Expression arg1 = new ConstantExpression(new Value(1));
        Expression arg2 = new ConstantExpression(new Value("Hello"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgIsIntegerAndSecondArgIsIntegerWithDifferentValues_thenReturnFalse() {
        // Given
        Expression arg1 = new ConstantExpression(new Value(1));
        Expression arg2 = new ConstantExpression(new Value(2));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgIsIntegerAndSecondArgIsIntegerWithSameValues_thenReturnTrue() {
        // Given
        Expression arg1 = new ConstantExpression(new Value(1));
        Expression arg2 = new ConstantExpression(new Value(1));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgIsStringAndSecondArgIsStringWithDifferentValues_thenReturnFalse() {
        // Given
        Expression arg1 = new ConstantExpression(new Value("Hello"));
        Expression arg2 = new ConstantExpression(new Value("World"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgIsStringAndSecondArgIsStringWithSameValues_thenReturnTrue() {
        // Given
        Expression arg1 = new ConstantExpression(new Value("Hello"));
        Expression arg2 = new ConstantExpression(new Value("Hello"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // When
        Object result = coreOperationEqual.computeValue(context);

        // Then
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}