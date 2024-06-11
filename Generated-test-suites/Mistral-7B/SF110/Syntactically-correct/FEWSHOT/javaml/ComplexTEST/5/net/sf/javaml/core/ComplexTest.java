package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstructor() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        assertEquals(c1.re, 1.0);
        assertEquals(c1.im, 2.0);
        assertEquals(c2.re, 3.0);
        assertEquals(c2.im, 4.0);
    }

    @Test
    public void testDefaultConstructor() {
        Complex c = new Complex();
        assertEquals(c.re, 0.0);
        assertEquals(c.im, 0.0);
    }

    @Test
    public void testToString() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals(c.toString(), "1.0 + 2.0i");
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals(c.abs(), Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = Complex.plus(a, b);
        assertEquals(c.re, 4.0);
        assertEquals(c.im, 6.0);

        a.plus(b);
        assertEquals(a.re, 4.0);
        assertEquals(a.im, 6.0);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = Complex.minus(a, b);
        assertEquals(c.re, -2.0);
        assertEquals(c.im, -2.0);

        a.minus(b);
        assertEquals(a.re, -2.0);
        assertEquals(a.im, -2.0);
    }

    @Test
    public void testTimes() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = Complex.multiply(a, b);
        assertEquals(c.re, -1.0 + 3.0 * 2.0);
        assertEquals(c.im, 1.0 * 3.0 + 2.0 * 4.0);

        a.times(b);
        assertEquals(a.re, -1.0 + 3.0 * 2.0);
        assertEquals(a.im, 1.0 * 3.0 + 2.0 * 4.0);
    }

    @Test
    public void testTimesDouble() {
        Complex a = new Complex(1.0, 2.0);
        Complex c = Complex.multiply(a, 3.0);
        assertEquals(c.re, 3.0 * 1.0);
        assertEquals(c.im, 3.0 * 2.0);

        a.times(3.0);
        assertEquals(a.re, 3.0 * 1.0);
        assertEquals(a.im, 3.0 * 2.0);
    }

    @Test
    public void testConjugate() {
        Complex a = new Complex(1.0, 2.0);
        Complex c = a.conjugate();
        assertEquals(c.re, 1.0);
        assertEquals(c.im, -2.0);
    }

    @Test
    public void testStaticComplexPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = Complex.plus(a, b);
        assertEquals(c.re, 4.0);
        assertEquals(c.im, 6.0);
    }

    @Test
    public void testStaticComplexMultiplyDouble() {
        Complex a = new Complex(1.0, 2.0);
        Complex c = Complex.multiply(a, 3.0);
        assertEquals(c.re, 3.0 * 1.0);
        assertEquals(c.im, 3.0 * 2.0);
    }

    @Test
    public void testStaticComplexMultiply() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = Complex.multiply(a, b);
        assertEquals(c.re, -1.0 + 3.0 * 2.0);
        assertEquals(c.im, 1.0 * 3.0 + 2.0 * 4.0);
    }

    @Test(expected = NullPointerException.class)
    public void testTimesNullComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = null;
        a.times(b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimesComplexImaginaryZero() {
        Complex a = new Complex(1.0, 0.0);
        Complex b = new Complex(0.0, 0.0);
        a.times(b);
    }
}