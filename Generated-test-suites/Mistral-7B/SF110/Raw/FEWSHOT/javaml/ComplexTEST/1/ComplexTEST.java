package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ComplexTest {

    // Test constructor
    @Test
    public void testComplexConstruction() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex();

        assertEquals(1.0, c1.re, 0.0);
        assertEquals(2.0, c1.im, 0.0);

        assertEquals(0.0, c2.re, 0.0);
        assertEquals(0.0, c2.im, 0.0);
    }

    // Test toString method
    @Test
    public void testToString() {
        Complex c1 = new Complex(3.0, 4.0);

        assertEquals("3.0 + 4.0i", c1.toString());
    }

    // Test abs method
    @Test
    public void testAbs() {
        Complex c1 = new Complex(3.0, 4.0);

        assertEquals(5.0, c1.abs(), 0.0);
    }

    // Test plus method
    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex sum = new Complex();
        c1.plus(c2);

        assertEquals(4.0, sum.re, 0.0);
        assertEquals(6.0, sum.im, 0.0);
    }

    // Test minus method
    @Test
    public void testMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex difference = new Complex();
        c1.minus(c2);

        assertEquals(-2.0, difference.re, 0.0);
        assertEquals(-2.0, difference.im, 0.0);
    }

    // Test times method (multiplication with another Complex)
    @Test
    public void testTimesComplex() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex product = new Complex();
        c1.times(c2);

        assertEquals(-5.0, product.re, 0.0);
        assertEquals(11.0, product.im, 0.0);
    }

    // Test times method (multiplication with a double)
    @Test
    public void testTimesDouble() {
        Complex c1 = new Complex(1.0, 2.0);

        Complex result = new Complex();
        c1.times(3.0);

        assertEquals(3.0, result.re, 0.0);
        assertEquals(6.0, result.im, 0.0);
    }

    // Test conjugate method
    @Test
    public void testConjugate() {
        Complex c1 = new Complex(1.0, 2.0);

        Complex conjugate = new Complex();
        c1.conjugate();

        assertEquals(1.0, conjugate.re, 0.0);
        assertEquals(-2.0, conjugate.im, 0.0);
    }

    // Test static plus method
    @Test
    public void testStaticPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex sum = Complex.plus(c1, c2);

        assertEquals(4.0, sum.re, 0.0);
        assertEquals(6.0, sum.im, 0.0);
    }

    // Test static multiply method (multiplication with a double)
    @Test
    public void testStaticMultiplyDouble() {
        Complex c1 = new Complex(1.0, 2.0);

        Complex result = Complex.multiply(c1, 3.0);

        assertEquals(3.0, result.re, 0.0);
        assertEquals(6.0, result.im, 0.0);
    }

    // Test static multiply method (multiplication with another Complex)
    @Test
    public void testStaticMultiplyComplex() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);

        Complex product = Complex.multiply(c1, c2);

        assertEquals(-5.0, product.re, 0.0);
        assertEquals(11.0, product.im, 0.0);
    }

    // Test I constant
    @Test
    public void testI() {
        Complex I = Complex.I;

        Complex expected = new Complex(0.0, 1.0);

        assertEquals(expected.re, I.re, 0.0);
        assertEquals(expected.im, I.im, 0.0);
    }

    // Test null input for methods
    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        Complex c1 = new Complex();

        Complex c2 = null;

        c1.plus(c2);
        c1.minus(c2);
        c1.times(c2);
        c1.conjugate();
    }
}