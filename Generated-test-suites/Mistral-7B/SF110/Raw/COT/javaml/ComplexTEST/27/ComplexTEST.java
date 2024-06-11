import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import net.sf.javaml.core.Complex;
import org.junit.Test;

public class ComplexTest {

 @Test
 public void testComplexConstructor() {
 Complex complex = new Complex(1.0, 2.0);
 assertEquals(1.0, complex.re);
 assertEquals(2.0, complex.im);
 }

 @Test
 public void testDefaultComplexConstructor() {
 Complex complex = new Complex();
 assertEquals(0.0, complex.re);
 assertEquals(0.0, complex.im);
 }

 @Test
 public void testToString() {
 Complex complex = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", complex.toString());
 }

 @Test
 public void testAbs() {
 Complex complex = new Complex(1.0, 2.0);
 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), complex.abs());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedOperationPlus() {
 Complex complex = new Complex();
 complex.plus(null);
 }

 @Test
 public void testPlus() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = new Complex(3.0, 4.0);
 Complex result = new Complex(4.0, 6.0);
 complex1.plus(complex2);
 assertEquals(result.re, complex1.re);
 assertEquals(result.im, complex1.im);
 }

 @Test
 public void testMinus() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = new Complex(3.0, 4.0);
 Complex result = new Complex(-2.0, -2.0);
 complex1.minus(complex2);
 assertEquals(result.re, complex1.re);
 assertEquals(result.im, complex1.im);
 }

 @Test
 public void testTimesWithComplex() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = new Complex(3.0, 4.0);
 Complex result = new Complex(-2.0, 10.0);
 complex1.times(complex2);
 assertEquals(result.re, complex1.re);
 assertEquals(result.im, complex1.im);
 }

 @Test
 public void testTimesWithDouble() {
 Complex complex = new Complex(1.0, 2.0);
 complex.times(2.0);
 Complex expected = new Complex(2.0, 4.0);
 assertEquals(expected.re, complex.re);
 assertEquals(expected.im, complex.im);
 }

 @Test
 public void testConjugate() {
 Complex complex = new Complex(1.0, 2.0);
 complex.conjugate();
 Complex expected = new Complex(1.0, -2.0);
 assertEquals(expected.re, complex.re);
 assertEquals(expected.im, complex.im);
 }

 @Test
 public void testPlusStatic() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = new Complex(3.0, 4.0);
 Complex result = Complex.plus(complex1, complex2);
 Complex expected = new Complex(4.0, 6.0);
 assertEquals(expected.re, result.re);
 assertEquals(expected.im, result.im);
 }

 @Test
 public void testMultiplyWithDoubleStatic() {
 Complex complex = new Complex(1.0, 2.0);
 Complex result = Complex.multiply(complex, 2.0);
 Complex expected = new Complex(2.0, 4.0);
 assertEquals(expected.re, result.re);
 assertEquals(expected.im, result.im);
 }

 @Test
 public void testMultiplyWithComplexStatic() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = new Complex(3.0, 4.0);
 Complex result = Complex.multiply(complex1, complex2);
 Complex expected = new Complex(-2.0, 10.0);
 assertEquals(expected.re, result.re);
 assertEquals(expected.im, result.im);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithNullComplex() {
 Complex complex = new Complex(1.0, 2.0);
 Complex result = Complex.multiply(complex, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPlusWithNullComplex() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex result = complex1.plus(null);
 }
}