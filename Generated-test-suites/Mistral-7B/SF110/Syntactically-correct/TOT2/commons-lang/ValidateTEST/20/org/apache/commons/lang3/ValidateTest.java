import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ValidateTest {

    @Test
    public void testIsTrueTrue() {
        Validate.isTrue(true);
    }

    @Test
    public void testIsTrueFalseWithEmptyMessage() {
        String message = "";
        try {
            Validate.isTrue(false, message);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsTrueFalseWithStringMessage() {
        String message = "The validated expression is false";
        try {
            Validate.isTrue(false, message);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testIsTrueFalseWithLongMessage() {
        String message = "The validated expression is false. This is a long message.";
        try {
            Validate.isTrue(false, message);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testIsTrueFalseWithObjectArrayMessage() {
        Object[] messageValues = new Object[]{"The validated expression is false", 123};
        String message = String.format("%s %d", "%s %d", messageValues[0], messageValues[1]);
        try {
            Validate.isTrue(false, message, messageValues);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(message, e.getMessage());
        }
    }
}