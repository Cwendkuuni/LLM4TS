import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

 @Test
 public void testFractionConstructorDouble() {
 Fraction fraction = new Fraction(1.5);
 assertEquals(1, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test(expected = FractionConversionException.class)
 public void testFractionConstructorDoubleOutOfRange() {
 try {
 Fraction fraction = new Fraction(Double.MAX_VALUE);
 fail();
 } catch (FractionConversionException e) {
 // expected
 }
 }

 @Test
 public void testFractionConstructorInt() {
 Fraction fraction = new Fraction(4);
 assertEquals(4, fraction.getNumerator());
 assertEquals(1, fraction.getDenominator());
 }

 @Test(expected = MathArithmeticException.class)
 public void testFractionConstructorIntDividedByZero() {
 try {
 Fraction fraction = new Fraction(4, 0);
 fail();
 } catch (MathArithmeticException e) {
 // expected
 }
 }

 @Test
 public void testFractionGcd() {
 Fraction fraction1 = new Fraction(3, 4);
 Fraction fraction2 = new Fraction(5, 6);
 Fraction expected = new Fraction(1, 2);
 assertEquals(expected, Fraction.getReducedFraction(3 * 6, 4 * 5));
 }

 @Test
 public void testFractionToString() {
 Fraction fraction = new Fraction(1, 3);
 assertEquals("1/3", fraction.toString());
 }

 @Test
 public void testFractionAdd() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(2, 3);
 Fraction sum = new Fraction(3, 6);
 assertEquals(sum, fraction1.add(fraction2));
 }

 @Test
 public void testFractionSubtract() {
 Fraction fraction1 = new Fraction(2, 3);
 Fraction fraction2 = new Fraction(1, 2);
 Fraction difference = new Fraction(1, 3);
 assertEquals(difference, fraction1.subtract(fraction2));
 }

 @Test
 public void testFractionMultiply() {
 Fraction fraction1 = new Fraction(1, 3);
 Fraction fraction2 = new Fraction(2, 4);
 Fraction product = new Fraction(2, 12);
 assertEquals(product, fraction1.multiply(fraction2));
 }

 @Test
 public void testFractionDivide() {
 Fraction fraction1 = new Fraction(2, 3);
 Fraction fraction2 = new Fraction(1, 2);
 Fraction quotient = new Fraction(2, 1);
 assertEquals(quotient, fraction1.divide(fraction2));
 }

 @Test(expected = MathArithmeticException.class)
 public void testFractionDivideByZero() {
 try {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction quotient = fraction1.divide(new Fraction(0, 1));
 fail();
 } catch (MathArithmeticException e) {
 // expected
 }
 }
}