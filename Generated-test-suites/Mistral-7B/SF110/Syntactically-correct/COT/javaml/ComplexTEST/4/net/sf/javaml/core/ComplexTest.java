package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex c3 = new Complex();
 Complex I = Complex.I;

 @Test
 public void testComplexConstructor() {
 Complex obj = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", obj.toString());
 }

 @Test
 public void testComplexNoArgumentConstructor() {
 Complex obj = new Complex();
 assertEquals("0.0 + 0.0i", obj.toString());
 }

 @Test
 public void testAbsMethod() {
 Complex obj = new Complex(1.0, 2.0);
 double result = obj.abs();
 double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
 assertEquals(expected, result);
 }

 @Test
 public void testPlusMethod() {
 Complex result = c1.plus(c2);
 Complex expected = new Complex(4.0, 6.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testMinusMethod() {
 Complex result = c1.minus(c2);
 Complex expected = new Complex(-2.0, -2.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testTimesMethodWithComplex() {
 Complex result = c1.times(c2);
 Complex expected = new Complex(-5.0, 2.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testTimesMethodWithDouble() {
 Complex result = c1.times(2.0);
 Complex expected = new Complex(2.0, 4.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testConjugateMethod() {
 Complex result = c1.conjugate();
 Complex expected = new Complex(1.0, -2.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticPlusMethod() {
 Complex result = Complex.plus(c1, c2);
 Complex expected = new Complex(4.0, 6.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticMultiplyMethodWithComplexDouble() {
 Complex result = Complex.multiply(c1, 2.0);
 Complex expected = new Complex(2.0, 4.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testStaticMultiplyMethodWithComplexComplex() {
 Complex result = Complex.multiply(c1, c2);
 Complex expected = new Complex(-5.0, 2.0);
 assertEquals(expected.toString(), result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPlusMethodWithNullComplex() {
 Complex result = c1.plus(null);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesMethodWithNullComplex() {
 Complex result = c1.times(null);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesMethodWithNaN() {
 Complex result = new Complex(0.0, Double.NaN);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesMethodWithInfiniteNumber() {
 Complex result = new Complex(0.0, Double.POSITIVE_INFINITY);
 fail("Should throw IllegalArgumentException");
 }

}