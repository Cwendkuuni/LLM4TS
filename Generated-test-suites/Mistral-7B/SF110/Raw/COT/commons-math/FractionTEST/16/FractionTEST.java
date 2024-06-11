import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionConstructorWithDouble() throws FractionConversionException {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleOutOfRange() throws FractionConversionException {
        new Fraction(Double.MAX_VALUE + 1.0);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleSmallDenominator() throws FractionConversionException {
        new Fraction(-0.5);
    }

    @Test
    public void testFractionConstructorWithInt() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithNegativeDenominator() {
        new Fraction(1, -1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithNegativeNumeratorAndPositiveDenominator() {
        new Fraction(-1, 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithNegativeNumeratorAndNegativeDenominator() {
        new Fraction(-1, -1);
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(1, -3);
        Fraction result = fraction.abs();
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 1);
        int result1 = fraction1.compareTo(fraction2);
        assertEquals(-1, result1);

        Fraction fraction3 = new Fraction(1, 2);
        Fraction fraction4 = new Fraction(1, 2);
        int result2 = fraction3.compareTo(fraction4);
        assertEquals(0, result2);

        Fraction fraction5 = new Fraction(2, 1);
        int result3 = fraction5.compareTo(fraction4);
        assertEquals(1, result3);
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
        assertEquals(fraction1, fraction2);

        Fraction fraction3 = new Fraction(1, 3);
        assertNotEquals(fraction1, fraction3);
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
        Fraction fraction = new Fraction(1, -3);
        Fraction result = fraction.negate();
        assertEquals(-1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.reciprocal();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
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
    public void testFractionAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(2, result.getNumerator());
        assertEquals(6, result.getDenominator());

        Fraction fraction3 = new Fraction(1, 2);
        Fraction fraction4 = new Fraction(1, 3);
        Fraction result2 = fraction1.addSub(fraction4, false);
        assertEquals(-1, result2.getNumerator());
        assertEquals(6, result2.getDenominator());
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
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testFractionPercentageValue() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(66.66666666666667, fraction.percentageValue(), 0.001);
    }

    @Test
    public void testFractionGetField() {
        Fraction fraction = new Fraction(1, 2);
        FractionField field = fraction.getField();
        assertEquals(FractionField.getInstance(), field);
    }
}