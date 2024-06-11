package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstructor() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex();

        assertEquals(1.0, c1.getRe(), 0.0);
        assertEquals(2.0, c1.getIm(), 0.0);
        assertEquals(3.0, c2.getRe(), 0.0);
        assertEquals(4.0, c2.getIm(), 0.0);
        assertEquals(0.0, c3.getRe(), 0.0);
        assertEquals(0.0, c3.getIm(), 0.0);
    }

    @Test
    public void testToString() {
        Complex c = new Complex(1.0, 2.0);
        Complex c1 = new Complex(1.0, 1.0);
        Complex c2 = new Complex(-1.0, 2.0);
        Complex c3 = new Complex(0.0, 3.0);
        Complex c4 = new Complex(1.0, -2.0);

        assertEquals("1.0 + 2.0i", c.toString());
        assertEquals("1.0 + 1.0i", c1.toString());
        assertEquals("-1.0 + 2.0i", c2.toString());
        assertEquals("0.0 + 3.0i", c3.toString());
        assertEquals("1.0 - 2.0i", c4.toString());
    }

    @Test
    public void testAbs() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(-1.0, 2.0);
        Complex c3 = new Complex(0.0, 3.0);

        assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.001);
        assertEquals(Math.sqrt(1.0 * 1.0 + (-2.0) * 2.0), c2.abs(), 0.001);
        assertEquals(Math.sqrt(0.0 * 0.0 + 3.0 * 3.0), c3.abs(), 0.001);
    }

    @Test
    public void testPlus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(4.0, 6.0);

        Complex sum = Complex.plus(c1, c2);

        assertEquals("4.0 + 6.0i", sum.toString());
        Complex sumManual = new Complex(4.0, 6.0);
        assertEquals(sum, sumManual);

        sum = Complex.plus(c1, c3);

        assertEquals("5.0 + 8.0i", sum.toString());
        sumManual = new Complex(5.0, 8.0);
        assertEquals(sum, sumManual);

        sum = Complex.plus(c1, new Complex(0.0, 0.0));

        assertEquals("1.0 + 2.0i", sum.toString());
        sumManual = new Complex(1.0, 2.0);
        assertEquals(sum, sumManual);
    }

    @Test
    public void testMinus() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(-2.0, -2.0);

        Complex difference = Complex.minus(c1, c2);

        assertEquals("-2.0 - 2.0i", difference.toString());
        Complex differenceManual = new Complex(-2.0, -2.0);
        assertEquals(difference, differenceManual);

        difference = Complex.minus(c1, c3);

        assertEquals("1.0 + 2.0i", difference.toString());
        differenceManual = new Complex(1.0, 2.0);
        assertEquals(difference, differenceManual);

        difference = Complex.minus(c2, c1);

        assertEquals("2.0 - 2.0i", difference.toString());
        differenceManual = new Complex(2.0, -2.0);
        assertEquals(difference, differenceManual);
    }

    @Test
    public void testTimes() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = new Complex(1.0, 1.0);
        Complex c4 = new Complex(0.0, 1.0);

        Complex product = Complex.multiply(c1, c2);

        assertEquals("-3.0 + 10.0i", product.toString());
        Complex productManual = new Complex(-3.0, 10.0);
        assertEquals(product, productManual);

        product = Complex.multiply(c1, c3);

        assertEquals("1.0 + 2.0i", product.toString());
        productManual = new Complex(1.0, 2.0);
        assertEquals(product, productManual);

        product = Complex.multiply(c1, c4);

        assertEquals("1.0 + 2.0i", product.toString());
        productManual = new Complex(1.0, 2.0);
        assertEquals(product, productManual);

        product = Complex.multiply(c1, Complex.I);

        assertEquals("1.0 + 2.0i", product.toString());
        productManual = new Complex(1.0, 2.0);
        assertEquals(product, productManual);

        product = Complex.multiply(Complex.I, c1);

        assertEquals("1.0 + 2.0i", product.toString());
        productManual = new Complex(1.0, 2.0);
        assertEquals(product, productManual);

        product = Complex.multiply(c2, c2);

        assertEquals("13.0 + 14.0i", product.toString());
        productManual = new Complex(13.0, 14.0);
        assertEquals(product, productManual);

        product = Complex.multiply(c1, 2.0);

        assertEquals("2.0 + 4.0i", product.toString());
        Complex productManual2 = new Complex(2.0, 4.0);
        assertEquals(product, productManual2);
    }

    @Test
    public void testConjugate() {
        Complex c1 = new Complex(1.0, 2.0);

        Complex conjugate = c1.conjugate();

        assertEquals("1.0 - 2.0i", conjugate.toString());
        Complex conjugateManual = new Complex(1.0, -2.0);
        assertEquals(conjugate, conjugateManual);
    }

    @Test
    public void testStaticMethods() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(3.0, 4.0);
        Complex c3 = Complex.I;

        Complex sum = Complex.plus(c1, c2);

        assertEquals("4.0 + 6.0i", sum.toString());
        Complex sumManual = new Complex(4.0, 6.0);
        assertEquals(sum, sumManual);

        Complex product = Complex.multiply(c1, c3);

        assertEquals("1.0 + 2.0i", product.toString());
        Complex productManual = new Complex(1.0, 2.0);
        assertEquals(product, productManual);

        Complex productDouble = Complex.multiply(c1, 2.0);

        assertEquals("2.0 + 4.0i", productDouble.toString());
        Complex productManual2 = new Complex(2.0, 4.0);
        assertEquals(productDouble, productManual2);

        Complex constant = Complex.multiply(Complex.I, Complex.I);

        assertEquals("1.0 + 0.0i", constant.toString());
        Complex constantManual = new Complex(1.0, 0.0);
        assertEquals(constant, constantManual);

        Complex zero = new Complex(0.0, 0.0);
        Complex constantZero = Complex.multiply(zero, zero);

        assertEquals("0.0 + 0.0i", constantZero.toString());
        Complex constantZeroManual = new Complex(0.0, 0.0);
        assertEquals(constantZero, constantZeroManual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimesWithNull() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = null;

        Complex result = Complex.times(c1, c2);

        // The test should fail with IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinusWithNull() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = null;

        Complex result = Complex.minus(c1, c2);

        // The test should fail with IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimesWithComplexZero() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(0.0, 0.0);

        Complex result = Complex.times(c1, c2);

        assertEquals("0.0 + 0.0i", result.toString());
        Complex resultManual = new Complex(0.0, 0.0);
        assertEquals(result, resultManual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlusWithNull() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = null;

        Complex result = Complex.plus(c1, c2);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinusWithSelf() {
        Complex c1 = new Complex(1.0, 2.0);

        try {
            Complex result = Complex.minus(c1, c1);
        } catch (IllegalArgumentException e) {
            // The test should fail with IllegalArgumentException
            return;
        }

        fail("The test should have failed with IllegalArgumentException");
    }

    @Test
    public void testGetters() {
        Complex c = new Complex(1.0, 2.0);

        assertEquals(1.0, c.getRe(), 0.0);
        assertEquals(2.0, c.getIm(), 0.0);

        c.setRe(3.0);
        c.setIm(4.0);

        assertEquals(3.0, c.getRe(), 0.0);
        assertEquals(4.0, c.getIm(), 0.0);
    }

    @Test
    public void testEquals() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex(1.0, 2.0);
        Complex c3 = new Complex(1.0, 3.0);
        Complex c4 = new Complex(3.0, 2.0);

        assertEquals(c1, c1);
        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, c4);
        Complex c5 = new Complex(c1);
        assertEquals(c1, c5);
    }
}