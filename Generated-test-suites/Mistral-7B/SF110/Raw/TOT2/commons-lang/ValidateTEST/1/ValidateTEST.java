import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class ValidateTest {
    @Test
    public void testIsTrueWithBooleanAndMessageAndLong() {
        Validate.isTrue(true, "The expression is true", 1L);
        Validate.isTrue(false, "The expression is false", 2L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseAndInvalidMessageAndLong() {
        Validate.isTrue(false, "Invalid message", 3L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseAndNullMessageAndLong() {
        Validate.isTrue(false, null, 4L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseAndEmptyMessageAndLong() {
        Validate.isTrue(false, "", 5L);
    }
}