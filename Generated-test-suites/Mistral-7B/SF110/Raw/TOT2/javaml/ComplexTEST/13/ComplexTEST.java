import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ComplexTest {

    @Test
    public void testComplexConstructor() {
        Complex c1 = new Complex(1.0, 2.0);
        Complex c2 = new Complex();
        Complex c3 = new Complex(3.0, 4.0);
        Complex c4 = new Complex(0.0, 0.0);

        assertEquals(1.0, c1.getRe(), 0.01);
        assertEquals(2.0, c1.getIm(), 0.01);
        assertEquals(0.0, c2.getRe(), 0.01);
        assertEquals(0.0, c2.getIm(), 0.01);
        assertEquals(3.0, c3.getRe(), 0.01);
        assertEquals(4.0, c3.getIm(), 0.01);
        assertEquals(0.0, c4.getRe(), 0.01);
        assertEquals(0.0, c4.getIm(), 0.01);
    }

    @Test
    public void testToString() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals("1.0 + 2.0i", c.toString());
    }

    @Test
    public void testAbs() {
        Complex c = new Complex(1.0, 2.0);
        assertEquals(Math.sqrt(5.0), c.abs(), 0.01);
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex(4.0, 6.0);
        a.plus(b);
        assertEquals(c.getRe(), a.getRe(), 0.01);
        assertEquals(c.getIm(), a.getIm(), 0.01);
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex(-2.0, -2.0);
        a.minus(b);
        assertEquals(c.getRe(), a.getRe(), 0.01);
        assertEquals(c.getIm(), a.getIm(), 0.01);
    }

    @Test
    public void testTimesComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex(1.0, 6.0);
        a.times(b);
        assertEquals(c.getRe(), a.getRe(), 0.01);
        assertEquals(c.getIm(), a.getIm(), 0.01);
    }

    @Test
    public void testTimesDouble() {
        Complex c = new Complex(1.0, 2.0);
        c.times(3.0);
        Complex expected = new Complex(3.0, 6.0);
        assertEquals(expected.getRe(), c.getRe(), 0.01);
        assertEquals(expected.getIm(), c.getIm(), 0.01);
    }

    @Test
    public void testConjugate() {
        Complex c = new Complex(1.0, 2.0);
        Complex expected = new Complex(1.0, -2.0);
        c.conjugate();
        assertEquals(expected.getRe(), c.getRe(), 0.01);
        assertEquals(expected.getIm(), c.getIm(), 0.01);
    }

    @Test
    public void testStaticPlus() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);
        Complex c = new Complex(4.0, 6.0);
        Complex d = Complex.plus(a, b);
        assertEquals(c.getRe(), d.getRe(), 0.01);
        assertEquals(c.getIm(), d.getIm(), 0.01);
    }

    @Test
    public void testStaticMultiplyDoubleComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(1.0, 1.0);
        Complex c = new Complex(1.0, 3.0);
        Complex d = Complex.multiply(a, b);
        assertEquals(c.getRe(), d.getRe(), 0.01);
        assertEquals(c.getIm(), d.getIm(), 0.01);
    }

    @Test
    public void testStaticMultiplyComplexComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(1.0, 1.0);
        Complex c = new Complex(2.0, 3.0);
        Complex d = Complex.multiply(a, b);
        Complex expected = new Complex(2.0, 3.0);
        assertEquals(expected.getRe(), d.getRe(), 0.01);
        assertEquals(expected.getIm(), d.getIm(), 0.01);
    }
}