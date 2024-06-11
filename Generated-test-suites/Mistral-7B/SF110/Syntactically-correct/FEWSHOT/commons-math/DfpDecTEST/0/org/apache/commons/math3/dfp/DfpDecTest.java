package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldFactory;
import org.apache.commons.math3.dfp.DfpFactory;
import org.junit.Test;

public class DfpDecTest {

    private static DfpField getDefaultField() {
        return DfpFieldFactory.getInstance();
    }

    private static DfpFactory getDefaultFactory() {
        return DfpFactory.getInstance(getDefaultField());
    }

    @Test
    public void testNewInstance() {
        DfpField factory = getDefaultField();
        DfpDec d1 = new DfpDec(getDefaultFactory());
        DfpDec d2 = new DfpDec(getDefaultFactory(), 1);
        DfpDec d3 = new DfpDec(getDefaultFactory(), 1L);
        DfpDec d4 = new DfpDec(getDefaultFactory(), 1.0);
        DfpDec d5 = new DfpDec(d1);
        DfpDec d6 = new DfpDec(getDefaultFactory(), d1);

        DfpDec d7 = new DfpDec(getDefaultFactory(), "1");
        DfpDec d8 = new DfpDec(getDefaultFactory(), "1.2345");
        DfpDec d9 = new DfpDec(getDefaultFactory(), "1.2345E-6");

        DfpDec d10 = new DfpDec(getDefaultFactory(), (byte) 1);
        DfpDec d11 = new DfpDec(getDefaultFactory(), (byte) 1, (byte) 3);

        assertEquals(d1, d5);
        assertEquals(d1, d6);
        assertEquals(d1, d1.newInstance());

        assertEquals(d1, d7.toDfpDec());
        assertEquals(d8, new DfpDec(getDefaultFactory(), d8.toRadixString()));
        assertEquals(d9, new DfpDec(getDefaultFactory(), d9.toRadixString(), 6));

        assertEquals(d10.sign, 1);
        assertEquals(d11.nans, 3);
    }

    @Test
    public void testRound() {
        DfpField factory = getDefaultField();
        DfpDec d1 = new DfpDec(factory, 1234567.89123456789);
        DfpDec d2 = new DfpDec(factory, -1234567.89123456789);

        int roundingMode = DfpField.ROUND_DOWN;
        int rounded1 = d1.round(roundingMode);
        int rounded2 = d2.round(roundingMode);

        roundingMode = DfpField.ROUND_UP;
        int rounded3 = d1.round(roundingMode);
        int rounded4 = d2.round(roundingMode);

        roundingMode = DfpField.ROUND_HALF_UP;
        int rounded5 = d1.round(roundingMode);
        int rounded6 = d2.round(roundingMode);

        roundingMode = DfpField.ROUND_HALF_DOWN;
        int rounded7 = d1.round(roundingMode);
        int rounded8 = d2.round(roundingMode);

        roundingMode = DfpField.ROUND_HALF_EVEN;
        int rounded9 = d1.round(roundingMode);
        int rounded10 = d2.round(roundingMode);

        roundingMode = DfpField.ROUND_HALF_ODD;
        int rounded11 = d1.round(roundingMode);
        int rounded12 = d2.round(roundingMode);

        roundingMode = DfpField.ROUND_CEIL;
        int rounded13 = d1.round(roundingMode);
        int rounded14 = d2.round(roundingMode);

        assertEquals(6, d1.getDecimalDigits());
        assertEquals(6, d2.getDecimalDigits());

        assertEquals(1234568, rounded1);
        assertEquals(-1234568, rounded2);
        assertEquals(1234568, rounded3);
        assertEquals(-1234568, rounded4);
        assertEquals(1234568, rounded5);
        assertEquals(-1234568, rounded6);
        assertEquals(1234569, rounded7);
        assertEquals(-1234568, rounded8);
        assertEquals(1234569, rounded9);
        assertEquals(-1234568, rounded10);
        assertEquals(1234569, rounded11);
        assertEquals(-1234568, rounded12);
        assertEquals(1234569, rounded13);
        assertEquals(-1234569, rounded14);
    }

    @Test
    public void testNextAfter() {
        DfpField factory = getDefaultField();
        DfpDec d1 = new DfpDec(factory, 1.0);
        DfpDec d2 = new DfpDec(factory, -1.0);
        DfpDec d3 = new DfpDec(factory, 0.5);
        DfpDec d4 = new DfpDec(factory, 1.0 / 3.0);
        DfpDec d5 = new DfpDec(factory, Double.NEGATIVE_INFINITY);
        DfpDec d6 = new DfpDec(factory, Double.POSITIVE_INFINITY);
        DfpDec d7 = new DfpDec(factory, Double.NaN);

        DfpDec nextAfter1 = d1.nextAfter(d2);
        DfpDec nextAfter2 = d2.nextAfter(d1);
        DfpDec nextAfter3 = d3.nextAfter(d1);
        DfpDec nextAfter4 = d3.nextAfter(d4);
        DfpDec nextAfter5 = d5.nextAfter(d1);
        DfpDec nextAfter6 = d6.nextAfter(d1);
        DfpDec nextAfter7 = d7.nextAfter(d1);

        assertEquals(d2, nextAfter1);
        assertEquals(d1, nextAfter2);
        assertNotEquals(d3, nextAfter1);
        assertEquals(d4, nextAfter3);
        assertEquals(Double.POSITIVE_INFINITY, nextAfter5.doubleValue());
        assertEquals(Double.POSITIVE_INFINITY, nextAfter6.doubleValue());
        assertEquals(Double.NaN, nextAfter7.doubleValue());
    }

    @Test
    public void testExceptionHandling() {
        DfpField factory = getDefaultField();
        DfpDec d1 = new DfpDec(factory, 1.0);
        DfpDec d2 = new DfpDec(factory, "foo");
        DfpDec d3 = new DfpDec(factory, null);

        assertEquals(1.0, d1.doubleValue(), 0.0);
        assertEquals(0.0, d1.round(DfpField.ROUND_DOWN), 0.0);
        assertEquals(1.0, d1.nextAfter(d1).doubleValue(), 0.0);

        assertNull(d2.toDfpDec());
        assertNull(d2.newInstance());

        assertNull(d3);
    }
}