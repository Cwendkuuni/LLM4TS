package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstruction() {
        Fraction fraction = new Fraction(1.0);
        assertEquals(1, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());

        fraction = new Fraction(2.5);
        assertEquals(2, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());

        fraction = new Fraction(-3.14);
        assertEquals(-3, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());

        fraction = new Fraction(0.12345);
        assertEquals(12345, fraction.getNumerator());
        assertEquals(100000, fraction.getDenominator());

        try {
            new Fraction(-1.0);
            fail();
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testFractionFromString() {
        Fraction fraction = Fraction.valueOf("1/2");
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        fraction = Fraction.valueOf("3/4");
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());

        fraction = Fraction.valueOf("-1/2");
        assertEquals(-1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        fraction = Fraction.valueOf("3.14/2.71828");
        assertEquals(314, fraction.getNumerator());
        assertEquals(271828, fraction.getDenominator());

        try {
            Fraction.valueOf("0/0");
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            Fraction.valueOf("3/0");
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            Fraction.valueOf("abc/xyz");
            fail();
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction abs = fraction.abs();
        assertEquals(3, abs.getNumerator());
        assertEquals(2, abs.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction fraction3 = new Fraction(1, 2);
        int result1 = fraction1.compareTo(fraction2);
        int result2 = fraction2.compareTo(fraction1);
        int result3 = fraction1.compareTo(fraction1);

        assertEquals(-1, result1);
        assertEquals(1, result2);
        assertEquals(0, result3);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(2, 3);
        double value = fraction.doubleValue();
        assertEquals(0.6666666666666666, value, 0.00000000000001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);
        Fraction fraction4 = new Fraction(3, 2);

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
        assertFalse(fraction1.equals(fraction4));
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        assertEquals(37 * (629 + fraction1.getNumerator()) + fraction1.getDenominator(), fraction1.hashCode());
        assertEquals(fraction1.hashCode(), fraction2.hashCode());
        assertNotEquals(fraction1.hashCode(), fraction3.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(2, 3);
        int value = fraction.intValue();
        assertEquals(2, value);
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(2, 3);
        long value = fraction.longValue();
        assertEquals(2, value);
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(3, 2);
        Fraction negate = fraction.negate();
        assertEquals(-3, negate.getNumerator());
        assertEquals(2, negate.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction reciprocal = fraction.reciprocal();
        assertEquals(3, reciprocal.getNumerator());
        assertEquals(2, reciprocal.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.add(fraction2);
        assertEquals(3, sum.getNumerator());
        assertEquals(6, sum.getDenominator());
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction sum = fraction.add(3);
        assertEquals(3, sum.getNumerator());
        assertEquals(2, sum.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction difference = fraction1.subtract(fraction2);
        assertEquals(1, difference.getNumerator());
        assertEquals(6, difference.getDenominator());
    }

    @Test
    public void testSubtractInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction difference = fraction.subtract(3);
        assertEquals(-1, difference.getNumerator());
        assertEquals(2, difference.getDenominator());
    }

    @Test
    public void testAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.addSub(fraction2, true);
        assertEquals(3, sum.getNumerator());
        assertEquals(6, sum.getDenominator());

        Fraction difference = fraction1.addSub(fraction2, false);
        assertEquals(-1, difference.getNumerator());
        assertEquals(5, difference.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(1, product.getNumerator());
        assertEquals(3, product.getDenominator());
    }

    @Test
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction product = fraction.multiply(3);
        assertEquals(3, product.getNumerator());
        assertEquals(2, product.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(3, quotient.getNumerator());
        assertEquals(2, quotient.getDenominator());
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction quotient = fraction.divide(3);
        assertEquals(-1, quotient.getNumerator());
        assertEquals(6, quotient.getDenominator());
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        double percentage = fraction.percentageValue();
        assertEquals(75.0, percentage, 0.01);
    }

    @Test
    public void testGetReducedFraction() {
        int numerator = -12345;
        int denominator = 12345;
        Fraction reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(1, reducedFraction.getNumerator());
        assertEquals(-12345, reducedFraction.getDenominator());

        numerator = 12345;
        denominator = -12345;
        reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(-1, reducedFraction.getNumerator());
        assertEquals(12345, reducedFraction.getDenominator());

        numerator = 12345;
        denominator = 67890;
        reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(12345, reducedFraction.getNumerator());
        assertEquals(67890, reducedFraction.getDenominator());

        numerator = 0;
        denominator = 1;
        reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(0, reducedFraction.getNumerator());
        assertEquals(1, reducedFraction.getDenominator());

        numerator = 1;
        denominator = 1;
        reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(1, reducedFraction.getNumerator());
        assertEquals(1, reducedFraction.getDenominator());

        numerator = 1;
        denominator = -1;
        reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(-1, reducedFraction.getNumerator());
        assertEquals(1, reducedFraction.getDenominator());

        numerator = Integer.MIN_VALUE;
        denominator = 1;
        reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(-2147483648, reducedFraction.getNumerator());
        assertEquals(1, reducedFraction.getDenominator());

        numerator = 1;
        denominator = Integer.MIN_VALUE;
        reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(1, reducedFraction.getNumerator());
        assertEquals(-2147483648, reducedFraction.getDenominator());
    }
}