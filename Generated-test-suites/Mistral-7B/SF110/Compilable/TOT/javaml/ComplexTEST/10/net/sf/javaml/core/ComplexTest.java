package net.sf.javaml.core;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ComplexTest {
    @Test
    public void testComplexConstructionAndAbs() {
        Complex c = new Complex(2.0, 3.0);
        assertEquals("2.0 + 3.0i", c.toString()); // Test toString() implicitly
        double expectedAbs = Math.sqrt(2.0 * 2.0 + 3.0 * 3.0);
        assertEquals(expectedAbs, c.abs(), 0.001); // Allow some error tolerance
    }
}