package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpDec d1 = new DfpDec(new DfpField(64));
        DfpDec d2 = new DfpDec(new DfpField(64), 123);
        DfpDec d3 = new DfpDec(new DfpDec(new DfpField(64), 456));
        DfpDec d4 = new DfpDec(new DfpDec(new DfpField(64), 789), 0.123);
        DfpDec d5 = new DfpDec(new Dfp("1.23456789E+314"), new DfpField(64));

        assertEquals(0, d1.compareTo(d1.newInstance()));
        assertEquals(123, d2.intValueExact());
        assertEquals(456, d3.intValueExact());
        assertEquals(789, d4.intValueExact(), 0.0);
        assertEquals(1.23456789E+314, d5.doubleValue(), 0.0);
    }

    @Test
    public void testNewInstanceWithInvalidInput() {
        try {
            new DfpDec(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            new DfpDec("abc");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new DfpDec(new DfpField(64), "123.45");
            fail("Should throw NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testNewInstanceWithDifferentRadixDigits() {
        DfpDec d1 = new DfpDec(new DfpField(64));
        DfpDec d2 = new DfpDec(new DfpField(32));

        assertNotEquals(0, d1.compareTo(d2));
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec d = new DfpDec(new DfpField(64));
        int decimalDigits = d.getDecimalDigits();
        assertEquals(2592, decimalDigits);
    }

    @Test
    public void testRound() {
        DfpDec d1 = new DfpDec(new DfpField(64), 123.456);
        DfpDec d2 = d1.newInstance(1);
        assertEquals(123.5, d2.doubleValue(), 0.0);

        DfpDec d3 = new DfpDec(new DfpField(64), -123.456);
        DfpDec d4 = d3.newInstance(-1);
        assertEquals(-123.5, d4.doubleValue(), 0.0);

        DfpDec d5 = new DfpDec(new DfpField(64), 123.556);
        DfpDec d6 = d5.newInstance(0);
        assertEquals(123.5, d6.doubleValue(), 0.0);

        DfpDec d7 = new DfpDec(new DfpField(64), 123.556);
        DfpDec d8 = d7.newInstance(2);
        assertEquals(123.6, d8.doubleValue(), 0.0);

        DfpDec d9 = new DfpDec(new DfpField(64), 123.556);
        DfpDec da = d9.newInstance(-2);
        assertEquals(123.4, da.doubleValue(), 0.0);
    }

    @Test
    public void testRoundWithDifferentRadixDigits() {
        DfpDec d1 = new DfpDec(new DfpField(64), 123.456);
        DfpDec d2 = new DfpDec(new DfpField(32), d1);
        DfpDec d3 = d2.newInstance(1);
        assertEquals(123.5, d3.doubleValue(), 0.0);

        DfpDec d4 = new DfpDec(new DfpField(64), -123.456);
        DfpDec d5 = new DfpDec(new DfpField(32), d4);
        DfpDec d6 = d5.newInstance(-1);
        assertEquals(-123.5, d6.doubleValue(), 0.0);
    }

    @Test
    public void testNextAfter() {
        DfpDec d1 = new DfpDec(new DfpField(64), 1.0);
        DfpDec d2 = d1.nextAfter(new DfpDec(new DfpField(64), 2.0));
        assertEquals(1.000000000000000232830606060601D, d2.doubleValue(), 0.0);

        DfpDec d3 = new DfpDec(new DfpField(64), -1.0);
        DfpDec d4 = d3.nextAfter(new DfpDec(new DfpField(64), 1.0));
        assertEquals(-1.000000000000000322122547160135D, d4.doubleValue(), 0.0);

        DfpDec d5 = new DfpDec(new DfpField(64), 0.5);
        DfpDec d6 = d5.nextAfter(new DfpDec(new DfpField(64), 0.5));
        assertEquals(0.500000000000000298023223810927D, d6.doubleValue(), 0.0);

        DfpDec d7 = new DfpDec(new DfpField(64), 0.5);
        DfpDec d8 = d7.nextAfter(new DfpDec(new DfpField(64), -0.5));
        assertEquals(0.500000000000000342155275829447D, d8.doubleValue(), 0.0);

        DfpDec d9 = new DfpDec(new DfpField(64), -0.5);
        DfpDec da = d9.nextAfter(new DfpDec(new DfpField(64), -0.5));
        assertEquals(-0.500000000000000333066858372603D, da.doubleValue(), 0.0);
    }

    @Test
    public void testNextAfterWithDifferentRadixDigits() {
        DfpDec d1 = new DfpDec(new DfpField(64), 1.0);
        DfpDec d2 = new DfpDec(new DfpField(32), d1);
        DfpDec d3 = d2.nextAfter(new DfpDec(new DfpField(32), 2.0));
        assertEquals(1.000000011920928955078125011523D, d3.doubleValue(), 0.0);

        DfpDec d4 = new DfpDec(new DfpField(64), -1.0);
        DfpDec d5 = new DfpDec(new DfpField(32), d4);
        DfpDec d6 = d5.nextAfter(new DfpDec(new DfpField(32), 1.0));
        assertEquals(-1.000000011920928880921874988516D, d6.doubleValue(), 0.0);
    }
}