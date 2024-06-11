package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 Complex obj1;
 Complex obj2;
 Complex obj3;
 Complex I = new Complex(0.0, 1.0);

 @Test
 public void testConstructorWithDoubleReDoubleIm() {
 obj1 = new Complex(1.0, 2.0);
 assertEquals(1.0, obj1.getRe(), 0.01);
 assertEquals(2.0, obj1.getIm(), 0.01);
 }

 @Test
 public void testConstructorWithNoArguments() {
 obj1 = new Complex();
 assertEquals(0.0, obj1.getRe(), 0.01);
 assertEquals(0.0, obj1.getIm(), 0.01);
 }

 @Test
 public void testToString() {
 obj1 = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", obj1.toString());
 }

 @Test
 public void testAbs() {
 obj1 = new Complex(1.0, 2.0);
 assertEquals(Math.sqrt(5.0), obj1.abs(), 0.01);
 }

 @Test(expected = IllegalStateException.class)
 public void testPlusWithNullComplex() {
 obj1.plus(null);
 }

 @Test
 public void testPlus() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj3 = new Complex();
 obj1.plus(obj2);
 assertEquals(4.0, obj3.getRe(), 0.01);
 assertEquals(6.0, obj3.getIm(), 0.01);
 }

 @Test(expected = IllegalStateException.class)
 public void testMinusWithNullComplex() {
 obj1.minus(null);
 }

 @Test
 public void testMinus() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj3 = new Complex();
 obj1.minus(obj2);
 assertEquals(-2.0, obj3.getRe(), 0.01);
 assertEquals(-6.0, obj3.getIm(), 0.01);
 }

 @Test(expected = IllegalStateException.class)
 public void testTimesWithNullComplex() {
 obj1.times(null);
 }

 @Test
 public void testTimesWithComplex() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj3 = new Complex();
 obj1.times(obj2);
 assertEquals(3.0 * 1.0 - 4.0 * 2.0, obj3.getRe(), 0.01);
 assertEquals(3.0 * 2.0 + 4.0 * 1.0, obj3.getIm(), 0.01);
 }

 @Test
 public void testTimesWithDouble() {
 obj1 = new Complex(1.0, 2.0);
 obj3 = new Complex();
 obj1.times(2.0);
 assertEquals(2.0 * 1.0, obj3.getRe(), 0.01);
 assertEquals(2.0 * 2.0, obj3.getIm(), 0.01);
 }

 @Test
 public void testConjugate() {
 obj1 = new Complex(1.0, 2.0);
 obj1.conjugate();
 assertEquals(1.0, obj1.getRe(), 0.01);
 assertEquals(-2.0, obj1.getIm(), 0.01);
 }

 @Test
 public void testPlusStatic() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex sum = Complex.plus(c1, c2);
 assertEquals(4.0, sum.getRe(), 0.01);
 assertEquals(6.0, sum.getIm(), 0.01);
 }

 @Test
 public void testMultiplyStaticWithComplexAndDouble() {
 Complex c1 = new Complex(1.0, 2.0);
 double d = 3.0;
 Complex product = Complex.multiply(c1, d);
 assertEquals(3.0 * 1.0, product.getRe(), 0.01);
 assertEquals(3.0 * 2.0, product.getIm(), 0.01);
 }

 @Test
 public void testMultiplyStaticWithComplexAndComplex() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex product = Complex.multiply(c1, c2);
 assertEquals(3.0 * 1.0 - 4.0 * 2.0, product.getRe(), 0.01);
 assertEquals(3.0 * 2.0 + 4.0 * 1.0, product.getIm(), 0.01);
 }

 @Test
 public void testStaticI() {
 Complex c1 = Complex.I;
 assertEquals(0.0, c1.getRe(), 0.01);
 assertEquals(1.0, c1.getIm(), 0.01);
 }
}