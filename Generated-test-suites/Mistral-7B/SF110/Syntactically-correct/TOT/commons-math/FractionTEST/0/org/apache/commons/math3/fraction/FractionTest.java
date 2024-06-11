import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FractionTest {

 @Test
 public void testConstructionWithDouble() {
 Fraction fraction = new Fraction(1.5);
 assertEquals(1, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testConstructionWithDoubleAndEpsilon() {
 Fraction fraction = new Fraction(1.5, 0.001, 100);
 assertEquals(1, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testConstructionWithDoubleAndMaxDenominator() {
 Fraction fraction = new Fraction(1.5, 5);
 assertEquals(1, fraction.getNumerator());
 assertEquals(2, fraction.getDenominator());
 }

 @Test
 public void testConstructionWithInt() {
 Fraction fraction = new Fraction(2);
 assertEquals(2, fraction.getNumerator());
 assertEquals(1, fraction.getDenominator());
 }

 @Test
 public void testConstructionWithIntAndInt() {
 Fraction fraction = new Fraction(2, 3);
 assertEquals(2, fraction.getNumerator());
 assertEquals(3, fraction.getDenominator());
 }

 @Test(expected = FractionConversionException.class)
 public void testConstructionWithDoubleOverflow() {
 Fraction fraction = new Fraction(Double.MAX_VALUE);
 }

 @Test(expected = FractionConversionException.class)
 public void testConstructionWithDoubleTooManyIterations() {
 Fraction fraction = new Fraction(1.5, 0.001, 10, 101);
 }

 @Test(expected = FractionConversionException.class)
 public void testConstructionWithIntDenominatorZero() {
 Fraction fraction = new Fraction(2, 0);
 }

 @Test(expected = MathArithmeticException.class)
 public void testConstructionWithIntDenominatorMinus() {
 Fraction fraction = new Fraction(2, -1);
 }

 @Test(expected = MathArithmeticException.class)
 public void testConstructionWithIntNumeratorMinusMaxValueAndDenominatorOne() {
 Fraction fraction = new Fraction(Integer.MIN_VALUE, 1);
 }

 @Test
 public void testEqualsObjectSameFraction() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 2);
 assertEquals(fraction1, fraction2);
 }

 @Test
 public void testEqualsObjectDifferentClass() {
 Fraction fraction = new Fraction(1, 2);
 Object obj = new Object();
 assertFalse(fraction.equals(obj));
 }

 @Test
 public void testEqualsObjectSameValueButDifferentObjects() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 2);
 Fraction fraction3 = new Fraction(1, 2);
 assertEquals(fraction1, fraction2);
 assertEquals(fraction1, fraction3);
 }

 @Test
 public void testCompareToZero() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(-1, fraction.compareTo(Fraction.ZERO));
 }

 @Test
 public void testCompareToPositive() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(2, 2);
 assertEquals(1, fraction1.compareTo(fraction2));
 }

 @Test
 public void testCompareToNegative() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(2, 1);
 assertEquals(-1, fraction1.compareTo(fraction2));
 }

 @Test
 public void testDoubleValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(0.5, fraction.doubleValue(), 0.0001);
 }

 @Test
 public void testFloatValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(0.5f, fraction.floatValue(), 0.001f);
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
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.negate();
 assertEquals(-1, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testReciprocal() {
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.reciprocal();
 assertEquals(2, result.getNumerator());
 assertEquals(1, result.getDenominator());
 }

 @Test
 public void testAdd() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction result = fraction1.add(fraction2);
 assertEquals(3, result.getNumerator());
 assertEquals(6, result.getDenominator());
 }

 @Test
 public void testAddInt() {
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.add(2);
 assertEquals(3, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testSubtract() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction result = fraction1.subtract(fraction2);
 assertEquals(-1, result.getNumerator());
 assertEquals(6, result.getDenominator());
 }

 @Test
 public void testSubtractInt() {
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.subtract(2);
 assertEquals(-3, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testAddSub() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction result = fraction1.addSub(fraction2, true);
 assertEquals(3, result.getNumerator());
 assertEquals(6, result.getDenominator());
 Fraction negResult = fraction1.addSub(fraction2, false);
 assertEquals(-3, negResult.getNumerator());
 assertEquals(6, negResult.getDenominator());
 }

 @Test
 public void testMultiply() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction result = fraction1.multiply(fraction2);
 assertEquals(1, result.getNumerator());
 assertEquals(6, result.getDenominator());
 }

 @Test
 public void testMultiplyInt() {
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.multiply(3);
 assertEquals(3, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testDivide() {
 Fraction fraction1 = new Fraction(1, 2);
 Fraction fraction2 = new Fraction(1, 3);
 Fraction result = fraction1.divide(fraction2);
 assertEquals(3, result.getNumerator());
 assertEquals(2, result.getDenominator());
 }

 @Test
 public void testDivideInt() {
 Fraction fraction = new Fraction(1, 2);
 Fraction result = fraction.divide(3);
 assertEquals(-1, result.getNumerator());
 assertEquals(6, result.getDenominator());
 }

 @Test
 public void testPercentageValue() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(50.0, fraction.percentageValue(), 0.01);
 }

 @Test
 public void testGetField() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals(FractionField.getInstance(), fraction.getField());
 }

 @Test
 public void testToString() {
 Fraction fraction = new Fraction(1, 2);
 assertEquals("1/2", fraction.toString());
 }
}