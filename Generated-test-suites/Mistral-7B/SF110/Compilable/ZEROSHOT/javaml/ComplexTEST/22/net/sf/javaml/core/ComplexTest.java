package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 private Complex c1;
 private Complex c2;
 private Complex c3;
 private Complex c4;
 private Complex c5;
 private Complex c6;

 @Before
 public void setUp() {
 c1 = new Complex(1.0, 2.0);
 c2 = new Complex(3.0, 4.0);
 c3 = new Complex(1.0, 0.0);
 c4 = new Complex();
 c5 = new Complex(0.0, 1.0);
 c6 = Complex.I;
 }

 @Test
 public void testConstructorWithReAndIm() {
 Complex complex = new Complex(2.0, 3.0);
 assertEquals("2.0 + 3.0i", complex.toString());
 }

 @Test
 public void testConstructorWithoutReAndIm() {
 Complex complex = new Complex();
 assertEquals("0.0 + 0.0i", complex.toString());
 }

 @Test
 public void testAbs() {
 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.001);
 }

 @Test
 public void testPlus() {
 Complex result = new Complex();
 c1.plus(c2);
 result.re = 4.0;
 result.im = 6.0;
 assertEquals(result, c1);
 }

 @Test
 public void testMinus() {
 Complex result = new Complex();
 c1.minus(c2);
 result.re = -2.0;
 result.im = -2.0;
 assertEquals(result, c1);
 }

 @Test
 public void testTimesWithComplex() {
 Complex result = new Complex();
 c1.times(c2);
 result.re = -1.0 * 3.0 * 1.0 + 1.0 * 4.0 * 1.0;
 result.im = 1.0 * 3.0 * 1.0 + 1.0 * 4.0 * 2.0;
 assertEquals(result, c1);
 }

 @Test
 public void testTimesWithDouble() {
 Complex result = new Complex();
 c1.times(2.0);
 result.re = 2.0 * 1.0;
 result.im = 2.0 * 2.0;
 assertEquals(result, c1);
 }

 @Test
 public void testConjugate() {
 Complex result = new Complex();
 c1.conjugate();
 result.re = 1.0;
 result.im = -2.0;
 assertEquals(result, c1);
 }

 @Test
 public void testPlusStatic() {
 Complex result = new Complex();
 Complex sum = Complex.plus(c1, c2);
 result.re = 4.0;
 result.im = 6.0;
 assertEquals(result, sum);
 }

 @Test
 public void testMultiplyStaticWithComplexAndDouble() {
 Complex result = new Complex();
 Complex product = Complex.multiply(c1, 2.0);
 result.re = 2.0 * 1.0;
 result.im = 2.0 * 2.0;
 assertEquals(result, product);
 }

 @Test
 public void testMultiplyStaticWithComplexAndComplex() {
 Complex result = new Complex();
 Complex product = Complex.multiply(c1, c2);
 result.re = -1.0 * 3.0 * 1.0 + 1.0 * 4.0 * 1.0;
 result.im = 1.0 * 3.0 * 1.0 + 1.0 * 4.0 * 2.0;
 assertEquals(result, product);
 }

 @Test
 public void testStaticInitializer() {
 assertEquals("0.0 + 1.0i", c5.toString());
 }
}