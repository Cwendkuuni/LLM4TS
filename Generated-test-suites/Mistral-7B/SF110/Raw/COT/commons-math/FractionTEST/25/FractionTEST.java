import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.exception.FractionConversionException;
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
    public void testFractionConstructorWithDouble_Overflow() throws FractionConversionException {
        new Fraction(1e12);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDouble_ZeroDenominator() throws FractionConversionException {
        new Fraction(-0.5);
    }

    @Test
    public void testFractionConstructorWithInt() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructorWithIntAndInt() {
        Fraction fraction = new Fraction(4, 2);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithIntAndZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithOverflow() {
        new Fraction(Integer.MIN_VALUE, 1);
    }

    @Test
    public void testAbsMethod() {
        Fraction fraction = new Fraction(-5, 6);
        Fraction result = fraction.abs();
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testCompareToMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 1);
        Fraction fraction3 = new Fraction(1, 2);

        int result = fraction1.compareTo(fraction2);
        assertEquals(-1, result);

        result = fraction1.compareTo(fraction3);
        assertEquals(0, result);

        result = fraction2.compareTo(fraction3);
        assertEquals(1, result);
    }

    @Test
    public void testDoubleValueMethod() {
        Fraction fraction = new Fraction(2, 3);
        double result = fraction.doubleValue();
        assertEquals(0.6666666666666666, result, 0.000000000000001);
    }

    @Test
    public void testEqualsMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @Test
    public void testGetDenominatorMethod() {
        Fraction fraction = new Fraction(3, 5);
        assertEquals(5, fraction.getDenominator());
    }

    @Test
    public void testGetNumeratorMethod() {
        Fraction fraction = new Fraction(5, 6);
        assertEquals(5, fraction.getNumerator());
    }

    @Test
    public void testHashCodeMethod() {
        Fraction fraction = new Fraction(1, 2);
        int result = fraction.hashCode();
        Fraction fraction2 = new Fraction(1, 2);
        int result2 = fraction2.hashCode();

        assertEquals(result, result2);
    }

    @Test
    public void testIntValueMethod() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(-1, fraction.intValue());
    }

    @Test
    public void testLongValueMethod() {
        Fraction fraction = new Fraction(5, 6);
        long result = fraction.longValue();
        assertEquals(-2, result);
    }

    @Test
    public void testNegateMethod() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.negate();
        assertEquals(-2, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testReciprocalMethod() {
        Fraction fraction = new Fraction(3, 2);
        Fraction result = fraction.reciprocal();
        assertEquals(2, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testAddMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(2, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testAddMethod_Zero() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.add(Fraction.ZERO);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testSubtractMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(-1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testSubtractMethod_Negative() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testAddSubMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(2, result.getNumerator());
        assertEquals(6, result.getDenominator());

        result = fraction1.addSub(fraction2, false);
        assertEquals(-1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testMultiplyMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testMultiplyMethod_Zero() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(Fraction.ZERO);
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testDivideMethod() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testDivideMethod_ZeroDivision() {
        Fraction fraction = new Fraction(1, 1);
        try {
            fraction.divide(Fraction.ZERO);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testDivideMethod_Negative() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(-1, 1);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(-2, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testPercentageValueMethod() {
        Fraction fraction = new Fraction(1, 3);
        double result = fraction.percentageValue();
        assertEquals(33.33333333333333, result, 0.000000000000001);
    }

    @Test
    public void testGetReducedFractionMethod() {
        int result = Fraction.getReducedFraction(13, 20).getNumerator();
        assertEquals(1, result);
        result = Fraction.getReducedFraction(21, -14).getNumerator();
        assertEquals(-7, result);
    }

    @Test
    public void testToStringMethod() {
        Fraction fraction = new Fraction(5, 8);
        assertEquals("5/8", fraction.toString());
    }

    @Test
    public void testGetFieldMethod() {
        FractionField result = Fraction.getField();
        assertNotNull(result);
    }
}