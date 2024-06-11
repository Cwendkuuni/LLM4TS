import org.apache.commons.math3.fraction.Fraction;
import org.junit.Assert;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructorDouble() {
        Fraction fraction = new Fraction(1.5);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(2, fraction.getDenominator());

        fraction = new Fraction(0.25);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(4, fraction.getDenominator());

        try {
            new Fraction(-1.5);
            Assert.fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            new Fraction(Double.POSITIVE_INFINITY);
            Assert.fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            new Fraction(Double.NEGATIVE_INFINITY);
            Assert.fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructorDoubleWithEpsilon() {
        Fraction fraction = new Fraction(1.4142135625);
        Assert.assertEquals(2, fraction.getDenominator());
        Assert.assertEquals(3, fraction.getNumerator());

        fraction = new Fraction(0.123456789, 0.000001, 100);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(123456789, fraction.getDenominator());

        try {
            new Fraction(Double.POSITIVE_INFINITY, 0.00001, 100);
            Assert.fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            new Fraction(Double.NEGATIVE_INFINITY, 0.00001, 100);
            Assert.fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }

        try {
            new Fraction(-1.4142135625, 0.00001, 100);
            Assert.fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructorDoubleMaxDenominator() {
        Fraction fraction = new Fraction(1.23456789, 5);
        Assert.assertEquals(4, fraction.getNumerator());
        Assert.assertEquals(2, fraction.getDenominator());

        Fraction fraction1 = new Fraction(1.123456789, 10);
        Assert.assertEquals(11, fraction1.getNumerator());
        Assert.assertEquals(10, fraction1.getDenominator());

        try {
            new Fraction(1.123456789, Integer.MAX_VALUE);
            Assert.fail("Expected FractionConversionException");
        } catch (FractionConversionException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructorInt() {
        Fraction fraction = new Fraction(1);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(1, fraction.getDenominator());

        fraction = new Fraction(5);
        Assert.assertEquals(5, fraction.getNumerator());
        Assert.assertEquals(1, fraction.getDenominator());

        fraction = new Fraction(1, 2);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(2, fraction.getDenominator());

        fraction = new Fraction(-1, 1);
        Assert.assertEquals(-1, fraction.getNumerator());
        Assert.assertEquals(1, fraction.getDenominator());

        try {
            new Fraction(Integer.MIN_VALUE);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            new Fraction(Integer.MIN_VALUE, 2);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testFractionConstructorIntInt() {
        Fraction fraction = new Fraction(1, 2);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(2, fraction.getDenominator());

        fraction = new Fraction(3, 4);
        Assert.assertEquals(3, fraction.getNumerator());
        Assert.assertEquals(4, fraction.getDenominator());

        fraction = new Fraction(-3, 4);
        Assert.assertEquals(-3, fraction.getNumerator());
        Assert.assertEquals(4, fraction.getDenominator());

        fraction = new Fraction(2, -3);
        Assert.assertEquals(-2, fraction.getNumerator());
        Assert.assertEquals(3, fraction.getDenominator());

        try {
            new Fraction(Integer.MIN_VALUE, Integer.MIN_VALUE);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            new Fraction(Integer.MAX_VALUE, Integer.MIN_VALUE);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            new Fraction(-Integer.MAX_VALUE, Integer.MIN_VALUE);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            new Fraction(Integer.MIN_VALUE, 2);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testFractionMethods() {
        Fraction fraction = new Fraction(1, 2);

        Assert.assertEquals(1.0 / 2.0, fraction.doubleValue(), 0.001);
        Assert.assertEquals(1, fraction.intValue());
        Assert.assertEquals(1, fraction.longValue());
        Assert.assertEquals(1, fraction.floatValue());

        Assert.assertEquals(1, fraction.abs().getNumerator());
        Assert.assertEquals(1, fraction.abs().getDenominator());

        Fraction negativeFraction = fraction.negate();
        Assert.assertEquals(-1, negativeFraction.getNumerator());
        Assert.assertEquals(1, negativeFraction.getDenominator());

        Fraction reciprocalFraction = fraction.reciprocal();
        Assert.assertEquals(2, reciprocalFraction.getNumerator());
        Assert.assertEquals(1, reciprocalFraction.getDenominator());

        Fraction sum = fraction.add(new Fraction(1, 3));
        Assert.assertEquals(3, sum.getNumerator());
        Assert.assertEquals(6, sum.getDenominator());

        Fraction difference = fraction.subtract(new Fraction(1, 3));
        Assert.assertEquals(-2, difference.getNumerator());
        Assert.assertEquals(6, difference.getDenominator());

        Fraction product = fraction.multiply(new Fraction(2, 3));
        Assert.assertEquals(2, product.getNumerator());
        Assert.assertEquals(6, product.getDenominator());

        Fraction quotient = fraction.divide(new Fraction(2, 3));
        Assert.assertEquals(-1, quotient.getNumerator());
        Assert.assertEquals(3, quotient.getDenominator());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(1, 2);

        Assert.assertLessThan(0, fraction1.compareTo(fraction2));
        Assert.assertEquals(0, fraction1.compareTo(fraction3));
    }

    @Test
    public void testFractionEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(1, 2);

        Assert.assertFalse(fraction1.equals(fraction2));
        Assert.assertTrue(fraction1.equals(fraction3));
    }

    @Test
    public void testFractionToString() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals("3/4", fraction.toString());
    }
}