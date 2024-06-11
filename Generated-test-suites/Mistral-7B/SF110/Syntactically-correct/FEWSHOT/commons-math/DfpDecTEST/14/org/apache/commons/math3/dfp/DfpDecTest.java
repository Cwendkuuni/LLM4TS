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

        Dfp d3 = new Dfp(1.0);
        DfpDec d4 = new DfpDec(d3);
        DfpDec d5 = d4.newInstance();
        assertEquals(d4, d5);

        DfpDec d6 = new DfpDec(factory, "123.45");
        DfpDec d7 = d6.newInstance();
        assertEquals(d6, d7);

        DfpDec d8 = new DfpDec(factory, 0x1.2p-3);
        DfpDec d9 = d8.newInstance();
        assertEquals(d8, d9);
    }

    @Test
    public void testNewInstanceWithSignAndNans() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, -1, 1);
        DfpDec d2 = d1.newInstance((byte) -1, 1);
        assertEquals(d1, d2);

        DfpDec d3 = new DfpDec(factory, 1, 3);
        DfpDec d4 = d3.newInstance((byte) 1, 3);
        assertEquals(d3, d4);
    }

    @Test
    public void testNewInstanceWithDouble() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 1.2345);
        DfpDec d2 = d1.newInstance();
        assertEquals(d1, d2);
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, new Dfp(1.2345));
        DfpDec d2 = d1.newInstance(new Dfp(1.2345));
        assertEquals(d1, d2);
    }

    @Test
    public void testRound() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 1.2345678912345);
        int roundingMode = DfpField.ROUND_HALF_UP;
        int expectedRoundedValue = 1234630891;
        int actualRoundedValue = d1.round(roundingMode);
        assertEquals(expectedRoundedValue, actualRoundedValue);

        DfpDec d2 = new DfpDec(factory, 1.2345678912345);
        roundingMode = DfpField.ROUND_DOWN;
        expectedRoundedValue = 1234567891;
        actualRoundedValue = d2.round(roundingMode);
        assertEquals(expectedRoundedValue, actualRoundedValue);

        DfpDec d3 = new DfpDec(factory, 1.2345678912345);
        roundingMode = DfpField.ROUND_HALF_EVEN;
        expectedRoundedValue = 1234567893;
        actualRoundedValue = d3.round(roundingMode);
        assertEquals(expectedRoundedValue, actualRoundedValue);

        DfpDec d4 = new DfpDec(factory, 1.2345678912345);
        roundingMode = DfpField.ROUND_HALF_ODD;
        expectedRoundedValue = 1234567892;
        actualRoundedValue = d4.round(roundingMode);
        assertEquals(expectedRoundedValue, actualRoundedValue);

        DfpDec d5 = new DfpDec(factory, 1.2345678912345);
        roundingMode = DfpField.ROUND_CEIL;
        expectedRoundedValue = 1234567892;
        actualRoundedValue = d5.round(roundingMode);
        assertEquals(expectedRoundedValue, actualRoundedValue);

        DfpDec d6 = new DfpDec(factory, 1.2345678912345);
        roundingMode = DfpField.ROUND_FLOOR;
        expectedRoundedValue = 1234567891;
        actualRoundedValue = d6.round(roundingMode);
        assertEquals(expectedRoundedValue, actualRoundedValue);
    }

    @Test
    public void testNextAfter() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 1.2345);
        DfpDec x = new DfpDec(factory, 1.2346);
        DfpDec result = d1.nextAfter(x);
        assertEquals(x, result);

        DfpDec d2 = new DfpDec(factory, 1.2345);
        DfpDec x2 = new DfpDec(factory, 1.2344);
        DfpDec result2 = d2.nextAfter(x2);
        assertEquals(x2, result2);

        DfpDec d3 = new DfpDec(factory, 1.2345);
        DfpDec x3 = new DfpDec(factory, 1.2345);
        DfpDec result3 = d3.nextAfter(x3);
        assertNotEquals(x3, result3);
        assertEquals(x3.add(Dfp.ONE), result3);

        DfpDec d4 = new DfpDec(factory, 1.2345);
        DfpDec x4 = new DfpDec(factory, 1.2344);
        DfpDec result4 = d4.nextAfter(x4);
        assertNotEquals(x4, result4);
        assertEquals(x4.add(Dfp.ONE), result4);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField factory = new Dfp128Field();
        DfpDec d1 = new DfpDec(factory, 1.2345);
        int decimalDigits = d1.getDecimalDigits();
        int expectedDecimalDigits = 7;
        assertEquals(expectedDecimalDigits, decimalDigits);
    }
}