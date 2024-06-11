import static org.junit.Assert.*;
import net.sf.javaml.core.Complex;

public class ComplexTest {

 private Complex complex1;
 private Complex complex2;
 private Complex complex3;
 private Complex complex4;
 private Complex complexI;

 @Before
 public void setUp() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = new Complex(0.0, 5.0);
 complex4 = new Complex(-1.0, -2.0);
 complexI = Complex.I;
 }

 // Test constructor with double arguments
 @Test
 public void testComplexConstructorDoubleDouble() {
 Complex testComplex = new Complex(2.0, 3.0);
 assertEquals("2.0 + 3.0i", testComplex.toString());
 }

 // Test default constructor
 @Test
 public void testComplexDefaultConstructor() {
 Complex testComplex = new Complex();
 assertEquals("0.0 + 0.0i", testComplex.toString());
 }

 // Test abs() method
 @Test
 public void testAbs() {
 double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
 assertEquals(expected, complex1.abs(), 0.001);
 }

 // Test plus() method
 @Test
 public void testPlus() {
 Complex result = new Complex();
 complex1.plus(complex2);
 result.re = 4.0;
 result.im = 6.0;
 assertEquals(result, complex1);
 complex1.re = 1.0;
 complex1.im = 2.0;
 complex2.re = 3.0;
 complex2.im = 4.0;
 Complex expected = new Complex(4.0, 6.0);
 result = new Complex();
 complex1.plus(complex2);
 assertEquals(expected, result);
 }

 // Test minus() method
 @Test
 public void testMinus() {
 Complex result = new Complex();
 complex1.minus(complex2);
 result.re = -2.0;
 result.im = -6.0;
 assertEquals(result, complex1);
 complex1.re = 1.0;
 complex1.im = 2.0;
 complex2.re = 3.0;
 complex2.im = 4.0;
 Complex expected = new Complex(-2.0, -6.0);
 result = new Complex();
 complex1.minus(complex2);
 assertEquals(expected, result);
 }

 // Test times() method with Complex argument
 @Test
 public void testTimesComplex() {
 Complex expected = new Complex(-5.0, 10.0);
 Complex result = new Complex();
 complex1.times(complex2);
 assertEquals(expected, result);
 }

 // Test times() method with double argument
 @Test
 public void testTimesDouble() {
 Complex expected = new Complex(3.0, 6.0);
 Complex result = new Complex();
 complex1.times(3.0);
 assertEquals(expected, result);
 }

 // Test conjugate() method
 @Test
 public void testConjugate() {
 Complex expected = new Complex(1.0, -2.0);
 Complex result = new Complex();
 complex1.conjugate();
 assertEquals(expected, result);
 }

 // Test static plus() method
 @Test
 public void testStaticPlus() {
 Complex expected = new Complex(4.0, 6.0);
 Complex result = Complex.plus(complex1, complex2);
 assertEquals(expected, result);
 }

 // Test static multiply() method with double argument
 @Test
 public void testStaticMultiplyDouble() {
 Complex expected = new Complex(3.0, 6.0);
 Complex result = Complex.multiply(complex1, 3.0);
 assertEquals(expected, result);
 }

 // Test static multiply() method with Complex argument
 @Test
 public void testStaticMultiplyComplex() {
 Complex expected = new Complex(-5.0, 10.0);
 Complex result = Complex.multiply(complex1, complex2);
 assertEquals(expected, result);
 }

 // Test edge cases: constructor with NaN arguments
 @Test(expected = IllegalArgumentException.class)
 public void testComplexConstructorDoubleNaN() {
 new Complex(Double.NaN, Double.NaN);
 }

 // Test edge cases: plus() method with zero complex number
 @Test
 public void testPlusZeroComplex() {
 Complex result = new Complex();
 complex1.plus(new Complex(0.0, 0.0));
 assertEquals("1.0 + 2.0i", complex1.toString());
 }

 // Test edge cases: minus() method with zero complex number
 @Test
 public void testMinusZeroComplex() {
 Complex result = new Complex();
 complex1.minus(new Complex(0.0, 0.0));
 assertEquals("1.0 + 2.0i", complex1.toString());
 }

 // Test edge cases: times() method with zero complex number
 @Test
 public void testTimesZeroComplex() {
 Complex result = new Complex();
 complex1.times(new Complex(0.0, 0.0));
 assertEquals("0.0 + 0.0i", result.toString());
 }

 // Test exception handling: times() method with non-complex argument
 @Test(expected = IllegalArgumentException.class)
 public void testTimesNonComplex() {
 complex1.times(4);
 }
}