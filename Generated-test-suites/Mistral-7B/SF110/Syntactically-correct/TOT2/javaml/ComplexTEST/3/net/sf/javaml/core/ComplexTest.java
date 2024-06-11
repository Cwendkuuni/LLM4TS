package net.sf.javaml.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ComplexTest {

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = Math.sqrt(3.0 * 3.0 + 4.0 * 4.0);
        double actual = complex.abs();
        assertEquals(expected, actual);
    }

    @Test
    public void testPlus() {
        Complex complexA = new Complex(3.0, 4.0);
        Complex complexB = new Complex(1.0, 2.0);
        Complex expected = new Complex(4.0, 6.0);
        complexA.plus(complexB);
        Complex actual = complexA;
        assertEquals(expected.re, actual.re);
        assertEquals(expected.im, actual.im);
    }

    @Test
    public void testTimes() {
        Complex complexA = new Complex(3.0, 4.0);
        Complex complexB = new Complex(1.0, 2.0);
        Complex expected = new Complex(6.0, -2.0);
        complexA.times(complexB);
        Complex actual = complexA;
        assertEquals(expected.re, actual.re);
        assertEquals(expected.im, actual.im);
    }

    @Test
    public void testToString() {
        Complex complex = new Complex(3.0, 4.0);
        String expected = "3.0 + 4.0i";
        String actual = complex.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(3.0, 4.0);
        Complex expected = new Complex(3.0, -4.0);
        complex.conjugate();
        Complex actual = complex;
        assertEquals(expected.re, actual.re);
        assertEquals(-expected.im, actual.im);
    }

    @Test
    public void testStaticPlus() {
        Complex complexA = new Complex(3.0, 4.0);
        Complex complexB = new Complex(1.0, 2.0);
        Complex expected = new Complex(4.0, 6.0);
        Complex actual = Complex.plus(complexA, complexB);
        assertEquals(expected.re, actual.re);
        assertEquals(expected.im, actual.im);
    }

    @Test
    public void testStaticMultiply() {
        Complex complexA = new Complex(3.0, 4.0);
        Complex complexB = new Complex(1.0, 2.0);
        Complex expected = new Complex(6.0, -2.0);
        Complex actual = Complex.multiply(complexA, complexB);
        assertEquals(expected.re, actual.re);
        assertEquals(expected.im, actual.im);
    }
}