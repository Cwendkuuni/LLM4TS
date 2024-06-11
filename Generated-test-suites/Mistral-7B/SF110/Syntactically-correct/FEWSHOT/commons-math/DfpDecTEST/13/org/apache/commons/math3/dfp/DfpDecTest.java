package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpField factory = new DfpField(48, 16, 12); // Radix, max exponent, maximum number of digits
        DfpDec d1 = new DfpDec(factory);
        DfpDec d2 = d1.newInstance();
        assertEquals(d1, d2);

        DfpDec d3 = new DfpDec(factory, 42);
        DfpDec d4 = d3.newInstance();
        assertEquals(d3, d4);

        DfpDec d5 = new DfpDec(factory, 0x1p-1234); // Hexadecimal constant for a very small negative number
        DfpDec d6 = d5.newInstance();
        assertEquals(d5, d6);

        DfpDec d7 = new DfpDec(factory, "0.1234567890123456789012345678901234567890123456789012345678901234567890123456789");
        DfpDec d8 = d7.newInstance();
        assertEquals(d7, d8);

        DfpDec d9 = new DfpDec(factory, 1, 1); // Sign and NaN
        DfpDec d10 = d9.newInstance();
        assertEquals(d9, d10);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField factory = new DfpField(48, 16, 12);
        DfpDec d = new DfpDec(factory);

        assertEquals(52, d.getDecimalDigits());
        d.setPrecision(20);
        assertEquals(36, d.getDecimalDigits());
        d.setPrecision(100);
        assertEquals(240, d.getDecimalDigits());
    }

    @Test
    public void testRound() {
        DfpField factory = new DfpField(48, 16, 12);
        DfpDec d = new DfpDec(factory, 1234.5678);

        assertEquals(1234.5678, d.round(0));
        assertEquals(1235, d.round(1));
        assertEquals(1234.6, d.round(1, RoundingMode.HALF_UP));
        assertEquals(1234.4, d.round(1, RoundingMode.HALF_DOWN));
        assertEquals(1234.5, d.round(1, RoundingMode.HALF_EVEN));
        assertEquals(1235, d.round(1, RoundingMode.UP));
        assertEquals(1234, d.round(1, RoundingMode.DOWN));
        assertEquals(1234.6, d.round(1, RoundingMode.CEILING));
        assertEquals(1234.4, d.round(1, RoundingMode.FLOOR));
    }

    @Test
    public void testNextAfter() {
        DfpField factory = new DfpField(48, 16, 12);
        DfpDec d1 = new DfpDec(factory, 1.1);
        DfpDec d2 = new DfpDec(factory, 1.5);

        DfpDec result = d1.nextAfter(d2);
        assertEquals(1.5, result.doubleValue(), 0.000001);

        result = d2.nextAfter(d1);
        assertEquals(1.6, result.doubleValue(), 0.000001);

        result = d1.nextAfter(d1);
        assertEquals(1.2, result.doubleValue(), 0.000001);

        result = d1.nextAfter(Dfp.ZERO);
        assertEquals(1.1, result.doubleValue(), 0.000001);

        result = d1.nextAfter(Dfp.NEGINF);
        assertEquals(Dfp.NEGINF, result);

        result = d1.nextAfter(Dfp.POSINF);
        assertEquals(Dfp.POSINF, result);
    }
}