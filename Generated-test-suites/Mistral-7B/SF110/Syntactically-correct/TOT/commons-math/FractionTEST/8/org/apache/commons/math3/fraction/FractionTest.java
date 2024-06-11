import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionConstructorDouble() {
        // Construct Fraction from double value
        Fraction fraction = new Fraction(2.5);
        assertEquals(5, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        // Test with negative double value
        fraction = new Fraction(-2.5);
        assertEquals(-5, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        // Test with small epsilon value
        fraction = new Fraction(0.123456789, 1e-10);
        assertEquals(123456789, fraction.getNumerator());
        assertEquals(100000000, fraction.getDenominator());

        // Test with large maxIterations value
        try {
            fraction = new Fraction(Math.PI, 100000);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        // Test with maxDenominator value
        try {
            fraction = new Fraction(2.5, 1, 10);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructorInt() {
        // Construct Fraction from int value
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());

        // Construct Fraction from int num and den
        fraction = new Fraction(4, 2);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());

        // Test with negative num and den
        try {
            new Fraction(-4, -2);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            new Fraction(Integer.MIN_VALUE, Integer.MIN_VALUE);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testFractionMethods() {
        Fraction fraction = new Fraction(3, 2);

        assertEquals(1.5, fraction.doubleValue(), 0.001);
        assertEquals(-1, fraction.compareTo(new Fraction(4, 2)));
        assertEquals(-3, fraction.compareTo(new Fraction(3, 2)));
        assertEquals(3, fraction.compareTo(new Fraction(3, 2)));
        assertEquals("3/2", fraction.toString());

        // Test abs()
        Fraction absFraction = fraction.abs();
        assertEquals(3, absFraction.getNumerator());
        assertEquals(2, absFraction.getDenominator());

        // Test negate()
        Fraction negateFraction = fraction.negate();
        assertEquals(-3, negateFraction.getNumerator());
        assertEquals(2, negateFraction.getDenominator());

        // Test reciprocal()
        Fraction reciprocalFraction = fraction.reciprocal();
        assertEquals(2, reciprocalFraction.getNumerator());
        assertEquals(3, reciprocalFraction.getDenominator());

        // Test add()
        Fraction sumFraction = fraction.add(new Fraction(1, 2));
        assertEquals(7, sumFraction.getNumerator());
        assertEquals(3, sumFraction.getDenominator());

        // Test subtract()
        Fraction diffFraction = fraction.subtract(new Fraction(1, 2));
        assertEquals(2, diffFraction.getNumerator());
        assertEquals(3, diffFraction.getDenominator());

        // Test addSub()
        Fraction sumSubFraction = fraction.addSub(new Fraction(1, 2), true);
        assertEquals(7, sumSubFraction.getNumerator());
        assertEquals(3, sumSubFraction.getDenominator());

        Fraction diffSubFraction = fraction.addSub(new Fraction(1, 2), false);
        assertEquals(2, diffSubFraction.getNumerator());
        assertEquals(3, diffSubFraction.getDenominator());

        // Test multiply()
        Fraction productFraction = fraction.multiply(new Fraction(2, 3));
        assertEquals(6, productFraction.getNumerator());
        assertEquals(6, productFraction.getDenominator());

        // Test divide()
        Fraction quotientFraction = fraction.divide(new Fraction(3, 2));
        assertEquals(3, quotientFraction.getNumerator());
        assertEquals(2, quotientFraction.getDenominator());
    }

    @Test
    public void testFractionConstants() {
        Fraction two = Fraction.TWO;
        Fraction one = Fraction.ONE;
        Fraction zero = Fraction.ZERO;
        Fraction fourFifths = Fraction.FOUR_FIFTHS;
        Fraction oneFifth = Fraction.ONE_FIFTH;
        Fraction oneHalf = Fraction.ONE_HALF;
        Fraction oneQuarter = Fraction.ONE_QUARTER;
        Fraction oneThird = Fraction.ONE_THIRD;
        Fraction threeFifths = Fraction.THREE_FIFTHS;
        Fraction threeQuarters = Fraction.THREE_QUARTERS;
        Fraction twoFifths = Fraction.TWO_FIFTHS;
        Fraction twoQuarters = Fraction.TWO_QUARTERS;
        Fraction twoThirds = Fraction.TWO_THIRDS;
        Fraction minusOne = Fraction.MINUS_ONE;

        assertEquals(2, two.getNumerator());
        assertEquals(1, two.getDenominator());
        assertEquals(1, one.getNumerator());
        assertEquals(1, one.getDenominator());
        assertEquals(0, zero.getNumerator());
        assertEquals(1, zero.getDenominator());
        assertEquals(4, fourFifths.getNumerator());
        assertEquals(5, fourFifths.getDenominator());
        assertEquals(1, oneFifth.getNumerator());
        assertEquals(5, oneFifth.getDenominator());
        assertEquals(1, oneHalf.getNumerator());
        assertEquals(2, oneHalf.getDenominator());
        assertEquals(1, oneQuarter.getNumerator());
        assertEquals(4, oneQuarter.getDenominator());
        assertEquals(1, oneThird.getNumerator());
        assertEquals(3, oneThird.getDenominator());
        assertEquals(3, threeFifths.getNumerator());
        assertEquals(5, threeFifths.getDenominator());
        assertEquals(3, threeQuarters.getNumerator());
        assertEquals(4, threeQuarters.getDenominator());
        assertEquals(2, twoFifths.getNumerator());
        assertEquals(5, twoFifths.getDenominator());
        assertEquals(2, twoQuarters.getNumerator());
        assertEquals(4, twoQuarters.getDenominator());
        assertEquals(-1, minusOne.getNumerator());
        assertEquals(1, minusOne.getDenominator());
    }

    @Test
    public void testFractionConversionException() {
        try {
            new Fraction(Double.POSITIVE_INFINITY);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            new Fraction(Double.NEGATIVE_INFINITY);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            new Fraction("asdf");
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            new Fraction(new Object());
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }
}