package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpField field = new Dfp32Field();
        DfpDec d1 = new DfpDec(field);
        DfpDec d2 = d1.newInstance();
        assertEquals(d1, d2);

        Dfp d3 = new Dfp(1.23456);
        DfpDec d4 = new DfpDec(d3);
        DfpDec d5 = d4.newInstance();
        assertEquals(d4, d5);

        DfpDec d6 = new DfpDec(field, "123.456");
        DfpDec d7 = d6.newInstance();
        assertEquals(d6, d7);
    }

    @Test
    public void testNewInstanceWithValue() {
        DfpField field = new Dfp32Field();
        DfpDec d1 = new DfpDec(field, 123);
        DfpDec d2 = d1.newInstance();
        assertEquals(d1, d2);

        DfpDec d3 = new DfpDec(field, 123.456);
        DfpDec d4 = d3.newInstance();
        assertEquals(d3, d4);
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpField field = new Dfp32Field();
        DfpDec d1 = new DfpDec(field, new Dfp(123.456));
        DfpDec d2 = d1.newInstance();
        assertEquals(d1, d2);
    }

    @Test
    public void testNewInstanceWithDifferentRadixDigits() {
        DfpDec d1 = new DfpDec(new Dfp128Field(), 123);
        DfpDec d2 = new DfpDec(new Dfp64Field(), 123);

        try {
            DfpDec d3 = d1.newInstance(d2);
            assert false;
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRound() {
        DfpField field = new Dfp32Field();
        DfpDec d1 = new DfpDec(field, 123.456789);
        int roundingMode = DfpField.ROUND_HALF_UP;
        int result = d1.round(roundingMode);
        assertEquals(16, result); // NaN is returned for invalid rounding modes

        DfpDec d2 = d1.newInstance();
        assertEquals(d1, d2);

        DfpDec d3 = d2.round(roundingMode);
        double expected = 123.457;
        assertEquals(expected, d3.doubleValue(), 0.00001);

        roundingMode = DfpField.ROUND_DOWN;
        result = d1.round(roundingMode);
        assertEquals(0, result);

        d2 = d1.newInstance();
        assertEquals(d1, d2);

        d3 = d2.round(roundingMode);
        expected = 123.456;
        assertEquals(expected, d3.doubleValue(), 0.00001);

        roundingMode = DfpField.ROUND_CEIL;
        result = d1.round(roundingMode);
        assertEquals(16, result); // NaN is returned for invalid rounding modes

        d2 = d1.newInstance();
        assertEquals(d1, d2);

        d3 = d2.round(roundingMode);
        expected = 124.0;
        assertEquals(expected, d3.doubleValue(), 0.0);

        roundingMode = DfpField.ROUND_FLOOR;
        result = d1.round(roundingMode);
        assertEquals(0, result);

        d2 = d1.newInstance();
        assertEquals(d1, d2);

        d3 = d2.round(roundingMode);
        expected = 123.0;
        assertEquals(expected, d3.doubleValue(), 0.0);
    }

    @Test
    public void testNextAfter() {
        DfpField field = new Dfp32Field();
        DfpDec d1 = new DfpDec(field, 0.123456789);
        DfpDec d2 = new DfpDec(field, 0.123456789);
        DfpDec d3 = d1.nextAfter(d2);
        double expected = 0.1234568;
        assertEquals(expected, d3.doubleValue(), 0.00001);

        DfpDec d4 = new DfpDec(field, 0.123456789);
        DfpDec d5 = new DfpDec(field, 1.0 / 0.0);
        DfpDec d6 = d4.nextAfter(d5);
        assertNull(d6);

        DfpDec d7 = new DfpDec(field, -0.123456789);
        DfpDec d8 = new DfpDec(field, -0.123456789);
        DfpDec d9 = d7.nextAfter(d8);
        double expectedNegative = -0.1234568;
        assertEquals(expectedNegative, d9.doubleValue(), 0.00001);

        DfpDec d10 = new DfpDec(field, -123456789.0);
        DfpDec d11 = new DfpDec(field, -123456789.0);
        DfpDec d12 = d10.nextAfter(d11);
        assertEquals(d11, d12); // nextAfter() does not change the value for negative numbers

        DfpDec d13 = new DfpDec(field, -123456789.0);
        DfpDec d14 = new DfpDec(field, -123456788.0);
        DfpDec d15 = d13.nextAfter(d14);
        assertEquals(d14, d15); // nextAfter() does not change the value for negative numbers with smaller magnitude
    }
}