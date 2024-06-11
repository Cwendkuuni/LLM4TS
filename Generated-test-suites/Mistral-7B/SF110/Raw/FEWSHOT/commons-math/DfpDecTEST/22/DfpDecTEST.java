package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpDec d1 = new DfpDec(new DfpField(10));
        assertEquals(0, d1.doubleValueExact(), 0);

        DfpDec d2 = new DfpDec(new DfpField(10), 123);
        assertEquals(123, d2.doubleValueExact(), 0);

        DfpDec d3 = new DfpDec(new DfpDec(new DfpField(10), 45.6));
        assertEquals(45.6, d3.doubleValueExact(), 0.00001);

        DfpDec d4 = new DfpDec(new DfpDec(new DfpField(10), 987654321));
        assertEquals(987654321, d4.doubleValueExact(), 0);

        DfpDec d5 = new DfpDec(new DfpDec(new DfpField(10), new DfpDec(new DfpField(10), 1.23)));
        assertEquals(1.23, d5.doubleValueExact(), 0.00001);
    }

    @Test
    public void testNewInstanceWithRounding() {
        DfpDec d1 = new DfpDec(new DfpField(10), 0.5);
        assertEquals(0.5, d1.doubleValueExact(), 0.00001);
        assertEquals(0, d1.round(0));

        DfpDec d2 = new DfpDec(new DfpField(10), 0.6);
        assertEquals(0.6, d2.doubleValueExact(), 0.00001);
        assertEquals(1, d2.round(0));

        DfpDec d3 = new DfpDec(new DfpField(10), 2.5);
        assertEquals(2.5, d3.doubleValueExact(), 0.00001);
        assertEquals(2, d3.round(0));

        DfpDec d4 = new DfpDec(new DfpField(10), -0.5);
        assertEquals(-0.5, d4.doubleValueExact(), 0.00001);
        assertEquals(-1, d4.round(0));

        DfpDec d5 = new DfpDec(new DfpField(10), -0.6);
        assertEquals(-0.6, d5.doubleValueExact(), 0.00001);
        assertEquals(-1, d5.round(0));

        DfpDec d6 = new DfpDec(new DfpField(10), -2.5);
        assertEquals(-2.5, d6.doubleValueExact(), 0.00001);
        assertEquals(-2, d6.round(0));
    }

    @Test
    public void testNewInstanceWithString() {
        DfpDec d1 = new DfpDec(new DfpField(10), "12345");
        assertEquals(12345, d1.doubleValueExact(), 0);

        DfpDec d2 = new DfpDec(new DfpField(10), "123.45");
        assertEquals(123.45, d2.doubleValueExact(), 0.00001);

        DfpDec d3 = new DfpDec(new DfpField(10), "-123.45");
        assertEquals(-123.45, d3.doubleValueExact(), 0.00001);

        DfpDec d4 = new DfpDec(new DfpField(10), "1.2345E6");
        assertEquals(1234500, d4.doubleValueExact(), 0);

        DfpDec d5 = new DfpDec(new DfpField(10), "1.2345E-6");
        assertEquals(0.000012345, d5.doubleValueExact(), 0.000000001);
    }

    @Test
    public void testRounding() {
        DfpDec d1 = new DfpDec(new DfpField(10), 0.5);

        // Round down
        assertEquals(0, d1.round(-1));
        assertEquals(0, d1.round(DfpField.ROUND_DOWN));
        assertEquals(0, d1.round(DfpField.ROUND_MIDDLE_DOWN));

        // Round up
        assertEquals(1, d1.round(DfpField.ROUND_UP));
        assertEquals(1, d1.round(DfpField.ROUND_MIDDLE_UP));

        // Round half up
        assertEquals(1, d1.round(DfpField.ROUND_HALF_UP));
        assertEquals(1, d1.round(DfpField.ROUND_HALF_MIDDLE_UP));

        // Round half even
        assertEquals(0, d1.round(DfpField.ROUND_HALF_EVEN));
        assertEquals(0, d1.round(DfpField.ROUND_HALF_MIDDLE_EVEN));

        // Round half odd
        assertEquals(1, d1.round(DfpField.ROUND_HALF_ODD));
        assertEquals(1, d1.round(DfpField.ROUND_HALF_MIDDLE_ODD));

        // Round ceil
        assertEquals(1, d1.round(DfpField.ROUND_CEILING));
        assertEquals(1, d1.round(DfpField.ROUND_MIDDLE_CEILING));

        // Round floor
        assertEquals(0, d1.round(DfpField.ROUND_FLOOR));
        assertEquals(0, d1.round(DfpField.ROUND_MIDDLE_FLOOR));
    }

    @Test
    public void testIntLog10() {
        DfpDec d1 = new DfpDec(new DfpField(10), 1);
        assertEquals(0, d1.intLog10(), 0);

        DfpDec d2 = new DfpDec(new DfpField(10), 10);
        assertEquals(1, d2.intLog10(), 0);

        DfpDec d3 = new DfpDec(new DfpField(10), 100);
        assertEquals(2, d3.intLog10(), 0);

        DfpDec d4 = new DfpDec(new DfpField(10), 1000);
        assertEquals(3, d4.intLog10(), 0);

        DfpDec d5 = new DfpDec(new DfpField(10), 10000);
        assertEquals(4, d5.intLog10(), 0);

        DfpDec d6 = new DfpDec(new DfpField(10), 100000);
        assertEquals(5, d6.intLog10(), 0);
    }

    @Test
    public void testNextAfter() {
        DfpDec d1 = new DfpDec(new DfpField(10), 0.123);
        DfpDec d2 = new DfpDec(new DfpField(10), 0.124);
        assertEquals(d2, d1.nextAfter(d2));

        DfpDec d3 = new DfpDec(new DfpField(10), 0.1);
        DfpDec d4 = new DfpDec(new DfpField(10), 0.2);
        assertEquals(d4, d3.nextAfter(d4));

        DfpDec d5 = new DfpDec(new DfpField(10), Long.MAX_VALUE);
        DfpDec d6 = new DfpDec(new DfpField(10), Long.MAX_VALUE + 1);
        assertEquals(d6, d5.nextAfter(d6));

        DfpDec d7 = new DfpDec(new DfpField(10), Long.MIN_VALUE);
        DfpDec d8 = new DfpDec(new DfpField(10), Long.MIN_VALUE - 1);
        assertEquals(d8, d7.nextAfter(d8));

        DfpDec d9 = new DfpDec(new DfpField(10), 0);
        DfpDec d10 = new DfpDec(new DfpField(10), 1);
        assertEquals(d10, d9.nextAfter(d10));

        DfpDec d11 = new DfpDec(new DfpField(10), 1);
        DfpDec d12 = new DfpDec(new DfpField(10), 1.5);
        assertEquals(d12, d11.nextAfter(d12));

        DfpDec d13 = new DfpDec(new DfpField(10), 1);
        DfpDec d14 = new DfpDec(new DfpField(10), 0.5);
        assertEquals(d14, d13.nextAfter(d14));
    }
}