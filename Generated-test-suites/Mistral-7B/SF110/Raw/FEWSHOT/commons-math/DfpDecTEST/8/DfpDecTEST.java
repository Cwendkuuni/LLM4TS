package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpField field = new Dfp128Field();
        DfpDec d1 = new DfpDec(field);
        DfpDec d2 = d1.newInstance();
        assertEquals(d1, d2);

        DfpDec d3 = new DfpDec(field, 12345);
        DfpDec d4 = d3.newInstance();
        assertEquals(d3, d4);

        DfpDec d5 = new DfpDec(field, "123.45");
        DfpDec d6 = d5.newInstance();
        assertEquals(d5, d6);

        DfpDec d7 = new DfpDec(field, 0x1234p5);
        DfpDec d8 = d7.newInstance();
        assertEquals(d7, d8);

        DfpDec d9 = new DfpDec(field, 1.2345);
        DfpDec d10 = d9.newInstance();
        assertEquals(d9, d10);
    }

    @Test
    public void testNewInstanceWithMismatchedRadixDigits() {
        DfpField field1 = new Dfp64Field();
        DfpDec d1 = new DfpDec(field1);
        DfpField field2 = new Dfp128Field();
        DfpDec d2 = new DfpDec(field2);

        DfpDec result = d1.newInstance(d2);
        assertNotEquals(d1, result);
        assertNull(result.getValue());
    }

    @Test
    public void testRounding() {
        DfpField field = new Dfp128Field();
        DfpDec d = new DfpDec(field, 1.5);

        int roundingMode = DfpField.ROUND_UP;
        DfpDec rounded = d.round(roundingMode);
        assertEquals(2.0, rounded.doubleValueExact(), 0.0);

        roundingMode = DfpField.ROUND_DOWN;
        rounded = d.round(roundingMode);
        assertEquals(1.0, rounded.doubleValueExact(), 0.0);

        roundingMode = DfpField.ROUND_HALF_UP;
        rounded = d.round(roundingMode);
        assertEquals(1.5, rounded.doubleValueExact(), 0.0);

        roundingMode = DfpField.ROUND_HALF_DOWN;
        rounded = d.round(roundingMode);
        assertEquals(1.0, rounded.doubleValueExact(), 0.0);

        roundingMode = DfpField.ROUND_HALF_EVEN;
        rounded = d.round(roundingMode);
        assertEquals(1.5, rounded.doubleValueExact(), 0.0);

        roundingMode = DfpField.ROUND_HALF_ODD;
        rounded = d.round(roundingMode);
        assertEquals(2.0, rounded.doubleValueExact(), 0.0);

        roundingMode = DfpField.ROUND_CEIL;
        rounded = d.round(roundingMode);
        assertEquals(2.0, rounded.doubleValueExact(), 0.0);

        roundingMode = DfpField.ROUND_FLOOR;
        rounded = d.round(roundingMode);
        assertEquals(1.0, rounded.doubleValueExact(), 0.0);
    }

    @Test
    public void testNextAfter() {
        DfpField field = new Dfp128Field();
        DfpDec d1 = new DfpDec(field, 1.0);
        DfpDec d2 = new DfpDec(field, 2.0);
        DfpDec d3 = new DfpDec(field, 3.0);

        DfpDec result = d1.nextAfter(d2);
        assertEquals(d2, result);

        result = d1.nextAfter(d3);
        assertEquals(d2, result);

        result = d2.nextAfter(d1);
        assertEquals(d3, result);

        result = d2.nextAfter(d2);
        assertEquals(d3, result);

        result = d3.nextAfter(d1);
        assertEquals(d3, result);

        result = d3.nextAfter(d2);
        assertEquals(Double.POSITIVE_INFINITY, result.doubleValueExact(), 0.0);

        DfpDec d4 = new DfpDec(field, Double.NEGATIVE_INFINITY);
        result = d3.nextAfter(d4);
        assertEquals(d1, result);
    }
}