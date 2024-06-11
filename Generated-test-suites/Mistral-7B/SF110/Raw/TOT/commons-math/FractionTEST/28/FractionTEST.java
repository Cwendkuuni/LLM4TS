import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

 @Test
 public void testFractionConstructionWithDouble() {
 Fraction fraction = new Fraction(1.25);
 assertEquals(5, fraction.getDenominator());
 assertEquals(2, fraction.getNumerator());
 }

 @Test(expected = FractionConversionException.class)
 public void testFractionConstructionWithDoubleOutOfRange() {
 try {
 Fraction fraction = new Fraction(Double.MAX_VALUE);
 fail("Expected FractionConversionException");
 } catch (FractionConversionException e) {
 // Expected
 }
 }

 @Test
 public void testFractionConstructionWithInt() {
 Fraction fraction = new Fraction(4);
 assertEquals(1, fraction.getDenominator());
 assertEquals(4, fraction.getNumerator());
 }

 @Test
 public void testFractionConstructionWithIntAndDenominator() {
 Fraction fraction = new Fraction(3, 5);
 assertEquals(5, fraction.getDenominator());
 assertEquals(3, fraction.getNumerator());
 }

 @Test(expected = MathArithmeticException.class)
 public void testFractionConstructionWithZeroDenominator() {
 try {
 Fraction fraction = new Fraction(1, 0);
 fail("Expected MathArithmeticException");
 } catch (MathArithmeticException e) {
 // Expected
 }
 }

 @Test(expected = MathArithmeticException.class)
 public void testFractionConstructionWithNegativeDenominator() {
 try {
 Fraction fraction = new Fraction(1, -3);
 fail("Expected MathArithmeticException");
 } catch (MathArithmeticException e) {
 // Expected
 }
 }

 @Test
 public void testFractionAbs() {
 Fraction fraction = new Fraction(-2, 3);
 Fraction result = fraction.abs();
 assertEquals(2, result.getNumerator());
 assertEquals(3, result.getDenominator());
 }

 @Test
 public void testFractionCompareTo() {
 Fraction fraction1 = new Fraction(2, 3);
 Fraction fraction2 = new Fraction(3, 2);
 int comparisonResult = fraction1.compareTo(fraction2);
 assertEquals(-1, comparisonResult);
 }

 @Test
 public void testFractionDoubleValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(0.5, fraction.doubleValue(), 0.0001);
 }

 @Test
 public void testFractionEqualsObject() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 2);
 assertTrue(fraction1.equals(fraction2));
 }

 @Test
 public void testFractionFloatValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(0.5f, fraction.floatValue(), 0.0001f);
 }

 @Test
 public void testFractionGetDenominator() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testFractionGetNumerator() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(1, fraction.getNumerator());
 }

 @Test
 public void testFractionHashCode() {
 Fraction fraction = new Fraction(1, 2);
 int hashCode = fraction.hashCode();
 int expectedHashCode = 37 * (629 + fraction.getNumerator()) + fraction.getDenominator();
 assertEquals(expectedHashCode, hashCode);
 }

 @Test
 public void testFractionIntValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(0, fraction.intValue());
 }

 @Test
 public void testFractionLongValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(0L, fraction.longValue());
 }

 @Test
 public void testFractionNegate() {
 Fraction fraction = new Fraction(-2, 3);
 Fraction result = fraction.negate();
 assertEquals(2, result.getNumerator());
 assertEquals(3, result.getDenominator());
 }

 @Test
 public void testFractionReciprocal() {
 Fraction fraction = new Fraction(2, 3);
 Fraction result = fraction.reciprocal();
 assertEquals(3, result.getDenominator());
 assertEquals(2, result.getNumerator());
 }

 @Test
 public void testFractionAdd() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 4);
 Fraction result = fraction1.add(fraction2);
 assertEquals(7, result.getNumerator());
 assertEquals(8, result.getDenominator());
 }

 @Test
 public void testFractionAddInt() {
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.add(1);
 assertEquals(3, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testFractionSubtract() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 4);
 Fraction result = fraction1.subtract(fraction2);
 assertEquals(-2, result.getNumerator());
 assertEquals(8, result.getDenominator());
 }

 @Test
 public void testFractionSubtractInt() {
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.subtract(1);
 assertEquals(-1, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testFractionAddSub() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 4);
 Fraction result1 = fraction1.add(fraction2);
 Fraction result2 = fraction2.add(fraction1);
 assertEquals(result1, result2);
 }

 @Test
 public void testFractionMultiply() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 4);
 Fraction result = fraction1.multiply(fraction2);
 assertEquals(3, result.getDenominator());
 assertEquals(2, result.getNumerator());
 }

 @Test
 public void testFractionMultiplyInt() {
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.multiply(3);
 assertEquals(3, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testFractionDivide() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 4);
 Fraction result = fraction1.divide(fraction2);
 assertEquals(13, result.getNumerator());
 assertEquals(12, result.getDenominator());
 }

 @Test
 public void testFractionDivideInt() {
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.divide(3);
 assertEquals(-1, result.getNumerator());
 assertEquals(6, result.getDenominator());
 }

 @Test
 public void testFractionPercentageValue() {
 Fraction fraction = new Fraction(3, 4);
 assertEquals(75.0, fraction.percentageValue(), 0.01);
 }

 @Test
 public void testFractionGetField() {
 Fraction fraction = new Fraction(1, 2);
 FractionField field = fraction.getField();
 assertEquals(FractionField.getInstance(), field);
 }
}