import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*; // for assertX methods
import net.sf.javaml.core.Complex;

public class ComplexTest {

 private Complex complex1, complex2, complex3, complex4;

 @Before
 public void setUp() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = new Complex(0.0, 5.0);
 complex4 = new Complex(-1.0, 0.0);
 }

 @Test
 public void testComplexConstructor() {
 Complex c = new Complex(1.0, 2.0);
 assertEquals(1.0, c.getRe(), 0.0);
 assertEquals(2.0, c.getIm(), 0.0);
 }

 @Test
 public void testComplexDefaultConstructor() {
 Complex c = new Complex();
 assertEquals(0.0, c.getRe(), 0.0);
 assertEquals(0.0, c.getIm(), 0.0);
 }

 @Test
 public void testToStringMethod() {
 Complex c = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", c.toString());
 }

 @Test
 public void testAbsMethod() {
 Complex c = new Complex(3.0, 4.0);
 assertEquals(5.0, c.abs(), 0.0);
 }

 @Test
 public void testPlusMethod() {
 Complex result = complex1;
 result.plus(complex2);
 assertEquals(4.0, result.getRe(), 0.0);
 assertEquals(6.0, result.getIm(), 0.0);
 }

 @Test
 public void testMinusMethod() {
 Complex result = complex1;
 result.minus(complex2);
 assertEquals(-2.0, result.getRe(), 0.0);
 assertEquals(-2.0, result.getIm(), 0.0);
 }

 @Test
 public void testTimesMethodWithComplex() {
 Complex result = complex1;
 result.times(complex2);
 assertEquals(-5.0, result.getRe(), 0.0);
 assertEquals(10.0, result.getIm(), 0.0);
 }

 @Test
 public void testTimesMethodWithDouble() {
 Complex result = complex1;
 result.times(2.0);
 assertEquals(2.0, result.getRe(), 0.0);
 assertEquals(4.0, result.getIm(), 0.0);
 }

 @Test
 public void testConjugateMethod() {
 Complex result = complex1;
 result.conjugate();
 assertEquals(1.0, result.getRe(), 0.0);
 assertEquals(-2.0, result.getIm(), 0.0);
 }

 @Test
 public void testPlusStaticMethod() {
 Complex result = Complex.plus(complex1, complex2);
 assertEquals(4.0, result.getRe(), 0.0);
 assertEquals(6.0, result.getIm(), 0.0);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndDouble() {
 Complex result = Complex.multiply(complex1, 2.0);
 assertEquals(2.0, result.getRe(), 0.0);
 assertEquals(4.0, result.getIm(), 0.0);
 }

 @Test
 public void testMultiplyStaticMethodWithComplexAndComplex() {
 Complex result = Complex.multiply(complex1, complex2);
 assertEquals(-5.0, result.getRe(), 0.0);
 assertEquals(10.0, result.getIm(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesMethodWithNullComplex() {
 Complex complex5 = null;
 Complex complex6 = new Complex(1.0, 2.0);
 complex6.times(complex5);
 fail("Expected IllegalArgumentException, but got none.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesMethodWithNullDouble() {
 Complex complex5 = new Complex(1.0, 2.0);
 double doubleValue = null;
 complex5.times(doubleValue);
 fail("Expected IllegalArgumentException, but got none.");
 }

 @Test
 public void testConstructorsWithInvalidArguments() {
 try {
 new Complex("invalid", 2.0);
 fail("Expected IllegalArgumentException, but got none.");
 } catch (IllegalArgumentException e) {
 // pass
 }

 try {
 new Complex(1.0, "invalid");
 fail("Expected IllegalArgumentException, but got none.");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @After
 public void tearDown() {
 // empty
 }
}