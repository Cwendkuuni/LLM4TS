package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpField factory = new Dfp34Field();
        Dfp d = new DfpDec(factory);
        Dfp newInstance = d.newInstance();
        assertEquals(d, newInstance);

        Dfp d2 = new DfpDec(factory, 42);
        Dfp newInstance2 = d2.newInstance();
        assertEquals(d2, newInstance2);

        Dfp d3 = new DfpDec(factory, -3.14159265358979323846);
        Dfp newInstance3 = d3.newInstance();
        assertEquals(d3, newInstance3);
    }

    @Test
    public void testNewInstanceWithDifferentRadixDigits() {
        DfpField factory = new Dfp34Field();
        DfpFactory25 dfpFactory25 = new Dfp25Field();
        Dfp d = new DfpDec(factory);
        Dfp newInstance = d.newInstance(dfpFactory25);
        assertNotEquals(d, newInstance);

        Dfp d2 = new DfpDec(factory, 42);
        Dfp newInstance2 = d2.newInstance(dfpFactory25);
        assertNotEquals(d2, newInstance2);
    }

    @Test
    public void testRound() {
        DfpField factory = new Dfp34Field();
        Dfp d = new DfpDec(factory, 1.5);
        assertEquals(1, d.round(0));
        assertEquals(1, d.round(1));
        assertEquals(2, d.round(-1));
        assertEquals(1, d.round(42));
        assertEquals(1, d.round(-42));

        Dfp d2 = new DfpDec(factory, 1.4999999999999999);
        assertEquals(1, d2.round(0));
        assertEquals(2, d2.round(0));

        Dfp d3 = new DfpDec(factory, 1.5000000000000001);
        assertEquals(2, d3.round(0));
        assertEquals(1, d3.round(1));

        Dfp d4 = new DfpDec(factory, 3.14159265358979323846);
        assertEquals(3, d4.round(-2));
        assertEquals(3, d4.round(1));
        assertEquals(4, d4.round(1));
        assertEquals(4, d4.round(1));
        assertEquals(5, d4.round(1));
        assertEquals(5, d4.round(1));
        assertEquals(6, d4.round(1));
        assertEquals(6, d4.round(1));
        assertEquals(7, d4.round(1));
        assertEquals(7, d4.round(1));
        assertEquals(8, d4.round(1));
        assertEquals(8, d4.round(1));
    }

    @Test
    public void testNextAfter() {
        DfpField factory = new Dfp34Field();
        Dfp d = new DfpDec(factory, 0.5);
        Dfp nextAfter = d.nextAfter(new DfpDec(factory, 1));
        assertEquals(1, nextAfter.compareTo(new DfpDec(factory, 1)));

        Dfp d2 = new DfpDec(factory, -0.5);
        Dfp nextAfter2 = d2.nextAfter(new DfpDec(factory, 0));
        assertEquals(-1, nextAfter2.compareTo(new DfpDec(factory, 0)));

        Dfp d3 = new DfpDec(factory, 1.5);
        Dfp nextAfter3 = d3.nextAfter(new DfpDec(factory, 2));
        assertEquals(2, nextAfter3.compareTo(new DfpDec(factory, 2)));

        Dfp d4 = new DfpDec(factory, 2.5);
        Dfp nextAfter4 = d4.nextAfter(new DfpDec(factory, 2));
        assertEquals(3, nextAfter4.compareTo(new DfpDec(factory, 2)));

        Dfp d5 = new DfpDec(factory, 3.5);
        Dfp nextAfter5 = d5.nextAfter(new DfpDec(factory, 3));
        assertEquals(4, nextAfter5.compareTo(new DfpDec(factory, 3)));

        Dfp d6 = new DfpDec(factory, 4.5);
        Dfp nextAfter6 = d6.nextAfter(new DfpDec(factory, 4));
        assertEquals(5, nextAfter6.compareTo(new DfpDec(factory, 4)));

        Dfp d7 = new DfpDec(factory, 5.5);
        Dfp nextAfter7 = d7.nextAfter(new DfpDec(factory, 5));
        assertEquals(6, nextAfter7.compareTo(new DfpDec(factory, 5)));

        Dfp d8 = new DfpDec(factory, 6.5);
        Dfp nextAfter8 = d8.nextAfter(new DfpDec(factory, 6));
        assertEquals(7, nextAfter8.compareTo(new DfpDec(factory, 6)));

        Dfp d9 = new DfpDec(factory, 7.5);
        Dfp nextAfter9 = d9.nextAfter(new DfpDec(factory, 7));
        assertEquals(8, nextAfter9.compareTo(new DfpDec(factory, 7)));

        Dfp d10 = new DfpDec(factory, 8.5);
        Dfp nextAfter10 = d10.nextAfter(new DfpDec(factory, 8));
        assertEquals(9, nextAfter10.compareTo(new DfpDec(factory, 8)));

        Dfp d11 = new DfpDec(factory, 9.5);
        Dfp nextAfter11 = d11.nextAfter(new DfpDec(factory, 9));
        assertEquals(10, nextAfter11.compareTo(new DfpDec(factory, 9)));
    }

    @Test
    public void testNextAfterWithDifferentRadixDigits() {
        DfpField factory = new Dfp34Field();
        DfpFactory25 dfpFactory25 = new Dfp25Field();
        Dfp d = new DfpDec(factory, 0.5);
        Dfp nextAfter = d.nextAfter(new DfpDec(factory, 1));
        Dfp d2 = new DfpDec(dfpFactory25, 0.5);
        Dfp nextAfter2 = d2.nextAfter(new DfpDec(dfpFactory25, 1));
        assertEquals(1, nextAfter.compareTo(nextAfter2));

        Dfp d3 = new DfpDec(factory, -0.5);
        Dfp nextAfter3 = d3.nextAfter(new DfpDec(factory, 0));
        Dfp d4 = new DfpDec(dfpFactory25, -0.5);
        Dfp nextAfter4 = d4.nextAfter(new DfpDec(dfpFactory25, 0));
        assertEquals(-1, nextAfter3.compareTo(nextAfter4));

        Dfp d5 = new DfpDec(factory, 1.5);
        Dfp nextAfter5 = d5.nextAfter(new DfpDec(factory, 2));
        Dfp d6 = new DfpDec(dfpFactory25, 1.5);
        Dfp nextAfter6 = d6.nextAfter(new DfpDec(dfpFactory25, 2));
        assertEquals(2, nextAfter5.compareTo(nextAfter6));

        Dfp d7 = new DfpDec(factory, 2.5);
        Dfp nextAfter7 = d7.nextAfter(new DfpDec(factory, 2));
        Dfp d8 = new DfpDec(dfpFactory25, 2.5);
        Dfp nextAfter8 = d8.nextAfter(new DfpDec(dfpFactory25, 2));
        assertEquals(3, nextAfter7.compareTo(nextAfter8));

        Dfp d9 = new DfpDec(factory, 3.5);
        Dfp nextAfter9 = d9.nextAfter(new DfpDec(factory, 3));
        Dfp d10 = new DfpDec(dfpFactory25, 3.5);
        Dfp nextAfter10 = d10.nextAfter(new DfpDec(dfpFactory25, 3));
        assertEquals(3, nextAfter9.compareTo(nextAfter10));

        Dfp d11 = new DfpDec(factory, 4.5);
        Dfp nextAfter11 = d11.nextAfter(new DfpDec(factory, 4));
        Dfp d12 = new DfpDec(dfpFactory25, 4.5);
        Dfp nextAfter12 = d12.nextAfter(new DfpDec(dfpFactory25, 4));
        assertEquals(4, nextAfter11.compareTo(nextAfter12));
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField factory = new Dfp34Field();
        Dfp d = new DfpDec(factory, 12345.6789);
        int decimalDigits = d.getDecimalDigits();
        assertEquals(21, decimalDigits);

        Dfp d2 = new DfpDec(factory, 12345);
        int decimalDigits2 = d2.getDecimalDigits();
        assertEquals(15, decimalDigits2);

        Dfp d3 = new DfpDec(factory, 123);
        int decimalDigits3 = d3.getDecimalDigits();
        assertEquals(6, decimalDigits3);

        Dfp d4 = new DfpDec(factory, 12);
        int decimalDigits4 = d4.getDecimalDigits();
        assertEquals(3, decimalDigits4);

        Dfp d5 = new DfpDec(factory, 1);
        int decimalDigits5 = d5.getDecimalDigits();
        assertEquals(0, decimalDigits5);
    }
}