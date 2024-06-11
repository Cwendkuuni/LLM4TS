package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ComplexTest {

 Complex obj1, obj2, obj3, obj4, obj5;
 double eps = 0.00001;

 @Before
 public void setUp() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj3 = new Complex(0.0, 5.0);
 obj4 = new Complex(-1.0, 0.0);
 obj5 = Complex.I;
 }

 @Test
 public void testComplexConstructor() {
 Complex c = new Complex(1.0, 2.0);
 assertEquals(1.0, c.getRe(), eps);
 assertEquals(2.0, c.getIm(), eps);
 }

 @Test
 public void testComplexNoArgumentConstructor() {
 Complex c = new Complex();
 assertEquals(0.0, c.getRe(), eps);
 assertEquals(0.0, c.getIm(), eps);
 }

 @Test
 public void testToString() {
 assertEquals("1.0 + 2.0i", obj1.toString());
 }

 @Test
 public void testAbs() {
 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), obj1.abs(), eps);
 }

 @Test(expected = IllegalStateException.class)
 public void testPlusWithNullComplex() {
 obj1.plus(null);
 }

 @Test
 public void testPlus() {
 Complex sum = new Complex();
 obj1.plus(obj2);
 sum.re = obj1.re + obj2.re;
 sum.im = obj1.im + obj2.im;
 assertNotSame(obj1, obj1.plus(obj2));
 assertEquals(sum.re, obj1.getRe(), eps);
 assertEquals(sum.im, obj1.getIm(), eps);
 }

 @Test
 public void testMinus() {
 Complex diff = new Complex();
 obj1.minus(obj2);
 diff.re = obj1.re - obj2.re;
 diff.im = obj1.im - obj2.im;
 assertNotSame(obj1, obj1.minus(obj2));
 assertEquals(diff.re, obj1.getRe(), eps);
 assertEquals(diff.im, obj1.getIm(), eps);
 }

 @Test
 public void testTimesWithComplex() {
 Complex prod = new Complex();
 obj1.times(obj2);
 prod.re = obj1.re * obj2.re - obj1.im * obj2.im;
 prod.im = obj1.re * obj2.im + obj1.im * obj2.re;
 assertNotSame(obj1, obj1.times(obj2));
 assertEquals(prod.re, obj1.getRe(), eps);
 assertEquals(prod.im, obj1.getIm(), eps);
 }

 @Test
 public void testTimesWithDouble() {
 Complex prod = new Complex();
 obj1.times(2.0);
 prod.re = obj1.re * 2.0;
 prod.im = obj1.im * 2.0;
 assertNotSame(obj1, obj1.times(2.0));
 assertEquals(prod.re, obj1.getRe(), eps);
 assertEquals(prod.im, obj1.getIm(), eps);
 }

 @Test
 public void testConjugate() {
 Complex conj = new Complex();
 obj1.conjugate();
 conj.im = -obj1.im;
 assertNotSame(obj1, obj1.conjugate());
 assertEquals(conj.re, obj1.getRe(), eps);
 assertEquals(conj.im, -obj1.getIm(), eps);
 }

 @Test
 public void testPlusStatic() {
 Complex sum = new Complex();
 Complex res = Complex.plus(obj1, obj2);
 sum.re = obj1.re + obj2.re;
 sum.im = obj1.im + obj2.im;
 assertNotSame(obj1, res);
 assertEquals(sum.re, res.getRe(), eps);
 assertEquals(sum.im, res.getIm(), eps);
 }

 @Test
 public void testMultiplyStaticWithComplexAndDouble() {
 Complex prod = new Complex();
 Complex res = Complex.multiply(obj1, obj2.re);
 prod.re = obj1.re * obj2.re;
 prod.im = obj1.im * obj2.re;
 assertNotSame(obj1, res);
 assertEquals(prod.re, res.getRe(), eps);
 assertEquals(prod.im, res.getIm(), eps);
 }

 @Test
 public void testMultiplyStaticWithComplexAndComplex() {
 Complex prod = new Complex();
 Complex res = Complex.multiply(obj1, obj2);
 prod.re = obj1.re * obj2.re - obj1.im * obj2.im;
 prod.im = obj1.re * obj2.im + obj1.im * obj2.re;
 assertNotSame(obj1, res);
 assertEquals(prod.re, res.getRe(), eps);
 assertEquals(prod.im, res.getIm(), eps);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyStaticWithInvalidArguments() {
 Complex.multiply(obj1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyStaticWithComplexAndNonComplex() {
 Complex.multiply(obj1, "String");
 }
}