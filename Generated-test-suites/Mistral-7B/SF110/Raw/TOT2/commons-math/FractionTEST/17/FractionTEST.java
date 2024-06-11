import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.fraction.Fraction;

public class FractionTest {

    @Test
    public void testConstructorWithDouble() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testConstructorWithDoubleAndMaxDenominator() {
        Fraction fraction = new Fraction(1.5, 10);
        assertEquals(2, fraction.getDenominator());
        assertEquals(1, fraction.getNumerator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithNegativeValue() {
        new Fraction(-1.5);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithHugeValue() {
        new Fraction(Math.pow(2, 64) + 1.0);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithSmallEpsilon() {
        new Fraction(1.5, 1e-9, 100);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithHugeMaxIterations() {
        new Fraction(1.5, 1.0E-5, Integer.MIN_VALUE);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithHugeMaxDenominator() {
        new Fraction(1.5, 1.0E-5, Long.MAX_VALUE);
    }
}