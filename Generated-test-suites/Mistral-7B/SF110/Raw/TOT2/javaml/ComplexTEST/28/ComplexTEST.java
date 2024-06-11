import static org.junit.Assert.*; // Import assertion statements
import net.sf.javaml.core.Complex;

public class ComplexTest {
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = Math.sqrt(3.0 * 3.0 + 4.0 * 4.0);
        double actual = complex.abs();
        assertEquals(expected, actual, 0.001); // Allow for a small tolerance
    }
}