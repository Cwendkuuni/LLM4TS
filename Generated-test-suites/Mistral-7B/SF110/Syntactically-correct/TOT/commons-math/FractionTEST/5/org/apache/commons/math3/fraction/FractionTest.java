import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.fraction.Fraction;

public class FractionTest {

    @Test
    public void testFractionConstructionWithDouble() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        
        fraction = new Fraction(-1.5);
        assertEquals(-1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        
        try {
            new Fraction(-2.5);
            fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // expected
        }
        
        try {
            new Fraction(2.0);
            Fraction expected = new Fraction(2, 1);
            assertEquals(expected, new Fraction(2.0));
        } catch (FractionConversionException e) {
            fail("Unexpected FractionConversionException");
        }
        
        try {
            new Fraction(0.0);
            assertEquals(0, new Fraction(0.0).getNumerator());
            assertEquals(1, new Fraction(0.0).getDenominator());
        } catch (FractionConversionException e) {
            fail("Unexpected FractionConversionException");
        }
    }

    @Test
    public void testFractionConstructionWithInts() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
        
        fraction = new Fraction(-2, 3);
        assertEquals(-2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
        
        try {
            new Fraction(-2, 0);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // expected
        }
        
        Fraction zero = new Fraction(0, 1);
        assertEquals(0, zero.getNumerator());
        assertEquals(1, zero.getDenominator());
        
        Fraction one = new Fraction(1, 1);
        assertEquals(1, one.getNumerator());
        assertEquals(1, one.getDenominator());
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-2, 3);
        Fraction expected = new Fraction(2, 3);
        assertEquals(expected, fraction.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(2, 1);
        Fraction fraction3 = new Fraction(1, 2);
        
        int result = fraction1.compareTo(fraction2);
        assertEquals(-1, result);
        
        result = fraction1.compareTo(fraction1);
        assertEquals(0, result);
        
        result = fraction1.compareTo(fraction3);
        assertEquals(1, result);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5, fraction.doubleValue(), 0.0001);
        
        fraction = new Fraction(-1, 2);
        assertEquals(-0.5, fraction.doubleValue(), 0.0001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(2, 1);
        Object obj = new Object();
        
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
        assertFalse(fraction1.equals(obj));
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(2, 1);
        
        assertEquals(fraction1.hashCode(), fraction1.hashCode());
        assertEquals(fraction1.hashCode(), fraction2.hashCode());
        assertNotEquals(fraction1.hashCode(), fraction3.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(4, 3);
        assertEquals(1, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(4, 3);
        assertEquals(1L, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(2, 3);
        Fraction expected = new Fraction(-2, 3);
        assertEquals(expected, fraction.negate());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction expected = new Fraction(3, 2);
        assertEquals(expected, fraction.reciprocal());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(3, 6);
        
        Fraction sum = fraction1.add(fraction2);
        assertEquals(expected, sum);

        expected = new Fraction(-1, 6);
        sum = fraction1.add(fraction2.negate());
        assertEquals(expected, sum);
        
        try {
            fraction1.add(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddSubWithInt() {
        Fraction fraction = new Fraction(2, 3);
        
        Fraction sum = fraction.add(1);
        Fraction expected = new Fraction(5, 3);
        assertEquals(expected, sum);
        
        sum = fraction.subtract(1);
        expected = new Fraction(1, 3);
        assertEquals(expected, sum);
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(-1, 6);
        
        Fraction difference = fraction1.subtract(fraction2);
        assertEquals(expected, difference);

        try {
            fraction1.subtract(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(1, 6);
        
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(expected, product);
    }

    @Test
    public void testMultiplyWithInt() {
        Fraction fraction = new Fraction(2, 3);
        
        Fraction product = fraction.multiply(2);
        Fraction expected = new Fraction(4, 3);
        assertEquals(expected, product);
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction expected = new Fraction(3, 2);
        
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(expected, quotient);

        try {
            fraction1.divide(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDivideWithInt() {
        Fraction fraction = new Fraction(4, 3);
        
        Fraction quotient = fraction.divide(2);
        Fraction expected = new Fraction(2, 1);
        assertEquals(expected, quotient);
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(75.0, fraction.percentageValue(), 0.01);
    }

    @Test
    public void testGetReducedFraction() {
        int numerator = 12;
        int denominator = 18;
        
        Fraction reducedFraction = Fraction.getReducedFraction(numerator, denominator);
        assertEquals(2, reducedFraction.getNumerator());
        assertEquals(9, reducedFraction.getDenominator());
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals("1/2", fraction.toString());
    }
}