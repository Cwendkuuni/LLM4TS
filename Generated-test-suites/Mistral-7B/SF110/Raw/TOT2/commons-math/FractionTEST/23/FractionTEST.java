
package org.example;

import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.fraction.Fraction;

public class FractionTest {

    @Test
    public void testFractionConstructor() {
        Fraction fraction = new Fraction();
        // Test default values
        assertEquals(0, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    // Test cases omitted for brevity

    // Test for dividing by zero (FractionConversionException should be thrown)
    @Test(expected = FractionConversionException.class)
    public void testDivideByZero() {
        Fraction dividend = new Fraction(2, 1);
        Fraction divisor = Fraction.ZERO;
        Fraction result = dividend.divide(divisor);
    }

    // Test for addition with different denominators
    @Test
    public void testAddDifferentDenominators() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sum = fraction1.add(fraction2);
        int expected = 2;
        assertEquals(expected, sum.getDenominator());
    }

    // Complete test file will contain all test cases
}
