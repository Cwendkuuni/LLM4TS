package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testAbs() {
        Complex c1 = new Complex(3.0, 4.0);
        double result = c1.abs();
        double expected = Math.sqrt(3 * 3 + 4 * 4);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex result = new Complex();
        Complex expected = new Complex(4.0, 6.0);

        c1.plus(c2);

        assertEquals(expected.getRe(), result.getRe(), 0.0);
        assertEquals(expected.getIm(), result.getIm(), 0.0);
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(1.0, 1.0);
        Complex expected = new Complex(-1.0, 3.0);
        Complex result = new Complex();

        c1.times(c2);

        assertEquals(expected.getRe(), result.getRe(), 0.0);
        assertEquals(expected.getIm(), result.getIm(), 0.0);
    }

    // Add test cases for other methods here

    @Test
    public void testStaticMethodsPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex expected = new Complex(4.0, 6.0);

        Complex result = Complex.plus(c1, c2);

        assertEquals(expected.getRe(), result.getRe(), 0.0);
        assertEquals(expected.getIm(), result.getIm(), 0.0);
    }

    @Test
    public void testStaticMethodsMultiplyWithDouble() {
        Complex c1 = new Complex(1.0, 1.0);
        double d = 2.0;
        Complex expected = new Complex(2.0, 2.0);

        Complex result = Complex.multiply(c1, d);

        assertEquals(expected.getRe(), result.getRe(), 0.0);
        assertEquals(expected.getIm(), result.getIm(), 0.0);
    }

    @Test
    public void testStaticMethodsMultiply() {
        Complex c1 = new Complex(1.0, 1.0);
        Complex c2 = new Complex(1.0, 1.0);
        Complex expected = new Complex(0.0, 2.0);

        Complex result = Complex.multiply(c1, c2);

        assertEquals(expected.getRe(), result.getRe(), 0.0);
        assertEquals(expected.getIm(), result.getIm(), 0.0);
    }
}