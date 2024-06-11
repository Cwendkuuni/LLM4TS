import org.apache.commons.math3.exception.FractionConversionException;
import org.junit.Test;
import org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionConstructorWithDouble() {
        Fraction fraction = new Fraction(3.14);
        assertEquals(3, fraction.getNumerator());
        assertEquals(7, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructorWithDoubleAndEpsilonAndMaxIterations() {
        Fraction fraction = new Fraction(3.14, 0.001, 100);
        assertEquals(3, fraction.getNumerator());
        assertEquals(7, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructorWithDoubleAndMaxDenominator() {
        Fraction fraction = new Fraction(3.14, 10);
        assertEquals(3, fraction.getNumerator());
        assertEquals(7, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithOverflow() {
        try {
            new Fraction(1.0E1000);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected exception
        }
    }
}