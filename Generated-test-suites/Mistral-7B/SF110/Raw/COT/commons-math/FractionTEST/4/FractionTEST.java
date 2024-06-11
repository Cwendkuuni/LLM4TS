package org.junit.tests.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.exception.FractionConversionException;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructors() {
        Fraction fraction1 = new Fraction(2.0);
        Fraction fraction2 = new Fraction(2.0, 1.0E-5, 100);
        Fraction fraction3 = new Fraction(2.0, 10);
        Fraction fraction4 = new Fraction(1);
        Fraction fraction5 = new Fraction(1, 2);
        Fraction fraction6 = new Fraction(1, 4);
        Fraction fraction7 = new Fraction(1, 3);

        assertEquals(2, fraction1.getNumerator());
        assertEquals(1, fraction1.getDenominator());

        assertEquals(2, fraction2.getNumerator());
        assertEquals(1, fraction2.getDenominator());

        assertEquals(2, fraction3.getNumerator());
        assertEquals(10, fraction3.getDenominator());

        assertEquals(1, fraction4.getNumerator());
        assertEquals(1, fraction4.getDenominator());

        assertEquals(1, fraction5.getNumerator());
        assertEquals(2, fraction5.getDenominator());

        assertEquals(1, fraction6.getNumerator());
        assertEquals(4, fraction6.getDenominator());

        assertEquals(1, fraction7.getNumerator());
        assertEquals(3, fraction7.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithOverflow() {
        new Fraction(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetters() {
        Fraction fraction = new Fraction(1, 2);

        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        Fraction fractionAbs = fraction.abs();
        assertEquals(1, fractionAbs.getNumerator());
        assertEquals(2, fractionAbs.getDenominator());

        assertEquals(1, fraction.compareTo(new Fraction(1, 2)));
        assertEquals(-1, fraction.compareTo(new Fraction(2, 1)));
        assertEquals(0, fraction.compareTo(new Fraction(1, 1)));

        assertEquals(0.5, fraction.doubleValue(), 0.001);

        Fraction fractionNegative = fraction.negate();
        assertEquals(-1, fractionNegative.getNumerator());
        assertEquals(2, fractionNegative.getDenominator());

        Fraction fractionReciprocal = fraction.reciprocal();
        assertEquals(2, fractionReciprocal.getNumerator());
        assertEquals(1, fractionReciprocal.getDenominator());

        Fraction fractionAdd = fraction.add(new Fraction(1, 1));
        assertEquals(3, fractionAdd.getNumerator());
        assertEquals(2, fractionAdd.getDenominator());

        Fraction fractionSub = fraction.subtract(new Fraction(1, 1));
        assertEquals(2, fractionSub.getNumerator());
        assertEquals(2, fractionSub.getDenominator());

        Fraction fractionMultiply = fraction.multiply(new Fraction(1, 2));
        assertEquals(1, fractionMultiply.getNumerator());
        assertEquals(2, fractionMultiply.getDenominator());

        Fraction fractionDivide = fraction.divide(new Fraction(1, 2));
        assertEquals(2, fractionDivide.getNumerator());
        assertEquals(1, fractionDivide.getDenominator());

        assertEquals(200.0, fraction.percentageValue(), 0.01);

        assertEquals(Fraction.getInstance(), fraction.getField());
    }

    @Test
    public void testEdgeCases() {
        Fraction fractionMinusOne = new Fraction(-1, 1);
        Fraction fractionZero = new Fraction(0, 1);

        assertEquals(-1, fractionMinusOne.getNumerator());
        assertEquals(1, fractionMinusOne.getDenominator());

        assertEquals(0, fractionZero.getNumerator());
        assertEquals(1, fractionZero.getDenominator());

        Fraction fractionOneOverMaxInt = new Fraction(1, Integer.MAX_VALUE);
        assertEquals(1, fractionOneOverMaxInt.getNumerator());
        assertEquals(Integer.MAX_VALUE, fractionOneOverMaxInt.getDenominator());

        Fraction fractionNegativeOverMaxInt = new Fraction(-1, Integer.MAX_VALUE);
        assertEquals(-1, fractionNegativeOverMaxInt.getNumerator());
        assertEquals(Integer.MAX_VALUE, fractionNegativeOverMaxInt.getDenominator());

        Fraction fractionMaxIntOverMaxInt = new Fraction(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, fractionMaxIntOverMaxInt.getNumerator());
        assertEquals(Integer.MAX_VALUE, fractionMaxIntOverMaxInt.getDenominator());

        Fraction fractionMinusMaxInt = new Fraction(-Integer.MAX_VALUE, 1);
        assertEquals(-Integer.MAX_VALUE, fractionMinusMaxInt.getNumerator());
        assertEquals(1, fractionMinusMaxInt.getDenominator());

        Fraction fractionMaxIntMinusOne = new Fraction(Integer.MAX_VALUE - 1, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE - 1, fractionMaxIntMinusOne.getNumerator());
        assertEquals(Integer.MAX_VALUE, fractionMaxIntMinusOne.getDenominator());

        Fraction fractionMaxIntOverMinusOne = new Fraction(Integer.MAX_VALUE, -1);
        assertEquals(Integer.MAX_VALUE, fractionMaxIntOverMinusOne.getNumerator());
        assertEquals(-1, fractionMaxIntOverMinusOne.getDenominator());
    }
}