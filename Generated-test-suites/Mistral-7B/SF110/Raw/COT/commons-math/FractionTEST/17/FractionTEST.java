import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

 @Test
 public void testFractionConstructorDouble() {
 Fraction fraction = new Fraction(0.5);
 assertEquals(1, fraction.getDenominator());
 assertEquals(1, fraction.getNumerator());
 }

 @Test(expected = FractionConversionException.class)
 public void testFractionConstructorDoubleOverflow() {
 Fraction fraction = new Fraction(Double.MAX_VALUE);
 }

 @Test
 public void testFractionConstructorInt() {
 Fraction fraction = new Fraction(3);
 assertEquals(1, fraction.getDenominator());
 assertEquals(3, fraction.getNumerator());
 }

 @Test(expected = MathArithmeticException.class)
 public void testFractionConstructorIntDivisionByZero() {
 Fraction fraction = new Fraction(3, 0);
 }

 @Test
 public void testFractionConstructorIntInt() {
 Fraction fraction = new Fraction(2, 4);
 assertEquals(4, fraction.getDenominator());
 assertEquals(2, fraction.getNumerator());
 }

 @Test(expected = MathArithmeticException.class)
 public void testFractionConstructorIntIntOverflow() {
 Fraction fraction = new Fraction(Integer.MAX_VALUE, 1);
 }

 @Test
 public void testFractionAbs() {
 Fraction fraction = new Fraction(-3, 4);
 Fraction result = fraction.abs();
 assertEquals(3, result.getNumerator());
 assertEquals(4, result.getDenominator());
 }

 @Test
 public void testFractionCompareTo() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(3, 4);
 Fraction fraction3 = new Fraction(1, 2);
 assertEquals(-1, fraction1.compareTo(fraction2));
 assertEquals(0, fraction1.compareTo(fraction3));
 }

 @Test
 public void testFractionDoubleValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(0.5, fraction.doubleValue(), 0.001);
 }

 @Test
 public void testFractionEquals() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 2);
 assertTrue(fraction1.equals(fraction2));
 Fraction fraction3 = new Fraction(2, 2);
 assertFalse(fraction1.equals(fraction3));
 }

 @Test
 public void testFractionFloatValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(0.5f, fraction.floatValue(), 0.001f);
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
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 2);
 assertEquals(fraction1.hashCode(), fraction2.hashCode());
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
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.negate();
 assertEquals(-1, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testFractionReciprocal() {
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.reciprocal();
 assertEquals(2, result.getNumerator());
 assertEquals(1, result.getDenominator());
 }

 @Test
 public void testFractionAdd() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction result = fraction1.add(fraction2);
 assertEquals(2, result.getNumerator());
 assertEquals(6, result.getDenominator());
 }

 @Test
 public void testFractionSubtract() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction result = fraction1.subtract(fraction2);
 assertEquals(1, result.getNumerator());
 assertEquals(6, result.getDenominator());
 }

 @Test
 public void testFractionAddInt() {
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.add(3);
 assertEquals(3, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testFractionSubtractInt() {
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.subtract(3);
 assertEquals(-1, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testFractionAddSubFraction() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction result = fraction1.add(fraction2);
 Fraction expected = new Fraction(2, 6);
 assertEquals(expected.getNumerator(), result.getNumerator());
 assertEquals(expected.getDenominator(), result.getDenominator());

 Fraction fraction3 = new Fraction(1, 2);
 Fraction fraction4 = new Fraction(1, 3);
 Fraction negativeFraction2 = new Fraction(1, 3);
 negativeFraction2.negate();
 Fraction result2 = fraction3.subtract(fraction4);
 Fraction expected2 = new Fraction(1, 6);
 assertEquals(expected2.getNumerator(), result2.getNumerator());
 assertEquals(expected2.getDenominator(), result2.getDenominator());

 Fraction result3 = fraction3.subtract(negativeFraction2);
 Fraction expected3 = new Fraction(2, 6);
 assertEquals(expected3.getNumerator(), result3.getNumerator());
 assertEquals(expected3.getDenominator(), result3.getDenominator());
 }

 @Test
 public void testFractionMultiply() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction result = fraction1.multiply(fraction2);
 assertEquals(1, result.getNumerator());
 assertEquals(6, result.getDenominator());
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
 Fraction fraction2 = new Fraction(1, 3);
 Fraction result = fraction1.divide(fraction2);
 assertEquals(3, result.getNumerator());
 assertEquals(2, result.getDenominator());
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
 FractionField expected = FractionField.getInstance();
 assertEquals(expected, fraction.getField());
 }
}