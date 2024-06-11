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
 private Complex I;

 @Before
 public void setUp() {
 c1 = new Complex(1.0, 2.0);
 c2 = new Complex(3.0, 4.0);
 c3 = new Complex();
 c4 = new Complex(0.0, -1.0);
 c5 = new Complex(5.0, 5.0);
 I = Complex.I;
 }

 @Test
 public void testConstructor() {
 Complex c = new Complex(2.0, 3.0);
 assertEquals("2.0 + 3.0i", c.toString());
 }

 @Test
 public void testToString() {
 assertEquals("1.0 + 2.0i", c1.toString());
 }

 @Test
 public void testAbs() {
 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), c1.abs(), 0.001);
 assertEquals(0.0, c3.abs());
 assertEquals(Math.sqrt(5.0 * 5.0 + 5.0 * 5.0), c5.abs(), 0.001);
 }

 @Test
 public void testPlus() {
 Complex result = new Complex();
 c1.plus(c2);
 result.re = 4.0;
 result.im = 6.0;
 assertEquals(result, c1.plus(c2));

 result = new Complex(6.0, 3.0);
 c1.plus(I);
 assertEquals(result, c1.plus(I));
 }

 @Test
 public void testMinus() {
 Complex result = new Complex();
 c1.minus(c2);
 result.re = -2.0;
 result.im = -2.0;
 assertEquals(result, c1.minus(c2));

 result = new Complex(1.0, 1.0);
 c1.minus(I);
 assertEquals(result, c1.minus(I));
 }

 @Test
 public void testTimes() {
 Complex result = new Complex(0.0, 0.0);
 c1.times(c2);
 result.re = (1.0 * 3.0 - 2.0 * 4.0);
 result.im = (1.0 * 4.0 + 2.0 * 3.0);
 assertEquals(result, c1.times(c2));

 result = new Complex(15.0, 12.0);
 c1.times(I);
 assertEquals(result, c1.times(I));
 }

 @Test
 public void testTimesDouble() {
 Complex result = new Complex(0.0, 0.0);
 c1.times(2.5);
 result.re = 2.5 * 1.0;
 result.im = 2.5 * 2.0;
 assertEquals(result, c1.times(2.5));
 }

 @Test
 public void testConjugate() {
 Complex result = new Complex();
 result.im = -c1.im;
 assertEquals(result, c1.conjugate());
 }

 @Test
 public void testStaticPlus() {
 Complex result = new Complex(4.0, 6.0);
 assertEquals(result, Complex.plus(c1, c2));
 }

 @Test
 public void testStaticMultiplyDouble() {
 Complex result = new Complex(5.0, 5.0);
 assertEquals(result, Complex.multiply(c5, 1.0));
 }

 @Test
 public void testStaticMultiply() {
 Complex result = new Complex(13.0, 11.0);
 assertEquals(result, Complex.multiply(c1, c2));
 }

 @Test
 public void testStaticI() {
 Complex result = new Complex(0.0, 1.0);
 assertEquals(result, I);
 }
}