package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFactory2;
import org.apache.commons.math3.exception.DivisionByZeroException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Precision;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DfpDecTest {

    private DfpField factory = new Dfp128Field();
    private DfpDec d1, d2, d3, result;

    @org.junit.Before
    public void setup() {
        d1 = new DfpDec(factory, 1.0);
        d2 = new DfpDec(factory, 2.0);
        d3 = new DfpDec(factory, 0.5);
    }

    @org.junit.Test
    public void testNewInstance() {
        DfpDec d = new DfpDec(d1);
        assertEquals(d1, d);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testNewInstanceWithInvalidRadixDigits() {
        new DfpDec(factory, d1.getField(), 5);
    }

    @org.junit.Test
    public void testGetDecimalDigits() {
        assertEquals(48, d1.getDecimalDigits());
    }

    @org.junit.Test
    public void testRound() {
        int rounded = d1.round(0);
        assertEquals(0, rounded);
        BigDecimal bd = new BigDecimal(BigInteger.ONE).pow(128).subtract(BigInteger.ONE).toBigDecimal();
        Dfp expected = factory.create(bd);
        rounded = d1.round(expected.getDigits());
        assertEquals(-1, rounded);
    }

    @org.junit.Test
    public void testNextAfter() {
        Dfp next = d1.nextAfter(d2);
        assertNotEquals(d1, next);
        assertEquals(d2.add(d3), next);
    }

    @org.junit.Test(expected = DivisionByZeroException.class)
    public void testNextAfterWithZero() {
        DfpDec zero = new DfpDec(factory, 0.0);
        zero.nextAfter(d1);
    }

    @org.junit.Test
    public void testNextAfterWithMinusZero() {
        DfpDec zero = new DfpDec(factory, 0.0);
        zero.setNegative();
        Dfp next = zero.nextAfter(d1);
        assertFalse(next.isNegative());
        assertEquals(d1, next);
    }

    @org.junit.Test
    public void testNextAfterWithMaximumValue() {
        Dfp maxValue = DfpFactory2.getInstance(factory.getPrecision()).create(BigDecimal.MAX_VALUE);
        Dfp next = maxValue.nextAfter(maxValue);
        assertTrue(next.isInfinite());
    }

    @org.junit.Test
    public void testNextAfterWithMinusMaximumValue() {
        Dfp maxValue = DfpFactory2.getInstance(factory.getPrecision()).create(BigDecimal.valueOf(-BigInteger.ONE));
        Dfp next = maxValue.nextAfter(maxValue);
        assertTrue(next.isNegative());
        assertTrue(next.isInfinite());
    }

    @org.junit.Test
    public void testNextAfterWithNaN() {
        Dfp naN = new DfpDec(factory, "NaN");
        Dfp next = naN.nextAfter(naN);
        assertTrue(next.isNaN());
    }

    @org.junit.Test
    public void testNewInstanceWithString() {
        DfpDec d = new DfpDec(factory, "3.14159265358979323846");
        assertEquals(d1.setValue(3.14159265358979323846), d);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testNewInstanceWithInvalidString() {
        new DfpDec(factory, "abcd");
    }
}