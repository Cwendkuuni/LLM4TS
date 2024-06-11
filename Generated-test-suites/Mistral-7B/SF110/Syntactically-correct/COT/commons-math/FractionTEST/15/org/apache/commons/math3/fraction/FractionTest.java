import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructorWithDouble() throws Exception {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        assertEquals(0.75, fraction.doubleValue(), 0.001);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleThrowsException() {
        new Fraction(-1.5);
    }

    @Test
    public void testFractionConstructorsWithInts() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
        assertEquals(2.0 / 3.0, fraction.doubleValue(), 0.001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorsWithZeroDenominatorThrowsException() {
        new Fraction(1, 0);
    }

    @Test
    public void testAbsMethod() {
        Fraction fraction = new Fraction(1, -2);
        Fraction absFraction = fraction.abs();
        assertEquals(1, absFraction.getNumerator());
        assertEquals(2, absFraction.getDenominator());
    }

    @Test
    public void testCompareToMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        assertEquals(-1, fraction1.compareTo(fraction2));
        Fraction fraction3 = new Fraction(1, 2);
        assertEquals(0, fraction1.compareTo(fraction3));
        Fraction fraction4 = new Fraction(1, 1);
        assertEquals(1, fraction1.compareTo(fraction4));
    }

    @Test
    public void testDoubleValueMethod() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5, fraction.doubleValue(), 0.001);
    }

    @Test
    public void testEqualsMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        assertTrue(fraction1.equals(fraction2));
        Fraction fraction3 = new Fraction(1, 3);
        assertFalse(fraction1.equals(fraction3));
    }

    @Test
    public void testGetDenominatorMethod() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetNumeratorMethod() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testHashCodeMethod() {
        Fraction fraction = new Fraction(1, 2);
        int hashCode = fraction.hashCode();
        Fraction fraction1 = new Fraction(1, 2);
        assertEquals(hashCode, fraction1.hashCode());
        Fraction fraction2 = new Fraction(1, 3);
        assertNotEquals(hashCode, fraction2.hashCode());
    }

    @Test
    public void testIntValueMethod() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.intValue());
    }

    @Test
    public void testLongValueMethod() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.longValue());
    }

    @Test
    public void testNegateMethod() {
        Fraction fraction = new Fraction(1, -2);
        Fraction negateFraction = fraction.negate();
        assertEquals(-1, negateFraction.getNumerator());
        assertEquals(2, negateFraction.getDenominator());
    }

    @Test
    public void testReciprocalMethod() {
        Fraction fraction = new Fraction(1, 2);
        Fraction reciprocalFraction = fraction.reciprocal();
        assertEquals(2, reciprocalFraction.getNumerator());
        assertEquals(1, reciprocalFraction.getDenominator());
    }

    @Test
    public void testAddMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.add(fraction2);
        assertEquals(3, sum.getNumerator());
        assertEquals(6, sum.getDenominator());
    }

    @Test
    public void testSubtractMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction difference = fraction1.subtract(fraction2);
        assertEquals(1, difference.getNumerator());
        assertEquals(-1, difference.getDenominator());
    }

    @Test
    public void testAddSubMethodWithBooleanTrue() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.addSub(fraction2, true);
        assertEquals(3, sum.getNumerator());
        assertEquals(6, sum.getDenominator());
    }

    @Test
    public void testAddSubMethodWithBooleanFalse() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction difference = fraction1.addSub(fraction2, false);
        assertEquals(-1, difference.getNumerator());
        assertEquals(6, difference.getDenominator());
    }

    @Test
    public void testMultiplyMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(1, product.getNumerator());
        assertEquals(6, product.getDenominator());
    }

    @Test
    public void testDivideMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(3, quotient.getNumerator());
        assertEquals(2, quotient.getDenominator());
    }

    @Test
    public void testGetReducedFractionStaticMethod() {
        int numerator = 12;
        int denominator = 15;
        Fraction reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(2, reducedFraction.getNumerator());
        assertEquals(3, reducedFraction.getDenominator());
    }

    @Test
    public void testToStringMethod() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals("0.5", fraction.toString());
    }
}