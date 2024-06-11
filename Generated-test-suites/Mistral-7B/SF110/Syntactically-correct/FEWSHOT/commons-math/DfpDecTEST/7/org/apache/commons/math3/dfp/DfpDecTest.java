package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testNewInstance() {
        DfpField factory = new Dfp32Field();
        DfpDec d1 = new DfpDec(factory);
        DfpDec d2 = new DfpDec(factory, 1);
        DfpDec d3 = new DfpDec(factory, -1);
        DfpDec d4 = new DfpDec(factory, 0.5);
        DfpDec d5 = new DfpDec(factory, new DfpDec(factory, 3.14));

        assertEquals(d1.getClass(), DfpDec.class);
        assertEquals(d2.doubleValueExact(), 1.0);
        assertEquals(d3.doubleValueExact(), -1.0);
        assertEquals(d4.doubleValueExact(), 0.5, 0.0001);
        assertEquals(d5.doubleValueExact(), 3.14, 0.0001);
    }

    @Test
    public void testNewInstanceWithRounding() {
        DfpField factory = new Dfp32Field();
        DfpDec d1 = new DfpDec(factory, 1.5);
        DfpDec d2 = new DfpDec(factory, new DfpDec(factory, 0.5), factory.getRoundingMode(RoundingMode.DOWN));
        DfpDec d3 = new DfpDec(factory, new DfpDec(factory, 0.5), factory.getRoundingMode(RoundingMode.UP));
        DfpDec d4 = new DfpDec(factory, new DfpDec(factory, 0.5), factory.getRoundingMode(RoundingMode.HALF_UP));
        DfpDec d5 = new DfpDec(factory, new DfpDec(factory, 0.5), factory.getRoundingMode(RoundingMode.HALF_DOWN));

        assertEquals(d1.doubleValueExact(), 1.5, 0.0001);
        assertEquals(d2.doubleValueExact(), 1.0, 0.0001);
        assertEquals(d3.doubleValueExact(), 2.0, 0.0001);
        assertEquals(d4.doubleValueExact(), 1.5, 0.0001);
        assertEquals(d5.doubleValueExact(), 2.0, 0.0001);
    }

    @Test
    public void testRounding() {
        DfpField factory = new Dfp32Field();
        DfpDec d1 = new DfpDec(factory, 1.49999999999);
        DfpDec d2 = new DfpDec(factory, new DfpDec(factory, 0.5), factory.getRoundingMode(RoundingMode.UP));

        assertEquals(d1.round(0).doubleValueExact(), 1.5, 0.0001);
        assertEquals(d2.round(0).doubleValueExact(), 2.0, 0.0001);

        DfpDec d3 = new DfpDec(factory, new DfpDec(factory, 1.50000000001));
        assertEquals(d3.round(0).doubleValueExact(), 2.0, 0.0001);
    }

    @Test
    public void testNextAfter() {
        DfpField factory = new Dfp32Field();
        DfpDec d1 = new DfpDec(factory, 0.5);
        DfpDec d2 = new DfpDec(factory, 1.1);
        DfpDec d3 = new DfpDec(factory, -0.5);
        DfpDec d4 = new DfpDec(factory, new DfpDec(factory, 1.1), factory.getRoundingMode(RoundingMode.UP));
        DfpDec d5 = new DfpDec(factory, new DfpDec(factory, -1.1), factory.getRoundingMode(RoundingMode.DOWN));

        DfpDec next1 = d1.nextAfter(d2);
        assertEquals(next1.doubleValueExact(), 1.1, 0.0001);

        DfpDec next2 = d1.nextAfter(d1);
        assertEquals(next2.doubleValueExact(), 1.0, 0.0001);

        DfpDec next3 = d3.nextAfter(d1);
        assertEquals(next3.doubleValueExact(), 0.6, 0.0001);

        DfpDec next4 = d4.nextAfter(d1);
        assertEquals(next4.doubleValueExact(), 2.0, 0.0001);

        DfpDec next5 = d5.nextAfter(d1);
        assertEquals(next5.doubleValueExact(), 0.5, 0.0001);

        DfpDec zero = new DfpDec(factory, 0.0);
        DfpDec negZero = new DfpDec(factory, 0.0, (byte) 3);
        assertEquals(zero.nextAfter(zero), zero);
        assertFalse(zero.nextAfter(negZero).isNaN());
        assertEquals(negZero.nextAfter(zero), negZero);
        assertFalse(negZero.nextAfter(negZero).isNaN());
    }

    @Test
    public void testPower10() {
        DfpField factory = new Dfp32Field();
        DfpDec ten = new DfpDec(factory, 10.0);

        DfpDec exp1 = new DfpDec(factory, 1);
        assertEquals(ten.power10(exp1.intValueExact()), new DfpDec(factory, 10.0));

        DfpDec exp2 = new DfpDec(factory, -1);
        assertEquals(ten.power10(exp2.intValueExact()), new DfpDec(factory, 0.1));

        DfpDec exp3 = new DfpDec(factory, 3);
        assertEquals(ten.power10(exp3.intValueExact()).doubleValueExact(), 1000.0, 0.0001);

        DfpDec exp4 = new DfpDec(factory, 233);
        assertEquals(ten.power10(exp4.intValueExact()).doubleValueExact(), 3.0402756466065e+77, 0.0001);
    }

    @Test
    public void testIntLog10() {
        DfpField factory = new Dfp32Field();
        DfpDec d1 = new DfpDec(factory, 0.000000001);
        DfpDec d2 = new DfpDec(factory, 10.0);
        DfpDec d3 = new DfpDec(factory, 100.0);
        DfpDec d4 = new DfpDec(factory, 1000000.0);

        assertEquals(d1.intLog10().intValueExact(), -23);
        assertEquals(d2.intLog10().intValueExact(), 0);
        assertEquals(d3.intLog10().intValueExact(), 3);
        assertEquals(d4.intLog10().intValueExact(), 6);
    }

    @Test
    public void testNextAfterWithDifferentRadixDigits() {
        DfpField factory1 = new Dfp32Field();
        DfpField factory2 = new Dfp64Field();
        DfpDec d1 = new DfpDec(factory1, 1.0);
        DfpDec d2 = new DfpDec(factory2, 1.0);

        DfpDec next = d1.nextAfter(d2);
        assertEquals(next.getClass(), DfpDec.class);
        assertNotEquals(next, d1);
        assertNotEquals(next, d2);

        DfpDec zero = new DfpDec(factory1, 0.0);
        DfpDec negZero = new DfpDec(factory1, 0.0, (byte) 3);
        DfpDec nextZero = zero.nextAfter(negZero);
        assertEquals(nextZero.isNaN(), true);

        DfpDec d3 = new DfpDec(factory1, 0.5);
        DfpDec d4 = new DfpDec(factory2, 1.1);
        DfpDec next3 = d3.nextAfter(d4);
        assertNotEquals(next3, d3);
        assertNotEquals(next3, d4);
    }
}