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

 @Before
 public void setUp() {
 c1 = new Complex(1.0, 2.0);
 c2 = new Complex(3.0, 4.0);
 c3 = new Complex(0.0, 5.0);
 c4 = new Complex(-1.0, 0.0);
 }

 @Test
 public void testConstructor() {
 Complex c5 = new Complex(5.0, 6.0);
 assertEquals(5.0, c5.getRe(), 0.01);
 assertEquals(6.0, c5.getIm(), 0.01);
 }

 @Test
 public void testDefaultConstructor() {
 Complex c6 = new Complex();
 assertEquals(0.0, c6.getRe(), 0.01);
 assertEquals(0.0, c6.getIm(), 0.01);
 }

 @Test
 public void testToString() {
 assertEquals("1.0 + 2.0i", c1.toString());
 }

 @Test
 public void testAbs() {
 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.01);
 }

 @Test
 public void testPlus() {
 c1.plus(c2);
 assertEquals(4.0, c1.getRe(), 0.01);
 assertEquals(6.0, c1.getIm(), 0.01);
 }

 @Test
 public void testMinus() {
 c1.minus(c2);
 assertEquals(-2.0, c1.getRe(), 0.01);
 assertEquals(-2.0, c1.getIm(), 0.01);
 }

 @Test
 public void testTimes() {
 c1.times(c2);
 Complex result = new Complex(-5.0, 2.0);
 assertEquals(result.getRe(), c1.getRe(), 0.01);
 assertEquals(result.getIm(), c1.getIm(), 0.01);
 }

 @Test
 public void testTimesDouble() {
 c1.times(2.0);
 assertEquals(2.0 * 1.0, c1.getRe(), 0.01);
 assertEquals(2.0 * 2.0, c1.getIm(), 0.01);
 }

 @Test
 public void testConjugate() {
 c1.conjugate();
 Complex result = new Complex(1.0, -2.0);
 assertNotSame(c1, result);
 assertEquals(result.getRe(), c1.getRe(), 0.01);
 assertEquals(result.getIm(), -c1.getIm(), 0.01);
 }

 @Test
 public void testPlusStatic() {
 Complex sum = Complex.plus(c1, c2);
 assertEquals(4.0, sum.getRe(), 0.01);
 assertEquals(6.0, sum.getIm(), 0.01);
 }

 @Test
 public void testMultiplyStaticWithDouble() {
 Complex product = Complex.multiply(c1, 2.0);
 assertEquals(2.0 * 1.0, product.getRe(), 0.01);
 assertEquals(2.0 * 2.0, product.getIm(), 0.01);
 }

 @Test
 public void testMultiplyStatic() {
 Complex product = Complex.multiply(c1, c2);
 assertEquals(-5.0, product.getRe(), 0.01);
 assertEquals(2.0, product.getIm(), 0.01);
 }

 private double getRe(Complex complex) {
 return complex.getRe();
 }

 private double getIm(Complex complex) {
 return complex.getIm();
 }
}