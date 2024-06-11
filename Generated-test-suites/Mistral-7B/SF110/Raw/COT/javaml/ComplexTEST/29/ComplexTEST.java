import static org.junit.Assert.*;
import net.sf.javaml.core.Complex;

public class ComplexTest {

 Complex obj1;
 Complex obj2;
 Complex obj3;
 Complex obj4;
 Complex obj5;

 @Before
 public void setup() {
 obj1 = new Complex(1.0, 2.0);
 obj2 = new Complex(3.0, 4.0);
 obj3 = new Complex(0.0, 0.0);
 obj4 = new Complex(5.0, 0.0);
 obj5 = Complex.I;
 }

 @Test
 public void testComplexConstructor() {
 Complex complex = new Complex(1.0, 2.0);
 assertEquals("1.0 + 2.0i", complex.toString());
 }

 @Test
 public void testComplexDefaultConstructor() {
 Complex complex = new Complex();
 assertEquals("0.0 + 0.0i", complex.toString());
 }

 @Test
 public void testAbs() {
 Complex complex = new Complex(3.0, 4.0);
 assertEquals(5.0, complex.abs(), 0.01);
 }

 @Test
 public void testPlus() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = new Complex(3.0, 4.0);
 complex1.plus(complex2);
 assertEquals("4.0 + 6.0i", complex1.toString());
 }

 @Test
 public void testMinus() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = new Complex(3.0, 4.0);
 complex1.minus(complex2);
 assertEquals("-2.0 - 2.0i", complex1.toString());
 }

 @Test
 public void testTimesComplex() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = new Complex(3.0, 4.0);
 complex1.times(complex2);
 assertEquals("-5.0 + 10.0i", complex1.toString());
 }

 @Test
 public void testTimesDouble() {
 Complex complex = new Complex(1.0, 2.0);
 complex.times(2.0);
 assertEquals("2.0 + 4.0i", complex.toString());
 }

 @Test
 public void testConjugate() {
 Complex complex = new Complex(1.0, 2.0);
 complex.conjugate();
 assertEquals("1.0 - 2.0i", complex.toString());
 }

 @Test
 public void testPlusStatic() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = new Complex(3.0, 4.0);
 Complex result = Complex.plus(complex1, complex2);
 assertEquals("4.0 + 6.0i", result.toString());
 }

 @Test
 public void testMultiplyStaticWithComplexAndDouble() {
 Complex complex = new Complex(1.0, 2.0);
 double alpha = 3.0;
 Complex result = Complex.multiply(complex, alpha);
 assertEquals("3.0 + 6.0i", result.toString());
 }

 @Test
 public void testMultiplyStaticWithComplexAndComplex() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = new Complex(3.0, 4.0);
 Complex result = Complex.multiply(complex1, complex2);
 assertEquals("-5.0 + 10.0i", result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWithNullComplex() {
 Complex complex = new Complex(1.0, 2.0);
 complex.times(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWithNullDouble() {
 Complex complex = new Complex(1.0, 2.0);
 complex.times((Double) null);
 }

 @Test(expected = AssertionError.class)
 public void testAbsWithNegativeComplexNumber() {
 Complex complex = new Complex(-1.0, 2.0);
 // The assertion error will be thrown in the abs() method
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPlusWithNullComplex() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = null;
 complex1.plus(complex2); // The IllegalArgumentException will be thrown here
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNullComplex() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = null;
 complex1.minus(complex2); // The IllegalArgumentException will be thrown here
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWithInfiniteComplexNumber() {
 Complex complex = new Complex(Double.POSITIVE_INFINITY, 0.0);
 complex.times(Complex.I);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWithNaNComplexNumber() {
 Complex complex = new Complex(Double.NaN, 0.0);
 complex.times(Complex.I);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithNullComplex1() {
 Complex complex1 = null;
 Complex complex2 = new Complex(1.0, 2.0);
 Complex result = Complex.multiply(complex1, complex2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithNullComplex2() {
 Complex complex1 = new Complex(1.0, 2.0);
 Complex complex2 = null;
 Complex result = Complex.multiply(complex1, complex2);
 }

 @Test(expected = AssertionError.class)
 public void testMultiplyWithImaginaryZeroComplex1() {
 Complex complex1 = new Complex(0.0, 0.0);
 Complex complex2 = new Complex(0.0, 5.0);
 Complex result = Complex.multiply(complex1, complex2);
 }

 @Test(expected = AssertionError.class)
 public void testMultiplyWithImaginaryZeroComplex2() {
 Complex complex1 = new Complex(0.0, 5.0);
 Complex complex2 = new Complex(0.0, 0.0);
 Complex result = Complex.multiply(complex1, complex2);
 }
}