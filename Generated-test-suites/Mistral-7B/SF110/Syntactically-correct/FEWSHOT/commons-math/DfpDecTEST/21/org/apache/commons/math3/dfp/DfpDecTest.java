package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory);
        DfpDec d2 = d1.newInstance();
        assertEquals(d1, d2);

        DfpDec d3 = new DfpDec(factory, 1);
        DfpDec d4 = d3.newInstance();
        assertEquals(d3, d4);

        DfpDec d5 = new DfpDec(factory, 0x123456789ABCDEF0L);
        DfpDec d6 = d5.newInstance();
        assertEquals(d5, d6);

        DfpDec d7 = new DfpDec(factory, 3.14159265358979323846);
        DfpDec d8 = d7.newInstance();
        assertEquals(d7, d8);

        DfpDec d9 = new DfpDec(factory, new DfpDec(factory, 1));
        DfpDec d10 = d9.newInstance();
        assertEquals(d9, d10);

        DfpDec d11 = new DfpDec(factory, new String("12345678901234567890"));
        DfpDec d12 = d11.newInstance();
        assertEquals(d11, d12);

        DfpDec d13 = new DfpDec(factory, (byte) 1, (byte) 3);
        DfpDec d14 = d13.newInstance();
        assertEquals(d13, d14);
    }

    @Test
    public void testNewInstanceWithDifferentRadixDigits() {
        DfpField factory1 = new Dfp128Field();
        DfpField factory2 = new Dfp64Field();
        DfpDec d1 = new DfpDec(factory1);
        try {
            DfpDec d2 = d1.newInstance(factory2);
            assert false; // should throw IllegalArgumentException
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRounding() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 1.456789);
        int roundingMode = RoundingMode.HALF_UP.getCode();
        DfpDec d2 = d1.newInstance(roundingMode);
        assertEquals(1.457, d2.doubleValue(), 0.000000001);

        DfpDec d3 = new DfpDec(factory, 1.543210);
        DfpDec d4 = d3.newInstance(roundingMode);
        assertEquals(1.544, d4.doubleValue(), 0.000000001);

        DfpDec d5 = new DfpDec(factory, 2.6789012);
        DfpDec d6 = d5.newInstance(roundingMode);
        assertEquals(2.679, d6.doubleValue(), 0.000000001);

        DfpDec d7 = new DfpDec(factory, 3.14159265358979323846);
        DfpDec d8 = d7.newInstance(roundingMode);
        assertEquals(3.1416, d8.doubleValue(), 0.000000001);

        DfpDec d9 = new DfpDec(factory, 4.5678901234567890123456);
        DfpDec d10 = d9.newInstance(roundingMode);
        assertEquals(4.5679, d10.doubleValue(), 0.000000001);
    }

    @Test
    public void testNextAfter() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 0.5);
        DfpDec d2 = new DfpDec(factory, 0.6);
        DfpDec d3 = d1.nextAfter(d2);
        assertEquals(0.6, d3.doubleValue(), 0.000000001);

        DfpDec d4 = new DfpDec(factory, -0.5);
        DfpDec d5 = new DfpDec(factory, -0.6);
        DfpDec d6 = d4.nextAfter(d5);
        assertEquals(-0.6, d6.doubleValue(), 0.000000001);

        DfpDec d7 = new DfpDec(factory, 1.234567890123456789012345678901234567890L);
        DfpDec d8 = new DfpDec(factory, 1.234567890123456789012345678901234567891L);
        DfpDec d9 = d7.nextAfter(d8);
        assertEquals(1.234567890123456789012345678901234567891L, d9.doubleValue());

        DfpDec d10 = new DfpDec(factory, Double.POSITIVE_INFINITY);
        DfpDec d11 = d10.nextAfter(d10);
        assertNull(d11);

        DfpDec d12 = new DfpDec(factory, Double.NEGATIVE_INFINITY);
        DfpDec d13 = d12.nextAfter(d12);
        assertNull(d13);

        DfpDec d14 = new DfpDec(factory, Double.NaN);
        DfpDec d15 = d14.nextAfter(d14);
        assertNull(d15);

        DfpDec d16 = new DfpDec(factory, new DfpDec(factory, 0.1234567890123456789012345678901234567890L));
        DfpDec d17 = new DfpDec(factory, new DfpDec(factory, 0x1p-127)); // denormalized number
        DfpDec d18 = d16.nextAfter(d17);
        assertEquals(d17.add(d16).doubleValue(), d18.doubleValue(), 0.000000001);
    }

    @Test
    public void testNextAfterWithDifferentRadixDigits() {
        DfpField factory1 = new Dfp128Field();
        DfpField factory2 = new Dfp64Field();
        DfpDec d1 = new DfpDec(factory1, 1.234567890123456789012345678901234567890L);
        DfpDec d2 = new DfpDec(factory2, 1.234567890123456789012345678901234567890L);
        DfpDec d3 = d1.nextAfter(d2);
        assertEquals(d2.nextAfter(d1).doubleValue(), d3.doubleValue(), 0.000000001);
    }
}