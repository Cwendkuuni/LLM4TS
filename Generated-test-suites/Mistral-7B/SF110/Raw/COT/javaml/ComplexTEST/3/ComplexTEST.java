package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ComplexTest {

 Complex obj1, obj2, obj3, obj4;
 Complex I = new Complex(0.0, 1.0);

 @Before
 public void setUp() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj3 = new Complex(0.0, 0.0);
 obj4 = new Complex(-1.0, 2.0);
 }

 @Test
 public void testConstructorWithReAndIm() {
 Complex obj5 = new Complex(5.0, 6.0);
 assertEquals("5.0 + 6.0i", obj5.toString());
 }

 @Test
 public void testDefaultConstructor() {
 Complex obj6 = new Complex();
 assertEquals("0.0 + 0.0i", obj6.toString());
 }

 @Test
 public void testAbsMethod() {
 double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
 assertEquals(expected, obj1.abs(), 0.001);
 }

 @Test(expected = ArithmeticException.class)
 public void testAbsMethodWithInvalidComplexNumber() {
 Complex obj5 = new Complex(0.0, 0.0);
 try {
 obj5.abs();
 } catch (ArithmeticException e) {
 // Pass, the test is to check for ArithmeticException
 }
 }

 @Test
 public void testPlusMethod() {
 Complex obj7 = new Complex();
 obj7.plus(obj1);
 assertEquals("(1.0 + 2.0)i + (1.0 + 2.0)", obj7.toString());
 }

 @Test
 public void testMinusMethod() {
 Complex obj7 = new Complex();
 obj7.minus(obj1);
 assertEquals("(-1.0 - 2.0)i", obj7.toString());
 }

 @Test
 public void testTimesMethodWithComplexNumber() {
 Complex obj7 = new Complex();
 Complex c = new Complex(2.0, 3.0);
 obj7.times(c);
 double expectedReal = 1.0 * 2.0 - 2.0 * 3.0;
 double expectedImag = 1.0 * 3.0 + 2.0 * 2.0;
 assertEquals(expectedReal, obj7.re, 0.001);
 assertEquals(expectedImag, obj7.im, 0.001);
 }

 @Test
 public void testTimesMethodWithDouble() {
 Complex obj5 = new Complex(5.0, 6.0);
 obj5.times(2.5);
 assertEquals("12.5 + 15.0i", obj5.toString());
 }

 @Test
 public void testConjugateMethod() {
 Complex obj5_conj = obj5.conjugate();
 assertEquals("5.0 - 6.0i", obj5_conj.toString());
 }

 @Test
 public void testPlusStaticMethod() {
 Complex result = Complex.plus(obj1, obj2);
 double expectedReal = obj1.re + obj2.re;
 double expectedImag = obj1.im + obj2.im;
 assertEquals(expectedReal, result.re, 0.001);
 assertEquals(expectedImag, result.im, 0.001);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndDouble() {
 Complex result = Complex.multiply(obj1, 2.0);
 double expectedReal = obj1.re * 2.0;
 double expectedImag = obj1.im * 2.0;
 assertEquals(expectedReal, result.re, 0.001);
 assertEquals(expectedImag, result.im, 0.001);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndComplex() {
 Complex result = Complex.multiply(obj1, obj2);
 double expectedReal = obj1.re * obj2.re - obj1.im * obj2.im;
 double expectedImag = obj1.re * obj2.im + obj1.im * obj2.re;
 assertEquals(expectedReal, result.re, 0.001);
 assertEquals(expectedImag, result.im, 0.001);
 }

 @Test
 public void testAddSameComplexNumbers() {
 Complex obj7 = new Complex();
 obj7.plus(obj1);
 obj7.plus(obj1);
 assertEquals("2.0 + 4.0i", obj7.toString());
 }

 @Test
 public void testSubtractSameComplexNumbers() {
 Complex obj7 = new Complex();
 obj7.minus(obj1);
 obj7.minus(obj1);
 assertEquals("0.0 - 4.0i", obj7.toString());
 }

 @Test
 public void testAddComplexNumberWithZero() {
 Complex obj7 = new Complex();
 obj7.plus(obj1);
 assertEquals("(1.0 + 2.0)i", obj7.toString());
 }

 @Test
 public void testSubtractComplexNumberWithZero() {
 Complex obj7 = new Complex();
 obj7.minus(obj1);
 assertEquals("(-1.0 - 2.0)i", obj7.toString());
 }

 @Test
 public void testMultiplyComplexNumberWithZero() {
 Complex obj7 = new Complex();
 obj7.times(obj1);
 assertEquals("0.0 + 0.0i", obj7.toString());
 }

 @Test
 public void testDivideComplexNumberByZero() {
 try {
 obj1.times(new Complex(0.0, 0.0));
 fail("Division by zero is not allowed");
 } catch (ArithmeticException e) {
 // Pass, the test is to check for ArithmeticException
 }
 }

 @Test
 public void testTimesMethodWithNegativeComplexNumber() {
 Complex obj5_neg = new Complex(-1.0, 2.0);
 obj5.times(obj5_neg);
 double expectedReal = obj1.re * obj5_neg.re + obj1.im * obj5_neg.im;
 double expectedImag = obj1.re * obj5_neg.im + obj1.im * obj5_neg.re;
 assertEquals(expectedReal, obj5.re, 0.001);
 assertEquals(expectedImag, obj5.im, 0.001);
 }

 @Test
 public void testConjugateStaticMethod() {
 Complex result = Complex.conjugate(obj1);
 assertEquals("1.0 - 2.0i", result.toString());
 }
}