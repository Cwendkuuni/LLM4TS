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

        DfpDec d3 = new DfpDec(field, 123);
        DfpDec d4 = d3.newInstance();
        assertEquals(d3, d4);

        DfpDec d5 = new DfpDec(field, 123.456);
        DfpDec d6 = d5.newInstance();
        assertEquals(d5, d6);

        DfpDec d7 = new DfpDec(field, 0x123456789ABCDEF0L);
        DfpDec d8 = d7.newInstance();
        assertEquals(d7, d8);
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpField field = new Dfp32Field();
        DfpDec d1 = new DfpDec(field, new DfpDec(field, 123));
        DfpDec d2 = d1.newInstance(new DfpDec(field, 123));
        assertEquals(d1, d2);
    }

    @Test
    public void testNewInstanceWithString() {
        DfpField field = new Dfp32Field();
        DfpDec d1 = new DfpDec(field, "123.456");
        DfpDec d2 = d1.newInstance("123.456");
        assertEquals(d1, d2);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField field = new Dfp32Field();
        DfpDec d = new DfpDec(field, 12345);
        int decimalDigits = d.getDecimalDigits();
        assertEquals(4, decimalDigits);
    }

    @Test
    public void testRound() {
        DfpField field = new Dfp32Field();
        DfpDec d = new DfpDec(field, 123.456);
        int rounded = d.round(0);
        assertEquals(0, rounded);

        DfpDec d1 = new DfpDec(field, 123.5);
        int rounded1 = d1.round(0);
        assertEquals(1, rounded1);

        DfpDec d2 = new DfpDec(field, 123.4);
        int rounded2 = d2.round(0);
        assertEquals(0, rounded2);

        DfpDec d3 = new DfpDec(field, 123.543);
        int rounded3 = d3.round(1);
        assertEquals(146, rounded3);

        DfpDec d4 = new DfpDec(field, 123.456);
        int rounded4 = d4.round(1);
        assertEquals(1465, rounded4);

        DfpDec d5 = new DfpDec(field, 123.543);
        int rounded5 = d5.round(1, RoundingMode.UP);
        assertEquals(1467, rounded5);

        DfpDec d6 = new DfpDec(field, 123.543);
        int rounded6 = d6.round(1, RoundingMode.DOWN);
        assertEquals(1466, rounded6);

        DfpDec d7 = new DfpDec(field, 123.543);
        int rounded7 = d7.round(1, RoundingMode.HALF_UP);
        assertEquals(1467, rounded7);

        DfpDec d8 = new DfpDec(field, 123.543);
        int rounded8 = d8.round(1, RoundingMode.HALF_DOWN);
        assertEquals(1466, rounded8);

        DfpDec d9 = new DfpDec(field, 123.543);
        int rounded9 = d9.round(1, RoundingMode.HALF_EVEN);
        assertEquals(1466, rounded9);

        DfpDec d10 = new DfpDec(field, 123.543);
        int rounded10 = d10.round(1, RoundingMode.HALF_ODD);
        assertEquals(1467, rounded10);

        DfpDec d11 = new DfpDec(field, 123.543);
        int rounded11 = d11.round(1, RoundingMode.CEIL);
        assertEquals(1467, rounded11);

        DfpDec d12 = new DfpDec(field, 123.543);
        int rounded12 = d12.round(1, RoundingMode.FLOOR);
        assertEquals(1466, rounded12);
    }

    @Test
    public void testNextAfter() {
        DfpField field = new Dfp32Field();
        DfpDec d1 = new DfpDec(field, 0.1);
        DfpDec d2 = new DfpDec(field, 0.2);
        DfpDec result = d1.nextAfter(d2);
        assertEquals(new DfpDec(field, 0.2), result);

        DfpDec d3 = new DfpDec(field, Long.MAX_VALUE);
        DfpDec result2 = d1.nextAfter(d3);
        assertEquals(new DfpDec(field, Long.MAX_VALUE + 0.1), result2);

        DfpDec d4 = new DfpDec(field, Long.MIN_VALUE);
        DfpDec result3 = d1.nextAfter(d4);
        assertEquals(new DfpDec(field, Long.MIN_VALUE), result3);

        DfpDec d5 = new DfpDec(field, 0);
        DfpDec result4 = d1.nextAfter(d5);
        assertNull(result4);

        DfpDec d6 = new DfpDec(field, 1.0 / 3.0);
        DfpDec result5 = d1.nextAfter(d6);
        assertEquals(new DfpDec(field, 1.0 / 3.0 + 0.000001), result5);

        DfpDec d7 = new DfpDec(field, 1.0 / 0.0);
        DfpDec result6 = d1.nextAfter(d7);
        assertNull(result6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithInvalidField() {
        DfpDec d = new DfpDec(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNegativeDecimalDigits() {
        DfpField field = new Dfp32Field();
        DfpDec d = new DfpDec(field, 123.456);
        d.round(-1);
        assertNull(d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRoundWithInvalidRoundingMode() {
        DfpField field = new Dfp32Field();
        DfpDec d = new DfpDec(field, 123.456);
        d.round(Integer.MAX_VALUE);
        assertNull(d);
    }
}