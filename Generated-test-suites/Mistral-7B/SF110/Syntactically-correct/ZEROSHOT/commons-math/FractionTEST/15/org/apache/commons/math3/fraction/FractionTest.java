import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructor_double() throws FractionConversionException {
        Fraction fraction = new Fraction(1.5);
        assertEquals("Tests constructor with double value", new Fraction(3, 2), fraction);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_exception() throws FractionConversionException {
        new Fraction(Double.POSITIVE_INFINITY);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_epsilon() throws FractionConversionException {
        new Fraction(0.3333333333333333, 0.000000000000001, 1000);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_maxDenominator() throws FractionConversionException {
        new Fraction(0.1, 1000000000);
    }

    @Test
    public void testConstructor_int() {
        Fraction fraction = new Fraction(4);
        assertEquals("Tests constructor with int value", new Fraction(4, 1), fraction);
    }

    @Test
    public void testConstructor_int_int() {
        Fraction fraction = new Fraction(4, 3);
        assertEquals("Tests constructor with int numerator and int denominator", new Fraction(4, 3), fraction);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_zeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_overflow() {
        new Fraction(Integer.MIN_VALUE, 1);
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-4, 3);
        assertEquals("Tests abs method", new Fraction(4, 3), fraction.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(4, 3);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(4, 3);

        int result1 = fraction1.compareTo(fraction2);
        int result2 = fraction2.compareTo(fraction1);
        int result3 = fraction1.compareTo(fraction1);

        assertEquals("Tests compareTo method. Fraction1 > Fraction2", 1, result1);
        assertEquals("Tests compareTo method. Fraction2 < Fraction1", -1, result2);
        assertEquals("Tests compareTo method. Fraction1 == Fraction1", 0, result3);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals("Tests doubleValue method", 1.5, fraction.doubleValue(), 0.001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(4, 3);
        Fraction fraction2 = new Fraction(4, 3);
        Fraction fraction3 = new Fraction(3, 4);

        assertTrue("Tests equals method. Fraction1 == Fraction1", fraction1.equals(fraction1));
        assertTrue("Tests equals method. Fraction1 == Fraction2", fraction1.equals(fraction2));
        assertFalse("Tests equals method. Fraction1 != Fraction3", fraction1.equals(fraction3));
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals("Tests floatValue method", 1.5f, fraction.floatValue(), 0.001f);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(4, 3);
        assertEquals("Tests getDenominator method", 3, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(4, 3);
        assertEquals("Tests getNumerator method", 4, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction1 = new Fraction(4, 3);
        Fraction fraction2 = new Fraction(4, 3);
        Fraction fraction3 = new Fraction(3, 4);

        int hashCode1 = fraction1.hashCode();
        int hashCode2 = fraction2.hashCode();
        int hashCode3 = fraction3.hashCode();

        assertEquals("Tests hashCode method. Fraction1 == Fraction1", hashCode1, hashCode1);
        assertEquals("Tests hashCode method. Fraction1 == Fraction2", hashCode1, hashCode2);
        assertNotEquals("Tests hashCode method. Fraction1 != Fraction3", hashCode1, hashCode3);
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals("Tests intValue method", 1, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 2);
        assertEquals("Tests longValue method", 1L, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(-4, 3);
        assertEquals("Tests negate method", new Fraction(4, 3), fraction.negate());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals("Tests reciprocal method", new Fraction(3, 2), fraction.reciprocal());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 2);

        Fraction result = fraction1.add(fraction2);

        assertEquals("Tests add method", new Fraction(7, 6), result);
    }

    @Test
    public void testAdd_int() {
        Fraction fraction = new Fraction(3, 2);

        Fraction result = fraction.add(1);

        assertEquals("Tests add method with int", new Fraction(7, 2), result);
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(3, 2);
        Fraction fraction2 = new Fraction(1, 2);

        Fraction result = fraction1.subtract(fraction2);

        assertEquals("Tests subtract method", new Fraction(1, 2), result);
    }

    @Test
    public void testSubtract_int() {
        Fraction fraction = new Fraction(3, 2);

        Fraction result = fraction.subtract(1);

        assertEquals("Tests subtract method with int", new Fraction(1, 2), result);
    }

    @Test
    public void testAddSub() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 2);

        Fraction result = fraction1.addSub(fraction2, true);

        assertEquals("Tests addSub method with add", new Fraction(7, 6), result);

        result = fraction1.addSub(fraction2, false);

        assertEquals("Tests addSub method with subtract", new Fraction(1, 6), result);
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(4, 5);

        Fraction result = fraction1.multiply(fraction2);

        assertEquals("Tests multiply method", new Fraction(8, 15), result);
    }

    @Test
    public void testMultiply_int() {
        Fraction fraction = new Fraction(3, 2);

        Fraction result = fraction.multiply(2);

        assertEquals("Tests multiply method with int", new Fraction(6, 1), result);
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(6, 5);
        Fraction fraction2 = new Fraction(2, 3);

        Fraction result = fraction1.divide(fraction2);

        assertEquals("Tests divide method", new Fraction(4, 3), result);
    }

    @Test
    public void testDivide_int() {
        Fraction fraction = new Fraction(3, 2);

        Fraction result = fraction.divide(2);

        assertEquals("Tests divide method with int", new Fraction(3, 4), result);
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);

        double percentage = fraction.percentageValue();

        assertEquals("Tests percentageValue method", 75.0, percentage, 0.01);
    }

    @Test
    public void testGetReducedFraction() {
        int numerator = 12;
        int denominator = 15;

        Fraction result = Fraction.getReducedFraction(numerator, denominator);

        assertEquals("Tests getReducedFraction method", new Fraction(4, 5), result);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(3, 2);

        String result = fraction.toString();

        assertEquals("Tests toString method", "3/2", result);
    }

    @Test
    public void testGetField() {
        Fraction fraction = new Fraction(3, 2);

        FractionField field = fraction.getField();

        assertEquals("Tests getField method", FractionField.getInstance(), field);
    }
}