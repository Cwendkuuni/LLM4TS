package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 private Complex c1;
 private Complex c2;
 private Complex c3;
 private Complex c4;
 private Complex zero = new Complex();

 @Test
 public void testComplexConstructorWithDoubleReAndIm() {
 c1 = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", c1.toString());
 }

 @Test
 public void testComplexConstructorWithDefaultValues() {
 c2 = new Complex();
 assertEquals("0.0 + 0.0i", c2.toString());
 }

 @Test
 public void testAbsMethod() {
 c3 = new Complex(3.0, 4.0);
 assertEquals(5.0, c3.abs(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbsMethodWithNullComplex() {
 Complex nullComplex = null;
 nullComplex.abs();
 }

 @Test
 public void testPlusMethod() {
 c1 = new Complex(1.0, 2.0);
 c2 = new Complex(3.0, 4.0);
 c1.plus(c2);
 assertEquals("4.0 + 6.0i", c1.toString());
 }

 @Test
 public void testMinusMethod() {
 c1 = new Complex(1.0, 2.0);
 c2 = new Complex(3.0, 4.0);
 c1.minus(c2);
 assertEquals("-2.0 - 2.0i", c1.toString());
 }

 @Test
 public void testTimesMethodWithComplex() {
 c1 = new Complex(1.0, 2.0);
 c2 = new Complex(3.0, 4.0);
 c1.times(c2);
 assertEquals("3.0 + 10.0i", c1.toString());
 }

 @Test
 public void testTimesMethodWithDouble() {
 c1 = new Complex(1.0, 2.0);
 c1.times(2.0);
 assertEquals("2.0 + 4.0i", c1.toString());
 }

 @Test
 public void testConjugateMethod() {
 c1 = new Complex(1.0, 2.0);
 c1.conjugate();
 assertEquals("1.0 - 2.0i", c1.toString());
 }

 @Test
 public void testPlusStaticMethod() {
 Complex c5 = new Complex(1.0, 2.0);
 Complex c6 = new Complex(3.0, 4.0);
 Complex c7 = Complex.plus(c5, c6);
 assertEquals("4.0 + 6.0i", c7.toString());
 }

 @Test
 public void testTimesStaticMethodWithComplexAndDouble() {
 Complex c8 = new Complex(1.0, 2.0);
 Complex c9 = new Complex(3.0, 4.0);
 Complex c10 = Complex.multiply(c8, 2.0);
 assertEquals("2.0 + 4.0i", c10.toString());
 }

 @Test
 public void testTimesStaticMethodWithComplexAndComplex() {
 Complex c11 = new Complex(1.0, 2.0);
 Complex c12 = new Complex(3.0, 4.0);
 Complex c13 = Complex.multiply(c11, c12);
 assertEquals("-2.0 + 12.0i", c13.toString());
 }

 @Test
 public void testStaticInitializer() {
 Complex I = Complex.I;
 assertEquals("0.0 + 1.0i", I.toString());
 }

}