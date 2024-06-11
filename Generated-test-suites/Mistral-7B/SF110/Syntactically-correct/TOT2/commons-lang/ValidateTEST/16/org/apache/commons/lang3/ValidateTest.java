import org.junit.Test;
import static org.junit.Assert.assertThrows;

public class ValidateTest {

    @Test
    public void testIsTrueThrowsExceptionWhenExpressionIsFalse() {
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false, "Expected true but was false"));
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false, "Expected true but was false", 42));
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false, "Expected true but was false", new Object[] {"test", 42}));
    }

    // Test other methods here...
}