import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionConstructorWithDouble() throws FractionConversionException {
        Fraction fraction = new Fraction(0.5);
        assertEquals(1, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleOutOfRange() throws FractionConversionException {
        new Fraction(Double.MAX_VALUE + 1);
    }

    @Test
    public void testFractionConstructorWithDoubleAndEpsilon() throws FractionConversionException {
        Fraction fraction = new Fraction(Math.E, 0.0001, 100);
        assertEquals(1, fraction.getDenominator());
        assertEquals((int)Math.round(Math.E * 10000), fraction.getNumerator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleAndInvalidMaxDenominator() throws FractionConversionException {
        new Fraction(2.0, 0, 1);
    }

    @Test
    public void testFractionConstructorWithDoubleAndMaxIterations() throws FractionConversionException {
        Fraction fraction = new Fraction(Math.PI, 10, 100);
        assertEquals(10, fraction.getDenominator());
        assertEquals((int)Math.round(Math.PI * 10000), fraction.getNumerator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleAndInvalidMaxIterations() throws FractionConversionException {
        new Fraction(2.0, 1, 1, 1);
    }

    @Test
    public void testFractionConstructorWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(1, fraction.getDenominator());
        assertEquals(4, fraction.getNumerator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithZeroDenominator() {
        new Fraction(2, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithNegativeDenominatorAndPositiveNumerator() {
        new Fraction(2, -3);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithNegativeDenominatorAndNegativeNumerator() {
        new Fraction(-2, -3);
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction result = fraction.abs();
        assertEquals(3, result.getDenominator());
        assertEquals(-3, result.getNumerator());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(2, 3);

        int comparisonResult = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult);

        comparisonResult = fraction2.compareTo(fraction1);
        assertEquals(1, comparisonResult);

        Fraction zero = Fraction.ZERO;
        comparisonResult = fraction1.compareTo(zero);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75, fraction.doubleValue(), 0.001);
    }

    @Test
    public void testFractionEquals() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction fraction3 = new Fraction(2, 4);

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @Test
    public void testFractionFloatValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75f, fraction.floatValue(), 0.001f);
    }

    @Test
    public void testFractionGetDenominator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testFractionGetNumerator() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
    }

    @Test
    public void testFractionHashCode() {
        Fraction fraction = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(3, 4);
        assertEquals(fraction.hashCode(), fraction2.hashCode());
    }

    @Test
    public void testFractionIntValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3 / 4, fraction.intValue(), 0);
    }

    @Test
    public void testFractionLongValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3L / 4L, fraction.longValue(), 0);
    }

    @Test
    public void testFractionNegate() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction result = fraction.negate();
        assertEquals(3, result.getDenominator());
        assertEquals(3, result.getNumerator());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.reciprocal();
        assertEquals(4, result.getDenominator());
        assertEquals(3, result.getNumerator());
    }

    @Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(11, result.getDenominator());
        assertEquals(15, result.getNumerator());
    }

    @Test
    public void testFractionAddInteger() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.add(1);
        assertEquals(7, result.getDenominator());
        assertEquals(11, result.getNumerator());
    }

    @Test
    public void testFractionSubtract() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(1, result.getDenominator());
        assertEquals(-1, result.getNumerator());
    }

    @Test
    public void testFractionSubtractInteger() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.subtract(1);
        assertEquals(3, result.getDenominator());
        assertEquals(-1, result.getNumerator());
    }

    @Test
    public void testFractionAddSub() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(11, result.getDenominator());
        assertEquals(15, result.getNumerator());

        result = fraction1.addSub(fraction2, false);
        assertEquals(1, result.getDenominator());
        assertEquals(-1, result.getNumerator());
    }

    @Test
    public void testFractionMultiply() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(2, result.getDenominator());
        assertEquals(6, result.getNumerator());
    }

    @Test
    public void testFractionMultiplyInteger() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.multiply(2);
        assertEquals(6, result.getDenominator());
        assertEquals(6, result.getNumerator());
    }

    @Test
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(2, result.getDenominator());
        assertEquals(3, result.getNumerator());
    }

    @Test
    public void testFractionDivideInteger() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.divide(2);
        assertEquals(3, result.getDenominator());
        assertEquals(-3, result.getNumerator());
    }

    @Test
    public void testFractionPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(75, fraction.percentageValue(), 0);
    }

    @Test
    public void testFractionGetReducedFraction() {
        int numerator = 12;
        int denominator = 21;
        Fraction result = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(2, result.getDenominator());
        assertEquals(3, result.getNumerator());
    }

    @Test
    public void testFractionToString() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals("3/4", fraction.toString());
    }
}