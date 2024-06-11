import static org.junit.Assert.*;
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
 Complex complex5 = new Complex(2.0, 3.0);
 assertEquals("2.0 + 3.0i", complex5.toString());
 }

 @Test
 public void testComplexToString() {
 assertEquals("1.0 + 2.0i", complex1.toString());
 }

 @Test
 public void testAbs() {
 assertEquals(3.1622776601683795, complex1.abs(), 0.001);
 }

 @Test
 public void testPlus() {
 complex1.plus(complex2);
 assertEquals("4.0 + 6.0i", complex1.toString());
 }

 @Test
 public void testMinus() {
 complex1.minus(complex2);
 assertEquals("-2.0 - 2.0i", complex1.toString());
 }

 @Test
 public void testTimesComplex() {
 Complex complex6 = new Complex(1.0, 1.0);
 complex1.times(complex6);
 assertEquals("-5.0 + 6.0i", complex1.toString());
 }

 @Test
 public void testTimesDouble() {
 complex1.times(5.0);
 assertEquals("5.0 + 10.0i", complex1.toString());
 }

 @Test
 public void testConjugate() {
 complex1.conjugate();
 assertEquals("1.0 - 2.0i", complex1.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbsWithNegativeComplexNumber() {
 Complex complex7 = new Complex(0.0, -5.0);
 complex7.abs();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWithZeroComplexNumber() {
 Complex complex8 = new Complex(0.0, 0.0);
 complex8.times(complex2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimesWithZeroDouble() {
 complex1.times(0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPlusWithZeroComplexNumber() {
 Complex complex9 = new Complex(0.0, 0.0);
 complex1.plus(complex9);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithZeroComplexNumber() {
 complex1.minus(complex9);
 }
}