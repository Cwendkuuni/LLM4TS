import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrueWithFalseExpressionAndMessage() {
        boolean expression = false;
        String message = "The validated expression is false";
        long value = 1L;

        try {
            Validate.isTrue(expression, message, value);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}