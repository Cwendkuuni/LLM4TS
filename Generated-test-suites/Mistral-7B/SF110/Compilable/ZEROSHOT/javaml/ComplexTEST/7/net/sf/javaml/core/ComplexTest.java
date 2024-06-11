package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 private Complex createComplex(double re, double im) {
 return new Complex(re, im);
 }

 @Test
 public void testConstructorWithReAndIm() {
 Complex complex = createComplex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", complex.toString());
 }

 @Test
 public void testDefaultConstructor() {
 Complex complex = new Complex();
 assertEquals("0.0 + 0.0i", complex.toString());
 }

 @Test
 public void testAbs() {
 Complex complex = createComplex(3.0, 4.0);
 assertEquals(5.0, complex.abs(), 0.0);
 }

 @Test
 public void testPlus() {
 Complex a = createComplex(1.0, 2.0);
 Complex b = createComplex(3.0, 4.0);
 a.plus(b);
 assertEquals("4.0 + 6.0i", a.toString());
 }

 @Test
 public void testMinus() {
 Complex a = createComplex(1.0, 2.0);
 Complex b = createComplex(3.0, 4.0);
 a.minus(b);
 assertEquals("-2.0 - 2.0i", a.toString());
 }

 @Test
 public void testTimes() {
 Complex a = createComplex(1.0, 2.0);
 Complex b = createComplex(3.0, 4.0);
 a.times(b);
 assertEquals("-5.0 + 10.0i", a.toString());
 }

 @Test
 public void testTimesWithDouble() {
 Complex complex = createComplex(2.0, 3.0);
 complex.times(3.0);
 assertEquals("6.0 + 9.0i", complex.toString());
 }

 @Test
 public void testConjugate() {
 Complex complex = createComplex(1.0, 2.0);
 complex.conjugate();
 assertEquals("1.0 - 2.0i", complex.toString());
 }

 @Test
 public void testStaticPlus() {
 Complex a = createComplex(1.0, 2.0);
 Complex b = createComplex(3.0, 4.0);
 Complex result = Complex.plus(a, b);
 assertEquals("4.0 + 6.0i", result.toString());
 }

 @Test
 public void testStaticMultiplyWithDouble() {
 Complex complex = createComplex(2.0, 3.0);
 Complex result = Complex.multiply(complex, 3.0);
 assertEquals("6.0 + 9.0i", result.toString());
 }

 @Test
 public void testStaticMultiply() {
 Complex a = createComplex(1.0, 2.0);
 Complex b = createComplex(3.0, 4.0);
 Complex result = Complex.multiply(a, b);
 assertEquals("-5.0 + 10.0i", result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStaticMultiplyWithNull() {
 Complex a = createComplex(1.0, 2.0);
 Complex b = null;
 try {
 Complex result = Complex.multiply(a, b);
 } catch (IllegalArgumentException e) {
 // pass
 }
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testStaticI() {
 Complex result = Complex.I;
 assertEquals("0.0 + 1.0i", result.toString());
 }
}