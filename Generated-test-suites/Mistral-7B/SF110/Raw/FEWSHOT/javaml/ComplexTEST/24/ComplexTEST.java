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
        assertEquals("1.0 + 2.0i", c1.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(1.0, 2.0);
        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.01);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex sum = new Complex();
        c1.plus(c2);

        sum.setRe(1.0 + 3.0);
        sum.setIm(2.0 + 4.0);
        assertEquals(sum.getRe(), c1.getRe(), 0.01);
        assertEquals(sum.getIm(), c1.getIm(), 0.01);
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex diff = new Complex();
        c1.minus(c2);

        diff.setRe(1.0 - 3.0);
        diff.setIm(2.0 - 4.0);
        assertEquals(diff.getRe(), c1.getRe(), 0.01);
        assertEquals(diff.getIm(), c1.getIm(), 0.01);
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex product = new Complex();
        c1.times(c2);

        double real = 1.0 * 3.0 - 2.0 * 4.0;
        double imag = 1.0 * 4.0 + 2.0 * 3.0;
        product.setRe(real);
        product.setIm(imag);
        assertEquals(product.getRe(), c1.getRe(), 0.01);
        assertEquals(product.getIm(), c1.getIm(), 0.01);
    }

    @Test
    public void testConjugate() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex conj = new Complex();
        c1.conjugate();

        conj.setRe(1.0);
        conj.setIm(-2.0);
        assertEquals(conj.getRe(), c1.getRe(), 0.01);
        assertEquals(conj.getIm(), -c1.getIm(), 0.01);
    }

    @Test
    public void testStaticMethodsPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex sum = Complex.plus(c1, c2);

        sum.setRe(1.0 + 3.0);
        sum.setIm(2.0 + 4.0);
        assertEquals(sum.getRe(), c1.getRe(), 0.01);
        assertEquals(sum.getIm(), c1.getIm(), 0.01);
    }

    @Test
    public void testStaticMethodsMultiplyReal() {
        Complex c1 = new Complex(1.0, 2.0);
        double alpha = 3.0;
        Complex product = new Complex();
        Complex.times(c1, alpha);

        product.setRe(1.0 * 3.0);
        product.setIm(2.0 * 3.0);
        assertEquals(product.getRe(), c1.getRe() * alpha, 0.01);
        assertEquals(product.getIm(), c1.getIm() * alpha, 0.01);
    }

    @Test
    public void testStaticMethodsMultiplyComplex() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex product = new Complex();
        Complex.multiply(c1, c2);

        double real = 1.0 * 3.0 - 2.0 * 4.0;
        double imag = 1.0 * 4.0 + 2.0 * 3.0;
        product.setRe(real);
        product.setIm(imag);
        assertEquals(product.getRe(), c1.getRe() * c2.getRe() - c1.getIm() * c2.getIm(), 0.01);
        assertEquals(product.getIm(), c1.getRe() * c2.getIm() + c1.getIm() * c2.getRe(), 0.01);
    }

    @Test
    public void testStaticMethodsI() {
        Complex i = Complex.I;
        assertEquals(0.0, i.getRe(), 0.01);
        assertEquals(1.0, i.getIm(), 0.01);
    }
}