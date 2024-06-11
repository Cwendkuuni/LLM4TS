import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionCreationFromDouble() {
        Fraction fr1 = new Fraction(1.5);
        Fraction fr2 = new Fraction(-3.25);
        Fraction fr3 = new Fraction(0.25);

        assertEquals("1.5", fr1.doubleValue(), 1.5, 0.001);
        assertEquals("-3.25", fr2.doubleValue(), -3.25, 0.001);
        assertEquals("0.25", fr3.doubleValue(), 0.25, 0.001);

        assertEquals("1/3", fr3.toString());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConversionFailure() {
        new Fraction(1.234567890123456789012345678901234567890123456789012345678901234567890123456789L);
    }

    @Test
    public void testFractionCreationFromInt() {
        Fraction fr1 = new Fraction(4);
        Fraction fr2 = new Fraction(-5);
        Fraction fr3 = new Fraction(1);

        assertEquals("4/1", fr1.toString());
        assertEquals("-5/1", fr2.toString());
        assertEquals("1/1", fr3.toString());
    }

    @Test
    public void testFractionCreationFromInvalidInput() {
        try {
            new Fraction("abc");
            fail("Expected FractionConversionException for String input");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            new Fraction(new Object());
            fail("Expected FractionConversionException for Object input");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testFractionComparison() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(2, 4);
        Fraction fr3 = new Fraction(1, 2);

        assertEquals(true, fr1.compareTo(fr3) == 0);
        assertEquals(true, fr1.compareTo(fr2) < 0);
    }

    @Test
    public void testFractionArithmetic() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(3, 4);

        Fraction sum = fr1.add(fr2);
        assertEquals("7/4", sum.toString());

        Fraction diff = fr1.subtract(fr2);
        assertEquals("-1/4", diff.toString());

        Fraction prod = fr1.multiply(fr2);
        assertEquals("3/8", prod.toString());

        Fraction quot = fr1.divide(fr2);
        assertEquals("3/2", quot.toString());
    }

    @Test
    public void testFractionAbs() {
        Fraction fr1 = new Fraction(-3, 4);
        Fraction fr2 = fr1.abs();

        assertEquals("3/4", fr2.toString());
    }

    @Test
    public void testFractionNegate() {
        Fraction fr1 = new Fraction(3, 4);
        Fraction fr2 = fr1.negate();

        assertEquals("-3/4", fr2.toString());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fr1 = new Fraction(2, 3);
        Fraction fr2 = fr1.reciprocal();

        assertEquals("3/2", fr2.toString());
    }
}