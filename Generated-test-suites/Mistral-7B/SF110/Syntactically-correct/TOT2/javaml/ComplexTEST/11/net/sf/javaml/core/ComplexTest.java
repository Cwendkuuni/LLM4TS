import static org.junit.Assert.*;
import net.sf.javaml.core.Complex;

public class ComplexTest {
    @Test
    public void testComplexConstructor() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", c.toString());
    }

    @Test
    public void testComplexEmptyConstructor() {
        Complex c = new Complex();
        assertEquals("0.0 + 0.0i", c.toString());
    }

    // Add test cases for other methods here
}