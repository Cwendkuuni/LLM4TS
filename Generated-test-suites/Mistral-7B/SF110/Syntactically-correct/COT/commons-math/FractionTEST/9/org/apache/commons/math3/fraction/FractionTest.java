import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testFractionConstructor_Double() throws Exception {
        Fraction fraction = new Fraction(2.5);
        assertEquals(5, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        Fraction exception = null;
        try {
            new Fraction(-3.5);
        } catch (FractionConversionException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    @Test
    public void testFractionConstructor_Double_Epsilon_MaxIterations() throws Exception {
        Fraction fraction = new Fraction(2.5, 0.001, 100);
        assertEquals(5, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        Fraction exception = null;
        try {
            new Fraction(-3.5, 0.001, 100);
        } catch (FractionConversionException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    @Test
    public void testFractionConstructor_Double_MaxDenominator() throws Exception {
        Fraction fraction = new Fraction(2.5, 10);
        assertEquals(5, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        Fraction exception = null;
        try {
            new Fraction(-3.5, 10);
        } catch (FractionConversionException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    @Test
    public void testFractionConstructor_Int() {
        Fraction fraction = new Fraction(4);
        assertEquals(4, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testFractionConstructor_Int_Int() {
        Fraction fraction = new Fraction(4, 2);
        assertEquals(2, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        try {
            new Fraction(0, -1);
        } catch (Exception e) {
            assertInstanceOf(MathArithmeticException.class, e);
        }

        try {
            new Fraction(Integer.MIN_VALUE, 1);
        } catch (Exception e) {
            assertInstanceOf(MathArithmeticException.class, e);
        }

        try {
            new Fraction(1, Integer.MIN_VALUE);
        } catch (Exception e) {
            assertInstanceOf(MathArithmeticException.class, e);
        }

        try {
            new Fraction(1, 0);
        } catch (Exception e) {
            assertInstanceOf(MathArithmeticException.class, e);
        }
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-5, 2);
        Fraction expected = new Fraction(5, 2);
        assertEquals(expected, fraction.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction fraction3 = new Fraction(1, 2);

        int comparisonResult1 = fraction1.compareTo(fraction2);
        assertEquals(-1, comparisonResult1);

        int comparisonResult2 = fraction1.compareTo(fraction1);
        assertEquals(0, comparisonResult2);

        int comparisonResult3 = fraction2.compareTo(fraction1);
        assertEquals(1, comparisonResult3);

        int comparisonResult4 = fraction3.compareTo(fraction1);
        assertEquals(0, comparisonResult4);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        double expected = 0.75;
        assertEquals(expected, fraction.doubleValue(), 0.0001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(5, 6);
        assertEquals(6, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(5, 6);
        assertEquals(5, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction fraction3 = new Fraction(1, 3);

        int hashCode1 = fraction1.hashCode();
        int hashCode2 = fraction1.hashCode();
        int hashCode3 = fraction2.hashCode();
        int hashCode4 = fraction3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 4);
        int expected = 3 * 4 / 2;
        assertEquals(expected, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 4);
        long expected = 3L * 4L / 2L;
        assertEquals(expected, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(-3, 4);
        assertEquals(expected, fraction.negate());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(4, 3);
        assertEquals(expected, fraction.reciprocal());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(2, 6);
        assertEquals(expected, fraction1.add(fraction2));
    }

    @Test
    public void testAdd_Int() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(3 + (3 * 4 / 2), 4);
        assertEquals(expected, fraction.add(3));
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(2, 6);
        assertEquals(expected, fraction1.subtract(fraction2));
    }

    @Test
    public void testSubtract_Int() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(3 - (3 * 4 / 2), 4);
        assertEquals(expected, fraction.subtract(3));
    }

    @Test
    public void testAddSub() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(2, 6);
        assertEquals(expected, fraction1.addSub(fraction2, true));

        Fraction fraction3 = new Fraction(1, 2);
        Fraction fraction4 = new Fraction(1, 3);
        Fraction expected2 = new Fraction(-1, 6);
        assertEquals(expected2, fraction1.addSub(fraction4, false));
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction expected = new Fraction(1, 6);
        assertEquals(expected, fraction1.multiply(fraction2));
    }

    @Test
    public void testMultiply_Int() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(3 * 5, 4);
        assertEquals(expected, fraction.multiply(5));
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction expected = new Fraction(3 * 2, 2 * 4);
        assertEquals(expected, fraction1.divide(fraction2));
    }

    @Test
    public void testDivide_Int() {
        Fraction fraction = new Fraction(3, 4);
        Fraction expected = new Fraction(3, 4 * 5);
        assertEquals(expected, fraction.divide(5));
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);
        double expected = 75.0;
        assertEquals(expected, fraction.percentageValue(), 0.0001);
    }

    @Test
    public void testGetReducedFraction() throws Exception {
        Fraction fraction = new Fraction(12, 18);
        Fraction expected = new Fraction(2, 3);
        assertEquals(expected, Fraction.getReducedFraction(12, 18));
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructor_Double_MaxIterations_Exception() throws Exception {
        new Fraction(2.5, 10, 1);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructor_Double_Epsilon_MaxDenominator_Exception() throws Exception {
        new Fraction(2.5, 0.001, 5);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructor_Int_DenominatorZero() throws Exception {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructor_Int_DenominatorNegative() throws Exception {
        new Fraction(1, -1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructor_Int_NumeratorMinusDenominator() throws Exception {
        new Fraction(-1, 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionConstructor_Int_NumeratorMinusDenominator_Negative() throws Exception {
        new Fraction(-1, -1);
    }
}