package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpField field = new Dfp128Field();
        DfpDec d1 = new DfpDec(field);
        DfpDec d2 = d1.newInstance();
        assertEquals(d1, d2);

        DfpDec d3 = new DfpDec(field, 1);
        DfpDec d4 = d3.newInstance();
        assertEquals(d3, d4);

        DfpDec d5 = new DfpDec(field, 100);
        DfpDec d6 = d5.newInstance();
        assertEquals(d5, d6);

        DfpDec d7 = new DfpDec(field, 1.23);
        DfpDec d8 = d7.newInstance();
        assertEquals(d7, d8);

        DfpDec d9 = new DfpDec(field, new DfpDec(field, 1.23));
        DfpDec d10 = d9.newInstance();
        assertEquals(d9, d10);

        DfpDec d11 = new DfpDec(field, "1.23");
        DfpDec d12 = d11.newInstance();
        assertEquals(d11, d12);

        DfpDec d13 = new DfpDec(field, (byte) 1, (byte) 3);
        DfpDec d14 = d13.newInstance();
        assertEquals(d13, d14);
    }

    @Test
    public void testNewInstanceWithDifferentRadixDigits() {
        DfpField field1 = new Dfp128Field();
        DfpField field2 = new Dfp16Field();

        DfpDec d1 = new DfpDec(field1);
        assertNull(d1.newInstance(field2));
    }

    @Test
    public void testRound() {
        DfpField field = new Dfp128Field();
        DfpDec d = new DfpDec(field, 1.234);

        int roundingModes[] = {DfpField.ROUND_DOWN, DfpField.ROUND_UP, DfpField.ROUND_HALF_UP, DfpField.ROUND_HALF_DOWN, DfpField.ROUND_HALF_EVEN, DfpField.ROUND_HALF_ODD, DfpField.ROUND_CEIL, DfpField.ROUND_FLOOR};

        for (int roundingMode : roundingModes) {
            DfpDec rounded = d.newInstance(roundingMode);
            round(rounded, 2);
            assertEquals(d.round(roundingMode), rounded);
        }
    }

    @Test
    public void testNextAfter() {
        DfpField field = new Dfp128Field();
        DfpDec d1 = new DfpDec(field, 0.1);
        DfpDec d2 = new DfpDec(field, 0.2);
        DfpDec d3 = new DfpDec(field, 0.3);
        DfpDec d4 = new DfpDec(field, 1.2);

        DfpDec next = d1.nextAfter(d2);
        assertEquals(d2, d1.add(next));

        next = d2.nextAfter(d3);
        assertEquals(d3, d2.add(next));

        next = d3.nextAfter(d4);
        assertTrue(d4.compareTo(d3.add(next)) > 0);

        next = d4.nextAfter(d1);
        assertTrue(d1.compareTo(d4.subtract(next)) < 0);
    }

    @Test
    public void testNextAfterWithZero() {
        DfpField field = new Dfp128Field();
        DfpDec d1 = new DfpDec(field, 0);

        DfpDec next = d1.nextAfter(d1);
        assertTrue(next.compareTo(d1) > 0);
    }

    @Test(expected = ArithmeticException.class)
    public void testNextAfterWithNaN() {
        DfpField field = new Dfp128Field();
        DfpDec d1 = new DfpDec(field, Dfp.NaN);
        DfpDec d2 = new DfpDec(field, Dfp.NaN);
        d1.nextAfter(d2);
    }

    @Test
    public void testNextAfterWithInfinities() {
        DfpField field = new Dfp128Field();
        DfpDec d1 = new DfpDec(field, Dfp.POSITIVE_INFINITY);
        DfpDec d2 = new DfpDec(field, Dfp.NEGATIVE_INFINITY);

        DfpDec next1 = d1.nextAfter(d1);
        assertEquals(d1, next1);

        DfpDec next2 = d2.nextAfter(d2);
        assertEquals(d2, next2);

        DfpDec next3 = d1.nextAfter(d2);
        assertEquals(Dfp.NEGATIVE_INFINITY, next3);

        DfpDec next4 = d2.nextAfter(d1);
        assertEquals(Dfp.POSITIVE_INFINITY, next4);
    }

    @Test
    public void testNextAfterWithSpecialValues() {
        DfpField field = new Dfp128Field();
        DfpDec d1 = new DfpDec(field, Dfp.POSITIVE_INFINITY);
        DfpDec d2 = new DfpDec(field, Dfp.NEGATIVE_INFINITY);
        DfpDec d3 = new DfpDec(field, Dfp.NaN);
        DfpDec d4 = new DfpDec(field, 0);

        DfpDec next1 = d1.nextAfter(d4);
        assertEquals(d4, d1.subtract(next1));

        DfpDec next2 = d2.nextAfter(d4);
        assertEquals(d4, d2.subtract(next2));

        DfpDec next3 = d1.nextAfter(d3);
        assertNull(next3);

        DfpDec next4 = d3.nextAfter(d1);
        assertNull(next4);

        DfpDec next5 = d2.nextAfter(d3);
        assertNull(next5);

        DfpDec next6 = d3.nextAfter(d2);
        assertNull(next6);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField field = new Dfp128Field();
        DfpDec d = new DfpDec(field, 1234.5678);
        int decimalDigits = d.getDecimalDigits();
        assertEquals(31, decimalDigits);
    }
}