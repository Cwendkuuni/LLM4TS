import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

 @Test
 public void testConstructor_double() {
 Fraction fraction = new Fraction(0.5);
 assertEquals(1, fraction.getDenominator());
 assertEquals(1, fraction.getNumerator());
 }

 @Test(expected = FractionConversionException.class)
 public void testConstructor_double_overflow() {
 try {
 Fraction fraction = new Fraction(Double.MAX_VALUE);
 } catch (FractionConversionException e) {
 // Expected
 }
 }

 @Test
 public void testConstructor_double_epsilon() {
 Fraction fraction = new Fraction(0.5, 0.0001, 100);
 assertEquals(1, fraction.getDenominator());
 assertEquals(1, fraction.getNumerator());
 }

 @Test(expected = FractionConversionException.class)
 public void testConstructor_double_epsilon_maxIterations() {
 try {
 Fraction fraction = new Fraction(0.5, 0.0001, Integer.MIN_VALUE, 100);
 } catch (FractionConversionException e) {
 // Expected
 }
 }

 @Test(expected = FractionConversionException.class)
 public void testConstructor_double_epsilon_maxDenominator() {
 try {
 Fraction fraction = new Fraction(0.5, 0.0001, Integer.MAX_VALUE, 100);
 } catch (FractionConversionException e) {
 // Expected
 }
 }

 @Test(expected = FractionConversionException.class)
 public void testConstructor_int() {
 try {
 Fraction fraction = new Fraction(Integer.MIN_VALUE);
 } catch (FractionConversionException e) {
 // Expected
 }
 }

 @Test
 public void testConstructor_int_denominator() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(2, fraction.getDenominator());
 assertEquals(1, fraction.getNumerator());
 }

 @Test(expected = MathArithmeticException.class)
 public void testConstructor_int_denominator_zero() {
 try {
 Fraction fraction = new Fraction(1, 0);
 } catch (MathArithmeticException e) {
 // Expected
 }
 }

 @Test
 public void testConstructor_int_negativeDenominator() {
 Fraction fraction = new Fraction(-1, 2);
 assertEquals(-2, fraction.getDenominator());
 assertEquals(1, fraction.getNumerator());
 }

 @Test
 public void testConstructor_int_maxDenominator() {
 Fraction fraction = new Fraction(1, Integer.MAX_VALUE);
 assertEquals(1, fraction.getDenominator());
 assertEquals(1, fraction.getNumerator());
 }

 @Test
 public void testAbs() {
 Fraction fraction = new Fraction(-1, 2);
 Fraction absFraction = fraction.abs();
 assertEquals(1, absFraction.getDenominator());
 assertEquals(1, absFraction.getNumerator());
 }

 @Test
 public void testCompareTo() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 int result = fraction1.compareTo(fraction2);
 assertEquals(-1, result);
 }

 @Test
 public void testDoubleValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(0.5, fraction.doubleValue(), 0.001);
 }

 @Test
 public void testEquals() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 2);
 assertTrue(fraction1.equals(fraction2));
 }

 @Test
 public void testFloatValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(0.5f, fraction.floatValue(), 0.001f);
 }

 @Test
 public void testGetDenominator() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testGetNumerator() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(1, fraction.getNumerator());
 }

 @Test
 public void testHashCode() {
 Fraction fraction = new Fraction(1, 2);
 int hashCode = fraction.hashCode();
 int expectedHashCode = 37 * (629 + fraction.getNumerator()) + fraction.getDenominator();
 assertEquals(expectedHashCode, hashCode);
 }

 @Test
 public void testIntValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(0, fraction.intValue());
 }

 @Test
 public void testLongValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(0L, fraction.longValue());
 }

 @Test
 public void testNegate() {
 Fraction fraction = new Fraction(-1, 2);
 Fraction negateFraction = fraction.negate();
 assertEquals(1, negateFraction.getDenominator());
 assertEquals(1, negateFraction.getNumerator());
 }

 @Test
 public void testReciprocal() {
 Fraction fraction = new Fraction(1, 2);
 Fraction reciprocalFraction = fraction.reciprocal();
 assertEquals(2, reciprocalFraction.getDenominator());
 assertEquals(1, reciprocalFraction.getNumerator());
 }

 @Test
 public void testAdd() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction sumFraction = fraction1.add(fraction2);
 assertEquals(2, sumFraction.getDenominator());
 assertEquals(2, sumFraction.getNumerator());
 }

 @Test
 public void testAdd_int() {
 Fraction fraction = new Fraction(1, 2);
 Fraction sumFraction = fraction.add(1);
 assertEquals(1, sumFraction.getDenominator());
 assertEquals(3, sumFraction.getNumerator());
 }

 @Test
 public void testSubtract() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction differenceFraction = fraction1.subtract(fraction2);
 assertEquals(1, differenceFraction.getDenominator());
 assertEquals(1, differenceFraction.getNumerator());
 }

 @Test
 public void testSubtract_int() {
 Fraction fraction = new Fraction(1, 2);
 Fraction differenceFraction = fraction.subtract(1);
 assertEquals(1, differenceFraction.getDenominator());
 assertEquals(-1, differenceFraction.getNumerator());
 }

 @Test
 public void testAddSub() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction sumFraction = fraction1.addSub(fraction2, true);
 assertEquals(2, sumFraction.getDenominator());
 assertEquals(2, sumFraction.getNumerator());
 Fraction differenceFraction = fraction1.addSub(fraction2, false);
 assertEquals(1, differenceFraction.getDenominator());
 assertEquals(-1, differenceFraction.getNumerator());
 }

 @Test
 public void testMultiply() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction productFraction = fraction1.multiply(fraction2);
 assertEquals(3, productFraction.getDenominator());
 assertEquals(1, productFraction.getNumerator());
 }

 @Test
 public void testMultiply_int() {
 Fraction fraction = new Fraction(1, 2);
 Fraction productFraction = fraction.multiply(3);
 assertEquals(3, productFraction.getDenominator());
 assertEquals(3, productFraction.getNumerator());
 }

 @Test
 public void testDivide() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction quotientFraction = fraction1.divide(fraction2);
 assertEquals(3, quotientFraction.getDenominator());
 assertEquals(1, quotientFraction.getNumerator());
 }

 @Test
 public void testDivide_int() {
 Fraction fraction = new Fraction(1, 2);
 Fraction quotientFraction = fraction.divide(3);
 assertEquals(3, quotientFraction.getDenominator());
 assertEquals(-1, quotientFraction.getNumerator());
 }

 @Test
 public void testPercentageValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(50.0, fraction.percentageValue(), 0.01);
 }

 @Test
 public void testGetReducedFraction() {
 int numerator = 5;
 int denominator = 12;
 Fraction reducedFraction = Fraction.getReducedFraction(numerator, denominator);
 assertEquals(1, reducedFraction.getDenominator());
 assertEquals(5, reducedFraction.getNumerator());
 }

 @Test
 public void testToString() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals("1/2", fraction.toString());
 }
}