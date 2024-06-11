
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.sf.javaml.core.Complex;

public class ComplexTest {

    @Test
    public void testAbs() {
        Complex c1 = new Complex(3.0, 4.0);
        double expected = Math.sqrt(3.0 * 3.0 + 4.0 * 4.0);
        double actual = c1.abs();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex expected = new Complex(4.0, 6.0);
        Complex actual = new Complex();
        c1.plus(c2);
        assertEquals(expected.re, actual.re, 0.001);
        assertEquals(expected.im, actual.im, 0.001);
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex expected = new Complex(-5.0, 11.0);
        Complex actual = new Complex();
        c1.times(c2);
        assertEquals(expected.re, actual.re, 0.001);
        assertEquals(expected.im, actual.im, 0.001);
    }

    @Test
    public void testToString() {
        Complex c = new Complex(1.0, 2.0);
        String expected = "1.0 + 2.0i";
        String actual = c.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimesWithNull() {
        Complex c = new Complex(1.0, 2.0);
        Complex nullComplex = null;
        c.times(nullComplex);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimesWithImproperComplex() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(1.0, "abc");
        Complex actual = new Complex();
        try {
            c1.times(c2);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}