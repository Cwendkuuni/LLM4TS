import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testDoubleConstructor() {
        Fraction fraction = new Fraction(2.5);
        assertEquals(5, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        fraction = new Fraction(0.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        try {
            new Fraction(-3.5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(Double.POSITIVE_INFINITY);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(Double.NEGATIVE_INFINITY);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testDoubleConstructorWithEpsilon() {
        Fraction fraction = new Fraction(2.5, 0.0001, 100);
        assertEquals(5, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        Fraction fraction1 = new Fraction(1.5, 0.001, 100);
        assertEquals(3, fraction1.getNumerator());
        assertEquals(2, fraction1.getDenominator());

        Fraction fraction2 = new Fraction(1.5);
        assertEquals(3, fraction2.getNumerator());
        assertEquals(2, fraction2.getDenominator());

        Fraction fraction3 = new Fraction(1.5, 0.001);
        assertEquals(3, fraction3.getNumerator());
        assertEquals(2, fraction3.getDenominator());

        try {
            new Fraction(2.5, 0.0, 100);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(2.5, 0.0, 1);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(2.5, 1.0 / 0.0, 100);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
    }
}