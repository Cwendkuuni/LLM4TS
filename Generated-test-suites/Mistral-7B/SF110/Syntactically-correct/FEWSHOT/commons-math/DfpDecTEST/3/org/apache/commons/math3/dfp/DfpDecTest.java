package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory);
        DfpDec d2 = d1.newInstance();
        assertEquals(d1, d2);

        Dfp dec = new Dfp(factory, 123.45);
        DfpDec d3 = dec.newInstance();
        assertEquals(dec, d3);
    }

    @Test
    public void testNewInstanceWithValue() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 123.45);
        DfpDec d2 = d1.newInstance();
        assertEquals(d1, d2);

        Dfp dec = new Dfp(factory, 123.45);
        DfpDec d3 = dec.newInstance();
        assertEquals(dec, d3);
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpField factory = new Dfp128Field();
        Dfp d1 = new Dfp(factory, 123.45);
        DfpDec d2 = d1.newInstance();
        assertEquals(d1, d2);

        DfpDec d3 = new DfpDec(factory, d1);
        assertEquals(d1, d3);
    }

    @Test
    public void testNewInstanceWithInvalidRadixDigits() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 123.45);
        DfpField factory2 = new Dfp64Field();
        DfpDec d2 = d1.newInstance();
        assertNotEquals(d1, d2);
    }

    @Test
    public void testRound() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 1.5);
        assertEquals(1, d1.round(0));
        assertEquals(1, d1.round(Math.round(1.5 * Math.pow(10, 4))));
        assertEquals(2, d1.round(Math.round(1.5 * Math.pow(10, 4)) + 0.5));
        assertEquals(2, d1.round(Math.round(1.5 * Math.pow(10, 4)) + 0.499999));
        assertEquals(0, d1.round(-1));
        assertEquals(0, d1.round(-Math.round(1.5 * Math.pow(10, 4))));
        assertEquals(0, d1.round(-Math.round(1.5 * Math.pow(10, 4)) - 0.5));
        assertEquals(0, d1.round(-Math.round(1.5 * Math.pow(10, 4)) - 0.499999));

        DfpDec d2 = new DfpDec(factory, -1.5);
        assertEquals(-1, d2.round(0));
        assertEquals(-1, d2.round(Math.round(-1.5 * Math.pow(10, 4))));
        assertEquals(-2, d2.round(Math.round(-1.5 * Math.pow(10, 4)) - 0.5));
        assertEquals(-2, d2.round(Math.round(-1.5 * Math.pow(10, 4)) - 0.499999));
        assertEquals(0, d2.round(1));
        assertEquals(0, d2.round(Math.round(-1.5 * Math.pow(10, 4)) + 0.5));
        assertEquals(0, d2.round(Math.round(-1.5 * Math.pow(10, 4)) + 0.499999));
    }

    @Test
    public void testRoundWithSpecialValues() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, Double.NEGATIVE_INFINITY);
        assertEquals(0, d1.round(0));
        assertEquals(0, d1.round(-1));
        assertEquals(1, d1.round(-16));
        assertEquals(1, d1.round(-16 + Math.ulp(Double.NEGATIVE_INFINITY)));

        DfpDec d2 = new DfpDec(factory, Double.POSITIVE_INFINITY);
        assertEquals(0, d2.round(0));
        assertEquals(0, d2.round(1));
        assertEquals(1, d2.round(16));
        assertEquals(1, d2.round(16 + Math.ulp(Double.POSITIVE_INFINITY)));

        DfpDec d3 = new DfpDec(factory, NaN.QUANTIZABLE);
        assertEquals(0, d3.round(0));
        assertEquals(0, d3.round(1));
        assertNull(d3.round(16));

        DfpDec d4 = new DfpDec(factory, NaN.SNAN);
        assertEquals(0, d4.round(0));
        assertEquals(0, d4.round(1));
        assertNull(d4.round(16));
    }

    @Test
    public void testIntLog10() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 0.0000000000000001);
        assertEquals(-2307, d1.intLog10());

        DfpDec d2 = new DfpDec(factory, 1000000000000.0);
        assertEquals(20, d2.intLog10());

        DfpDec d3 = new DfpDec(factory, 123.45);
        assertEquals(2.1763269568543, d3.intLog10(), 0.000001);
    }

    @Test
    public void testPower10() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 2.0);
        DfpDec d2 = d1.power10(3);
        assertEquals(8192.0, d2.doubleValueExact());

        DfpDec d3 = new DfpDec(factory, 0.5);
        DfpDec d4 = d3.power10(-3);
        assertEquals(0.125, d4.doubleValueExact());
    }

    @Test
    public void testNextAfter() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 1.0 / 3.0);
        DfpDec d2 = d1.nextAfter(new DfpDec(factory, 0.0));
        assertEquals(0.333333333333333333333333333333333333333, d2.doubleValueExact(), 0.0000000000000000000000000000000000000001);

        DfpDec d3 = new DfpDec(factory, 1.0 / 3.0);
        DfpDec d4 = d3.nextAfter(new DfpDec(factory, 1.0));
        assertEquals(1.0 / 3.0 + 0.333333333333333333333333333333333333333, d4.doubleValueExact(), 0.0000000000000000000000000000000000000001);

        DfpDec d5 = new DfpDec(factory, -1.0 / 3.0);
        DfpDec d6 = d5.nextAfter(new DfpDec(factory, -0.0));
        assertEquals(-0.333333333333333333333333333333333333333, d6.doubleValueExact(), 0.0000000000000000000000000000000000000001);

        DfpDec d7 = new DfpDec(factory, -1.0 / 3.0);
        DfpDec d8 = d7.nextAfter(new DfpDec(factory, -1.0));
        assertEquals(-1.0 / 3.0 - 0.333333333333333333333333333333333333333, d8.doubleValueExact(), 0.0000000000000000000000000000000000000001);
    }
}