package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.FractionConversionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testFractionConstructionFromDouble() throws FractionConversionException {
        Fraction f = new Fraction(1.5);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
        assertEquals(0.75, f.doubleValue(), Precision.EPSILON);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConstructionFromDouble_OutOfRange() throws FractionConversionException {
        new Fraction(Double.MAX_VALUE);
    }

    @Test
    public void testFractionConstructionFromDouble_SmallEpsilon() throws FractionConversionException {
        Fraction f = new Fraction(1.5, 1e-12, 100);
        assertEquals(1, f.getNumerator());
        assertEquals(3, f.getDenominator());
        assertEquals(0.33333333333333336, f.doubleValue(), Precision.EPSILON); // 1/3 is not exactly representable as a Fraction
    }

    @Test
    public void testFractionConstructionFromDouble_MaxDenominator() throws FractionConversionException {
        Fraction f = new Fraction(1.5, 1, Integer.MAX_VALUE, 100);
        assertEquals(78185, f.getNumerator());
        assertEquals(39090625, f.getDenominator());
    }

    @Test
    public void testFractionConstructionFromInt() {
        Fraction f = new Fraction(4);
        assertEquals(4, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    public void testFractionConstructionFromInt_Negative() {
        Fraction f = new Fraction(-4);
        assertEquals(-4, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    public void testFractionConstructionFromInt_ZeroDenominator() {
        try {
            new Fraction(0);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testFractionConstructionFromInt_BigNumbers() {
        Fraction f = new Fraction(Integer.MAX_VALUE, Integer.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, (long)f.getNumerator());
        assertEquals(-Long.MAX_VALUE, (long)f.getDenominator());
    }

    @Test
    public void testFractionGCD() {
        Fraction f1 = new Fraction(15, 12);
        Fraction f2 = new Fraction(21, 14);
        Fraction f3 = Fraction.getReducedFraction(15 * 21, 12 * 14);
        assertEquals(3, f3.getDenominator());
        assertEquals(f3.getNumerator(), f1.getNumerator() * 2 * f2.getNumerator() / 3);
        assertEquals(f3.getDenominator(), f1.getDenominator() * f2.getDenominator());
    }

    @Test
    public void testFractionEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(2, 1);
        Fraction f4 = new Fraction(2, 1);
        Fraction f5 = new Fraction(1, 3);
        Fraction f6 = new Object();
        assertFalse(f1.equals(f3));
        assertFalse(f1.equals(f5));
        assertFalse(f1.equals(f6));
        assertTrue(f1.equals(f2));
        assertTrue(f3.equals(f4));
        assertTrue(f1.compareTo(f5) < 0);
        assertTrue(f5.compareTo(f1) > 0);
    }

    @Test
    public void testFractionComparison() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(2, 1);
        Fraction f4 = new Fraction(0, 1);
        Fraction f5 = new Fraction(1, 3);
        Fraction f6 = new Fraction(1, 4);
        Fraction f7 = new Fraction(2, 3);
        Fraction f8 = new Fraction(1, 6);
        Fraction f9 = new Fraction(-1, 1);
        int result;
        result = f1.compareTo(f2);
        assertEquals(0, result);
        result = f1.compareTo(f3);
        assertEquals(-1, result);
        result = f3.compareTo(f1);
        assertEquals(1, result);
        result = f1.compareTo(f4);
        assertEquals(1, result);
        result = f4.compareTo(f1);
        assertEquals(-1, result);
        result = f1.compareTo(f5);
        assertEquals(-1, result);
        result = f5.compareTo(f1);
        assertEquals(1, result);
        result = f1.compareTo(f6);
        assertEquals(1, result);
        result = f6.compareTo(f1);
        assertEquals(-1, result);
        result = f1.compareTo(f7);
        assertEquals(-1, result);
        result = f7.compareTo(f1);
        assertEquals(1, result);
        result = f1.compareTo(f8);
        assertEquals(1, result);
        result = f8.compareTo(f1);
        assertEquals(-1, result);
        result = f1.compareTo(f9);
        assertEquals(-1, result);
        result = f9.compareTo(f1);
        assertEquals(1, result);
        result = f5.compareTo(f6);
        assertEquals(1, result);
        result = f6.compareTo(f5);
        assertEquals(-1, result);
        result = f5.compareTo(f8);
        assertEquals(1, result);
        result = f8.compareTo(f5);
        assertEquals(-1, result);
        result = f7.compareTo(f8);
        assertEquals(1, result);
        result = f8.compareTo(f7);
        assertEquals(-1, result);
    }

    @Test
    public void testFractionConversionToNumber() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0.5, f.doubleValue(), Precision.EPSILON);
        assertEquals(1, f.intValue());
        assertEquals(1, f.longValue());
        assertEquals(1, f.floatValue());
    }

    @Test
    public void testFractionNegation() {
        Fraction f = new Fraction(-2, 3);
        Fraction fNegated = f.negate();
        assertEquals(2, fNegated.getNumerator());
        assertEquals(3, fNegated.getDenominator());
    }

    @Test
    public void testFractionReciprocal() {
        Fraction f = new Fraction(1, 2);
        Fraction fReciprocal = f.reciprocal();
        assertEquals(2, fReciprocal.getNumerator());
        assertEquals(1, fReciprocal.getDenominator());
    }

    @Test
    public void testFractionAddition() {
        Fraction a = new Fraction(1, 3);
        Fraction b = new Fraction(2, 3);
        Fraction c = a.add(b);
        assertEquals(1, c.getNumerator());
        assertEquals(3, c.getDenominator());
        assertEquals(0.33333333333333336, c.doubleValue(), Precision.EPSILON);
    }

    @Test
    public void testFractionSubtraction() {
        Fraction a = new Fraction(1, 3);
        Fraction b = new Fraction(2, 3);
        Fraction c = a.subtract(b);
        assertEquals(-1, c.getNumerator());
        assertEquals(3, c.getDenominator());
        assertEquals(-0.33333333333333336, c.doubleValue(), Precision.EPSILON);
    }

    @Test
    public void testFractionMultiplication() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);
        Fraction c = a.multiply(b);
        assertEquals(1, c.getNumerator());
        assertEquals(6, c.getDenominator());
        assertEquals(1.0/6.0, c.doubleValue(), Precision.EPSILON);
    }

    @Test
    public void testFractionDivision() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);
        Fraction c = a.divide(b);
        assertEquals(3, c.getNumerator());
        assertEquals(2, c.getDenominator());
        assertEquals(1.5, c.doubleValue(), Precision.EPSILON);
    }

    @Test
    public void testFractionToString() {
        Fraction f = new Fraction(1, 2);
        assertEquals("1/2", f.toString());
    }

    @Test
    public void testFractionFromString() {
        Fraction f = new Fraction("1/2");
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    public void testFractionFromCustomFormatString() {
        Fraction f = new Fraction("-3 1/2");
        assertEquals(-3, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    public void testFractionFromCustomFormatString_SingleFraction() {
        Fraction f = new Fraction("3/4");
        assertEquals(3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    public void testFractionFromCustomFormatString_MultipleFractions() {
        Fraction f = new Fraction("2 - 1/3 + 1/2");
        assertEquals(5, f.getNumerator());
        assertEquals(6, f.getDenominator());
        assertEquals(5.8333333333333335, f.doubleValue(), Precision.EPSILON);
    }

    @Test
    public void testFractionSerializedDeserialized() throws IOException, ClassNotFoundException {
        Fraction f = new Fraction(1, 2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(f);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Fraction f2 = (Fraction) ois.readObject();
        assertEquals(1, f2.getNumerator());
        assertEquals(2, f2.getDenominator());
        assertEquals(0.5, f2.doubleValue(), Precision.EPSILON);
    }

}