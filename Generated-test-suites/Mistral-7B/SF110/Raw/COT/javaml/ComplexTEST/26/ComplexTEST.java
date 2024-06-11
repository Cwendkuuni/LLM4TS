package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ComplexTest {

 Complex obj1, obj2, obj3, obj4;
 final double EPS = 0.00001;

 @Before
 public void setUp() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj3 = new Complex(0.0, 5.0);
 obj4 = new Complex();
 }

 @Test
 public void testComplexConstructor() {
 Complex c1 = new Complex(2.0, 3.0);
 assertEquals(2.0, c1.re, EPS);
 assertEquals(3.0, c1.im, EPS);
 }

 @Test
 public void testComplexDefaultConstructor() {
 Complex c1 = new Complex();
 assertEquals(0.0, c1.re, EPS);
 assertEquals(0.0, c1.im, EPS);
 }

 @Test
 public void testToString() {
 assertEquals("1.0 + 2.0i", obj1.toString());
 }

 @Test
 public void testAbs() {
 double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
 assertEquals(expected, obj1.abs(), EPS);
 }

 @Test(expected = IllegalStateException.class)
 public void testAbsWhenInstanceIsNull() {
 Complex nullComplex = null;
 nullComplex.abs();
 }

 @Test
 public void testPlusMethod() {
 Complex expected = new Complex(4.0, 6.0);
 obj1.plus(obj2);
 assertEquals(expected.re, obj1.re, EPS);
 assertEquals(expected.im, obj1.im, EPS);
 }

 @Test
 public void testMinusMethod() {
 Complex expected = new Complex(-1.0, -2.0);
 obj1.minus(obj2);
 assertEquals(expected.re, obj1.re, EPS);
 assertEquals(expected.im, obj1.im, EPS);
 }

 @Test
 public void testTimesMethodWithComplex() {
 Complex expected = new Complex(-1.0, 6.0);
 obj1.times(obj2);
 assertEquals(expected.re, obj1.re, EPS);
 assertEquals(expected.im, obj1.im, EPS);
 }

 @Test
 public void testTimesMethodWithDouble() {
 Complex expected = new Complex(3.0, 2.0);
 obj1.times(3.0);
 assertEquals(expected.re, obj1.re, EPS);
 assertEquals(expected.im, obj1.im, EPS);
 }

 @Test
 public void testConjugateMethod() {
 Complex expected = new Complex(1.0, -2.0);
 obj1.conjugate();
 assertNotSame(obj1, expected);
 assertEquals(expected.re, obj1.re, EPS);
 assertEquals(expected.im, obj1.im, EPS);
 }

 @Test
 public void testPlusStaticMethod() {
 Complex expected = new Complex(4.0, 6.0);
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex result = Complex.plus(c1, c2);
 assertEquals(expected.re, result.re, EPS);
 assertEquals(expected.im, result.im, EPS);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndDouble() {
 Complex expected = new Complex(3.0, 2.0);
 Complex c1 = new Complex(1.0, 2.0);
 double d = 3.0;
 Complex result = Complex.multiply(c1, d);
 assertEquals(expected.re, result.re, EPS);
 assertEquals(expected.im, result.im, EPS);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndComplex() {
 Complex expected = new Complex(-1.0, 6.0);
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex result = Complex.multiply(c1, c2);
 assertEquals(expected.re, result.re, EPS);
 assertEquals(expected.im, result.im, EPS);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyStaticMethodWithInvalidArguments() {
 Complex result = Complex.multiply(null, obj2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyStaticMethodWithInvalidArguments2() {
 Complex result = Complex.multiply(obj1, null);
 }
}