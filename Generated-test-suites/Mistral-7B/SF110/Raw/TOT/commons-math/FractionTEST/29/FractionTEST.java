import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructorDouble() {
        // Test valid input
        Fraction fr1 = new Fraction(1.5);
        assertEquals(1, fr1.getNumerator());
        assertEquals(2, fr1.getDenominator());

        // Test edge case: maximum denominator
        Fraction fr2 = new Fraction(Double.POSITIVE_INFINITY);
        assertEquals(0, fr2.getNumerator());
        assertEquals(1, fr2.getDenominator());

        // Test error case: negative value
        try {
            new Fraction(-1.5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorDoubleEpsilon() {
        // Test valid input
        Fraction fr1 = new Fraction(1.5, 0.001, 100);
        assertEquals(1, fr1.getNumerator());
        assertEquals(2, fr1.getDenominator());

        // Test error case: maximum iterations
        try {
            new Fraction(1.5, 0.001, 1);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        // Test error case: error tolerance is too small
        try {
            new Fraction(1.5, 0.0001, 100);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorDoubleMaxDenominator() {
        // Test valid input
        Fraction fr1 = new Fraction(1.5, 1000);
        assertEquals(1089, fr1.getNumerator());
        assertEquals(594000, fr1.getDenominator());

        // Test error case: denominator is too small
        try {
            new Fraction(1.5, 1);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorInt() {
        // Test valid input
        Fraction fr1 = new Fraction(3);
        assertEquals(3, fr1.getNumerator());
        assertEquals(1, fr1.getDenominator());

        // Test valid input with denominator
        Fraction fr2 = new Fraction(3, 2);
        assertEquals(3, fr2.getNumerator());
        assertEquals(2, fr2.getDenominator());

        // Test error case: zero denominator
        try {
            new Fraction(0);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        // Test error case: negative denominator
        try {
            new Fraction(3, -1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        // Test error case: overflow (minimum integer value and large denominator)
        try {
            new Fraction(Integer.MIN_VALUE, 2);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testAbs() {
        Fraction fr1 = new Fraction(-1, 2);
        Fraction fr2 = fr1.abs();
        assertEquals(1, fr2.getNumerator());
        assertEquals(2, fr2.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(2, 3);

        int result = fr1.compareTo(fr2);
        assertEquals(-1, result);

        result = fr2.compareTo(fr1);
        assertEquals(1, result);

        Fraction fr3 = new Fraction(1, 2);
        result = fr1.compareTo(fr3);
        assertEquals(0, result);

        result = fr3.compareTo(fr1);
        assertEquals(0, result);
    }

    @Test
    public void testDoubleValue() {
        Fraction fr1 = new Fraction(1, 2);
        double result = fr1.doubleValue();
        assertEquals(0.5, result, 0.001);
    }

    @Test
    public void testEquals() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 2);
        assertTrue(fr1.equals(fr2));

        Fraction fr3 = new Fraction(1, 3);
        assertFalse(fr1.equals(fr3));

        Fraction fr4 = new Fraction(1, 2);
        assertTrue(fr1.equals(fr4));
    }

    @Test
    public void testFloatValue() {
        Fraction fr1 = new Fraction(1, 2);
        float result = fr1.floatValue();
        assertEquals(0.5f, result, 0.01f);
    }

    @Test
    public void testGetDenominator() {
        Fraction fr1 = new Fraction(1, 2);
        assertEquals(2, fr1.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fr1 = new Fraction(1, 2);
        assertEquals(1, fr1.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 2);
        assertEquals(fr1.hashCode(), fr2.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fr1 = new Fraction(1, 2);
        int result = fr1.intValue();
        assertEquals(0, result);
    }

    @Test
    public void testLongValue() {
        Fraction fr1 = new Fraction(1, 2);
        long result = fr1.longValue();
        assertEquals(0L, result);
    }

    @Test
    public void testNegate() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = fr1.negate();
        assertEquals(-1, fr2.getNumerator());
        assertEquals(2, fr2.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = fr1.reciprocal();
        assertEquals(2, fr2.getNumerator());
        assertEquals(1, fr2.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 3);
        Fraction sum = fr1.add(fr2);
        assertEquals(3, sum.getNumerator());
        assertEquals(6, sum.getDenominator());
    }

    @Test
    public void testAddInt() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction sum = fr1.add(2);
        assertEquals(3, sum.getNumerator());
        assertEquals(2, sum.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 3);
        Fraction difference = fr1.subtract(fr2);
        assertEquals(1, difference.getNumerator());
        assertEquals(6, difference.getDenominator());
    }

    @Test
    public void testSubtractInt() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction difference = fr1.subtract(2);
        assertEquals(-1, difference.getNumerator());
        assertEquals(2, difference.getDenominator());
    }

    @Test
    public void testAddSub() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 3);

        Fraction sum = fr1.addSub(fr2, true);
        assertEquals(3, sum.getNumerator());
        assertEquals(6, sum.getDenominator());

        Fraction difference = fr1.addSub(fr2, false);
        assertEquals(-1, difference.getNumerator());
        assertEquals(5, difference.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 3);
        Fraction product = fr1.multiply(fr2);
        assertEquals(1, product.getNumerator());
        assertEquals(6, product.getDenominator());
    }

    @Test
    public void testMultiplyInt() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction product = fr1.multiply(3);
        assertEquals(3, product.getNumerator());
        assertEquals(2, product.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 3);
        Fraction quotient = fr1.divide(fr2);
        assertEquals(3, quotient.getNumerator());
        assertEquals(6, quotient.getDenominator());
    }

    @Test
    public void testDivideInt() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction quotient = fr1.divide(3);
        assertEquals(-1, quotient.getNumerator());
        assertEquals(6, quotient.getDenominator());
    }

    @Test
    public void testPercentageValue() {
        Fraction fr1 = new Fraction(1, 2);
        double result = fr1.percentageValue();
        assertEquals(50.0, result, 0.01);
    }

    @Test
    public void testGetReducedFraction() {
        int numerator = 21;
        int denominator = 22;
        Fraction reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(1, reducedFraction.getNumerator());
        assertEquals(22, reducedFraction.getDenominator());
    }
}