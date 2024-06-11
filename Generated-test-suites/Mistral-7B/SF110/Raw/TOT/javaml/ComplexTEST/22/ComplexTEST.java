
package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {
    @Test
    public void testComplexConstructorValidArgs() {
        Complex complex = new Complex(2.0, 3.0);
        assertEquals("2.0 + 3.0i", complex.toString());
    }

    @Test
    public void testAbsValidComplex() {
        Complex complex = new Complex(1.0, 2.0);
        double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
        assertEquals(expected, complex.abs(), 0.001);
    }

    @Test
    public void testAbsNegativeComplex() {
        Complex complex = new Complex(-1.0, 2.0);
        double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
        assertEquals(expected, complex.abs(), 0.001);
    }

    @Test
    public void testPlusValidComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex expected = new Complex(4.0, 6.0);
        a.plus(b);
        assertEquals(expected.toString(), a.toString());
    }

    @Test
    public void testMinusValidComplex() {
        Complex a = new Complex(3.0, 4.0);
        Complex b = new Complex(1.0, 2.0);
        Complex expected = new Complex(2.0, -2.0);
        a.minus(b);
        assertEquals(expected.toString(), a.toString());
    }

    @Test
    public void testTimesValidComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex expected = new Complex(-5.0, 11.0);
        a.times(b);
        assertEquals(expected.toString(), a.toString());
    }

    @Test
    public void testTimesComplexWithScalar() {
        Complex complex = new Complex(1.0, 2.0);
        double scalar = 3.0;
        Complex expected = new Complex(3.0, 6.0);
        complex.times(scalar);
        assertEquals(expected.toString(), complex.toString());
    }

    @Test
    public void testConjugateValidComplex() {
        Complex complex = new Complex(1.0, 2.0);
        Complex expected = new Complex(1.0, -2.0);
        complex.conjugate();
        assertEquals(expected.toString(), complex.toString());
    }

    @Test
    public void testStaticPlusComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex expected = new Complex(4.0, 6.0);
        Complex result = Complex.plus(a, b);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testStaticMultiplyComplexWithScalar() {
        Complex complex = new Complex(1.0, 2.0);
        double scalar = 3.0;
        Complex expected = new Complex(3.0, 6.0);
        Complex result = Complex.multiply(complex, scalar);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testStaticMultiplyComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex expected = new Complex(-5.0, 11.0);
        Complex result = Complex.multiply(a, b);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testStaticI() {
        Complex expected = new Complex(0.0, 1.0);
        Complex result = Complex.I;
        assertEquals(expected.toString(), result.toString());
    }
}
