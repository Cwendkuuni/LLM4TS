package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex();

        assertEquals(1.0, c1.getRe(), 0.01);
        assertEquals(2.0, c1.getIm(), 0.01);
        assertEquals(3.0, c2.getRe(), 0.01);
        assertEquals(4.0, c2.getIm(), 0.01);
        assertEquals(0.0, c3.getRe(), 0.01);
        assertEquals(0.0, c3.getIm(), 0.01);
    }

    @Test
    public void testToString() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex();

        assertEquals("1.0 + 2.0i", c1.toString());
        assertEquals("3.0 + 4.0i", c2.toString());
        assertEquals("0.0 + 0.0i", c3.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(-1.0, 2.0);
        Complex c3 = new Complex(0.0, 0.0);

        assertEquals(Math.sqrt(5.0), c1.abs(), 0.01);
        assertEquals(Math.sqrt(5.0), c2.abs(), 0.01);
        assertEquals(0.0, c3.abs(), 0.01);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex sum = new Complex();

        a.plus(b);

        assertEquals(4.0, sum.getRe(), 0.01);
        assertEquals(6.0, sum.getIm(), 0.01);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex diff = new Complex();

        a.minus(b);

        assertEquals(-2.0, diff.getRe(), 0.01);
        assertEquals(-2.0, diff.getIm(), 0.01);
    }

    @Test
    public void testTimes() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex product = new Complex();

        a.times(b);

        assertEquals(-5.0, product.getRe(), 0.01);
        assertEquals(11.0, product.getIm(), 0.01);
    }

    @Test
    public void testConjugate() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex conj = new Complex();

        c1.conjugate();

        assertEquals(1.0, conj.getRe(), 0.01);
        assertEquals(-2.0, conj.getIm(), 0.01);
    }

    @Test
    public void testStaticPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex sum = Complex.plus(a, b);

        assertEquals(4.0, sum.getRe(), 0.01);
        assertEquals(6.0, sum.getIm(), 0.01);
    }

    @Test
    public void testStaticMultiplyDouble() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(c1, 3.0);

        assertEquals(3.0, result.getRe(), 0.01);
        assertEquals(6.0, result.getIm(), 0.01);
    }

    @Test
    public void testStaticMultiply() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex product = Complex.multiply(a, b);

        assertEquals(-5.0, product.getRe(), 0.01);
        assertEquals(11.0, product.getIm(), 0.01);
    }

    private Complex getComplex(double re, double im) {
        Complex c = new Complex();
        c.setRe(re);
        c.setIm(im);
        return c;
    }

    private double getRe(Complex c) {
        return c.getRe();
    }

    private double getIm(Complex c) {
        return c.getIm();
    }

    @Test
    public void testComplexOperations() {
        Complex a = getComplex(1.0, 2.0);
        Complex b = getComplex(3.0, 4.0);
        Complex sum = new Complex();
        Complex diff = new Complex();
        Complex product = new Complex();
        Complex conjugate = new Complex();

        // Plus
        a.plus(b);
        assertEquals(4.0, getRe(sum), 0.01);
        assertEquals(6.0, getIm(sum), 0.01);

        // Minus
        a.minus(b);
        assertEquals(-2.0, getRe(diff), 0.01);
        assertEquals(-2.0, getIm(diff), 0.01);

        // Times
        a.times(b);
        assertEquals(-5.0, getRe(product), 0.01);
        assertEquals(11.0, getIm(product), 0.01);

        // Conjugate
        a.conjugate();
        assertEquals(1.0, getRe(conjugate), 0.01);
        assertEquals(-2.0, getIm(conjugate), 0.01);
    }

    @Test
    public void testStaticI() {
        Complex i = Complex.I;
        assertEquals(0.0, i.getRe(), 0.01);
        assertEquals(1.0, i.getIm(), 0.01);
    }
}