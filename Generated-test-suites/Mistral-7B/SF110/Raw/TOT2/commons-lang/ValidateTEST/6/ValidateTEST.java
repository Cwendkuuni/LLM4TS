import static org.junit.Assert.assertTrue;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        // Test with boolean expression and message
        Validate.isTrue(true, "Expected true", 1L);
        
        // Test with boolean expression and no message
        Validate.isTrue(true);
        
        // Test with false boolean expression and message
        try {
            Validate.isTrue(false, "Expected true", 1L);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test with false boolean expression and no message
        try {
            Validate.isTrue(false);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    // Repeat this pattern for other isTrue methods
}