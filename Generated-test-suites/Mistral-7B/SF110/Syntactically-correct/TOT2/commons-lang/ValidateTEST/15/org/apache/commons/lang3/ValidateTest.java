import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrueWithBooleanExpressionAndValues() {
        Validate.isTrue(true, "Expected: true, Actual: %s", "true");
        Validate.isTrue(false, "Expected: true, Actual: %s", "false");
        Validate.isTrue(true, "Expected: true, Actual: %s", 42);
        Validate.isTrue(false, "Expected: true, Actual: %s", 42.0);
        Validate.isTrue(true, "Expected: true, Actual: %s, %s, %s", "expected", "actual1", "actual2");
        Validate.isTrue(true, "");
        Validate.isTrue(true, "Expected: true", new Object[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseExpressionAndValues() {
        Validate.isTrue(false, "Expected: true, Actual: %s", "true");
        Validate.isTrue(false, "Expected: true, Actual: %s", "false");
        Validate.isTrue(false, "Expected: true, Actual: %s", 42);
        Validate.isTrue(false, "Expected: true, Actual: %s", 42.0);
        Validate.isTrue(false, "Expected: true, Actual: %s, %s, %s", "expected", "actual1", "actual2");
        Validate.isTrue(false, "");
        Validate.isTrue(false, "Expected: true", new Object[0]);
        Validate.isTrue(false, "The validated expression is false");
    }

    @Test
    public void testIsTrueWithBooleanExpression() {
        Validate.isTrue(true);
        Validate.isTrue(false);
    }

    // Test other isTrue methods with different parameter types here.
}