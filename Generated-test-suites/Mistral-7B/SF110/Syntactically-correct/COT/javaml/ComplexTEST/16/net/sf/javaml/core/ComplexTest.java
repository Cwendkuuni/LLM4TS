package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 private Complex complex1;
 private Complex complex2;
 private Complex complex3;
 private Complex complexI;

 @Before
 public void setUp() {
 complex1 = new Complex(3.0, 4.0);
 complex2 = new Complex(1.0, 2.0);
 complex3 = new Complex();
 complexI = Complex.I;
 }

 @Test
 public void testComplexConstructor() {
 Complex c = new Complex(5.0, 6.0);
 assertEquals("5.0 + 6.0i", c.toString());
 }

 @Test
 public void testComplexZeroConstructor() {
 Complex c = new Complex();
 assertEquals("0.0 + 0.0i", c.toString());
 }

 @Test
 public void testAbs() {
 assertEquals(5.0, complex1.abs(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbsWithNullComplex() {
 Complex nullComplex = null;
 nullComplex.abs();
 }

 @Test
 public void testPlus() {
 Complex result = new Complex();
 complex1.plus(complex2);
 result.re = 4.0;
 result.im = 6.0;
 assertEquals(result, complex1);
 }

 @Test
 public void testMinus() {
 Complex result = new Complex();
 complex1.minus(complex2);
 result.re = 2.0;
 result.im = -2.0;
 assertEquals(result, complex1);
 }

 @Test
 public void testTimesComplex() {
 Complex result = new Complex();
 complex1.times(complex2);
 result.re = -5.0;
 result.im = 12.0;
 assertEquals(result, complex1);
 }

 @Test
 public void testTimesDouble() {
 Complex result = new Complex();
 complex1.times(3.0);
 result.re = 9.0;
 result.im = 12.0;
 assertEquals(result, complex1);
 }

 @Test
 public void testConjugate() {
 Complex result = new Complex();
 complex1.conjugate();
 result.re = 3.0;
 result.im = -4.0;
 assertEquals(result, complex1);
 }

 @Test
 public void testPlusStatic() {
 Complex result = new Complex();
 Complex sum = Complex.plus(complex1, complex2);
 result.re = 4.0;
 result.im = 6.0;
 assertEquals(result, sum);
 }

 @Test
 public void testTimesStaticWithComplex() {
 Complex result = new Complex();
 Complex product = Complex.multiply(complex1, complex2);
 result.re = -5.0;
 result.im = 12.0;
 assertEquals(result, product);
 }

 @Test
 public void testTimesStaticWithDouble() {
 Complex result = new Complex();
 Complex multipliedComplex = new Complex(3.0, 4.0);
 Complex expectedResult = new Complex(9.0, 12.0);
 Complex product = Complex.multiply(multipliedComplex, 3.0);
 result.re = expectedResult.re;
 result.im = expectedResult.im;
 assertEquals(result, product);
 }

 @Test
 public void testPlusStaticWithNullComplex() {
 Complex a = null;
 Complex b = new Complex(1.0, 1.0);
 Complex sum = Complex.plus(a, b);
 assertNull(sum);
 }

 @Test
 public void testTimesStaticWithNullComplex() {
 Complex a = null;
 Complex b = new Complex(1.0, 1.0);
 Complex product = Complex.multiply(a, b);
 assertNull(product);
 }

 @Test
 public void testTimesStaticWithNullComplexAndDouble() {
 Complex a = null;
 double d = 3.0;
 Complex product = Complex.multiply(a, d);
 assertNull(product);
 }

 @Test
 public void testTimesStaticWithNullComplexAndComplex() {
 Complex a = null;
 Complex b = new Complex(1.0, 1.0);
 Complex product = Complex.multiply(a, b);
 assertNull(product);
 }
}