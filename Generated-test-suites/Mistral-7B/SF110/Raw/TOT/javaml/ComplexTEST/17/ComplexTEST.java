import static org.junit.jupiter.api.AssertAsserts.assertEquals;
import static org.junit.jupiter.api.AssertAsserts.assertThrows;

import org.junit.Test;

import net.sf.javaml.core.Complex;

public class ComplexTest {

    @Test
    public void testAbs() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals(5.0, c.abs(), 0.001); // typical use case
        Complex z = new Complex(0.0, 0.0);
        assertEquals(0.0, z.abs(), 0.001); // edge case: real and imaginary parts are zero
        Complex n = new Complex(-1.0, -1.0);
        assertEquals(Math.sqrt(2.0), n.abs(), 0.001); // edge case: negative values
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex sum = new Complex();
        a.plus(b);
        Complex expectedSum = new Complex(4.0, 6.0);
        assertEquals(expectedSum.re, sum.re);
        assertEquals(expectedSum.im, sum.im);

        sum = Complex.plus(a, b);
        assertEquals(expectedSum.re, sum.re);
        assertEquals(expectedSum.im, sum.im);
    }

    @Test
    public void testTimesWithDouble() {
        Complex a = new Complex(1.0, 2.0);
        double alpha = 3.0;
        Complex product = new Complex();
        a.times(alpha);
        Complex expectedProduct = new Complex(3.0, 6.0);
        assertEquals(expectedProduct.re, product.re);
        assertEquals(expectedProduct.im, product.im);

        product = Complex.multiply(a, alpha);
        assertEquals(expectedProduct.re, product.re);
        assertEquals(expectedProduct.im, product.im);
    }

    @Test
    public void testTimesWithComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex product = new Complex();
        a.times(b);
        Complex expectedProduct = new Complex(-5.0, 2.0);
        assertEquals(expectedProduct.re, product.re);
        assertEquals(expectedProduct.im, product.im);

        product = Complex.multiply(a, b);
        assertEquals(expectedProduct.re, product.re);
        assertEquals(expectedProduct.im, product.im);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(1.0, 2.0);
        Complex difference = new Complex();
        a.minus(b);
        Complex expectedDifference = new Complex(2.0, -2.0);
        assertEquals(expectedDifference.re, difference.re);
        assertEquals(expectedDifference.im, difference.im);

        difference = Complex.minus(a, b);
        assertEquals(expectedDifference.re, difference.re);
        assertEquals(expectedDifference.im, difference.im);
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(3.0, 4.0);
        Complex conjugate = new Complex();
        c.conjugate();
        Complex expectedConjugate = new Complex(3.0, -4.0);
        assertEquals(expectedConjugate.re, conjugate.re);
        assertEquals(expectedConjugate.im, conjugate.im);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidArguments() {
        // Test with negative real part
        DoubleSupplier real = () -> -1.0;
        DoubleSupplier imag = () -> 2.0;
        assertThrows(IllegalArgumentException.class, () -> new Complex(real.get(), imag.get()));

        // Test with negative imaginary part
        real = () -> 1.0;
        imag = () -> -2.0;
        assertThrows(IllegalArgumentException.class, () -> new Complex(real.get(), imag.get()));

        // Test with null reference for Complex constructor
        Complex a = new Complex();
        assertThrows(NullPointerException.class, () -> new Complex(null, a));
        assertThrows(NullPointerException.class, () -> new Complex(a, null));
    }

    @Test
    public void testToString() {
        Complex c = new Complex(3.0, 4.0);
        assertEquals("3.0 + 4.0i", c.toString());
    }

    @Test
    public void testStaticMethods() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);

        Complex sum = Complex.plus(a, b);
        Complex expectedSum = new Complex(4.0, 6.0);
        assertEquals(expectedSum.re, sum.re);
        assertEquals(expectedSum.im, sum.im);

        Complex product = Complex.multiply(a, b);
        Complex expectedProduct = new Complex(-5.0, 2.0);
        assertEquals(expectedProduct.re, product.re);
        assertEquals(expectedProduct.im, product.im);
    }
}