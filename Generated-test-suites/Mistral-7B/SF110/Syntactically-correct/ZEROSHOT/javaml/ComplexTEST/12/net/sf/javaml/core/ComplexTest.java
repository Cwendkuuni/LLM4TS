package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ComplexTest {

 private Complex c1;
 private Complex c2;
 private Complex c3;
 private Complex c4;
 private Complex c5;

 @Before
 public void setUp() {
 c1 = new Complex(1.0, 2.0);
 c2 = new Complex(3.0, 4.0);
 c3 = new Complex(0.0, 5.0);
 c4 = new Complex(-1.0, 0.0);
 c5 = Complex.I;
 }

 @Test
 public void testConstructor() {
 Complex c6 = new Complex(1.0, 1.0);
 assertEquals("1.0 + 1.0i", c6.toString());
 }

 @Test
 public void testAbs() {
 double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
 assertEquals(expected, c1.abs(), 0.001);
 }

 @Test
 public void testConjugate() {
 Complex c7 = c1.conjugate();
 assertEquals("1.0 - 2.0i", c7.toString());
 }

 @Test
 public void testTimesImaginaryComplex() {
 Complex c8 = new Complex(1.0, 2.0);
 c8.times(c2);
 double expectedReal = 1.0 * 3.0 - 2.0 * 4.0;
 double expectedImag = 1.0 * 4.0 + 2.0 * 3.0;
 assertEquals(expectedReal, c8.re, 0.001);
 assertEquals(expectedImag, c8.im, 0.001);
 }

 @Test
 public void testTimesRealNumber() {
 c3.times(2.0);
 double expected = 2.0 * 5.0;
 assertEquals(expected, c3.re, 0.001);
 }

 @Test
 public void testPlusComplexNumbers() {
 Complex c9 = Complex.plus(c1, c2);
 double expectedReal = 1.0 + 3.0;
 double expectedImag = 2.0 + 4.0;
 assertEquals(expectedReal, c9.re, 0.001);
 assertEquals(expectedImag, c9.im, 0.001);
 }

 @Test
 public void testMinusComplexNumbers() {
 Complex c10 = Complex.minus(c1, c2);
 double expectedReal = 1.0 - 3.0;
 double expectedImag = 2.0 - 4.0;
 assertEquals(expectedReal, c10.re, 0.001);
 assertEquals(expectedImag, c10.im, 0.001);
 }

 @Test
 public void testTimesComplexNumbers() {
 Complex c11 = Complex.multiply(c1, c2);
 double expectedReal = 1.0 * 3.0 - 2.0 * 4.0;
 double expectedImag = 1.0 * 4.0 + 2.0 * 3.0;
 assertEquals(expectedReal, c11.re, 0.001);
 assertEquals(expectedImag, c11.im, 0.001);
 }

 @Test
 public void testIdentity() {
 Complex c12 = Complex.I;
 assertEquals("0 + 1.0i", c12.toString());
 }

 @Test
 public void testEquals() {
 Complex c13 = new Complex(1.0, 2.0);
 Complex c14 = new Complex(1.0, 2.0);
 assertEquals(c13, c14);
 Complex c15 = new Complex(1.0, 3.0);
 assertNotSame(c13, c15);
 }
}