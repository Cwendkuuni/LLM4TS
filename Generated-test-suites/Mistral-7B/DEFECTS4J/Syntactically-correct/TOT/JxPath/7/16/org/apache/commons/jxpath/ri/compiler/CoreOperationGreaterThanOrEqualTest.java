import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtils;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testGreaterThanOrEqual() {
        // Given
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = TestUtils.newContext();

        // When
        Object result = expression.computeValue(context);

        // Then
        assertTrue("5.0 >= 5.0 should be true", result instanceof Boolean);
        assertTrue("5.0 >= 5.0 should be true", ((Boolean) result).booleanValue());
    }

    @Test
    public void testGreaterThanButNotEqual() {
        // Given
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(4.0);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = TestUtils.newContext();

        // When
        Object result = expression.computeValue(context);

        // Then
        assertTrue("5.0 > 4.0 should be true", result instanceof Boolean);
        assertFalse("5.0 > 4.0 should be false", ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanButNotEqual() {
        // Given
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(6.0);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = TestUtils.newContext();

        // When
        Object result = expression.computeValue(context);

        // Then
        assertFalse("5.0 < 6.0 should be false", result instanceof Boolean);
        assertNull("5.0 < 6.0 should return null, not a boolean", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArg1IsNull() {
        // Given
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(null, arg2);

        // When & Then
        expression.computeValue(TestUtils.newContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArg2IsNull() {
        // Given
        Expression arg1 = new LiteralExpression(5.0);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, null);

        // When & Then
        expression.computeValue(TestUtils.newContext());
    }

    @Test
    public void testMixedTypes() {
        // Given
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression("5");
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = TestUtils.newContext();

        // When
        Object result = expression.computeValue(context);

        // Then
        assertTrue("5.0 > 5 (as a string) should be true", result instanceof Boolean);
        assertTrue("5.0 > 5 (as a string) should be true", ((Boolean) result).booleanValue());
    }

    @Test
    public void testNaN() {
        // Given
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(Double.NaN);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = TestUtils.newContext();

        // When
        Object result = expression.computeValue(context);

        // Then
        assertTrue("NaN >= NaN should be true", result instanceof Boolean);
        assertTrue("NaN >= NaN should be true", ((Boolean) result).booleanValue());
    }

    @Test
    public void testNegativeNumber() {
        // Given
        Expression arg1 = new LiteralExpression(-5.0);
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = TestUtils.newContext();

        // When
        Object result = expression.computeValue(context);

        // Then
        assertFalse("5.0 > -5.0 should be true", result instanceof Boolean);
        assertNull("5.0 > -5.0 should return null, not a boolean", result);
    }

    @Test
    public void testZero() {
        // Given
        Expression arg1 = new LiteralExpression(0.0);
        Expression arg2 = new LiteralExpression(0.0);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = TestUtils.newContext();

        // When
        Object result = expression.computeValue(context);

        // Then
        assertTrue("0.0 >= 0.0 should be true", result instanceof Boolean);
        assertTrue("0.0 >= 0.0 should be true", ((Boolean) result).booleanValue());
    }

    @Test
    public void testZeroAndNegativeNumber() {
        // Given
        Expression arg1 = new LiteralExpression(0.0);
        Expression arg2 = new LiteralExpression(-5.0);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = TestUtils.newContext();

        // When
        Object result = expression.computeValue(context);

        // Then
        assertFalse("0.0 >= -5.0 should be false", result instanceof Boolean);
        assertNull("0.0 >= -5.0 should return null, not a boolean", result);
    }
}