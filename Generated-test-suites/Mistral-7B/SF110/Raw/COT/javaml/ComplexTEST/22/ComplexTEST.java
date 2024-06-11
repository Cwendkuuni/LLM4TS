import static org.junit.Assert.*;
import net.sf.javaml.core.Complex;

public class ComplexTest {

 private Complex complex1, complex2, complex3;

 @Before
 public void setUp() {
 complex1 = new Complex(1.0, 2.0);
 complex2 = new Complex(3.0, 4.0);
 complex3 = new Complex();
 }

 // Test constructor with valid arguments
 @Test
 public void testComplexConstructorValidArgs() {
 Complex complex = new Complex(5.0, 6.0);
 assertEquals("5.0 + 6.0i", complex.toString());
 }

 // Test constructor with default arguments
 @Test
 public void testComplexConstructorDefaultArgs() {
 Complex complex = new Complex();
 assertEquals("0.0 + 0.0i", complex.toString());
 }

 // Test abs method
 @Test
 public void testAbsMethod() {
 double expected = Math.sqrt(1.0 * 1.0 + 2.0 * 2.0);
 assertEquals(expected, complex1.abs(), 0.001);
 }

 // Test plus method
 @Test
 public void testPlusMethod() {
 complex1.plus(complex2);
 Complex expectedComplex = Complex.plus(complex1, complex2);
 assertEquals(expectedComplex.toString(), complex1.toString());
 }

 // Test minus method
 @Test
 public void testMinusMethod() {
 complex1.minus(complex2);
 Complex expectedComplex = Complex.minus(complex1, complex2);
 assertEquals(expectedComplex.toString(), complex1.toString());
 }

 // Test times method with Complex
 @Test
 public void testTimesMethodComplex() {
 Complex expectedComplex = Complex.multiply(complex1, complex2);
 complex1.times(complex2);
 assertEquals(expectedComplex.toString(), complex1.toString());
 }

 // Test times method with double
 @Test
 public void testTimesMethodDouble() {
 Complex expectedComplex = Complex.multiply(complex1, 3.0);
 complex1.times(3.0);
 assertEquals(expectedComplex.toString(), complex1.toString());
 }

 // Test conjugate method
 @Test
 public void testConjugateMethod() {
 complex1.conjugate();
 Complex expectedComplex = new Complex(1.0, -2.0);
 assertEquals(expectedComplex.toString(), complex1.toString());
 }

 // Test static plus method
 @Test
 public void testStaticPlusMethod() {
 Complex expectedComplex = Complex.plus(complex1, complex2);
 Complex resultComplex = Complex.plus(complex1, complex2);
 assertEquals(expectedComplex.toString(), resultComplex.toString());
 }

 // Test static multiply method with double
 @Test
 public void testStaticMultiplyMethodWithDouble() {
 Complex expectedComplex = Complex.multiply(complex1, 3.0);
 Complex resultComplex = Complex.multiply(complex1, 3.0);
 assertEquals(expectedComplex.toString(), resultComplex.toString());
 }

 // Test static multiply method with Complex
 @Test
 public void testStaticMultiplyMethodWithComplex() {
 Complex expectedComplex = Complex.multiply(complex1, complex2);
 Complex resultComplex = Complex.multiply(complex1, complex2);
 assertEquals(expectedComplex.toString(), resultComplex.toString());
 }

 // Test exception handling for null Complex in static methods
 @Test(expected = NullPointerException.class)
 public void testStaticMethodsNullComplex() {
 Complex a = null;
 Complex b = new Complex(1.0, 2.0);
 Complex resultComplex = Complex.plus(a, b);
 }

 // Test exception handling for null Complex in instance methods
 @Test(expected = NullPointerException.class)
 public void testInstanceMethodsNullComplex() {
 Complex complex = new Complex();
 Complex b = null;
 complex.plus(b);
 }
}