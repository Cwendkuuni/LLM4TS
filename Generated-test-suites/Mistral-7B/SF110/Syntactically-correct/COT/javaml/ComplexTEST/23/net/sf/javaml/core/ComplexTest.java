package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 private Complex c1;
 private Complex c2;
 private Complex c3;
 private Complex c4;
 private Complex I = new Complex(0.0, 1.0);

 @Before
 public void setUp() {
 c1 = new Complex(1.0, 2.0);
 c2 = new Complex(3.0, 4.0);
 c3 = new Complex(-1.0, -2.0);
 c4 = new Complex();
 }

 @Test
 public void testConstructorWithReAndIm() {
 Complex complex = new Complex(1.5, 2.5);
 assertEquals("1.5 + 2.5i", complex.toString());
 }

 @Test
 public void testDefaultConstructor() {
 Complex complex = new Complex();
 assertEquals("0.0 + 0.0i", complex.toString());
 }

 @Test
 public void testAbs() {
 double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
 assertEquals(expected, c1.abs(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbsWithNullComplex() {
 Complex complex = null;
 complex.abs();
 }

 @Test
 public void testPlus() {
 Complex complexSum = new Complex();
 c1.plus(c2);
 complexSum.re = 4.0;
 complexSum.im = 6.0;
 assertEquals(complexSum, c1.plus(c2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPlusWithNullComplexB() {
 c1.plus(null);
 }

 @Test
 public void testMinus() {
 Complex complexDifference = new Complex();
 c1.minus(c2);
 complexDifference.re = -2.0;
 complexDifference.im = -6.0;
 assertEquals(complexDifference, c1.minus(c2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNullComplexB() {
 c1.minus(null);
 }

 @Test
 public void testTimes() {
 Complex complexProduct = new Complex();
 c1.times(c2);
 complexProduct.re = 3.0 * 1.0 - 2.0 * 4.0;
 complexProduct.im = 3.0 * 4.0 + 1.0 * 4.0;
 assertEquals(complexProduct, c1.times(c2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWithNullComplexB() {
 c1.times(null);
 }

 @Test
 public void testTimesWithDouble() {
 Complex complexProduct = new Complex();
 c1.times(2.5);
 complexProduct.re = 1.0 * 2.5;
 complexProduct.im = 2.0 * 2.5;
 assertEquals(complexProduct, c1.times(2.5));
 }

 @Test
 public void testConjugate() {
 Complex complexConjugate = new Complex();
 c1.conjugate();
 complexConjugate.im = -2.0;
 assertEquals(complexConjugate, c1.conjugate());
 }

 @Test
 public void testPlusStatic() {
 Complex complexSum = new Complex();
 Complex complexA = new Complex(1.0, 2.0);
 Complex complexB = new Complex(3.0, 4.0);
 complexSum = Complex.plus(complexA, complexB);
 complexSum.re = 4.0;
 complexSum.im = 6.0;
 assertEquals(complexSum, Complex.plus(complexA, complexB));
 }

 @Test
 public void testMultiplyStaticWithComplexAndDouble() {
 Complex complexProduct = new Complex();
 complexProduct = Complex.multiply(c1, 2.5);
 complexProduct.re = 1.0 * 2.5;
 complexProduct.im = 2.0 * 2.5;
 assertEquals(complexProduct, Complex.multiply(c1, 2.5));
 }

 @Test
 public void testMultiplyStaticWithComplexAndComplex() {
 Complex complexProduct = new Complex();
 complexProduct = Complex.multiply(c1, c2);
 complexProduct.re = 3.0 * 1.0 - 2.0 * 4.0;
 complexProduct.im = 3.0 * 4.0 + 1.0 * 4.0;
 assertEquals(complexProduct, Complex.multiply(c1, c2));
 }

}