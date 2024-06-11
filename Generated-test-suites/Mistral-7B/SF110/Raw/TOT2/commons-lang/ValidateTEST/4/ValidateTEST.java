import static org.apache.commons.lang3.Validate.isTrue;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        isTrue(true, "Expected true", 42);
    }
}