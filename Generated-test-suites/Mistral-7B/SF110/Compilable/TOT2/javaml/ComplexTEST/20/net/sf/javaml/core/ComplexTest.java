package net.sf.javaml.core;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ComplexTest {
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expectedResult = Math.sqrt(3.0 * 3.0 + 4.0 * 4.0);
        double actualResult = complex.abs();
        assertEquals(expectedResult, actualResult, 0.001);
    }
}