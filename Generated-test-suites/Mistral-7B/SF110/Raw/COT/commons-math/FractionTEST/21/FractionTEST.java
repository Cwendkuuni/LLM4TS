import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.fraction.Fraction;

public class FractionTest {

    @Test
    public void testFractionConstructor_double() throws FractionConversionException {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructor_double_exception() throws FractionConversionException {
        new Fraction(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testFractionConstructor_int() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructor_int_int() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructor_int_int_zeroDenominator() {
        new Fraction(1, 0);
    }

    @Test
    public void testFractionMethods() {
        Fraction fraction = new Fraction(1, 2);

        assertEquals(1.0 / 2.0, fraction.doubleValue(), 0.001);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        Fraction negativeFraction = fraction.negate();
        assertEquals(-1, negativeFraction.getNumerator());
        assertEquals(2, negativeFraction.getDenominator());

        Fraction reciprocalFraction = fraction.reciprocal();
        assertEquals(1.0 / 2.0, reciprocalFraction.doubleValue(), 0.001);
        assertEquals(2, reciprocalFraction.getNumerator());
        assertEquals(1, reciprocalFraction.getDenominator());

        Fraction sum = fraction.add(new Fraction(1, 3));
        assertEquals(3.0 / 6.0, sum.doubleValue(), 0.001);
        assertEquals(3, sum.getNumerator());
        assertEquals(6, sum.getDenominator());

        Fraction difference = fraction.subtract(new Fraction(1, 3));
        assertEquals(2.0 / 6.0, difference.doubleValue(), 0.001);
        assertEquals(2, difference.getNumerator());
        assertEquals(6, difference.getDenominator());

        Fraction product = fraction.multiply(new Fraction(1, 3));
        assertEquals(1.0 / 6.0, product.doubleValue(), 0.001);
        assertEquals(1, product.getNumerator());
        assertEquals(6, product.getDenominator());

        Fraction quotient = fraction.divide(new Fraction(1, 3));
        assertEquals(3.0 / 2.0, quotient.doubleValue(), 0.001);
        assertEquals(3, quotient.getNumerator());
        assertEquals(2, quotient.getDenominator());
    }

    @Test
    public void testFractionMethods_bigNumbers() {
        Fraction fraction1 = new Fraction(BigInteger.valueOf(12345678901234567890L), BigInteger.valueOf(23456789012345678901L));
        Fraction fraction2 = new Fraction(BigInteger.valueOf(34567890123456789012L), BigInteger.valueOf(45678901234567890123L));

        Fraction sum = fraction1.add(fraction2);
        assertEquals(BigInteger.valueOf(468116377785163777815L), sum.getNumerator());
        assertEquals(BigInteger.valueOf(679115283150213444813L), sum.getDenominator());

        Fraction product = fraction1.multiply(fraction2);
        assertEquals(BigInteger.valueOf(45250387401481574252936872462055057338662643585648802383083789427857L), product.getNumerator());
        assertEquals(BigInteger.valueOf(80305611836578892133366912722668894964533616506719333734687896619613L), product.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionMethod_maxIterations() throws FractionConversionException {
        new Fraction(Math.E, 10, 1, 5);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionMethod_maxDenominator() throws FractionConversionException {
        new Fraction(1.2, 1, Integer.MAX_VALUE, 1);
    }
}