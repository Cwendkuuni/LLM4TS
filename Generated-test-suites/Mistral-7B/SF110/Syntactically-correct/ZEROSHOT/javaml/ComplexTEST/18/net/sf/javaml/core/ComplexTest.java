package net.sf.javaml.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import net.sf.javaml.core.Complex;

public class ComplexTest {

 // Test constructor
 @Test
 public void testComplexConstructor() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex c3 = new Complex();

 assertEquals("1.0 + 2.0i", c1.toString());
 assertEquals("3.0 + 4.0i", c2.toString());
 assertEquals("0.0 + 0.0i", c3.toString());
 }

 // Test abs method
 @Test
 public void testAbsMethod() {
 Complex c = new Complex(3.0, 4.0);
 double result = c.abs();

 assertEquals(5.0, result, 0.001);
 }

 // Test plus method
 @Test
 public void testPlusMethod() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex result;

 c1.plus(c2);
 result = c1;

 assertEquals("4.0 + 6.0i", result.toString());
 }

 // Test minus method
 @Test
 public void testMinusMethod() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex result;

 c1.minus(c2);
 result = c1;

 assertEquals("-2.0 - 2.0i", result.toString());
 }

 // Test times method (Complex)
 @Test
 public void testTimesMethodComplex() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex result;

 c1.times(c2);
 result = c1;

 assertEquals("-5.0 + 10.0i", result.toString());
 }

 // Test times method (double)
 @Test
 public void testTimesMethodDouble() {
 Complex c = new Complex(3.0, 4.0);
 double alpha = 2.0;
 Complex result;

 c.times(alpha);
 result = c;

 assertEquals("6.0 + 8.0i", result.toString());
 }

 // Test conjugate method
 @Test
 public void testConjugateMethod() {
 Complex c = new Complex(1.0, 2.0);
 Complex result;

 c.conjugate();
 result = c;

 assertEquals("1.0 - 2.0i", result.toString());
 }

 // Test static methods
 @Test
 public void testStaticMethods() {
 Complex c1 = new Complex(1.0, 2.0);
 Complex c2 = new Complex(3.0, 4.0);
 Complex result1, result2;

 result1 = Complex.plus(c1, c2);
 result2 = new Complex(4.0, 6.0);

 assertEquals(result1, result2);

 result1 = Complex.multiply(c1, 2.0);
 result2 = new Complex(2.0, 4.0);

 assertEquals(result1, result2);

 result1 = Complex.multiply(c1, c2);
 result2 = new Complex(-5.0, 10.0);

 assertEquals(result1, result2);
 }

 // Test static final I
 @Test
 public void testStaticFinalI() {
 Complex result = Complex.I;

 assertEquals("0.0 + 1.0i", result.toString());
 }
}