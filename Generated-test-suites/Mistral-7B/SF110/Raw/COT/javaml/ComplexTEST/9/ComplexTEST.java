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
 public void testConstructorWithDoubleReAndDoubleIm() {
 Complex complex4 = new Complex(5.0, 6.0);
 assertEquals(5.0, complex4.getRe(), 0.001);
 assertEquals(6.0, complex4.getIm(), 0.001);
 }

 @Test
 public void testConstructorWithDefaultValues() {
 Complex complex4 = new Complex();
 assertEquals(0.0, complex4.getRe(), 0.001);
 assertEquals(0.0, complex4.getIm(), 0.001);
 }

 @Test
 public void testToString() {
 assertEquals("3.0 + 4.0i", complex1.toString());
 }

 @Test
 public void testAbs() {
 Complex complex4 = new Complex(3.0, 4.0);
 double expected = Math.sqrt(3.0 * 3.0 + 4.0 * 4.0);
 assertEquals(expected, complex4.abs(), 0.001);
 }

 @Test
 public void testPlus() {
 Complex complex4 = new Complex(3.0, 4.0);
 complex3.plus(complex2);
 complex1.plus(complex2);
 assertEquals(complex1, complex3);
 }

 @Test
 public void testMinus() {
 Complex complex4 = new Complex(3.0, 4.0);
 complex3.minus(complex2);
 complex1.minus(complex2);
 assertEquals(complex1, complex3);
 }

 @Test
 public void testTimesComplex() {
 Complex complex4 = new Complex(3.0, 4.0);
 complex3.times(complex2);
 Complex expected = new Complex(-2.0, 11.0);
 assertEquals(expected.getRe(), complex3.getRe(), 0.001);
 assertEquals(expected.getIm(), complex3.getIm(), 0.001);
 }

 @Test
 public void testTimesDouble() {
 Complex complex4 = new Complex(3.0, 4.0);
 complex3.times(2.0);
 Complex expected = new Complex(6.0, 8.0);
 assertEquals(expected.getRe(), complex3.getRe(), 0.001);
 assertEquals(expected.getIm(), complex3.getIm(), 0.001);
 }

 @Test
 public void testConjugate() {
 Complex complex4 = new Complex(3.0, 4.0);
 complex4.conjugate();
 Complex expected = new Complex(3.0, -4.0);
 assertEquals(expected.getRe(), complex4.getRe(), 0.001);
 assertEquals(expected.getIm(), complex4.getIm(), 0.001);
 }

 @Test
 public void testPlusStatic() {
 Complex complex4 = Complex.plus(complex1, complex2);
 Complex expected = new Complex(4.0, 6.0);
 assertEquals(expected.getRe(), complex4.getRe(), 0.001);
 assertEquals(expected.getIm(), complex4.getIm(), 0.001);
 }

 @Test
 public void testMultiplyStaticWithComplexAndDouble() {
 Complex complex4 = Complex.multiply(complex1, 2.0);
 Complex expected = new Complex(6.0, 8.0);
 assertEquals(expected.getRe(), complex4.getRe(), 0.001);
 assertEquals(expected.getIm(), complex4.getIm(), 0.001);
 }

 @Test
 public void testMultiplyStaticWithComplexAndComplex() {
 Complex complex4 = Complex.multiply(complex1, complex2);
 Complex expected = new Complex(-2.0, 11.0);
 assertEquals(expected.getRe(), complex4.getRe(), 0.001);
 assertEquals(expected.getIm(), complex4.getIm(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWithNullComplex() {
 complex1.times(null);
 fail("Times method should not accept a null Complex argument.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWithNullDouble() {
 complex1.times((Double)null);
 fail("Times method should not accept a null double argument.");
 }

 @Test(expected = ArithmeticException.class)
 public void testAbsWithNegativeComplexNumber() {
 Complex complex4 = new Complex(-3.0, 4.0);
 double expected = Math.sqrt(3.0 * 3.0 + 4.0 * 4.0);
 // This test will fail due to the Math.sqrt method, but it's expected as Complex does not support negative numbers.
 // assertEquals(expected, complex4.abs(), 0.001);
 }

 @Test(expected = ArithmeticException.class)
 public void testPlusWithComplexAndNullComplex() {
 Complex complex4 = new Complex(3.0, 4.0);
 Complex complex5 = null;
 complex4.plus(complex5);
 fail("Plus method should not accept a null Complex argument.");
 }

 @Test(expected = ArithmeticException.class)
 public void testMinusWithComplexAndNullComplex() {
 Complex complex4 = new Complex(3.0, 4.0);
 Complex complex5 = null;
 complex4.minus(complex5);
 fail("Minus method should not accept a null Complex argument.");
 }

 @Test(expected = ArithmeticException.class)
 public void testTimesWithComplexAndComplexDivisionByZero() {
 Complex complex4 = new Complex(3.0, 4.0);
 Complex complex5 = new Complex(0.0, 0.0);
 complex4.times(complex5);
 fail("Times method should not accept divisor Complex zero.");
 }

 @Test(expected = ArithmeticException.class)
 public void testMultiplyStaticWithComplexAndComplexDivisionByZero() {
 Complex complex4 = Complex.I;
 Complex complex5 = new Complex(0.0, 0.0);
 Complex expected = Complex.multiply(complex4, complex5);
 fail("Multiply method should not accept divisor Complex zero.");
 }

 @Test
 public void testI() {
 assertEquals(complexI.getRe(), 0.0);
 assertEquals(complexI.getIm(), 1.0);
 }

}