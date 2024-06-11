package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstruction() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex();

        assertEquals(1.0, c1.re, 0.0);
        assertEquals(2.0, c1.im, 0.0);
        assertEquals(0.0, c2.re, 0.0);
        assertEquals(0.0, c2.im, 0.0);
    }

    @Test
    public void testComplexToString() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", c.toString());
    }

    @Test
    public void testComplexAbs() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c.abs(), 0.001);
    }

    @Test
    public void testComplexPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex sum = new Complex();
        Complex expectedSum = new Complex(4.0, 6.0);

        c1.plus(c2);
        assertEquals(expectedSum.re, c1.re, 0.0);
        assertEquals(expectedSum.im, c1.im, 0.0);

        sum = Complex.plus(c1, c2);
        assertEquals(expectedSum.re, sum.re, 0.0);
        assertEquals(expectedSum.im, sum.im, 0.0);
    }

    @Test
    public void testComplexMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex difference = new Complex();
        Complex expectedDifference = new Complex(-2.0, -2.0);

        c1.minus(c2);
        assertEquals(expectedDifference.re, c1.re, 0.0);
        assertEquals(expectedDifference.im, c1.im, 0.0);

        difference = Complex.minus(c1, c2);
        assertEquals(expectedDifference.re, difference.re, 0.0);
        assertEquals(expectedDifference.im, difference.im, 0.0);
    }

    @Test
    public void testComplexTimesReal() {
        Complex c = new Complex(1.0, 2.0);
        Complex result = new Complex();

        c.times(3.0);
        assertEquals(3.0 * 1.0, result.re, 0.0);
        assertEquals(3.0 * 2.0, result.im, 0.0);
    }

    @Test
    public void testComplexTimesComplex() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex product = new Complex();
        Complex expectedProduct = new Complex(3.0 * 1.0 - 2.0 * 4.0, 3.0 * 2.0 + 1.0 * 4.0);

        c1.times(c2);
        assertEquals(expectedProduct.re, product.re, 0.0);
        assertEquals(expectedProduct.im, product.im, 0.0);

        product = Complex.multiply(c1, c2);
        assertEquals(expectedProduct.re, product.re, 0.0);
        assertEquals(expectedProduct.im, product.im, 0.0);
    }

    @Test
    public void testComplexConjugate() {
        Complex c = new Complex(1.0, 2.0);
        Complex conjugate = new Complex();

        c.conjugate();
        assertEquals(c.re, conjugate.re, 0.0);
        assertEquals(-c.im, conjugate.im, 0.0);
    }

    @Test
    public void testComplexI() {
        Complex i = Complex.I;
        Complex zero = new Complex();

        assertEquals(0.0, zero.re, 0.0);
        assertEquals(1.0, i.im, 0.0);

        Complex result = new Complex();
        Complex expectedResult = new Complex(0.0, 1.0);

        result.times(i);
        assertEquals(expectedResult.re, result.re, 0.0);
        assertEquals(expectedResult.im, result.im, 0.0);

        result = Complex.multiply(zero, i);
        assertEquals(expectedResult.re, result.re, 0.0);
        assertEquals(expectedResult.im, result.im, 0.0);
    }

    @Test
    public void testComplexNull() {
        Complex c1 = null;
        Complex c2 = new Complex(1.0, 2.0);
        Complex sum = new Complex();

        try {
            c1.plus(c2);
            assert false; // Should throw NPE
        } catch (NullPointerException e) {
            // Expected
        }

        sum = Complex.plus(c1, c2);
        assertNull(sum);

        try {
            Complex.multiply(c1, c2);
            assert false; // Should throw NPE
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            c1.times(c2);
            assert false; // Should throw NPE
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            c1.conjugate();
            assert false; // Should throw NPE
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            Complex.plus(c1, c2);
            assert false; // Should throw NPE
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            Complex.multiply(c1, c2);
            assert false; // Should throw NPE
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testComplexZeroTimesComplex() {
        Complex zero = new Complex();
        Complex c = new Complex(1.0, 2.0);
        Complex result = new Complex();

        try {
            zero.times(c);
            assert false; // Should throw ArithmeticException
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            Complex.multiply(zero, c);
            assert false; // Should throw ArithmeticException
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testComplexZeroTimesReal() {
        Complex zero = new Complex();
        double alpha = 3.0;
        Complex result = new Complex();

        try {
            zero.times(alpha);
            assert false; // Should throw ArithmeticException
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            Complex.multiply(zero, alpha);
            assert false; // Should throw ArithmeticException
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testComplexRealTimesComplex() {
        Complex real = new Complex(3.0, 0.0);
        Complex c = new Complex(1.0, 2.0);
        Complex result = new Complex();

        try {
            real.times(c);
            assert false; // Should throw ArithmeticException
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            Complex.multiply(real, c);
            assert false; // Should throw ArithmeticException
        } catch (ArithmeticException e) {
            // Expected
        }
    }
}