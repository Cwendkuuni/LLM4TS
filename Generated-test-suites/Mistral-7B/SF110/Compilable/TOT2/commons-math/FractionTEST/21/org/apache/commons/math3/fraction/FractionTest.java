package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.Fraction;

public class FractionTest {
    @Test
    public void testFractionConstructor_double() {
        // Test with a positive value
        Fraction fraction1 = new Fraction(2.5);
        assertEquals(2, fraction1.getNumerator());
        assertEquals(5, fraction1.getDenominator());
        assertEquals(1.0 / 2.0, fraction1.doubleValue(), 0.001);

        // Test with a negative value
        Fraction fraction2 = new Fraction(-3.25);
        assertEquals(-3, fraction2.getNumerator());
        assertEquals(5, fraction2.getDenominator());
        assertEquals(-0.65, fraction2.doubleValue(), 0.001);

        // Test with a zero value
        Fraction fraction3 = new Fraction(0.0);
        assertEquals(0, fraction3.getNumerator());
        assertEquals(1, fraction3.getDenominator());
        assertEquals(0.0, fraction3.doubleValue());

        // Test with a value larger than the maximum representable value
        try {
            new Fraction(Double.MAX_VALUE);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }
}