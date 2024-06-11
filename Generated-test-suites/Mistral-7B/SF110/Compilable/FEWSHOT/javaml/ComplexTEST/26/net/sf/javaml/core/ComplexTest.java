package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex();
        assertEquals(1.0, c1.re, 0.0);
        assertEquals(2.0, c1.im, 0.0);
        assertEquals(0.0, c2.re);
        assertEquals(0.0, c2.im);
    }

    @Test
    public void testToString() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", c.toString());
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals(Math.sqrt(5.0), c.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex();
        a.plus(b);
        assertEquals(4.0, c.re);
        assertEquals(6.0, c.im);

        c = Complex.plus(a, b);
        assertEquals(4.0, c.re);
        assertEquals(6.0, c.im);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex();
        a.minus(b);
        assertEquals(-2.0, c.re);
        assertEquals(-2.0, c.im);

        c = Complex.plus(a, Complex.multiply(b, -1.0));
        assertEquals(-2.0, c.re);
        assertEquals(-2.0, c.im);
    }

    @Test
    public void testTimes() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex();

        a.times(b);
        assertEquals(-2.0, c.re);
        assertEquals(6.0, c.im);

        c = Complex.multiply(a, b);
        assertEquals(-2.0, c.re);
        assertEquals(6.0, c.im);
    }

    @Test
    public void testTimesWithScalar() {
        Complex c = new Complex(1.0, 2.0);
        Complex d = Complex.multiply(c, 3.0);
        assertEquals(3.0, d.re);
        assertEquals(6.0, d.im);
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(1.0, 2.0);
        Complex d = new Complex(1.0, -2.0);
        c.conjugate();
        assertEquals(d.re, c.re);
        assertEquals(d.im, -c.im);
    }

    @Test
    public void testStaticMethods() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex();

        c = Complex.plus(a, b);
        assertEquals(4.0, c.re);
        assertEquals(6.0, c.im);

        c = Complex.multiply(a, b);
        assertEquals(-2.0, c.re);
        assertEquals(6.0, c.im);

        c = Complex.multiply(a, 3.0);
        assertEquals(3.0, c.re);
        assertEquals(6.0, c.im);
    }

    @Test
    public void testStaticMethodsWithNull() {
        assertNull(Complex.plus(null, null));
        Complex a = new Complex(1.0, 2.0);
        assertNull(Complex.plus(null, a));
        assertNull(Complex.plus(a, null));

        assertNull(Complex.multiply(null, null));
        assertNull(Complex.multiply(null, new Complex(1.0, 2.0)));
        assertNull(Complex.multiply(new Complex(1.0, 2.0), null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimesWithZeroDivision() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(0.0, 0.0);
        a.times(b);
    }
}