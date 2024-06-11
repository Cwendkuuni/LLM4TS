package org.apache.commons.math3.dfp.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpRoundingMode;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

public class DfpDecTest {

    private static final DfpField FIELD = new Dfp128Field();

    @org.junit.Test
    public void testConstructorWithFactory() {
        DfpDec d = new DfpDec(FIELD);
        assertEquals(0.0, d.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testConstructorWithFactoryAndValue() {
        DfpDec d = new DfpDec(FIELD, 123.456);
        assertEquals(123.456, d.doubleValue(), 0.0);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithFactoryAndNaN() {
        new DfpDec(FIELD, Double.NaN);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithFactoryAndInfinity() {
        new DfpDec(FIELD, Double.POSITIVE_INFINITY);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithFactoryAndNegativeInfinity() {
        new DfpDec(FIELD, Double.NEGATIVE_INFINITY);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithInvalidDfp() {
        DfpDec d = new DfpDec();
        Dfp invalidDfp = new Dfp(FIELD, "not-a-number");
        Dfp expectedResult = d.newInstance(invalidDfp);
        assertEquals(expectedResult.classify(), 3);
    }

    @org.junit.Test
    public void testNewInstanceWithValidDfp() {
        DfpDec d = new DfpDec();
        Dfp validDfp = new DfpDec(FIELD, 123.456);
        Dfp result = d.newInstance(validDfp);
        assertEquals(result.doubleValue(), 123.456, 0.0);
    }

    @org.junit.Test
    public void testNewInstanceWithZero() {
        DfpDec d = new DfpDec();
        Dfp result = d.newInstance(0.0);
        assertEquals(0.0, result.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testNewInstanceWithInt() {
        DfpDec d = new DfpDec();
        Dfp result = d.newInstance(123);
        assertEquals(123.0, result.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testNewInstanceWithLong() {
        DfpDec d = new DfpDec();
        Dfp result = d.newInstance(123456789012345L);
        assertEquals(123456789012345.0, result.doubleValue(), 0.0);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNegativeInt() {
        DfpDec d = new DfpDec();
        d.newInstance(-1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNegativeLong() {
        DfpDec d = new DfpDec();
        d.newInstance(-123456789012345L);
    }

    @org.junit.Test
    public void testRoundWithRoundDown() {
        DfpDec d = new DfpDec(FIELD);
        d.set(123.567);
        d.round(DfpRoundingMode.ROUND_DOWN);
        assertEquals(123.5, d.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testRoundWithRoundUp() {
        DfpDec d = new DfpDec(FIELD);
        d.set(123.567);
        d.round(DfpRoundingMode.ROUND_UP);
        assertEquals(124.0, d.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testRoundWithRoundHalfUp() {
        DfpDec d = new DfpDec(FIELD);
        d.set(123.5);
        d.round(DfpRoundingMode.ROUND_HALF_UP);
        assertEquals(124.0, d.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testRoundWithRoundHalfDown() {
        DfpDec d = new DfpDec(FIELD);
        d.set(123.5);
        d.round(DfpRoundingMode.ROUND_HALF_DOWN);
        assertEquals(123.0, d.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testRoundWithRoundHalfEven() {
        DfpDec d = new DfpDec(FIELD);
        d.set(123.5);
        d.round(DfpRoundingMode.ROUND_HALF_EVEN);
        assertEquals(124.0, d.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testRoundWithRoundHalfOdd() {
        DfpDec d = new DfpDec(FIELD);
        d.set(123.5);
        d.round(DfpRoundingMode.ROUND_HALF_ODD);
        assertEquals(123.0, d.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testRoundWithRoundCeil() {
        DfpDec d = new DfpDec(FIELD);
        d.set(-123.5);
        d.round(DfpRoundingMode.ROUND_CEIL);
        assertEquals(-123.0, d.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testRoundWithRoundFloor() {
        DfpDec d = new DfpDec(FIELD);
        d.set(-123.5);
        d.round(DfpRoundingMode.ROUND_FLOOR);
        assertEquals(-124.0, d.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testNextAfterWithZero() {
        DfpDec d = new DfpDec(FIELD);
        Dfp result = d.nextAfter(Dfp.ZERO);
        assertEquals(0.0, result.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testNextAfterWithPositiveNumber() {
        DfpDec d = new DfpDec(FIELD);
        d.set(123.456);
        Dfp result = d.nextAfter(new DfpDec(FIELD, 0.001));
        assertEquals(123.457, result.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testNextAfterWithNegativeNumber() {
        DfpDec d = new DfpDec(FIELD);
        d.set(-123.456);
        Dfp result = d.nextAfter(new DfpDec(FIELD, 0.001));
        assertEquals(-123.455, result.doubleValue(), 0.0);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testNextAfterWithDifferentRadixDigits() {
        DfpDec d1 = new DfpDec(FIELD);
        DfpDec d2 = new DfpDec(new Dfp128Field());
        Dfp result = d1.nextAfter(d2);
    }

    @org.junit.Test
    public void testNextAfterWithNaN() {
        DfpDec d1 = new DfpDec(FIELD);
        DfpDec d2 = new DfpDec(FIELD);
        d2.set(Double.NaN);
        Dfp result = d1.nextAfter(d2);
        assertEquals(Double.NaN, result.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testNextAfterWithInfinity() {
        DfpDec d1 = new DfpDec(FIELD);
        DfpDec d2 = new DfpDec(FIELD);
        d2.set(Double.POSITIVE_INFINITY);
        Dfp result = d1.nextAfter(d2);
        assertEquals(Double.POSITIVE_INFINITY, result.doubleValue(), 0.0);
    }

    @org.junit.Test
    public void testNextAfterWithNegativeInfinity() {
        DfpDec d1 = new DfpDec(FIELD);
        DfpDec d2 = new DfpDec(FIELD);
        d2.set(Double.NEGATIVE_INFINITY);
        Dfp result = d1.nextAfter(d2);
        assertEquals(Double.NEGATIVE_INFINITY, result.doubleValue(), 0.0);
    }
}