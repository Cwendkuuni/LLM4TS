package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {
    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = Math.sqrt(3.0 * 3.0 + 4.0 * 4.0);
        assertEquals(expected, complex.abs());
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex expected = new Complex(4.0, 6.0);
        a.plus(b);
        assertEquals(expected.getRe(), a.getRe());
        assertEquals(expected.getIm(), a.getIm());
    }

    @Test
    public void testTimes() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(1.0, 1.0);
        Complex expected = new Complex(-1.0, 3.0);
        a.times(b);
        assertEquals(expected.getRe(), a.getRe());
        assertEquals(expected.getIm(), a.getIm());
    }

    @Test
    public void testToString() {
        Complex complex = new Complex(2.0, 3.0);
        String expected = "2.0 + 3.0i";
        assertEquals(expected, complex.toString());
    }

    @Test
    public void testConjugate() {
        Complex complex = new Complex(1.0, 2.0);
        Complex expected = new Complex(1.0, -2.0);
        complex.conjugate();
        assertEquals(expected.getRe(), complex.getRe());
        assertEquals(expected.getIm(), complex.getIm());
    }

    @Test
    public void testComplexTest() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);

        // Test the entire Complex class
        Complex c = a.copy();
        Complex d = new Complex(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), 0.0);
        Complex sum = a.copy();
        Complex product = a.copy();
        Complex sumOfTwoComplexNumbers = Complex.plus(a, b);
        Complex productOfComplexNumberAndNumber = Complex.multiply(a, 2.0);
        Complex productOfTwoComplexNumbers = Complex.multiply(a, b);
        Complex expectedSum = new Complex(4.0, 6.0);
        Complex expectedProductOfComplexNumberAndNumber = new Complex(2.0, 4.0);
        Complex expectedProductOfTwoComplexNumbers = new Complex(-5.0, 2.0);

        c.abs();
        assertEquals(d.getRe(), c.getRe());
        assertEquals(d.getIm(), c.getIm(), 0.0);

        sum.plus(b);
        assertEquals(expectedSum.getRe(), sum.getRe());
        assertEquals(expectedSum.getIm(), sum.getIm());

        product.times(b);
        assertEquals(expectedProductOfComplexNumberAndNumber.getRe(), product.getRe());
        assertEquals(expectedProductOfComplexNumberAndNumber.getIm(), product.getIm());

        productOfTwoComplexNumbers = Complex.multiply(a, b);
        assertEquals(expectedProductOfTwoComplexNumbers.getRe(), productOfTwoComplexNumbers.getRe());
        assertEquals(expectedProductOfTwoComplexNumbers.getIm(), productOfTwoComplexNumbers.getIm());

        Complex copiedComplex = a.copy();
        assertEquals(a.getRe(), copiedComplex.getRe());
        assertEquals(a.getIm(), copiedComplex.getIm());

        Complex sumOfTwoComplexNumbersFromStaticMethod = Complex.plus(a, b);
        assertEquals(expectedSum.getRe(), sumOfTwoComplexNumbersFromStaticMethod.getRe());
        assertEquals(expectedSum.getIm(), sumOfTwoComplexNumbersFromStaticMethod.getIm());

        Complex productOfComplexNumberAndNumberFromStaticMethod = Complex.multiply(a, 2.0);
        assertEquals(expectedProductOfComplexNumberAndNumber.getRe(), productOfComplexNumberAndNumberFromStaticMethod.getRe());
        assertEquals(expectedProductOfComplexNumberAndNumber.getIm(), productOfComplexNumberAndNumberFromStaticMethod.getIm());

        Complex productOfTwoComplexNumbersFromStaticMethod = Complex.multiply(a, b);
        assertEquals(expectedProductOfTwoComplexNumbers.getRe(), productOfTwoComplexNumbersFromStaticMethod.getRe());
        assertEquals(expectedProductOfTwoComplexNumbers.getIm(), productOfTwoComplexNumbersFromStaticMethod.getIm());
    }
}