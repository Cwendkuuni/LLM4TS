import static org.junit.Assert.*;

public class ValidateTest {
    @Test
    public void testIsTrue() {
        Validate.isTrue(true, "Expected: true", (Object[]) null);
        Validate.isTrue(false, "Expected: false", (Object[]) null);
        Validate.isTrue(true, "Expected: true", new Object[]{"Test"});
        Validate.isTrue(false, "Expected: false", new Object[]{"Test"});
        Validate.isTrue(true, "Expected: true");
        Validate.isTrue(true);
    }
}