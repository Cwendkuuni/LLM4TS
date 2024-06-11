import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.*;
import org.junit.*;

public class FractionTest {
    @Test
    public void testEmptyConstructor() {
        Fraction fraction = new Fraction();
        assertEquals(0, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testDoubleValueConstructorWithNegativeValue() {
        new Fraction(-3.4);
    }

    @Test(expected = FractionConversionException.class)
    public void testDoubleValueConstructorWithLargeDenominator() {
        new Fraction(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testIntConstructor() {
        Fraction fraction = new Fraction(3);
        assertEquals(3, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testIntConstructorWithDenominator() {
        Fraction fraction = new Fraction(4, 5);
        assertEquals(4, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testIntConstructorWithZeroDenominator() {
        new Fraction(3, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDoubleValueMethodWithNegativeValue() {
        Fraction fraction = new Fraction(new Fraction(-3.4));
        assertNotEquals(-3.4, fraction.doubleValue(), 0.001);
    }

    @Test
    public void testEqualsMethodWithSameFraction() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(3, 4);
        assertTrue(fraction1.equals(fraction2));
    }

    @Test
    public void testEqualsMethodWithDifferentClass() {
        Fraction fraction = new Fraction(3, 4);
        assertFalse(fraction.equals("not a Fraction"));
    }

    @Test
    public void testEqualsMethodWithDifferentFraction() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(2, 4);
        assertFalse(fraction1.equals(fraction2));
    }

    @Test
    public void testGetDenominatorMethod() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testGetNumeratorMethod() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
    }

    @Test
    public void testHashCodeMethod() {
        Fraction fraction = new Fraction(3, 4);
        int expectedHashCode = 481339246;
        int calculatedHashCode = fraction.hashCode();
        assertEquals(expectedHashCode, calculatedHashCode);
    }

    @Test
    public void testIntValueMethod() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(-1, fraction.intValue());
    }

    @Test
    public void testLongValueMethod() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(-1, fraction.longValue());
    }

    @Test
    public void testFloatValueMethod() {
        Fraction fraction = new Fraction(3, 4);
        assertNotSame(3.0f, fraction.floatValue(), 0.001f);
    }

    @Test
    public void testDoubleValueMethod() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(0.75, fraction.doubleValue(), 0.001);
    }

    @Test(expected = FractionConversionException.class)
    public void testAddMethodWithNullFraction() {
        new Fraction(1).add(null);
    }

    @Test
    public void testAddMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = new Fraction(2, 6);
        Fraction result = fraction1.add(fraction2);
        assertEquals(sum, result);
    }

    @Test
    public void testAddMethodWithInt() {
        Fraction fraction = new Fraction(2, 3);
        Fraction sum = new Fraction(8, 3);
        Fraction result = fraction.add(3);
        assertEquals(sum, result);
    }

    @Test
    public void testSubtractMethod() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 4);
        Fraction difference = new Fraction(2, 4);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(difference, result);
    }

    @Test
    public void testSubtractMethodWithInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction difference = new Fraction(1, 4);
        Fraction result = fraction.subtract(1);
        assertEquals(difference, result);
    }

    @Test
    public void testReciprocalMethod() {
        Fraction fraction = new Fraction(2, 3);
        Fraction reciprocal = new Fraction(3, 2);
        Fraction result = fraction.reciprocal();
        assertEquals(reciprocal, result);
    }

    @Test(expected = FractionConversionException.class)
    public void testAddSubMethodWithNullFraction() {
        new Fraction(1).addSub(null, true);
    }

    @Test
    public void testAddSubMethod() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 4);
        Fraction sum = new Fraction(7, 12);
        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(sum, result);
    }

    @Test
    public void testAddSubMethodWithFalse() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 4);
        Fraction difference = new Fraction(-1, 12);
        Fraction result = fraction1.addSub(fraction2, false);
        assertEquals(difference, result);
    }

    @Test
    public void testMultiplyMethodWithNullFraction() {
        new Fraction(1).multiply(null);
    }

    @Test
    public void testMultiplyMethod() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction product = new Fraction(3, 8);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(product, result);
    }

    @Test
    public void testMultiplyMethodWithInt() {
        Fraction fraction = new Fraction(2, 3);
        Fraction product = new Fraction(6, 3);
        Fraction result = fraction.multiply(2);
        assertEquals(product, result);
    }

    @Test
    public void testDivideMethodWithNullFraction() {
        new Fraction(1).divide(null);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideMethodWithZeroNumerator() {
        new Fraction(0).divide(new Fraction(3, 4));
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideMethodWithZeroDenominator() {
        new Fraction(3, 4).divide(new Fraction(0));
    }

    @Test
    public void testDivideMethod() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction quotient = new Fraction(3, 2);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(quotient, result);
    }

    @Test
    public void testDivideMethodWithInt() {
        Fraction fraction = new Fraction(3, 4);
        Fraction quotient = new Fraction(3, 2);
        Fraction result = fraction.divide(1);
        assertEquals(quotient, result);
    }

    @Test
    public void testPercentageValueMethod() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(75, fraction.percentageValue(), 0.01);
    }
}