package org.junit.tests;

import static org.apache.commons.math3.util.Precision.equals;
import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.BigInteger;

public class FractionTest {

    @org.junit.Test
    public void testFractionConstructionWithDouble() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        try {
            new Fraction(-1.5);
            fail("Expected FractionConversionException for negative double");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(1.5, 0.0);
            fail("Expected FractionConversionException for division by zero");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testFractionConstructionWithDoubleAndParameters() {
        Fraction fraction = new Fraction(1.5, 1e-6, Integer.MAX_VALUE, 100);
        assertEquals(1, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());

        Fraction fractionWithOverflow = new Fraction(1.5, 1e-6, Integer.MIN_VALUE, 100);
        assertEquals(1, fractionWithOverflow.getNumerator());
        assertEquals(2, fractionWithOverflow.getDenominator());
        assertEquals("1/2", fractionWithOverflow.toString());
    }

    @org.junit.Test
    public void testFractionConstructionWithDoubleAndMaxDenominator() {
        Fraction fraction = new Fraction(1.5, Integer.MAX_VALUE);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        Fraction fractionWithMaxIterations = new Fraction(1.5, 100);
        assertEquals(1, fractionWithMaxIterations.getNumerator());
        assertEquals(3, fractionWithMaxIterations.getDenominator());
    }

    @org.junit.Test
    public void testFractionConstructionWithInt() {
        Fraction fraction = new Fraction(3);
        assertEquals(3, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());

        Fraction fractionWithDenominator = new Fraction(3, 4);
        assertEquals(3, fractionWithDenominator.getNumerator());
        assertEquals(4, fractionWithDenominator.getDenominator());

        try {
            new Fraction(Integer.MIN_VALUE);
            fail("Expected MathArithmeticException for negative numerator");
        } catch (MathArithmeticException e) {
            // expected
        }

        try {
            new Fraction(Integer.MIN_VALUE, Integer.MIN_VALUE);
            fail("Expected MathArithmeticException for negative numerator and denominator");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction absFraction = fraction.abs();
        assertEquals(3, absFraction.getNumerator());
        assertEquals(2, absFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction fraction3 = new Fraction(3, 2);

        int comparisonResult1 = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult1);

        int comparisonResult2 = fraction2.compareTo(fraction1);
        assertEquals(1, comparisonResult2);

        int comparisonResult3 = fraction1.compareTo(fraction3);
        assertEquals(0, comparisonResult3);
    }

    @org.junit.Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1.5, fraction.doubleValue(), 0.001);
    }

    @org.junit.Test
    public void testFractionEquals() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(3, 2);
        Fraction fraction3 = new Fraction(4, 2);

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @org.junit.Test
    public void testFractionFloatValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals((float) 1.5f, fraction.floatValue(), 0.001f);
    }

    @org.junit.Test
    public void testFractionGetDenominator() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionGetNumerator() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(3, fraction.getNumerator());
    }

    @org.junit.Test
    public void testFractionHashCode() {
        Fraction fraction = new Fraction(3, 2);
        int hashCode = fraction.hashCode();
        int expectedHashCode = 37 * (629 + 3) + 2;
        assertEquals(expectedHashCode, hashCode);
    }

    @org.junit.Test
    public void testFractionIntValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1, fraction.intValue());
    }

    @org.junit.Test
    public void testFractionLongValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(1L, fraction.longValue());
    }

    @org.junit.Test
    public void testFractionNegate() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction negateFraction = fraction.negate();
        assertEquals(3, negateFraction.getNumerator());
        assertEquals(2, negateFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(3, 2);
        Fraction reciprocalFraction = fraction.reciprocal();
        assertEquals(2, reciprocalFraction.getNumerator());
        assertEquals(3, reciprocalFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction sumFraction = fraction1.add(fraction2);
        assertEquals(5, sumFraction.getNumerator());
        assertEquals(4, sumFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionAddInt() {
        Fraction fraction = new Fraction(3, 2);
        Fraction sumFraction = fraction.add(1);
        assertEquals(4, sumFraction.getNumerator());
        assertEquals(2, sumFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction differenceFraction = fraction1.subtract(fraction2);
        assertEquals(-2, differenceFraction.getNumerator());
        assertEquals(6, differenceFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionSubtractInt() {
        Fraction fraction = new Fraction(3, 2);
        Fraction differenceFraction = fraction.subtract(1);
        assertEquals(2, differenceFraction.getNumerator());
        assertEquals(2, differenceFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction sumFraction = fraction1.addSub(fraction2, true);
        assertEquals(5, sumFraction.getNumerator());
        assertEquals(4, sumFraction.getDenominator());

        Fraction differenceFraction = fraction1.addSub(fraction2, false);
        assertEquals(-2, differenceFraction.getNumerator());
        assertEquals(6, differenceFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction productFraction = fraction1.multiply(fraction2);
        assertEquals(3, productFraction.getNumerator());
        assertEquals(4, productFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionMultiplyInt() {
        Fraction fraction = new Fraction(3, 2);
        Fraction productFraction = fraction.multiply(2);
        assertEquals(6, productFraction.getNumerator());
        assertEquals(2, productFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction quotientFraction = fraction1.divide(fraction2);
        assertEquals(1, quotientFraction.getNumerator());
        assertEquals(6, quotientFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionDivideInt() {
        Fraction fraction = new Fraction(3, 2);
        Fraction quotientFraction = fraction.divide(2);
        assertEquals(3, quotientFraction.getNumerator());
        assertEquals(4, quotientFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionPercentageValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals(150.0, fraction.percentageValue(), 0.01);
    }

    @org.junit.Test
    public void testFractionGetReducedFraction() {
        int numerator = 21;
        int denominator = 13;
        Fraction reducedFraction = Fraction.getReducedFraction(numerator, denominator);

        assertEquals(3, reducedFraction.getNumerator());
        assertEquals(7, reducedFraction.getDenominator());
    }

    @org.junit.Test
    public void testFractionToString() {
        Fraction fraction = new Fraction(-3, 2);
        assertEquals("-3/2", fraction.toString());
    }

    @org.junit.Test
    public void testFractionGetField() {
        Fraction fraction = new Fraction(3, 2);
        FractionField field = fraction.getField();
        assertEquals("org.apache.commons.math3.fraction.FractionField", field.getClass().getName());
    }

    @org.junit.Test(expected = NullArgumentException.class)
    public void testFractionAddNull() {
        Fraction fraction = new Fraction(3, 2);
        Fraction nullFraction = null;
        Fraction sumFraction = fraction.add(nullFraction);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testFractionDivideByZero() {
        Fraction fraction = new Fraction(3, 2);
        Fraction zeroFraction = new Fraction(0, 1);
        Fraction quotientFraction = fraction.divide(zeroFraction);
    }

    @org.junit.Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithDoubleOverflow() {
        Fraction fraction = new Fraction(1e201, 1);
    }

    @org.junit.Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithIntNegativeNumeratorAndDenominator() {
        Fraction fraction = new Fraction(-3, -2);
    }

    @org.junit.Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithIntOverflow() {
        Fraction fraction = new Fraction(Integer.MAX_VALUE + 1, 1);
    }

    @org.junit.Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithIntDenominatorZero() {
        Fraction fraction = new Fraction(3, 0);
    }

    @org.junit.Test(expected = FractionConversionException.class)
    public void testFractionConstructionWithIntNumeratorZeroAndDenominatorZero() {
        Fraction fraction = new Fraction(0, 0);
    }

    @org.junit.Test(expected = ArithmeticException.class)
    public void testFractionConstructionWithBigIntegerValues() {
        BigInteger bigNumerator = BigInteger.valueOf(2147483648L);
        BigInteger bigDenominator = BigInteger.valueOf(5);
        Fraction fraction = new Fraction(bigNumerator, bigDenominator);
    }
}