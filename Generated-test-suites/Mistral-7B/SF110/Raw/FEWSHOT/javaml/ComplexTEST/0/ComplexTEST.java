package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstruction() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex();

        assertEquals(1.0, c1.re, 0.0);
        assertEquals(2.0, c1.im, 0.0);
        assertEquals(3.0, c2.re, 0.0);
        assertEquals(4.0, c2.im, 0.0);
        assertEquals(0.0, c3.re, 0.0);
        assertEquals(0.0, c3.im, 0.0);
    }

    @Test
    public void testToString() {
        Complex c1 = new Complex(1.0, 2.0);

        assertEquals("1.0 + 2.0i", c1.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(1.0, 2.0);

        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(4.0, 6.0);

        c1.plus(c2);

        assertEquals(4.0, c1.re, 0.0);
        assertEquals(6.0, c1.im, 0.0);

        Complex sum = Complex.plus(c1, c2);

        assertEquals(4.0, sum.re, 0.0);
        assertEquals(6.0, sum.im, 0.0);
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(0.0, 2.0);

        c1.minus(c2);

        assertEquals(-2.0, c1.re, 0.0);
        assertEquals(-2.0, c1.im, 0.0);

        Complex diff = Complex.minus(c1, c2);

        assertEquals(-2.0, diff.re, 0.0);
        assertEquals(-2.0, diff.im, 0.0);
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(-2.0, 5.0);

        c1.times(c2);

        assertEquals(-5.0, c1.re, 0.0);
        assertEquals(11.0, c1.im, 0.0);

        Complex product = Complex.multiply(c1, c2);

        assertEquals(-5.0, product.re, 0.0);
        assertEquals(11.0, product.im, 0.0);
    }

    @Test
    public void testTimesDouble() {
        Complex c1 = new Complex(1.0, 2.0);

        c1.times(2.0);

        assertEquals(2.0, c1.re, 0.0);
        assertEquals(4.0, c1.im, 0.0);

        Complex scaled = Complex.multiply(c1, 2.0);

        assertEquals(2.0, scaled.re, 0.0);
        assertEquals(4.0, scaled.im, 0.0);
    }

    @Test
    public void testConjugate() {
        Complex c1 = new Complex(1.0, 2.0);

        c1.conjugate();

        assertEquals(1.0, c1.re, 0.0);
        assertEquals(-2.0, c1.im, 0.0);
    }

    @Test
    public void testStaticMethods() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex sum = Complex.plus(c1, c2);
        Complex product = Complex.multiply(c1, c2);

        Complex i = Complex.I;

        assertEquals(4.0, sum.re, 0.0);
        assertEquals(6.0, sum.im, 0.0);
        assertEquals(-5.0, product.re, 0.0);
        assertEquals(11.0, product.im, 0.0);
        assertEquals(0.0, i.re, 0.0);
        assertEquals(1.0, i.im, 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        Complex c1 = new Complex();
        c1.plus(null);
    }

    @Test
    public void testStaticMethodsNullInput() {
        Complex c1 = new Complex();

        assertNull(Complex.plus(c1, null));
        assertNull(Complex.multiply(c1, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentInput() {
        Complex c1 = new Complex();
        Complex c2 = new Complex();

        c1.times(c2);
        c1.conjugate();

        Complex zero = new Complex(0.0, 0.0);

        c1.divide(zero);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticMethodsIllegalArgumentInput() {
        Complex zero = new Complex(0.0, 0.0);

        Complex result = Complex.divide(Complex.I, zero);
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        Complex c1 = new Complex(1.0, 0.0);
        Complex c2 = new Complex(0.0, 0.0);

        c1.divide(c2);
    }

    @Test(expected = ArithmeticException.class)
    public void testStaticMethodsDivideByZero() {
        Complex c1 = new Complex(1.0, 0.0);

        Complex result = Complex.divide(c1, new Complex(0.0, 0.0));
    }

    @Test
    public void testEquals() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(1.0, 2.0);
        Complex c3 = new Complex(3.0, 4.0);

        assertTrue(c1.equals(c2));
        assertFalse(c1.equals(c3));
    }

    @Test
    public void testEqualsHashCode() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(1.0, 2.0);

        assertEquals(c1.hashCode(), c2.hashCode());
    }
}