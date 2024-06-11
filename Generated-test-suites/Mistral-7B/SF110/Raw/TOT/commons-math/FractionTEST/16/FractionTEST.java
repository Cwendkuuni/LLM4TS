import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testCreationFromDouble() {
        Fraction fr = new Fraction(1.5);
        assertEquals(1, fr.getNumerator());
        assertEquals(2, fr.getDenominator());

        try {
            new Fraction(-1.5);
            fail("Expected FractionConversionException for negative double");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(Double.POSITIVE_INFINITY);
            fail("Expected FractionConversionException for infinity");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(Double.NEGATIVE_INFINITY);
            fail("Expected FractionConversionException for negative infinity");
        } catch (FractionConversionException e) {
            // expected
        }

        try {
            new Fraction(0.0);
            fail("Expected FractionConversionException for zero");
        } catch (FractionConversionException e) {
            // expected
        }
    }

    @Test
    public void testCreationFromInts() {
        Fraction fr = new Fraction(2, 3);
        assertEquals(2, fr.getNumerator());
        assertEquals(3, fr.getDenominator());

        Fraction fr2 = new Fraction(0, 2);
        assertEquals(0, fr2.getNumerator());
        assertEquals(2, fr2.getDenominator());

        Fraction fr3 = new Fraction(-3, 2);
        assertEquals(-3, fr3.getNumerator());
        assertEquals(2, fr3.getDenominator());

        Fraction fr4 = new Fraction(1, 0);
        assertEquals(1, fr4.getNumerator());
        assertEquals(0, fr4.getDenominator());
        try {
            fr4.doubleValue();
            fail("Expected MathArithmeticException for division by zero");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testAbs() {
        Fraction fr = new Fraction(-3, 2);
        Fraction result = fr.abs();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fr1 = new Fraction(2, 3);
        Fraction fr2 = new Fraction(1, 2);
        Fraction fr3 = new Fraction(4, 3);

        int c1 = fr1.compareTo(fr2);
        assertEquals(1, c1);

        int c2 = fr2.compareTo(fr1);
        assertEquals(-1, c2);

        int c3 = fr1.compareTo(fr3);
        assertEquals(-1, c3);

        int c4 = fr3.compareTo(fr1);
        assertEquals(1, c4);
    }

    @Test
    public void testEquals() {
        Fraction fr1 = new Fraction(2, 3);
        Fraction fr2 = new Fraction(2, 3);
        Fraction fr3 = new Fraction(1, 2);
        Fraction fr4 = new Object();

        assertTrue(fr1.equals(fr1));
        assertTrue(fr1.equals(fr2));
        assertFalse(fr1.equals(fr3));
        assertFalse(fr1.equals(fr4));
    }

    @Test
    public void testHashCode() {
        Fraction fr1 = new Fraction(2, 3);
        Fraction fr2 = new Fraction(2, 3);
        Fraction fr3 = new Fraction(1, 2);

        int hc1 = fr1.hashCode();
        int hc2 = fr2.hashCode();
        int hc3 = fr3.hashCode();

        assertEquals(hc1, hc2);
        assertNotEquals(hc1, hc3);
    }

    @Test
    public void testIntValue() {
        Fraction fr = new Fraction(2, 3);
        int iv = fr.intValue();
        assertEquals(2, iv / 3);
    }

    @Test
    public void testLongValue() {
        Fraction fr = new Fraction(2, 3);
        long lv = fr.longValue();
        assertEquals(2, lv / 3);
    }

    @Test
    public void testFloatValue() {
        Fraction fr = new Fraction(2, 3);
        float fv = fr.floatValue();
        assertEquals(2.0f / 3.0f, fv, 0.001f);
    }

    @Test
    public void testDoubleValue() {
        Fraction fr = new Fraction(2, 3);
        double dv = fr.doubleValue();
        assertEquals(2.0 / 3.0, dv, 0.00001);
    }

    @Test
    public void testNegate() {
        Fraction fr = new Fraction(2, 3);
        Fraction result = fr.negate();
        assertEquals(-2, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction fr = new Fraction(2, 3);
        Fraction result = fr.reciprocal();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testReciprocalZeroDenominator() {
        Fraction fr = new Fraction(0, 1);
        fr.reciprocal();
    }

    @Test
    public void testAdd() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(3, 2);
        Fraction result = fr1.add(fr2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());

        Fraction fr3 = new Fraction(1, 2);
        Fraction fr4 = new Fraction(1, 2);
        Fraction result2 = fr3.add(fr4);
        assertEquals(2, result2.getNumerator());
        assertEquals(1, result2.getDenominator());

        Fraction fr5 = new Fraction(1, 2);
        Fraction fr6 = new Fraction(1, 1);
        Fraction result3 = fr5.add(fr6);
        assertEquals(2, result3.getNumerator());
        assertEquals(1, result3.getDenominator());

        Fraction fr7 = new Fraction(1, 2);
        Fraction fr8 = new Fraction(1, 1);
        Fraction result4 = fr7.add(fr8);
        assertEquals(3, result4.getNumerator());
        assertEquals(1, result4.getDenominator());
    }

    @Test
    public void testAddInt() {
        Fraction fr = new Fraction(1, 2);
        Fraction result = fr.add(3);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fr1 = new Fraction(3, 2);
        Fraction fr2 = new Fraction(1, 2);
        Fraction result = fr1.subtract(fr2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());

        Fraction fr3 = new Fraction(1, 2);
        Fraction fr4 = new Fraction(1, 2);
        Fraction result2 = fr3.subtract(fr4);
        assertEquals(0, result2.getNumerator());
        assertEquals(1, result2.getDenominator());

        Fraction fr5 = new Fraction(1, 2);
        Fraction fr6 = new Fraction(1, 1);
        Fraction result3 = fr5.subtract(fr6);
        assertEquals(-1, result3.getNumerator());
        assertEquals(1, result3.getDenominator());

        Fraction fr7 = new Fraction(1, 2);
        Fraction fr8 = new Fraction(1, 1);
        Fraction result4 = fr7.subtract(fr8);
        assertEquals(1, result4.getNumerator());
        assertEquals(1, result4.getDenominator());
    }

    @Test
    public void testSubtractInt() {
        Fraction fr = new Fraction(3, 2);
        Fraction result = fr.subtract(1);
        assertEquals(2, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(2, 3);
        Fraction result = fr1.multiply(fr2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());

        Fraction fr3 = new Fraction(1, 2);
        Fraction fr4 = new Fraction(1, 2);
        Fraction result2 = fr3.multiply(fr4);
        assertEquals(1, result2.getNumerator());
        assertEquals(1, result2.getDenominator());

        Fraction fr5 = new Fraction(1, 2);
        Fraction fr6 = new Fraction(3, 2);
        Fraction result3 = fr5.multiply(fr6);
        assertEquals(3, result3.getNumerator());
        assertEquals(2, result3.getDenominator());

        Fraction fr7 = new Fraction(1, 2);
        Fraction fr8 = new Fraction(2, 3);
        Fraction result4 = fr7.multiply(fr8);
        assertEquals(2, result4.getNumerator());
        assertEquals(3, result4.getDenominator());
    }

    @Test
    public void testMultiplyInt() {
        Fraction fr = new Fraction(1, 2);
        Fraction result = fr.multiply(2);
        assertEquals(2, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fr1 = new Fraction(3, 2);
        Fraction fr2 = new Fraction(1, 2);
        Fraction result = fr1.divide(fr2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());

        Fraction fr3 = new Fraction(1, 2);
        Fraction fr4 = new Fraction(1, 2);
        Fraction result2 = fr3.divide(fr4);
        assertEquals(1, result2.getNumerator());
        assertEquals(1, result2.getDenominator());

        Fraction fr5 = new Fraction(1, 2);
        Fraction fr6 = new Fraction(3, 2);
        Fraction result3 = fr5.divide(fr6);
        assertEquals(-1, result3.getNumerator());
        assertEquals(2, result3.getDenominator());

        Fraction fr7 = new Fraction(1, 2);
        Fraction fr8 = new Fraction(1, 3);
        Fraction result4 = fr7.divide(fr8);
        assertEquals(3, result4.getNumerator());
        assertEquals(2, result4.getDenominator());
    }

    @Test
    public void testDivideInt() {
        Fraction fr = new Fraction(3, 2);
        Fraction result = fr.divide(1);
        assertEquals(1.5, result.doubleValue(), 0.001);
    }

    @Test
    public void testPercentageValue() {
        Fraction fr = new Fraction(1, 2);
        assertEquals(50, fr.percentageValue());
    }
}