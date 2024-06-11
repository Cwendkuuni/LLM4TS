package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ComplexTest {

 private Complex complex1;
 private Complex complex2;
 private Complex complex3;
 private Complex complex4;
 private Complex complexI;

 @Test
 public void testComplexConstructorWithReAndIm() {
 complex1 = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", complex1.toString());
 }

 @Test
 public void testComplexConstructorWithoutReAndIm() {
 complex1 = new Complex();
 assertEquals("0.0 + 0.0i", complex1.toString());
 }

 @Test
 public void testAbsMethod() {
 complex1 = new Complex(3.0, 4.0);
 assertEquals(5.0, complex1.abs(), 0.001);
 }

 @Test
 public void testPlusMethod() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex1.plus(complex2);
 assertEquals("4.0 + 6.0i", complex1.toString());
 }

 @Test
 public void testMinusMethod() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex1.minus(complex2);
 assertEquals("-2.0 - 2.0i", complex1.toString());
 }

 @Test
 public void testTimesMethodWithComplex() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex1.times(complex2);
 assertEquals("-5.0 + 10.0i", complex1.toString());
 }

 @Test
 public void testTimesMethodWithDouble() {
 complex1 = new Complex(1.0, 2.0);
 complex1.times(2.0);
 assertEquals("2.0 + 4.0i", complex1.toString());
 }

 @Test
 public void testConjugateMethod() {
 complex1 = new Complex(1.0, 2.0);
 complex1.conjugate();
 assertEquals("1.0 - 2.0i", complex1.toString());
 }

 @Test
 public void testPlusStaticMethod() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = Complex.plus(c1, c2);
 assertEquals("4.0 + 6.0i", sum.toString());
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndDouble() {
 Complex c1 = new Complex(1.0, 2.0);
 double alpha = 3.0;
 Complex prod = Complex.multiply(c1, alpha);
 assertEquals("3.0 + 6.0i", prod.toString());
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndComplex() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex prod = Complex.multiply(c1, c2);
 assertEquals("-5.0 + 10.0i", prod.toString());
 }

 @Test
 public void testIStaticVariable() {
 complexI = Complex.I;
 assertEquals("0.0 + 1.0i", complexI.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesMethodWithNullComplex() {
 complex1 = new Complex(1.0, 2.0);
 Complex nullComplex = null;
 complex1.times(nullComplex);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesMethodWithNullDouble() {
 complex1 = new Complex(1.0, 2.0);
 double nullDouble = null;
 complex1.times(nullDouble);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPlusMethodWithNullComplex() {
 complex1 = new Complex(1.0, 2.0);
 Complex nullComplex = null;
 complex1.plus(nullComplex);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusMethodWithNullComplex() {
 complex1 = new Complex(1.0, 2.0);
 Complex nullComplex = null;
 complex1.minus(nullComplex);
 }

 @Test(expected = ArithmeticException.class)
 public void testAbsMethodWithNaN() {
 complex1 = new Complex(Double.NaN, 0.0);
 Assert.assertEquals(Double.NaN, complex1.abs(), 0.0);
 }

 @Test(expected = ArithmeticException.class)
 public void testAbsMethodWithInfinite() {
 complex1 = new Complex(Double.POSITIVE_INFINITY, 0.0);
 Assert.assertEquals(Double.POSITIVE_INFINITY, complex1.abs(), 0.0);
 }

 @Test(expected = ArithmeticException.class)
 public void testAbsMethodWithNegativeInfinite() {
 complex1 = new Complex(Double.NEGATIVE_INFINITY, 0.0);
 Assert.assertEquals(Double.NEGATIVE_INFINITY, complex1.abs(), 0.0);
 }

 @Test(expected = ArithmeticException.class)
 public void testTimesMethodWithZeroComplex() {
 complex1 = new Complex(1.0, 0.0);
 complex2 = new Complex(0.0, 0.0);
 complex1.times(complex2);
 }

 @Test(expected = ArithmeticException.class)
 public void testTimesMethodWithZeroDouble() {
 complex1 = new Complex(1.0, 0.0);
 complex1.times(0.0);
 }

 @Test
 public void testEquality() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(1.0, 2.0);
 complex3 = new Complex(1.0, 2.0);
 assertEquals(complex1, complex2);
 assertNotSame(complex1, complex3);
 }
}