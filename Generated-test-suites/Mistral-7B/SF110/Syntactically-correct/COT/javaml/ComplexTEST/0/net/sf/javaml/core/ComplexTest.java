package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

 private Complex complex1;
 private Complex complex2;
 private Complex complex3;
 private Complex complexI;
 private double delta = 0.001;

 @Before
 public void setUp() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = new Complex(0.0, 0.0);
 complexI = Complex.I;
 }

 @Test
 public void testComplexConstructorWithReAndIm() {
 Complex complex = new Complex(2.0, 3.0);
 assertEquals(2.0, complex.getRe(), delta);
 assertEquals(3.0, complex.getIm(), delta);
 }

 @Test
 public void testComplexConstructorWithDefaultValues() {
 Complex complex = new Complex();
 assertEquals(0.0, complex.getRe(), delta);
 assertEquals(0.0, complex.getIm(), delta);
 }

 @Test
 public void testToString() {
 assertEquals("1.0 + 2.0i", complex1.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbsWithNullComplex() {
 Complex complex = null;
 try {
 complex.abs();
 } catch (IllegalArgumentException e) {
 // test passes if the correct exception is thrown
 }
 }

 @Test
 public void testAbs() {
 double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
 assertEquals(expected, complex1.abs(), delta);
 }

 @Test
 public void testPlus() {
 Complex expected = new Complex(4.0, 6.0);
 complex1.plus(complex2);
 assertEquals(expected.getRe(), complex1.getRe(), delta);
 assertEquals(expected.getIm(), complex1.getIm(), delta);
 }

 @Test
 public void testMinus() {
 Complex expected = new Complex(-1.0, -2.0);
 complex1.minus(complex2);
 assertEquals(expected.getRe(), complex1.getRe(), delta);
 assertEquals(expected.getIm(), complex1.getIm(), delta);
 }

 @Test
 public void testTimesWithComplex() {
 Complex expected = new Complex(-5.0, 10.0);
 complex1.times(complex2);
 assertEquals(expected.getRe(), complex1.getRe(), delta);
 assertEquals(expected.getIm(), complex1.getIm(), delta);
 }

 @Test
 public void testTimesWithDouble() {
 Complex expected = new Complex(3.0, 6.0);
 complex1.times(3.0);
 assertEquals(expected.getRe(), complex1.getRe(), delta);
 assertEquals(expected.getIm(), complex1.getIm(), delta);
 }

 @Test
 public void testConjugate() {
 Complex expected = new Complex(1.0, -2.0);
 complex1.conjugate();
 assertEquals(expected.getRe(), complex1.getRe(), delta);
 assertEquals(expected.getIm(), complex1.getIm(), delta);
 }

 @Test
 public void testPlusStatic() {
 Complex expected = new Complex(4.0, 6.0);
 Complex complexA = new Complex(1.0, 2.0);
 Complex complexB = new Complex(3.0, 4.0);
 Complex result = Complex.plus(complexA, complexB);
 assertEquals(expected.getRe(), result.getRe(), delta);
 assertEquals(expected.getIm(), result.getIm(), delta);
 }

 @Test
 public void testMultiplyStaticWithComplexAndDouble() {
 Complex expected = new Complex(-3.0, 6.0);
 Complex complexA = new Complex(1.0, 2.0);
 double multiplier = 3.0;
 Complex result = Complex.multiply(complexA, multiplier);
 assertEquals(expected.getRe(), result.getRe(), delta);
 assertEquals(expected.getIm(), result.getIm(), delta);
 }

 @Test
 public void testMultiplyStaticWithComplexAndComplex() {
 Complex expected = new Complex(-5.0, 10.0);
 Complex complexA = new Complex(1.0, 2.0);
 Complex complexB = new Complex(3.0, 4.0);
 Complex result = Complex.multiply(complexA, complexB);
 assertEquals(expected.getRe(), result.getRe(), delta);
 assertEquals(expected.getIm(), result.getIm(), delta);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyStaticWithNullComplexA() {
 Complex complexB = new Complex(3.0, 4.0);
 try {
 Complex result = Complex.multiply(null, complexB);
 } catch (IllegalArgumentException e) {
 // test passes if the correct exception is thrown
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyStaticWithNullComplexB() {
 Complex complexA = new Complex(1.0, 2.0);
 try {
 Complex result = Complex.multiply(complexA, null);
 } catch (IllegalArgumentException e) {
 // test passes if the correct exception is thrown
 }
 }
}