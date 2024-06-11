package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex();

        assertEquals(1.0, c1.getRe(), 0.001);
        assertEquals(2.0, c1.getIm(), 0.001);
        assertEquals(3.0, c2.getRe(), 0.001);
        assertEquals(4.0, c2.getIm(), 0.001);
        assertEquals(0.0, c3.getRe(), 0.001);
        assertEquals(0.0, c3.getIm(), 0.001);
    }

    @Test
    public void testToString() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", c.toString());
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(1.0, 1.0);
        assertEquals(Math.sqrt(2.0), c.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex(4.0, 6.0);

        a.plus(b);
        assertEquals(c.getRe(), a.getRe(), 0.001);
        assertEquals(c.getIm(), a.getIm(), 0.001);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex(-2.0, -2.0);

        a.minus(b);
        assertEquals(c.getRe(), a.getRe(), 0.001);
        assertEquals(c.getIm(), a.getIm(), 0.001);
    }

    @Test
    public void testTimes() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex(-1.0, 11.0);

        a.times(b);
        assertEquals(c.getRe(), a.getRe(), 0.001);
        assertEquals(c.getIm(), a.getIm(), 0.001);
    }

    @Test
    public void testConjugate() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(1.0, -2.0);

        a.conjugate();
        assertEquals(b.getRe(), a.getRe(), 0.001);
        assertEquals(b.getIm(), a.getIm(), 0.001);
    }

    @Test
    public void testStaticMethods() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = Complex.plus(a, b);
        Complex d = Complex.multiply(a, 2.0);
        Complex e = Complex.multiply(a, b);

        assertEquals(c.getRe(), a.getRe() + b.getRe(), 0.001);
        assertEquals(c.getIm(), a.getIm() + b.getIm(), 0.001);
        assertEquals(d.getRe(), a.getRe() * 2.0, 0.001);
        assertEquals(d.getIm(), a.getIm() * 2.0, 0.001);
        assertEquals(e.getRe(), a.getRe() * b.getRe() - a.getIm() * b.getIm(), 0.001);
        assertEquals(e.getIm(), a.getRe() * b.getIm() + a.getIm() * b.getRe(), 0.001);
    }

    private Complex getComplex(double real, double imaginary) {
        return new Complex(real, imaginary);
    }

    @Test
    public void testStaticMethodsWithPrivateObjects() {
        Complex a = getComplex(1.0, 2.0);
        Complex b = getComplex(3.0, 4.0);
        Complex c = Complex.plus(a, b);
        Complex d = Complex.multiply(a, 2.0);
        Complex e = Complex.multiply(a, b);

        assertEquals(c.getRe(), a.getRe() + b.getRe(), 0.001);
        assertEquals(c.getIm(), a.getIm() + b.getIm(), 0.001);
        assertEquals(d.getRe(), a.getRe() * 2.0, 0.001);
        assertEquals(d.getIm(), a.getIm() * 2.0, 0.001);
        assertEquals(e.getRe(), a.getRe() * b.getRe() - a.getIm() * b.getIm(), 0.001);
        assertEquals(e.getIm(), a.getRe() * b.getIm() + a.getIm() * b.getRe(), 0.001);
    }
}