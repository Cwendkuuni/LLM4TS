import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionConstructorWithDouble() {
        Fraction fraction = new Fraction(2.5);
        assertNotNull(fraction);
        assertEquals(2, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleNegativeDenominator() {
        new Fraction(-2.5);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleDenominatorGreaterThanMaxValue() {
        new Fraction(2.5, Integer.MAX_VALUE);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleEpsilonTooSmall() {
        new Fraction(2.5, 0.0);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleMaxIterations() {
        new Fraction(2.5, Integer.MAX_VALUE, 100);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithIntNegativeNumerator() {
        new Fraction(-3);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithIntZeroDenominator() {
        try {
            new Fraction(3, 0);
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, e.getMessage());
            throw e;
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithIntMaxValueNumeratorAndNegativeDenominator() {
        try {
            new Fraction(Integer.MAX_VALUE, -1);
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW_IN_FRACTION, e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction expected = new Fraction(3, 4);
        Fraction result = fraction.abs();
        assertEquals(expected, result);
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        int result = fraction1.compareTo(fraction2);
        assertEquals(-1, result);
    }

    @Test
    public void testFractionCompareToEqual() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        int result = fraction1.compareTo(fraction2);
        assertEquals(0, result);
    }

    @Test
    public void testFractionCompareToGreater() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 2);
        int result = fraction1.compareTo(fraction2);
        assertEquals(1, result);
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(2, 3);
        double result = fraction.doubleValue();
        assertEquals(0.666666667, result, 0.0000001);
    }

    @Test
    public void testFractionEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        assertTrue(fraction1.equals(fraction2));
    }

    @Test
    public void testFractionEqualsNull() {
        Fraction fraction = new Fraction(1, 1);
        assertFalse(fraction.equals(null));
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
        int result1 = fraction1.hashCode();
        int result2 = fraction2.hashCode();
        assertEquals(result1, result2);
    }

    @Test
    public void testFractionIntValue() {
        Fraction fraction = new Fraction(1, 2);
        int result = fraction.intValue();
        assertEquals(0, result);
    }

    @Test
    public void testFractionLongValue() {
        Fraction fraction = new Fraction(1, 2);
        long result = fraction.longValue();
        assertEquals(0L, result);
    }

    @Test
    public void testFractionNegate() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(-3, 4);
        Fraction result = fraction.negate();
        assertEquals(expected, result);
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction expected = new Fraction(3, 2);
        Fraction result = fraction.reciprocal();
        assertEquals(expected, result);
    }

    @Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(3, 6);
        Fraction result = fraction1.add(fraction2);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionAddInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction result = fraction.add(new Fraction(1, 1));
        assertEquals(expected, result);
    }

    @Test
    public void testFractionSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(1, 6);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionSubtractInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction expected = new Fraction(1, 4);
        Fraction result = fraction.subtract(new Fraction(1, 2));
        assertEquals(expected, result);
    }

    @Test
    public void testFractionAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(2, 6);
        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(expected, result);
        result = fraction1.addSub(fraction2, false);
        expected = new Fraction(-1, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(1, 6);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionMultiplyInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction result = fraction.multiply(new Fraction(1, 1));
        assertEquals(expected, result);
    }

    @Test
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(3, 2);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionDivideInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 2);
        Fraction result = fraction.divide(new Fraction(1, 1));
        assertEquals(expected, result);
    }

    @Test
    public void testFractionPercentageValue() {
        Fraction fraction = new Fraction(1, 3);
        double result = fraction.percentageValue();
        assertEquals(33.333333333, result, 0.0000001);
    }

    @Test
    public void testFractionGetReducedFraction() {
        Fraction fraction = new Fraction(11, 15);
        Fraction expected = new Fraction(2, 3);
        Fraction result = Fraction.getReducedFraction(11, 15);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionToString() {
        Fraction fraction = new Fraction(1, 2);
        String result = fraction.toString();
        assertEquals("1/2", result);
    }
}