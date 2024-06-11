import org.junit.Test;
import org.apache.commons.lang3.Validate;

public class ValidateTest {

    @Test
    public void testIsTrueWithBooleanTrue() {
        boolean expression = true;
        String message = "The validated expression is false";
        long value = 10;

        Validate.isTrue(expression, message, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithBooleanFalse() {
        boolean expression = false;
        String message = "The validated expression is false";
        long value = 10;

        Validate.isTrue(expression, message, value);
    }
}