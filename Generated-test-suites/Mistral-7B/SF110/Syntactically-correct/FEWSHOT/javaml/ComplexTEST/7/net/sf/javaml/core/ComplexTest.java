package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructorWithDoubleReDoubleIm() {
        Complex complex = new Complex(1.0, 2.0);
        assertEquals(1.0, complex.re, 0.0);
        assertEquals(2.0, complex.im, 0.0);
    }

    @Test
    public void testConstructorWithNoArguments() {
        Complex complex = new Complex();
        assertEquals(0.0, complex.re, 0.0);
        assertEquals(0.0, complex.im, 0.0);
    }

    @Test
    public void testToString() {
        Complex complex = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", complex.toString());
    }

    @Test
    public void testAbs() {
        Complex complex = new Complex(1.0, 2.0);
        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), complex.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex complexA = new Complex(1.0, 2.0);
        Complex complexB = new Complex(3.0, 4.0);
        Complex result = Complex.plus(complexA, complexB);
        assertEquals(4.0, result.re, 0.0);
        assertEquals(6.0, result.im, 0.0);

        complexA.plus(complexB);
        assertEquals(4.0, complexA.re, 0.0);
        assertEquals(6.0, complexA.im, 0.0);
    }

    @Test
    public void testMinus() {
        Complex complexA = new Complex(1.0, 2.0);
        Complex complexB = new Complex(3.0, 4.0);
        Complex result = Complex.minus(complexA, complexB);
        assertEquals(-2.0, result.re, 0.0);
        assertEquals(-2.0, result.im, 0.0);

        complexA.minus(complexB);
        assertEquals(-2.0, complexA.re, 0.0);
        assertEquals(-2.0, complexA.im, 0.0);
    }

    @Test
    public void testTimes() {
        Complex complexA = new Complex(1.0, 2.0);
        Complex complexB = new Complex(3.0, 4.0);
        Complex result = Complex.multiply(complexA, complexB);
        assertEquals(-5.0, result.re, 0.0);
        assertEquals(10.0, result.im, 0.0);

        complexA.times(complexB);
        assertEquals(-5.0, complexA.re, 0.0);
        assertEquals(10.0, complexA.im, 0.0);
    }

    @Test
    public void testTimesWithDouble() {
        Complex complex = new Complex(1.0, 2.0);
        complex.times(3.0);
        assertEquals(3.0 * 1.0, complex.re, 0.0);
        assertEquals(3.0 * 2.0, complex.im, 0.0);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(1.0, 2.0);
        complex.conjugate();
        assertEquals(1.0, complex.re, 0.0);
        assertEquals(-2.0, complex.im, 0.0);
    }

    @Test
    public void testI() {
        assertEquals(0.0, Complex.I.re, 0.0);
        assertEquals(1.0, Complex.I.im, 0.0);
    }

    @Test
    public void testPlusWithI() {
        Complex complexA = new Complex(1.0, 2.0);
        Complex complexB = Complex.I;
        Complex result = Complex.plus(complexA, complexB);
        assertEquals(1.0 + 1.0, result.re, 0.0);
        assertEquals(2.0, result.im, 0.0);

        complexA.plus(Complex.I);
        assertEquals(1.0 + 1.0, complexA.re, 0.0);
        assertEquals(2.0, complexA.im, 0.0);
    }

    @Test
    public void testMultiplyWithI() {
        Complex complexA = new Complex(1.0, 2.0);
        Complex result = Complex.multiply(complexA, Complex.I);
        assertEquals(1.0, result.re, 0.0);
        assertEquals(2.0, result.im, 0.0);

        complexA.times(Complex.I);
        assertEquals(1.0, complexA.re, 0.0);
        assertEquals(2.0, complexA.im, 0.0);
    }
}