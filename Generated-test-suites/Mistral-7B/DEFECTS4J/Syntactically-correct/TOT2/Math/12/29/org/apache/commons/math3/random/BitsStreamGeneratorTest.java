import org.apache.commons.math3.random.BitsStreamGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        // Basic test
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        // Edge cases
        generator.setSeed(1);
        assertTrue(generator.nextBoolean());
        generator.setSeed(0);
        assertFalse(generator.nextBoolean());
        // Exception handling
        generator.setSeed(-1);
        assertThrows(IllegalArgumentException.class, () -> generator.nextBoolean());
    }
}