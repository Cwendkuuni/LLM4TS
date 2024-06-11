package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory);
        DfpDec d2 = new DfpDec(factory.newInstance(123L));
        DfpDec d3 = new DfpDec(factory.newInstance(123.45));
        DfpDec d4 = new DfpDec(factory, "123.45");
        DfpDec d5 = new DfpDec(factory, "0x1.2p3");
        DfpDec d6 = new DfpDec(factory, "NaN");
        DfpDec d7 = new DfpDec(factory, "inf");

        assertEquals(d1.getClass(), DfpDec.class);
        assertEquals(d2.getClass(), DfpDec.class);
        assertEquals(d3.getClass(), DfpDec.class);
        assertEquals(d4.getClass(), DfpDec.class);
        assertEquals(d5.getClass(), DfpDec.class);
        assertEquals(d6.getClass(), DfpDec.class);
        assertEquals(d7.getClass(), DfpDec.class);

        assertEquals(d1.getRadixDigits(), factory.getRadixDigits());
        assertEquals(d2.doubleValueExact(), 123.0);
        assertEquals(d3.doubleValueExact(), 123.45);
        assertEquals(d4.doubleValueExact(), 123.45);
        assertEquals(d5.doubleValueExact(), 1.2 * Math.pow(10, 3));
        assertNull(d6.doubleValueExact());
        assertNotEquals(d7.doubleValueExact(), Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNewInstanceWithRounding() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 123.445);
        DfpDec d2 = new DfpDec(factory, 123.45);

        assertEquals(d1.doubleValueExact(), 123.445, 0.00001);
        assertEquals(d2.doubleValueExact(), 123.45);

        DfpDec d3 = new DfpDec(factory, new DfpDec(factory, 123.445), 0);
        assertEquals(d3.doubleValueExact(), 123.45);

        DfpDec d4 = new DfpDec(factory, new DfpDec(factory, 123.445), 1);
        assertEquals(d4.doubleValueExact(), 123.45, 0.00001);

        DfpDec d5 = new DfpDec(factory, new DfpDec(factory, 123.445), 2);
        assertEquals(d5.doubleValueExact(), 123.45, 0.000001);

        DfpDec d6 = new DfpDec(factory, new DfpDec(factory, 123.445), 3);
        assertEquals(d6.doubleValueExact(), 123.45, 0.0000001);
    }

    @Test
    public void testRound() {
        DfpField factory = new Dfp128Field();
        DfpDec d = new DfpDec(factory, 123.445);

        final int in = 2;
        int r = d.round(in);

        DfpDec expected = new DfpDec(factory, 123.45);
        assertEquals(expected.doubleValueExact(), d.doubleValueExact(), 0.00001);
        assertEquals(r, 0);

        d.round(in);
        assertEquals(expected.doubleValueExact(), d.doubleValueExact(), 0.00001);

        final int lsd = 1;
        final int lsbthreshold = 1000;
        int lsb = 5;
        int digits = 4 * factory.getRadixDigits();
        int cmaxdigits = digits * factory.getRadixDigits();

        DfpDec d1 = new DfpDec(factory, lsb / lsbthreshold * lsbthreshold, in);
        int r1 = d1.round(in);
        assertEquals(r1, 0);
        assertEquals(d1.doubleValueExact(), (lsb / lsbthreshold) * lsbthreshold, 0.00001);

        DfpDec d2 = new DfpDec(factory, lsb * 10 / lsbthreshold % 10 + lsb % (lsbthreshold / 10), in);
        int r2 = d2.round(in);
        assertEquals(r2, 0);
        assertEquals(d2.doubleValueExact(), lsb * 10 / lsbthreshold % 10 + lsb % (lsbthreshold / 10), 0.00001);

        DfpDec d3 = new DfpDec(factory, lsb * 10 / lsbthreshold % 10 + lsb % (lsbthreshold / 10) + 1, in);
        int r3 = d3.round(in);
        assertEquals(r3, 1);
        assertEquals(d3.doubleValueExact(), Math.ceil(lsb * 10 / lsbthreshold % 10 + lsb % (lsbthreshold / 10)), 0.00001);

        final int up = 1;
        DfpDec d4 = new DfpDec(factory, 123.44499999999999999999, up);
        int r4 = d4.round(in);
        assertEquals(r4, 1);
        assertEquals(d4.doubleValueExact(), Math.ceil(123.44499999999999999999), 0.000000001);

        final int down = 0;
        DfpDec d5 = new DfpDec(factory, 123.44500000000000001, down);
        int r5 = d5.round(in);
        assertEquals(r5, 0);
        assertEquals(d5.doubleValueExact(), 123.445, 0.00001);

        DfpDec d6 = new DfpDec(factory, 123.445, down);
        int r6 = d6.round(in);
        assertEquals(r6, 0);
        assertEquals(d6.doubleValueExact(), 123.445, 0.00001);

        DfpDec d7 = new DfpDec(factory, 123.445, up);
        int r7 = d7.round(in);
        assertEquals(r7, 1);
        assertEquals(d7.doubleValueExact(), Math.ceil(123.445), 0.00001);

        DfpDec d8 = new DfpDec(factory, 123.445, up);
        int r8 = d8.round(in);
        assertEquals(r8, 1);
        assertEquals(d8.doubleValueExact(), Math.ceil(123.445), 0.00001);
    }

    @Test
    public void testNextAfter() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 123.445);
        DfpDec d2 = new DfpDec(factory, 123.45);

        DfpDec d3 = d1.nextAfter(d2);
        assertEquals(d3.getClass(), DfpDec.class);
        assertEquals(d3.doubleValueExact(), 123.45, 0.00001);

        DfpDec d4 = d2.nextAfter(d1);
        assertEquals(d4.getClass(), DfpDec.class);
        assertEquals(d4.doubleValueExact(), 123.45, 0.00001);

        DfpDec d5 = d1.nextAfter(d1);
        assertEquals(d5.getClass(), DfpDec.class);
        assertEquals(d5.doubleValueExact(), 123.45, 0.00001);

        DfpDec d6 = new DfpDec(factory, 0);
        DfpDec d7 = new DfpDec(factory, 1);
        DfpDec d8 = new DfpDec(factory, -1);

        DfpDec d9 = d6.nextAfter(d7);
        assertEquals(d9.getClass(), DfpDec.class);
        assertEquals(d9.doubleValueExact(), 1, 0.00001);

        DfpDec d10 = d7.nextAfter(d6);
        assertEquals(d10.getClass(), DfpDec.class);
        assertEquals(d10.doubleValueExact(), 1, 0.00001);

        DfpDec d11 = d8.nextAfter(d6);
        assertEquals(d11.getClass(), DfpDec.class);
        assertEquals(d11.doubleValueExact(), 0, 0.00001);

        DfpDec d12 = d6.nextAfter(d8);
        assertEquals(d12.getClass(), DfpDec.class);
        assertEquals(d12.doubleValueExact(), 0, 0.00001);

        DfpDec d13 = new DfpDec(factory, 123.445, 1);
        DfpDec d14 = new DfpDec(factory, 123.446, 0);

        DfpDec d15 = d13.nextAfter(d14);
        assertEquals(d15.getClass(), DfpDec.class);
        assertEquals(d15.doubleValueExact(), 123.446, 0.00001);

        DfpDec d16 = d14.nextAfter(d13);
        assertEquals(d16.getClass(), DfpDec.class);
        assertEquals(d16.doubleValueExact(), 123.446, 0.00001);

        DfpDec d17 = new DfpDec(factory, 123.445, 3);
        DfpDec d18 = new DfpDec(factory, 123.446, 0);

        DfpDec d19 = d17.nextAfter(d18);
        assertEquals(d19.getClass(), DfpDec.class);
        assertEquals(d19.doubleValueExact(), 123.446, 0.00001);

        DfpDec d20 = d18.nextAfter(d17);
        assertEquals(d20.getClass(), DfpDec.class);
        assertEquals(d20.doubleValueExact(), 123.446, 0.00001);

        DfpDec d21 = new DfpDec(factory, Double.POSITIVE_INFINITY);
        DfpDec d22 = new DfpDec(factory, Double.POSITIVE_INFINITY);

        DfpDec d23 = d21.nextAfter(d22);
        assertEquals(d23.getClass(), DfpDec.class);
        assertEquals(d23.doubleValueExact(), Double.POSITIVE_INFINITY);

        DfpDec d24 = d22.nextAfter(d21);
        assertEquals(d24.getClass(), DfpDec.class);
        assertEquals(d24.doubleValueExact(), Double.POSITIVE_INFINITY);

        DfpDec d25 = new DfpDec(factory, Double.NaN);
        DfpDec d26 = new DfpDec(factory, Double.NaN);

        DfpDec d27 = d25.nextAfter(d26);
        assertEquals(d27.getClass(), DfpDec.class);
        assertEquals(d27.doubleValueExact(), Double.NaN);

        DfpDec d28 = d26.nextAfter(d25);
        assertEquals(d28.getClass(), DfpDec.class);
        assertEquals(d28.doubleValueExact(), Double.NaN);
    }

    @Test
    public void testNewInstanceWithRoundingAndRadixMismatch() {
        DfpField factory1 = new Dfp128Field();
        DfpField factory2 = new Dfp64Field();

        DfpDec d1 = new DfpDec(factory1, 123.445);
        DfpDec d2 = new DfpDec(factory2, d1);

        assertEquals(d1.getClass(), DfpDec.class);
        assertEquals(d2.getClass(), DfpDec.class);
        assertEquals(d1.getRadixDigits(), factory1.getRadixDigits());
        assertEquals(d2.getRadixDigits(), factory2.getRadixDigits());
        assertEquals(d1.doubleValueExact(), d2.doubleValueExact(), 0.00001);
    }
}