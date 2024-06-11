import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

 @Test
 public void testFractionConstructorWithDouble() {
 Fraction fraction = new Fraction(1.5);
 assertEquals(1, fraction.getNumerator());
 assertEquals(3, fraction.getDenominator());
 }

 @Test(expected = FractionConversionException.class)
 public void testFractionConstructorWithDoubleOutOfRange() {
 try {
 Fraction fraction = new Fraction(Double.MAX_VALUE);
 fail("FractionConversionException expected but not thrown.");
 } catch (FractionConversionException e) {
 // Expected exception
 }
 }

 @Test(expected = FractionConversionException.class)
 public void testFractionConstructorWithDoubleSmallDenominator() {
 try {
 Fraction fraction = new Fraction(0.5);
 fail("FractionConversionException expected but not thrown.");
 } catch (FractionConversionException e) {
 // Expected exception
 }
 }

 @Test
 public void testFractionConstructorWithInt() {
 Fraction fraction = new Fraction(4);
 assertEquals(4, fraction.getNumerator());
 assertEquals(1, fraction.getDenominator());
 }

 @Test(expected = MathArithmeticException.class)
 public void testFractionConstructorWithZeroDenominator() {
 try {
 Fraction fraction = new Fraction(1, 0);
 fail("MathArithmeticException expected but not thrown.");
 } catch (MathArithmeticException e) {
 // Expected exception
 }
 }

 @Test(expected = MathArithmeticException.class)
 public void testFractionConstructorWithNegativeDenominator() {
 try {
 Fraction fraction = new Fraction(1, -1);
 fail("MathArithmeticException expected but not thrown.");
 } catch (MathArithmeticException e) {
 // Expected exception
 }
 }

 @Test
 public void testFractionGcd() {
 Fraction fraction1 = new Fraction(15, 21);
 Fraction fraction2 = new Fraction(7, 14);
 Fraction expected = new Fraction(1, 3);
 Fraction result = Fraction.getReducedFraction(fraction1.getNumerator(), fraction1.getDenominator());
 assertEquals(expected.getNumerator(), result.getNumerator());
 assertEquals(expected.getDenominator(), result.getDenominator());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFractionMultiplyWithNullFraction() {
 try {
 Fraction a = null;
 Fraction b = new Fraction(2, 3);
 Fraction result = a.multiply(b);
 fail("IllegalArgumentException expected but not thrown.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testFractionMultiply() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 4);
 Fraction expected = new Fraction(3, 8);
 Fraction result = fraction1.multiply(fraction2);
 assertEquals(expected.getNumerator(), result.getNumerator());
 assertEquals(expected.getDenominator(), result.getDenominator());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFractionAddWithNullFraction() {
 try {
 Fraction a = null;
 Fraction b = new Fraction(2, 3);
 Fraction result = a.add(b);
 fail("IllegalArgumentException expected but not thrown.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testFractionAdd() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 4);
 Fraction expected = new Fraction(5, 6);
 Fraction result = fraction1.add(fraction2);
 assertEquals(expected.getNumerator(), result.getNumerator());
 assertEquals(expected.getDenominator(), result.getDenominator());
 }

 @Test
 public void testFractionSubtract() {
 Fraction fraction1 = new Fraction(4, 3);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction expected = new Fraction(3, 3);
 Fraction result = fraction1.subtract(fraction2);
 assertEquals(expected.getNumerator(), result.getNumerator());
 assertEquals(expected.getDenominator(), result.getDenominator());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFractionSubtractWithNullFraction() {
 try {
 Fraction a = null;
 Fraction b = new Fraction(2, 3);
 Fraction result = a.subtract(b);
 fail("IllegalArgumentException expected but not thrown.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testFractionNegate() {
 Fraction fraction = new Fraction(1, 2);
 Fraction expected = new Fraction(-1, 2);
 Fraction result = fraction.negate();
 assertEquals(expected.getNumerator(), result.getNumerator());
 assertEquals(expected.getDenominator(), result.getDenominator());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFractionDivideWithNullFraction() {
 try {
 Fraction a = null;
 Fraction b = new Fraction(2, 3);
 Fraction result = a.divide(b);
 fail("IllegalArgumentException expected but not thrown.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testFractionDivide() {
 Fraction fraction1 = new Fraction(4, 3);
 Fraction fraction2 = new Fraction(2, 3);
 Fraction expected = new Fraction(2, 1);
 Fraction result = fraction1.divide(fraction2);
 assertEquals(expected.getNumerator(), result.getNumerator());
 assertEquals(expected.getDenominator(), result.getDenominator());
 }

 @Test(expected = ArithmeticException.class)
 public void testFractionDivideByZero() {
 try {
 Fraction numerator = new Fraction(1, 1);
 Fraction denominator = Fraction.ZERO;
 Fraction result = numerator.divide(denominator);
 fail("ArithmeticException expected but not thrown.");
 } catch (ArithmeticException e) {
 // Expected exception
 }
 }

 @Test
 public void testFractionCompareTo() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 4);
 Fraction fraction3 = new Fraction(1, 2);
 int expected1 = -1;
 int expected2 = 1;
 int result1 = fraction1.compareTo(fraction2);
 assertEquals(expected1, result1);
 int result2 = fraction2.compareTo(fraction1);
 assertEquals(expected2, result2);
 int result3 = fraction1.compareTo(fraction1);
 assertEquals(0, result3);
 }

 @Test
 public void testFractionToString() {
 Fraction fraction = new Fraction(1, 2);
 String expected = "1/2";
 String result = fraction.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testFractionAbs() {
 Fraction fraction = new Fraction(-1, 2);
 Fraction expected = new Fraction(1, 2);
 Fraction result = fraction.abs();
 assertEquals(expected.getNumerator(), result.getNumerator());
 assertEquals(expected.getDenominator(), result.getDenominator());
 }
}