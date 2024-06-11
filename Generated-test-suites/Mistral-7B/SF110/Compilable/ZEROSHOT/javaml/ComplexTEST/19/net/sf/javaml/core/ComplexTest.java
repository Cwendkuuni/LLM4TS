package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

Complex c1;
Complex c2;
Complex c3;
Complex c4;
Complex c5;
Complex c6;
Complex c7;

@Test
public void testComplexConstructor() {
 c1 = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", c1.toString());
}

@Test
public void testComplexDefaultConstructor() {
 c2 = new Complex();
 assertEquals("0.0 + 0.0i", c2.toString());
}

@Test
public void testGetAndSetReAndIm() {
 c3 = new Complex(3.0, 4.0);
 c3.re = 5.0;
 c3.im = 6.0;
 assertEquals("5.0 + 6.0i", c3.toString());
}

@Test
public void testAbs() {
 c4 = new Complex(3.0, 4.0);
 assertEquals(5.0, c4.abs(), 0.01); // Allow for small error due to floating point precision
}

@Test
public void testPlus() {
 c5 = new Complex(1.0, 2.0);
 c6 = new Complex(3.0, 4.0);
 c5.plus(c6);
 assertEquals("4.0 + 6.0i", c5.toString());
}

@Test
public void testMinus() {
 c5 = new Complex(1.0, 2.0);
 c6 = new Complex(3.0, 4.0);
 c5.minus(c6);
 assertEquals("-2.0 - 2.0i", c5.toString());
}

@Test
public void testTimesComplex() {
 c5 = new Complex(1.0, 1.0);
 c6 = new Complex(1.0, 1.0);
 c7 = Complex.multiply(c5, c6);
 assertEquals("1.0 + 1.0i", c7.toString());
}

@Test
public void testTimesDouble() {
 c5 = new Complex(1.0, 1.0);
 Complex result = Complex.multiply(c5, 3.0);
 assertEquals("3.0 + 3.0i", result.toString());
}

@Test
public void testConjugate() {
 c5 = new Complex(1.0, 2.0);
 c5.conjugate();
 assertEquals("1.0 - 2.0i", c5.toString());
}

@Test
public void testStaticPlus() {
 Complex a = new Complex(1.0, 2.0);
 Complex b = new Complex(3.0, 4.0);
 Complex sum = Complex.plus(a, b);
 assertEquals("4.0 + 6.0i", sum.toString());
}

@Test
public void testStaticMultiplyComplexAndDouble() {
 Complex a = new Complex(1.0, 1.0);
 Complex result = Complex.multiply(a, 3.0);
 assertEquals("3.0 + 3.0i", result.toString());
}

@Test
public void testStaticMultiplyComplexAndComplex() {
 Complex a = new Complex(1.0, 1.0);
 Complex b = new Complex(1.0, 1.0);
 Complex result = Complex.multiply(a, b);
 assertEquals("1.0 + 1.0i", result.toString());
}

@Test
public void testStaticConstantI() {
 Complex i = Complex.I;
 assertEquals("0.0 + 1.0i", i.toString());
}
}