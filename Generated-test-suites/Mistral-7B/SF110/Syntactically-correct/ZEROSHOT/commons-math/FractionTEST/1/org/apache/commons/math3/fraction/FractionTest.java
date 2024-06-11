import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.exception.FractionConversionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructorWithDouble() throws FractionConversionException {
        Fraction fr = new Fraction(1.5);
        assertEquals("1/2", fr.toString());
        assertEquals(1, fr.getNumerator());
        assertEquals(2, fr.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleOutOfRange() throws FractionConversionException {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testConstructorWithDoubleAndEpsilon() throws FractionConversionException {
        Fraction fr = new Fraction(1.5, 0.001, 100);
        assertEquals("1/2", fr.toString());
        assertEquals(1, fr.getNumerator());
        assertEquals(2, fr.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleAndInvalidEpsilon() throws FractionConversionException {
        new Fraction(1.5, 0.0, 100);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorWithDoubleAndMaxDenominator() throws FractionConversionException {
        new Fraction(1.5, 0.0, 5);
    }

    @Test
    public void testConstructorWithInt() {
        Fraction fr = new Fraction(2);
        assertEquals("2", fr.toString());
        assertEquals(2, fr.getNumerator());
        assertEquals(1, fr.getDenominator());
    }

    @Test
    public void testConstructorWithIntAndDenominator() {
        Fraction fr = new Fraction(2, 3);
        assertEquals("2/3", fr.toString());
        assertEquals(2, fr.getNumerator());
        assertEquals(3, fr.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroDenominator() {
        new Fraction(2, 0);
    }

    @Test
    public void testEquals() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 2);
        Fraction fr3 = new Fraction(1, 3);
        Fraction fr4 = new Fraction(2, 2);
        Fraction fr5 = new Object();
        assertTrue(fr1.equals(fr2));
        assertFalse(fr1.equals(fr3));
        assertFalse(fr1.equals(fr4));
        assertFalse(fr1.equals(fr5));
    }

    @Test
    public void testCompareTo() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 3);
        Fraction fr3 = new Fraction(1, 2);
        Fraction fr4 = new Fraction(2, 2);
        int cmp1 = fr1.compareTo(fr2);
        int cmp2 = fr1.compareTo(fr3);
        int cmp3 = fr1.compareTo(fr4);
        assertEquals(-1, cmp1);
        assertEquals(0, cmp2);
        assertEquals(1, cmp3);
    }

    @Test
    public void testAdd() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 3);
        Fraction sum = fr1.add(fr2);
        assertEquals("5/6", sum.toString());
        assertEquals(5, sum.getNumerator());
        assertEquals(6, sum.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 3);
        Fraction difference = fr1.subtract(fr2);
        assertEquals("1/6", difference.toString());
        assertEquals(1, difference.getNumerator());
        assertEquals(6, difference.getDenominator());
    }

    @Test
    public void testAddWithInt() {
        Fraction fr = new Fraction(1, 2);
        Fraction sum = fr.add(2);
        assertEquals("3/2", sum.toString());
        assertEquals(3, sum.getNumerator());
        assertEquals(2, sum.getDenominator());
    }

    @Test
    public void testSubtractWithInt() {
        Fraction fr = new Fraction(1, 2);
        Fraction difference = fr.subtract(2);
        assertEquals("-1/2", difference.toString());
        assertEquals(-1, difference.getNumerator());
        assertEquals(2, difference.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 3);
        Fraction product = fr1.multiply(fr2);
        assertEquals("1/6", product.toString());
        assertEquals(1, product.getNumerator());
        assertEquals(6, product.getDenominator());
    }

    @Test
    public void testMultiplyWithInt() {
        Fraction fr = new Fraction(1, 2);
        Fraction product = fr.multiply(3);
        assertEquals("3/2", product.toString());
        assertEquals(3, product.getNumerator());
        assertEquals(2, product.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fr1 = new Fraction(1, 2);
        Fraction fr2 = new Fraction(1, 3);
        Fraction quotient = fr1.divide(fr2);
        assertEquals("3/2", quotient.toString());
        assertEquals(3, quotient.getNumerator());
        assertEquals(2, quotient.getDenominator());
    }

    @Test
    public void testDivideWithInt() {
        Fraction fr = new Fraction(1, 2);
        Fraction quotient = fr.divide(3);
        assertEquals("-1/6", quotient.toString());
        assertEquals(-1, quotient.getNumerator());
        assertEquals(6, quotient.getDenominator());
    }

    @Test
    public void testNegate() {
        Fraction fr = new Fraction(-1, 2);
        Fraction negated = fr.negate();
        assertEquals("1/2", negated.toString());
        assertEquals(1, negated.getNumerator());
        assertEquals(2, negated.getDenominator());
    }

    @Test
    public void testReciprocal() {
        Fraction fr = new Fraction(1, 2);
        Fraction reciprocal = fr.reciprocal();
        assertEquals("2/1", reciprocal.toString());
        assertEquals(2, reciprocal.getNumerator());
        assertEquals(1, reciprocal.getDenominator());
    }

    @Test
    public void testGetDenominator() {
        Fraction fr = new Fraction(1, 2);
        assertEquals(2, fr.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fr = new Fraction(1, 2);
        assertEquals(1, fr.getNumerator());
    }

    @Test
    public void testGetField() {
        Fraction fr = new Fraction(1, 2);
        assertEquals(FractionField.getInstance(), fr.getField());
    }

    @Test
    public void testPercentageValue() {
        Fraction fr = new Fraction(3, 4);
        assertEquals(75.0, fr.percentageValue(), 0.01);
    }

    @Test
    public void testToString() {
        Fraction fr = new Fraction(1, 2);
        assertEquals("1/2", fr.toString());
    }

    @Test
    public void testGetReducedFraction() {
        assertEquals(Fraction.ONE_HALF, Fraction.getReducedFraction(2, 4));
        assertEquals(Fraction.ZERO, Fraction.getReducedFraction(0, 1));
        assertEquals(Fraction.ONE, Fraction.getReducedFraction(1, 1));
        assertEquals(Fraction.MINUS_ONE, Fraction.getReducedFraction(-1, 1));
    }

    @Test
    public void testStaticInitializers() {
        Fraction fr1 = Fraction.TWO;
        Fraction fr2 = Fraction.ONE;
        Fraction fr3 = Fraction.ZERO;
        Fraction fr4 = Fraction.FOUR_FIFTHS;
        Fraction fr5 = Fraction.ONE_FIFTH;
        Fraction fr6 = Fraction.ONE_HALF;
        Fraction fr7 = Fraction.ONE_QUARTER;
        Fraction fr8 = Fraction.ONE_THIRD;
        Fraction fr9 = Fraction.THREE_FIFTHS;
        Fraction fr10 = Fraction.THREE_QUARTERS;
        Fraction fr11 = Fraction.TWO_FIFTHS;
        Fraction fr12 = Fraction.TWO_QUARTERS;
        Fraction fr13 = Fraction.TWO_THIRDS;
        Fraction fr14 = Fraction.MINUS_ONE;

        assertEquals("2", fr1.toString());
        assertEquals(2, fr1.getNumerator());
        assertEquals(1, fr1.getDenominator());
        assertEquals("1", fr2.toString());
        assertEquals(1, fr2.getNumerator());
        assertEquals(1, fr2.getDenominator());
        assertEquals("0", fr3.toString());
        assertEquals(0, fr3.getNumerator());
        assertEquals(1, fr3.getDenominator());
        assertEquals("4/5", fr4.toString());
        assertEquals(4, fr4.getNumerator());
        assertEquals(5, fr4.getDenominator());
        assertEquals("1/5", fr5.toString());
        assertEquals(1, fr5.getNumerator());
        assertEquals(5, fr5.getDenominator());
        assertEquals("1/2", fr6.toString());
        assertEquals(1, fr6.getNumerator());
        assertEquals(2, fr6.getDenominator());
        assertEquals("1/4", fr7.toString());
        assertEquals(1, fr7.getNumerator());
        assertEquals(4, fr7.getDenominator());
        assertEquals("1/3", fr8.toString());
        assertEquals(1, fr8.getNumerator());
        assertEquals(3, fr8.getDenominator());
        assertEquals("3/5", fr9.toString());
        assertEquals(3, fr9.getNumerator());
        assertEquals(5, fr9.getDenominator());
        assertEquals("3/4", fr10.toString());
        assertEquals(3, fr10.getNumerator());
        assertEquals(4, fr10.getDenominator());
        assertEquals("2/5", fr11.toString());
        assertEquals(2, fr11.getNumerator());
        assertEquals(5, fr11.getDenominator());
        assertEquals("2/4", fr12.toString());
        assertEquals(2, fr12.getNumerator());
        assertEquals(4, fr12.getDenominator());
        assertEquals("-1", fr13.toString());
        assertEquals(-1, fr13.getNumerator());
        assertEquals(1, fr13.getDenominator());
    }
}