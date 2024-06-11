import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class ValidateTest {
    @Test
    public void testIsTrueWithTrueValue() {
        boolean expression = true;
        String message = "The validated expression is false";
        long value = 42L;
        Validate.isTrue(expression, message, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseValue() {
        boolean expression = false;
        String message = "The validated expression is false";
        long value = 42L;
        Validate.isTrue(expression, message, value);
    }

    @Test
    public void testIsTrueWithoutMessageOrValue() {
        boolean expression = true;
        Validate.isTrue(expression);
    }
}