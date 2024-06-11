package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFactory3;
import org.apache.commons.math3.dfp.DfpUtil;
import org.apache.commons.math3.util.Precision;

public class DfpDecTest {

    private static final DfpField FACTORY = DfpFactory3.getInstance();

    @Test
    public void testNewInstance() {
        DfpDec a = new DfpDec(FACTORY);
        DfpDec b = new DfpDec(FACTORY, 1);
        DfpDec c = new DfpDec(FACTORY, 123);
        DfpDec d = new DfpDec(FACTORY, 123456789L);
        DfpDec e = new DfpDec(FACTORY, 1.2345);
        DfpDec f = new DfpDec(FACTORY, DfpDec.this);

        assertEquals(a.getClass(), DfpDec.class);
        assertEquals(b.intValueExact(), 1);
        assertEquals(c.intValueExact(), 123);
        assertEquals(d.longValueExact(), 123456789L);
        assertEquals(e.doubleValue(), 1.2345, 0.0001);
        assertEquals(f.doubleValue(), 1.2345, 0.0001);
    }

    @Test
    public void testNewInstanceFromDfp() {
        DfpDec a = new DfpDec(FACTORY, new DfpDec(FACTORY, 1.23));
        DfpDec b = new DfpDec(FACTORY, new DfpDec(FACTORY, 1.23).negate());

        assertEquals(a.doubleValue(), 1.23, 0.0001);
        assertEquals(b.doubleValue(), -1.23, 0.0001);
    }

    @Test
    public void testNewInstanceFromString() {
        DfpDec a = new DfpDec(FACTORY, "1.23");
        DfpDec b = new DfpDec(FACTORY, "-123.456");
        DfpDec c = new DfpDec(FACTORY, "12345678901234567890123456789");

        assertEquals(a.doubleValue(), 1.23, 0.0001);
        assertEquals(b.doubleValue(), -123.456, 0.001);
        assertEquals(c.doubleValue(), 12345678901234567890123456789.0, 0.0000001);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec a = new DfpDec(FACTORY, 12345678.9123);
        int decimals = a.getDecimalDigits();

        assertEquals(decimals, 6);
    }

    @Test
    public void testRound() {
        DfpDec a = new DfpDec(FACTORY, 1.23456789);
        int roundingMode = DfpField.ROUND_UP;

        DfpDec rounded = a.round(roundingMode);

        double expected = 1.234568;
        assertEquals(rounded.doubleValue(), expected, 0.000001);

        roundingMode = DfpField.ROUND_DOWN;
        rounded = a.round(roundingMode);

        expected = 1.234567;
        assertEquals(rounded.doubleValue(), expected, 0.000001);

        roundingMode = DfpField.ROUND_HALF_UP;
        rounded = a.round(roundingMode);

        expected = 1.234568;
        assertEquals(rounded.doubleValue(), expected, 0.000001);

        roundingMode = DfpField.ROUND_HALF_DOWN;
        rounded = a.round(roundingMode);

        expected = 1.234567;
        assertEquals(rounded.doubleValue(), expected, 0.000001);

        roundingMode = DfpField.ROUND_HALF_EVEN;
        rounded = a.round(roundingMode);

        expected = 1.234567;
        assertEquals(rounded.doubleValue(), expected, 0.000001);

        roundingMode = DfpField.ROUND_HALF_ODD;
        rounded = a.round(roundingMode);

        expected = 1.234568;
        assertEquals(rounded.doubleValue(), expected, 0.000001);

        roundingMode = DfpField.ROUND_CEIL;
        rounded = a.round(roundingMode);

        expected = 1.234569;
        assertEquals(rounded.doubleValue(), expected, 0.000001);

        roundingMode = DfpField.ROUND_FLOOR;
        rounded = a.round(roundingMode);

        expected = 1.234567;
        assertEquals(rounded.doubleValue(), expected, 0.000001);
    }

    @Test
    public void testNextAfter() {
        DfpDec a = new DfpDec(FACTORY, 1.23456789);
        DfpDec b = new DfpDec(FACTORY, 1.23456789);
        DfpDec c = new DfpDec(FACTORY, Double.NEGATIVE_INFINITY);
        DfpDec d = new DfpDec(FACTORY, Double.POSITIVE_INFINITY);

        DfpDec nextA = a.nextAfter(b);
        DfpDec nextB = b.nextAfter(a);

        assertEquals(a.compareTo(nextB), 1);
        assertEquals(b.compareTo(nextA), -1);

        nextA = a.nextAfter(c);
        nextB = c.nextAfter(a);

        assertEquals(nextA.equals(a), false);
        assertEquals(nextB.equals(a), false);

        nextA = a.nextAfter(d);
        nextB = d.nextAfter(a);

        assertEquals(nextA.equals(a), false);
        assertEquals(nextB.equals(a), false);
    }

    @Test
    public void testNewInstanceFromDfpWithSameRadixDigits() {
        DfpDec a = new DfpDec(FACTORY, new DfpDec(FACTORY, 1.23));
        DfpDec b = new DfpDec(FACTORY, new DfpDec(FACTORY, 1.23).negate());

        assertEquals(a.doubleValue(), 1.23, 0.0001);
        assertEquals(b.doubleValue(), -1.23, 0.0001);
    }

    @Test
    public void testNewInstanceFromDfpWithDifferentRadixDigits() {
        DfpField faFactory = DfpFactory3.getInstance(64, 32);
        DfpDec a = new DfpDec(faFactory, new DfpDec(FACTORY, 1.23));
        DfpDec b = new DfpDec(FACTORY, new DfpDec(faFactory, 1.23).negate());

        assertEquals(a.doubleValue(), 1.23, 0.0001);
        assertEquals(b.doubleValue(), -1.23, 0.0001);
    }

    @Test
    public void testNewInstanceFromDfpWithZeroMantissa() {
        DfpDec a = new DfpDec(FACTORY, new DfpDec(FACTORY, 0));
        DfpDec b = new DfpDec(FACTORY, new DfpDec(FACTORY, 0));

        assertEquals(a.compareTo(b), 0);
    }

    @Test
    public void testNewInstanceFromDfpWithNegativeZeroMantissa() {
        DfpDec a = new DfpDec(FACTORY, new DfpDec(FACTORY, DfpDec.this.negate()).negate());
        DfpDec b = new DfpDec(FACTORY, new DfpDec(FACTORY, DfpDec.this.negate()).negate());

        assertEquals(a.compareTo(b), 0);
    }

    @Test
    public void testNewInstanceFromStringWithInvalidFormat() {
        DfpDec a = null;

        try {
            a = new DfpDec(FACTORY, "1.2.3");
        } catch (NumberFormatException e) {
            // Expected
        }

        assertNull(a);
    }

    @Test
    public void testNewInstanceFromStringWithLeadingZero() {
        DfpDec a = new DfpDec(FACTORY, "0.123");

        assertEquals(a.doubleValue(), 0.123, 0.0001);
    }

    @Test
    public void testNewInstanceFromStringWithTrailingZero() {
        DfpDec a = new DfpDec(FACTORY, "1.230");

        assertEquals(a.doubleValue(), 1.23, 0.0001);
    }

    @Test
    public void testNewInstanceFromStringWithExponent() {
        DfpDec a = new DfpDec(FACTORY, "1.23e5");

        assertEquals(a.doubleValue(), 123500.0, 0.01);
    }

    @Test
    public void testNewInstanceFromStringWithExponentNegative() {
        DfpDec a = new DfpDec(FACTORY, "1.23e-5");

        assertEquals(Precision.isClose(a.doubleValue(), 0.0000123, 0.0000001), true);
    }

    @Test
    public void testNewInstanceFromStringWithExponentVeryLarge() {
        DfpDec a = new DfpDec(FACTORY, "1.23e307");

        assertEquals(Precision.isClose(a.doubleValue(), Double.POSITIVE_INFINITY, 0.0), true);
    }

    @Test
    public void testNewInstanceFromStringWithExponentVerySmall() {
        DfpDec a = new DfpDec(FACTORY, "1.23e-308");

        assertEquals(Precision.isClose(a.doubleValue(), Double.NEGATIVE_INFINITY, 0.0), true);
    }

    @Test
    public void testNewInstanceFromStringWithNaN() {
        DfpDec a = new DfpDec(FACTORY, "NaN");

        assertEquals(a.isNaN(), true);
        assertEquals(a.isNaN(DfpField.RADIX_2), true);
        assertEquals(a.isInfinity(), false);
    }

    @Test
    public void testNewInstanceFromStringWithInfinity() {
        DfpDec a = new DfpDec(FACTORY, "Inf");

        assertEquals(a.isNaN(), false);
        assertEquals(a.isInfinity(), true);
        assertEquals(a.isNegativeInfinity(), false);
    }

    @Test
    public void testNewInstanceFromStringWithNegativeInfinity() {
        DfpDec a = new DfpDec(FACTORY, "-Inf");

        assertEquals(a.isNaN(), false);
        assertEquals(a.isInfinity(), false);
        assertEquals(a.isNegativeInfinity(), true);
    }

}