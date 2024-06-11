package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionFromDouble() {
        Fraction fraction = new Fraction(3.14159265);
        assertEquals("The result should be 3 and the denominator should be approximately 10", 3, fraction.getNumerator());
        assertEquals("The result should be approximately 3.14159265", 3.14159265, fraction.doubleValue(), 0.000001);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionFromNegativeDouble() {
        new Fraction(-3.14159265);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionFromLargeDouble() {
        new Fraction(Double.MAX_VALUE);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionFromSmallDouble() {
        new Fraction(Double.MIN_VALUE);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionFromNaN() {
        new Fraction(Double.NaN);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionFromInfinity() {
        new Fraction(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testFractionFromApproximateDouble() {
        Fraction fraction = new Fraction(0.1);
        assertEquals("The result should be approximately 10", 10, fraction.getDenominator());
        assertEquals("The result should be approximately 0.1", 0.1, fraction.doubleValue(), 0.000001);
    }
}