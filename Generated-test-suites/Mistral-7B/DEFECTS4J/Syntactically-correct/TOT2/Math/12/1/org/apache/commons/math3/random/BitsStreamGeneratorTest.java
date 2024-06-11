import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {
    // 
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        // Basic test
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        // Edge cases
        for (int i = 0; i < 1000; i++) {
            assertTrue(generator.nextBoolean());
        }
        // Negative test
        for (int i = 1000; i >= 0; i--) {
            assertFalse(generator.nextBoolean());
        }
    }
    // 
}