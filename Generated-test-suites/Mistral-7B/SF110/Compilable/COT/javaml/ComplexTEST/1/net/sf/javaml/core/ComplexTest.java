package net.sf.javaml.core;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.Complex;

public class ComplexTest {

 private Complex complex1, complex2, complex3, complex4, complex5, complex6;

 @Test
 public void testComplexConstructor() {
 complex1 = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", complex1.toString());
 }

 @Test
 public void testComplexNoArgumentConstructor() {
 complex2 = new Complex();
 assertEquals("0.0 + 0.0i", complex2.toString());
 }

 @Test
 public void testAbsMethod() {
 complex3 = new Complex(3.0, 4.0);
 assertEquals(5.0, complex3.abs(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbsMethodWithNullComplex() {
 Complex nullComplex = null;
 nullComplex.abs();
 }

 @Test
 public void testPlusMethod() {
 complex4 = new Complex(1.0, 2.0);
 Complex complexB = new Complex(3.0, 4.0);
 complex4.plus(complexB);
 assertEquals("4.0 + 6.0i", complex4.toString());
 }

 @Test
 public void testMinusMethod() {
 complex4 = new Complex(1.0, 2.0);
 Complex complexB = new Complex(3.0, 4.0);
 complex4.minus(complexB);
 assertEquals("-2.0 - 2.0i", complex4.toString());
 }

 @Test
 public void testTimesMethodWithComplex() {
 complex5 = new Complex(1.0, 2.0);
 Complex complexB = new Complex(3.0, 4.0);
 complex5.times(complexB);
 assertEquals("-5.0 + 10.0i", complex5.toString());
 }

 @Test
 public void testTimesMethodWithDouble() {
 complex5 = new Complex(1.0, 2.0);
 complex5.times(5.0);
 assertEquals("5.0 + 10.0i", complex5.toString());
 }

 @Test
 public void testConjugateMethod() {
 complex6 = new Complex(1.0, 2.0);
 complex6.conjugate();
 assertEquals("1.0 - 2.0i", complex6.toString());
 }

 @Test
 public void testPlusStaticMethod() {
 Complex complexC = new Complex(1.0, 2.0);
 Complex complexD = new Complex(3.0, 4.0);
 Complex result = Complex.plus(complexC, complexD);
 assertEquals("4.0 + 6.0i", result.toString());
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndDouble() {
 Complex complexC = new Complex(1.0, 2.0);
 Complex result = Complex.multiply(complexC, 5.0);
 assertEquals("5.0 + 10.0i", result.toString());
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndComplex() {
 Complex complexC = new Complex(1.0, 2.0);
 Complex complexD = new Complex(3.0, 4.0);
 Complex result = Complex.multiply(complexC, complexD);
 assertEquals("-5.0 + 10.0i", result.toString());
 }

}