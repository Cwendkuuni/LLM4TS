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

        DfpDec d5 = new DfpDec(field, 0x123456789ABCDEF0L);
        DfpDec d6 = d5.newInstance();
        assertEquals(d5, d6);

        DfpDec d7 = new DfpDec(field, 3.14159265358979323846);
        DfpDec d8 = d7.newInstance();
        assertEquals(d7, d8);

        DfpDec d9 = new DfpDec(new DfpDec(field, 1.0));
        DfpDec d10 = d9.newInstance();
        assertEquals(d9, d10);

        DfpDec d11 = new DfpDec(field, "123.45");
        DfpDec d12 = d11.newInstance();
        assertEquals(d11, d12);

        DfpDec d13 = new DfpDec(field, (byte) 0x1, (byte) 0x3);
        DfpDec d14 = d13.newInstance();
        assertEquals(d13, d14);
    }

    @Test
    public void testNewInstanceWithDifferentRadixDigits() {
        DfpField field1 = new Dfp128Field();
        DfpField field2 = new Dfp64Field();

        DfpDec d1 = new DfpDec(field1);
        try {
            DfpDec d2 = d1.newInstance();
            assertFalse("Exception expected due to different radix digits", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        DfpDec d3 = new DfpDec(field1, 1);
        try {
            DfpDec d4 = d3.newInstance();
            assertFalse("Exception expected due to different radix digits", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        DfpDec d5 = new DfpDec(field1, 0x123456789ABCDEF0L);
        try {
            DfpDec d6 = d5.newInstance();
            assertFalse("Exception expected due to different radix digits", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        DfpDec d7 = new DfpDec(field1, 3.14159265358979323846);
        try {
            DfpDec d8 = d7.newInstance();
            assertFalse("Exception expected due to different radix digits", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        DfpDec d9 = new DfpDec(new DfpDec(field1, 1.0));
        try {
            DfpDec d10 = d9.newInstance();
            assertFalse("Exception expected due to different radix digits", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        DfpDec d11 = new DfpDec(field1, "123.45");
        try {
            DfpDec d12 = d11.newInstance();
            assertFalse("Exception expected due to different radix digits", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        DfpDec d13 = new DfpDec(field1, (byte) 0x1, (byte) 0x3);
        try {
            DfpDec d14 = d13.newInstance();
            assertFalse("Exception expected due to different radix digits", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        DfpDec d15 = new DfpDec(field2);
        DfpDec d16 = d15.newInstance();
        assertNull(d16);
    }

    @Test
    public void testRound() {
        DfpField field = new Dfp128Field();
        DfpDec d = new DfpDec(field, 1.5);

        assertEquals(0, d.round(0));
        assertEquals(1, d.round(1));
        assertEquals(1, d.round(2));
        assertEquals(1, d.round(3));
        assertEquals(2, d.round(4));
        assertEquals(2, d.round(5));
    }

    @Test
    public void testRoundWithDifferentRoundingModes() {
        DfpField field = new Dfp128Field();
        DfpDec d = new DfpDec(field, 1.5);

        DfpDec resultDown = d.round(DfpField.ROUND_DOWN);
        DfpDec resultUp = d.round(DfpField.ROUND_UP);
        DfpDec resultHalfUp = d.round(DfpField.ROUND_HALF_UP);
        DfpDec resultHalfDown = d.round(DfpField.ROUND_HALF_DOWN);
        DfpDec resultHalfEven = d.round(DfpField.ROUND_HALF_EVEN);
        DfpDec resultHalfOdd = d.round(DfpField.ROUND_HALF_ODD);
        DfpDec resultCeil = d.round(DfpField.ROUND_CEIL);
        DfpDec resultFloor = d.round(DfpField.ROUND_FLOOR);

        assertEquals(1, resultDown.doubleValueExact());
        assertEquals(2, resultUp.doubleValueExact());
        assertEquals(2, resultHalfUp.doubleValueExact());
        assertEquals(1, resultHalfDown.doubleValueExact());
        assertEquals(2, resultHalfEven.doubleValueExact());
        assertEquals(2, resultHalfOdd.doubleValueExact());
        assertEquals(2, resultCeil.doubleValueExact());
        assertEquals(1, resultFloor.doubleValueExact());
    }

    @Test(expected = ArithmeticException.class)
    public void testRoundWithInvalidInput() {
        DfpField field = new Dfp128Field();
        DfpDec d = new DfpDec(field, Double.NaN);
        d.round(0);
    }

    @Test
    public void testNextAfter() {
        DfpField field = new Dfp128Field();
        DfpDec x = new DfpDec(field, 0.123456789);
        DfpDec y = new DfpDec(field, 0.1234567899);

        DfpDec result = x.nextAfter(y);
        assertEquals(y, result);

        x = new DfpDec(field, Long.MIN_VALUE);
        y = new DfpDec(field, 0.5);

        result = x.nextAfter(y);
        assertEquals(Double.POSITIVE_INFINITY, result.doubleValueExact());

        x = new DfpDec(field, 0.5);
        y = new DfpDec(field, Long.MIN_VALUE);

        result = x.nextAfter(y);
        assertEquals(Double.NEGATIVE_INFINITY, result.doubleValueExact());

        x = new DfpDec(field, 0.5);
        y = new DfpDec(field, 0.5);

        result = x.nextAfter(y);
        assertEquals(y, result);

        x = new DfpDec(field, 0.5);
        y = new DfpDec(field, 1.0);

        result = x.nextAfter(y);
        assertEquals(y, result);

        x = new DfpDec(field, 1.0);
        y = new DfpDec(field, 1.0);

        result = x.nextAfter(y);
        assertEquals(y, result);

        x = new DfpDec(field, Long.MAX_VALUE);
        y = new DfpDec(field, 0.5);

        result = x.nextAfter(y);
        assertEquals(Double.POSITIVE_INFINITY, result.doubleValueExact());

        x = new DfpDec(field, 0.5);
        y = new DfpDec(field, Long.MAX_VALUE);

        result = x.nextAfter(y);
        assertEquals(Double.NEGATIVE_INFINITY, result.doubleValueExact());

        x = new DfpDec(field, 3.14159265358979323846);
        y = new DfpDec(field, 3.14159265358979323847);

        result = x.nextAfter(y);
        assertEquals(y, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testNextAfterWithInvalidInput() {
        DfpField field = new Dfp128Field();
        DfpDec x = new DfpDec(field, Double.NaN);
        DfpDec y = new DfpDec(field, 0.1);
        x.nextAfter(y);
    }

    @Test(expected = ArithmeticException.class)
    public void testNextAfterWithDifferentRadixDigits() {
        DfpField field1 = new Dfp128Field();
        DfpField field2 = new Dfp64Field();

        DfpDec x = new DfpDec(field1, 0.123456789);
        DfpDec y = new DfpDec(field2, 0.1234567899);
        x.nextAfter(y);
    }
}