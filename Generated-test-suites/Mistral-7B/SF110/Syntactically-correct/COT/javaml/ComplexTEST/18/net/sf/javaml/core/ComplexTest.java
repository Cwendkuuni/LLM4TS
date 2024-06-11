import static org.junit.Assert.*;
import net.sf.javaml.core.Complex;

public class ComplexTest {

 private Complex complex1;
 private Complex complex2;
 private Complex complex3;
 private Complex complexI;

 @Before
 public void setUp() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = new Complex(0.0, 0.0);
 complexI = Complex.I;
 }

 // Test constructor with valid arguments
 @Test
 public void testComplexConstructorValidArgs() {
 Complex complex = new Complex(2.0, 3.0);
 assertEquals("2.0 + 3.0i", complex.toString());
 }

 // Test constructor with default arguments
 @Test
 public void testComplexConstructorDefaultArgs() {
 Complex complex = new Complex();
 assertEquals("0.0 + 0.0i", complex.toString());
 }

 // Test toString method
 @Test
 public void testToString() {
 assertEquals("1.0 + 2.0i", complex1.toString());
 }

 // Test abs method
 @Test
 public void testAbs() {
 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), complex1.abs(), 0.001);
 }

 // Test plus method
 @Test
 public void testPlus() {
 complex1.plus(complex2);
 assertEquals("4.0 + 6.0i", complex1.toString());
 }

 // Test minus method
 @Test
 public void testMinus() {
 complex1.minus(complex2);
 assertEquals("-2.0 - 2.0i", complex1.toString());
 }

 // Test times method with Complex
 @Test
 public void testTimesComplex() {
 complex1.times(complex2);
 assertEquals("-5.0 - 10.0i", complex1.toString());
 }

 // Test times method with double
 @Test
 public void testTimesDouble() {
 complex1.times(5.0);
 assertEquals("5.0 + 10.0i", complex1.toString());
 }

 // Test conjugate method
 @Test
 public void testConjugate() {
 complex1.conjugate();
 assertEquals("1.0 - 2.0i", complex1.toString());
 }

 // Test plus static method
 @Test
 public void testPlusStatic() {
 Complex result = Complex.plus(complex1, complex2);
 assertEquals("4.0 + 6.0i", result.toString());
 }

 // Test multiply static method with double
 @Test
 public void testMultiplyStaticWithDouble() {
 Complex result = Complex.multiply(complex1, 5.0);
 assertEquals("5.0 + 10.0i", result.toString());
 }

 // Test multiply static method with Complex
 @Test
 public void testMultiplyStaticWithComplex() {
 Complex result = Complex.multiply(complex1, complex2);
 assertEquals("-5.0 - 10.0i", result.toString());
 }

 // Test edge cases and exceptions
 @Test(expected = IllegalArgumentException.class)
 public void testTimesWithNullComplex() {
 complex1.times(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWithNullDouble() {
 complex1.times(null);
 }

 @Test
 public void testAbsWithZeroComplex() {
 Complex zeroComplex = new Complex(0.0, 0.0);
 assertEquals(0.0, zeroComplex.abs(), 0.001);
 }

 @Test
 public void testAbsWithNegativeComplex() {
 Complex negativeComplex = new Complex(-1.0, -2.0);
 assertEquals(Math.sqrt(1.0 * 1.0 + 2.0 * 2.0), negativeComplex.abs(), 0.001);
 }

 @Test
 public void testPlusWithZeroComplex() {
 Complex zeroComplex = new Complex(0.0, 0.0);
 complex1.plus(zeroComplex);
 assertEquals("1.0 + 2.0i", complex1.toString());
 }

 @Test
 public void testMinusWithZeroComplex() {
 Complex zeroComplex = new Complex(0.0, 0.0);
 complex1.minus(zeroComplex);
 assertEquals("1.0 + 2.0i", complex1.toString());
 }

 @Test
 public void testTimesWithZeroComplex() {
 Complex zeroComplex = new Complex(0.0, 0.0);
 complex1.times(zeroComplex);
 assertEquals("0.0 + 0.0i", complex1.toString());
 }

 @Test
 public void testTimesWithZeroDouble() {
 complex1.times(0.0);
 assertEquals("0.0 + 0.0i", complex1.toString());
 }

}