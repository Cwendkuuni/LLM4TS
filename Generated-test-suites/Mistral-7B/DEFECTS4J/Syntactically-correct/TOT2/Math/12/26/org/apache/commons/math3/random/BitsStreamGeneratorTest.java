import org.apache.commons.math3.random.BitsStreamGenerator;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BitsStreamGeneratorTest {
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        // Test with true value
        assertTrue(generator.nextBoolean());
        // Test with false value
        assertFalse(generator.nextBoolean());
    }
}