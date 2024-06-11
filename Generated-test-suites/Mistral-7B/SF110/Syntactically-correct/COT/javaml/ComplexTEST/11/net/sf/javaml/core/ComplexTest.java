import static org.junit.Assert.*;
import net.sf.javaml.core.Complex;

public class ComplexTest {

 private Complex complexObject1;
 private Complex complexObject2;
 private Complex complexObject3;
 private Complex complexImaginaryUnit;

 @Before
 public void setup() {
 complexObject1 = new Complex(1.0, 2.0);
 complexObject2 = new Complex(3.0, 4.0);
 complexObject3 = new Complex(0.0, 0.0);
 complexImaginaryUnit = Complex.I;
 }

 // Test constructor
 @Test
 public void testComplexConstructor() {
 Complex complexObject = new Complex(5.0, 6.0);
 assertEquals("5.0 + 6.0i", complexObject.toString());
 }

 // Test abs method
 @Test
 public void testAbsMethod() {
 // Positive Complex Number
 Complex complexObject = new Complex(3.0, 4.0);
 double expectedResult = Math.sqrt(3.0 * 3.0 + 4.0 * 4.0);
 double result = complexObject.abs();
 assertEquals(expectedResult, result);

 // Negative Complex Number
 Complex complexObjectNegative = new Complex(-1.0, 2.0);
 expectedResult = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
 result = complexObjectNegative.abs();
 assertEquals(expectedResult, result);
 }

 // Test plus method
 @Test
 public void testPlusMethod() {
 complexObject1.plus(complexObject2);
 Complex expectedComplex = new Complex(4.0, 6.0);
 Complex resultComplex = complexObject1;
 assertEquals(expectedComplex.toString(), resultComplex.toString());
 }

 // Test minus method
 @Test
 public void testMinusMethod() {
 complexObject1.minus(complexObject2);
 Complex expectedComplex = new Complex(-2.0, -2.0);
 Complex resultComplex = complexObject1;
 assertEquals(expectedComplex.toString(), resultComplex.toString());
 }

 // Test times method for Complex Number
 @Test
 public void testTimesComplexNumberMethod() {
 complexObject1.times(complexObject2);
 Complex expectedComplex = new Complex(-5.0, 2.0);
 Complex resultComplex = complexObject1;
 assertEquals(expectedComplex.toString(), resultComplex.toString());
 }

 // Test times method for double
 @Test
 public void testTimesDoubleMethod() {
 complexObject1.times(2.0);
 Complex expectedComplex = new Complex(2.0, 4.0);
 Complex resultComplex = complexObject1;
 assertEquals(expectedComplex.toString(), resultComplex.toString());
 }

 // Test conjugate method
 @Test
 public void testConjugateMethod() {
 complexObject1.conjugate();
 Complex expectedComplex = new Complex(1.0, -2.0);
 Complex resultComplex = complexObject1;
 assertEquals(expectedComplex.toString(), resultComplex.toString());
 }

 // Test plus static method
 @Test
 public void testPlusStaticMethod() {
 Complex expectedComplex = new Complex(4.0, 6.0);
 Complex resultComplex = Complex.plus(complexObject1, complexObject2);
 assertEquals(expectedComplex.toString(), resultComplex.toString());
 }

 // Test multiply static method with double
 @Test
 public void testMultiplyStaticMethodWithDouble() {
 Complex expectedComplex = new Complex(5.0, 6.0);
 Complex resultComplex = Complex.multiply(complexObject1, 2.0);
 assertEquals(expectedComplex.toString(), resultComplex.toString());
 }

 // Test multiply static method with Complex Number
 @Test
 public void testMultiplyStaticMethodWithComplexNumber() {
 Complex expectedComplex = new Complex(-5.0, 10.0);
 Complex resultComplex = Complex.multiply(complexObject1, complexObject2);
 assertEquals(expectedComplex.toString(), resultComplex.toString());
 }

 // Test edge cases for plus method
 @Test(expected = IllegalArgumentException.class)
 public void testPlusMethodWithNullComplexNumber() {
 complexObject1.plus(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPlusMethodWithDifferentDimensions() {
 Complex complexObjectDifferentDimension = new Complex(1.0, 2.0, 3.0);
 complexObject1.plus(complexObjectDifferentDimension);
 }

 // Test edge cases for minus method
 @Test(expected = IllegalArgumentException.class)
 public void testMinusMethodWithNullComplexNumber() {
 complexObject1.minus(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusMethodWithDifferentDimensions() {
 Complex complexObjectDifferentDimension = new Complex(1.0, 2.0, 3.0);
 complexObject1.minus(complexObjectDifferentDimension);
 }

 // Test edge cases for times method with Complex Number
 @Test(expected = IllegalArgumentException.class)
 public void testTimesComplexNumberMethodWithNullComplexNumber() {
 complexObject1.times(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesComplexNumberMethodWithDifferentDimensions() {
 Complex complexObjectDifferentDimension = new Complex(1.0, 2.0, 3.0);
 complexObject1.times(complexObjectDifferentDimension);
 }

 // Test edge cases for times method with double
 @Test(expected = IllegalArgumentException.class)
 public void testTimesDoubleMethodWithNegativeDouble() {
 complexObject1.times(-2.0);
 }

 // Test exception handling for plusStaticMethod with null Complex Number
 @Test(expected = IllegalArgumentException.class)
 public void testPlusStaticMethodWithNullComplexNumber() {
 Complex expectedComplex = null;
 Complex resultComplex = Complex.plus(null, complexObject2);
 assertNull(resultComplex);
 }

 // Test exception handling for plusStaticMethod with different dimensions Complex Numbers
 @Test(expected = IllegalArgumentException.class)
 public void testPlusStaticMethodWithDifferentDimensionsComplexNumbers() {
 Complex complexObjectDifferentDimension1 = new Complex(1.0, 2.0, 3.0);
 Complex complexObjectDifferentDimension2 = new Complex(1.0, 2.0, 4.0);
 Complex resultComplex = Complex.plus(complexObjectDifferentDimension1, complexObjectDifferentDimension2);
 assertNull(resultComplex);
 }

 // Test exception handling for multiplyStaticMethod with null Complex Number
 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyStaticMethodWithNullComplexNumber() {
 Complex expectedComplex = null;
 Complex resultComplex = Complex.multiply(null, complexObject2);
 assertNull(resultComplex);
 }

 // Test exception handling for multiplyStaticMethod with different dimensions Complex Numbers
 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyStaticMethodWithDifferentDimensionsComplexNumbers() {
 Complex complexObjectDifferentDimension1 = new Complex(1.0, 2.0, 3.0);
 Complex complexObjectDifferentDimension2 = new Complex(1.0, 2.0, 4.0);
 Complex resultComplex = Complex.multiply(complexObjectDifferentDimension1, complexObjectDifferentDimension2);
 assertNull(resultComplex);
 }
}