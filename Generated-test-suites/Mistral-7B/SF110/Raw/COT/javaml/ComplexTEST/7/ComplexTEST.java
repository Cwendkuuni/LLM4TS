package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ComplexTest {

 private Complex c1, c2, c3, c4;

 @before
 public void setUp() {
 c1 = new Complex(1.0, 2.0);
 c2 = new Complex(3.0, 4.0);
 c3 = new Complex(0.0, 5.0);
 c4 = new Complex(-1.0, 0.0);
 }

 @Test
 public void testConstructorWithReAndIm() {
 Complex complex = new Complex(2.0, 3.0);
 assertEquals(2.0, complex.getRe(), 0.0);
 assertEquals(3.0, complex.getIm(), 0.0);
 }

 @Test
 public void testDefaultConstructor() {
 Complex complex = new Complex();
 assertEquals(0.0, complex.getRe(), 0.0);
 assertEquals(0.0, complex.getIm(), 0.0);
 }

 @Test
 public void testToString() {
 Complex complex = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", complex.toString());
 }

 @Test
 public void testAbs() {
 Complex complex = new Complex(3.0, 4.0);
 assertEquals(5.0, complex.abs(), 0.0);
 }

 @Test
 public void testPlusMethod() {
 Complex result = new Complex();
 c1.plus(c2);
 result = c1;
 Complex expected = new Complex(4.0, 6.0);
 assertEquals(expected.getRe(), result.getRe(), 0.0);
 assertEquals(expected.getIm(), result.getIm(), 0.0);
 }

 @Test
 public void testMinusMethod() {
 Complex result = new Complex();
 c1.minus(c2);
 result = c1;
 Complex expected = new Complex(-2.0, -2.0);
 assertEquals(expected.getRe(), result.getRe(), 0.0);
 assertEquals(expected.getIm(), result.getIm(), 0.0);
 }

 @Test
 public void testTimesMethodWithComplex() {
 Complex result = new Complex();
 c1.times(c2);
 result = c1;
 Complex expected = new Complex(-5.0, 10.0);
 assertEquals(expected.getRe(), result.getRe(), 0.0);
 assertEquals(expected.getIm(), result.getIm(), 0.0);
 }

 @Test
 public void testTimesMethodWithDouble() {
 Complex result = new Complex();
 c1.times(3.0);
 result = c1;
 Complex expected = new Complex(3.0, 6.0);
 assertEquals(expected.getRe(), result.getRe(), 0.0);
 assertEquals(expected.getIm(), result.getIm(), 0.0);
 }

 @Test
 public void testConjugateMethod() {
 Complex result = new Complex();
 c1.conjugate();
 result = c1;
 Complex expected = new Complex(1.0, -2.0);
 assertEquals(expected.getRe(), result.getRe(), 0.0);
 assertEquals(expected.getIm(), result.getIm(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesMethodWithNullComplex() {
 c1.times(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesMethodWithNullDouble() {
 c1.times(null);
 }

 @Test
 public void testPlusStaticMethod() {
 Complex result = new Complex();
 Complex sum = Complex.plus(c1, c2);
 result = sum;
 Complex expected = new Complex(4.0, 6.0);
 assertEquals(expected.getRe(), result.getRe(), 0.0);
 assertEquals(expected.getIm(), result.getIm(), 0.0);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndDouble() {
 Complex result = new Complex();
 Complex product = Complex.multiply(c1, 3.0);
 result = product;
 Complex expected = new Complex(3.0, 6.0);
 assertEquals(expected.getRe(), result.getRe(), 0.0);
 assertEquals(expected.getIm(), result.getIm(), 0.0);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndComplex() {
 Complex result = new Complex();
 Complex product = Complex.multiply(c1, c2);
 result = product;
 Complex expected = new Complex(-5.0, 10.0);
 assertEquals(expected.getRe(), result.getRe(), 0.0);
 assertEquals(expected.getIm(), result.getIm(), 0.0);
 }

 @Test
 public void testStaticConstants() {
 Complex i = Complex.I;
 Complex zero = new Complex(0.0, 0.0);
 assertNotEquals(i, zero);
 assertEquals(0.0, i.getIm(), 0.0);
 assertEquals(1.0, i.getRe(), 0.0);
 }

}