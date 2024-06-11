import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionConstructionWithDouble() throws FractionConversionException {
        Fraction fraction = new Fraction(3.14);
        assertEquals(3, fraction.getNumerator());
        assertEquals(7, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithDoubleThresholdOutOfRange() throws FractionConversionException {
        new Fraction(Double.MAX_VALUE);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithDoubleThresholdTooSmall() throws FractionConversionException {
        new Fraction(Double.MIN_VALUE);
    }

    @Test
    public void testFractionConstructionWithDoubleMaxDenominator() throws FractionConversionException {
        Fraction fraction = new Fraction(3.14, 1000);
        assertEquals(3, fraction.getNumerator());
        assertEquals(1000, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithDoubleMaxIterations() throws FractionConversionException {
        new Fraction(3.14, 1, 1);
    }

    @Test
    public void testFractionConstructionWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructionWithIntAndDenominator() {
        Fraction fraction = new Fraction(3, 5);
        assertEquals(3, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructionWithZeroDenominator() {
        new Fraction(3, 0);
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, fraction.abs());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(2, 4);
        int comparisonResult = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult);

        Fraction fraction3 = new Fraction(3, 4);
        Fraction fraction4 = new Fraction(3, 4);
        comparisonResult = fraction3.compareTo(fraction4);
        assertEquals(0, comparisonResult);

        Fraction fraction5 = new Fraction(3, 4);
        Fraction fraction6 = new Fraction(4, 3);
        comparisonResult = fraction5.compareTo(fraction6);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        double expected = 0.75;
        assertEquals(expected, fraction.doubleValue(), 0.0001);
    }

    @Test
    public void testFractionEquals() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(3, 4);
        boolean result = fraction1.equals(fraction2);
        assertTrue(result);

        Fraction fraction3 = new Fraction(3, 5);
        result = fraction1.equals(fraction3);
        assertFalse(result);

        Object obj = "test";
        result = fraction1.equals(obj);
        assertFalse(result);
    }

    @Test
    public void testFractionFloatValue() {
        Fraction fraction = new Fraction(3, 4);
        float expected = 0.75f;
        assertEquals(expected, fraction.floatValue(), 0.0001f);
    }

    @Test
    public void testFractionGetDenominator() {
        Fraction fraction = new Fraction(3, 4);
        int expected = 4;
        assertEquals(expected, fraction.getDenominator());
    }

    @Test
    public void testFractionGetNumerator() {
        Fraction fraction = new Fraction(3, 4);
        int expected = 3;
        assertEquals(expected, fraction.getNumerator());
    }

    @Test
    public void testFractionHashCode() {
        Fraction fraction = new Fraction(3, 4);
        int expected = 37 * (629 + 3) + 4;
        assertEquals(expected, fraction.hashCode());
    }

    @Test
    public void testFractionIntValue() {
        Fraction fraction = new Fraction(3, 4);
        int expected = 3 / 4;
        assertEquals(expected, fraction.intValue());
    }

    @Test
    public void testFractionLongValue() {
        Fraction fraction = new Fraction(3, 4);
        long expected = 3L / 4L;
        assertEquals(expected, fraction.longValue());
    }

    @Test
    public void testFractionNegate() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(-3, 4);
        assertEquals(expected, fraction.negate());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(4, 3);
        assertEquals(expected, fraction.reciprocal());
    }

    @Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction expected = new Fraction(7, 8);
        Fraction result = fraction1.add(fraction2);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionAddWithInt() {
        Fraction fraction = new Fraction(1, 2);
        int intValueToAdd = 5;
        Fraction expected = new Fraction(7, 4);
        Fraction result = fraction.add(intValueToAdd);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionSubtract() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction expected = new Fraction(2, 2);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionSubtractWithInt() {
        Fraction fraction = new Fraction(3, 2);
        int intValueToSubtract = 1;
        Fraction expected = new Fraction(5, 2);
        Fraction result = fraction.subtract(intValueToSubtract);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction expected = new Fraction(7, 8);
        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(expected, result);

        Fraction fraction3 = new Fraction(1, 2);
        Fraction fraction4 = new Fraction(3, 4);
        expected = new Fraction(-2, 8);
        result = fraction1.addSub(fraction2, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction expected = new Fraction(3, 8);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionMultiplyWithInt() {
        Fraction fraction = new Fraction(1, 2);
        int intValueToMultiply = 5;
        Fraction expected = new Fraction(5, 2);
        Fraction result = fraction.multiply(intValueToMultiply);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 1);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionDivideWithInt() {
        Fraction fraction = new Fraction(3, 2);
        int intValueToDivide = 2;
        Fraction expected = new Fraction(3, 1);
        Fraction result = fraction.divide(intValueToDivide);
        assertEquals(expected, result);
    }

    @Test
    public void testFractionPercentageValue() {
        Fraction fraction = new Fraction(1, 2);
        double expected = 50.0;
        assertEquals(expected, fraction.percentageValue(), 0.01);
    }

    @Test
    public void testFractionGetField() {
        Fraction fraction = new Fraction(1, 2);
        FractionField expected = FractionField.getInstance();
        FractionField result = fraction.getField();
        assertEquals(expected, result);
    }
}