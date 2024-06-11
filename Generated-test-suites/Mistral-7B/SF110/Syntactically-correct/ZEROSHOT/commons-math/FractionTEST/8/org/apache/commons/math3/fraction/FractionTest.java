import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructor_double() throws FractionConversionException {
        Fraction fraction = new Fraction(1.5);
        assertEquals(new Fraction(3, 2), fraction);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructor_double_invalid() throws FractionConversionException {
        new Fraction(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testConstructor_int() {
        Fraction fraction = new Fraction(4);
        assertEquals(new Fraction(4, 1), fraction);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_denominatorZero() {
        new Fraction(4, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructor_int_overflow() {
        new Fraction(Integer.MIN_VALUE);
    }

    @Test
    public void testConstructor_int_numDenom() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(new Fraction(2, 3), fraction);
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction result = fraction.abs();
        assertEquals(new Fraction(3, 4), result);
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        int result = fraction1.compareTo(fraction2);
        assertEquals(-1, result);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(1, 2);
        double result = fraction.doubleValue();
        assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void testEquality() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        assertTrue(fraction1.equals(fraction2));
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = fraction.negate();
        assertEquals(new Fraction(-3, 4), result);
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.reciprocal();
        assertEquals(new Fraction(3, 2), result);
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(new Fraction(5, 6), result);
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(new Fraction(1, 3), result);
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.add(3);
        assertEquals(new Fraction(7, 2), result);
    }

    @Test
    public void testSubtractInt() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.subtract(3);
        assertEquals(new Fraction(-1, 3), result);
    }

    @Test
    public void testAddSub_isAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.addSub(fraction2, true);
        assertEquals(new Fraction(5, 6), result);
    }

    @Test
    public void testAddSub_isSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.addSub(fraction2, false);
        assertEquals(new Fraction(-1, 6), result);
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(new Fraction(1, 6), result);
    }

    @Test
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.multiply(3);
        assertEquals(new Fraction(3, 1), result);
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(new Fraction(3, 1), result);
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(1, 2);
        Fraction result = fraction.divide(3);
        assertEquals(new Fraction(1, 6), result);
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        double result = fraction.percentageValue();
        assertEquals(75.0, result, 0.001);
    }

    @Test
    public void testGetField() {
        Fraction fraction = new Fraction(1, 2);
        FractionField field = fraction.getField();
        assertEquals(FractionField.getInstance(), field);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(1, 2);
        String result = fraction.toString();
        assertEquals("1/2", result);
    }

    @Test
    public void testStaticMethods() {
        Fraction fraction = Fraction.getReducedFraction(6, 8);
        assertEquals(new Fraction(3, 4), fraction);
    }
}