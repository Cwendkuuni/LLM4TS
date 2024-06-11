import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ValidateTest {
    @Test
    public void testIsTrue() {
        Validate.isTrue(true);
        Validate.isTrue(false, "Expected true but got false");
    }
}