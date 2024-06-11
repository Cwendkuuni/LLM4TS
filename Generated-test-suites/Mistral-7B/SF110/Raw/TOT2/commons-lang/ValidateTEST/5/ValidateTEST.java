import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ValidateTest {
    @Test
    public void testIsTrue() {
        Validate.isTrue(true, "Expected true");
        Validate.isTrue(false, "Expected false", 1L);
        Validate.isTrue(42.0, "Expected true");
        Validate.isTrue(new Object[]{"apple", "banana", "cherry"}, "Expected true");
    }
}