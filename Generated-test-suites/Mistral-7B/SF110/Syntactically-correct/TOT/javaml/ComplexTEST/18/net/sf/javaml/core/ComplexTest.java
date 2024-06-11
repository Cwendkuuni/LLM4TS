import static org.junit.Assert.assertEquals;
import net.sf.javaml.core.Complex;

public class ComplexTest {
    @Test
    public void testAbs() {
        Complex c1 = new Complex(3.0, 4.0);
        double expected = Math.sqrt(3.0 * 3.0 + 4.0 * 4.0);
        double actual = c1.abs();
        assertEquals(expected, actual, 0.001);
    }
}