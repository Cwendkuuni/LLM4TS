import org.apache.commons.math3.fraction.Fraction;
import org.junit.Assert;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructorWithDouble() {
        Fraction fraction = new Fraction(2.5);
        Assert.assertEquals(2, fraction.getNumerator());
        Assert.assertEquals(5, fraction.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleOutOfRange() {
        Fraction fraction = new Fraction(Double.MAX_VALUE);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleZeroDenominator() {
        Fraction fraction = new Fraction(0.0);
    }

    @Test
    public void testFractionConstructorWithInt() {
        Fraction fraction = new Fraction(4);
        Assert.assertEquals(4, fraction.getNumerator());
        Assert.assertEquals(1, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithNegativeDenominator() {
        Fraction fraction = new Fraction(-2, 3);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructorWithZeroDenominator() {
        Fraction fraction = new Fraction(1, 0);
    }

    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction absFraction = fraction.abs();
        Assert.assertEquals(3, absFraction.getNumerator());
        Assert.assertEquals(2, absFraction.getDenominator());
    }

    @Test
    public void testFractionCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(1, 2);
        int result1 = fraction1.compareTo(fraction2);
        int result2 = fraction2.compareTo(fraction1);
        int result3 = fraction1.compareTo(fraction1);
        Assert.assertEquals(-1, result1);
        Assert.assertEquals(1, result2);
        Assert.assertEquals(0, result3);
    }

    @Test
    public void testFractionDoubleValue() {
        Fraction fraction = new Fraction(3, 2);
        double value = fraction.doubleValue();
        Assert.assertEquals(1.5, value, 0.001);
    }

    @Test
    public void testFractionEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);
        Assert.assertTrue(fraction1.equals(fraction1));
        Assert.assertTrue(fraction1.equals(fraction2));
        Assert.assertFalse(fraction1.equals(fraction3));
    }

    @Test
    public void testFractionGetDenominator() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testFractionGetNumerator() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals(3, fraction.getNumerator());
    }

    @Test
    public void testFractionHashCode() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);
        int hashCode1 = fraction1.hashCode();
        int hashCode2 = fraction2.hashCode();
        int hashCode3 = fraction3.hashCode();
        Assert.assertEquals(hashCode1, hashCode2);
        Assert.assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testFractionIntValue() {
        Fraction fraction = new Fraction(3, 2);
        int value = fraction.intValue();
        Assert.assertEquals(1, value);
    }

    @Test
    public void testFractionLongValue() {
        Fraction fraction = new Fraction(3, 2);
        long value = fraction.longValue();
        Assert.assertEquals(1L, value);
    }

    @Test
    public void testFractionNegate() {
        Fraction fraction = new Fraction(-3, 2);
        Fraction negateFraction = fraction.negate();
        Assert.assertEquals(3, negateFraction.getNumerator());
        Assert.assertEquals(2, negateFraction.getDenominator());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction fraction = new Fraction(3, 2);
        Fraction reciprocalFraction = fraction.reciprocal();
        Assert.assertEquals(2, reciprocalFraction.getNumerator());
        Assert.assertEquals(3, reciprocalFraction.getDenominator());
    }

    @Test
    public void testFractionAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction sumFraction = fraction1.add(fraction2);
        Assert.assertEquals(5, sumFraction.getNumerator());
        Assert.assertEquals(6, sumFraction.getDenominator());
    }

    @Test
    public void testFractionSubtract() {
        Fraction fraction1 = new Fraction(4, 3);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction differenceFraction = fraction1.subtract(fraction2);
        Assert.assertEquals(3, differenceFraction.getNumerator());
        Assert.assertEquals(3, differenceFraction.getDenominator());
    }

    @Test
    public void testFractionMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction productFraction = fraction1.multiply(fraction2);
        Assert.assertEquals(3, productFraction.getNumerator());
        Assert.assertEquals(4, productFraction.getDenominator());
    }

    @Test
    public void testFractionDivide() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction quotientFraction = fraction1.divide(fraction2);
        Assert.assertEquals(3, quotientFraction.getNumerator());
        Assert.assertEquals(1, quotientFraction.getDenominator());
    }

    @Test(expected = NullArgumentException.class)
    public void testFractionAddWithNull() {
        Fraction fraction = new Fraction(1, 2);
        Fraction nullFraction = null;
        Fraction sumFraction = fraction.add(nullFraction);
    }

    @Test(expected = NullArgumentException.class)
    public void testFractionSubtractWithNull() {
        Fraction fraction = new Fraction(1, 2);
        Fraction nullFraction = null;
        Fraction differenceFraction = fraction.subtract(nullFraction);
    }

    @Test(expected = NullArgumentException.class)
    public void testFractionMultiplyWithNull() {
        Fraction fraction = new Fraction(1, 2);
        Fraction nullFraction = null;
        Fraction productFraction = fraction.multiply(nullFraction);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleOutOfRangeDenominator() {
        Fraction fraction = new Fraction(-0.1);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleOutOfRangeNumerator() {
        Fraction fraction = new Fraction(Double.MAX_VALUE, 1);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructorWithDoubleSmallDenominator() {
        Fraction fraction = new Fraction(0.1, 0.00001);
    }

    @Test
    public void testFractionToString() {
        Fraction fraction = new Fraction(3, 4);
        String result = fraction.toString();
        Assert.assertEquals("3/4", result);
    }

    @Test
    public void testFractionGetField() {
        Fraction fraction = new Fraction(1, 2);
        FractionField field = fraction.getField();
        Assert.assertNotNull(field);
    }
}