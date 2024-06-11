package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testNewInstance() {
        DfpDec d1 = new DfpDec(new DfpField(10));
        DfpDec d2 = new DfpDec(new DfpDec(new DfpField(10)));
        DfpDec d3 = new DfpDec(new DfpDec(new DfpField(10), 1));
        DfpDec d4 = new DfpDec(new DfpDec(new DfpField(10), 0.12345));
        DfpDec d5 = new DfpDec(new DfpDec(new DfpField(10), 1.2345E-6));
        DfpDec d6 = new DfpDec(new DfpDec(new DfpField(10), "1.2345"));
        DfpDec d7 = new DfpDec(new DfpDec(new DfpField(10), new DfpDec(new DfpField(10), 1)), 1);
        DfpDec d8 = new DfpDec(new DfpDec(new DfpField(10), new DfpDec(new DfpField(10), 1)), 0);
        DfpDec d9 = new DfpDec(new DfpDec(new DfpField(10), new DfpDec(new DfpField(10), 1)), -1);

        assertEquals(d1, d1.newInstance());
        assertEquals(d2, d2.newInstance());
        assertEquals(d3, d3.newInstance());
        assertEquals(d4, d4.newInstance());
        assertEquals(d5, d5.newInstance());
        assertEquals(d6, d6.newInstance());
        assertEquals(d7, d7.newInstance());
        assertEquals(d8, d8.newInstance());
        assertEquals(d9, d9.newInstance());
    }

    @Test
    public void testRound() {
        DfpField field = new DfpField(10);
        DfpDec d = new DfpDec(field, 1.5);
        int roundingModes[] = {DfpField.ROUND_DOWN, DfpField.ROUND_UP, DfpField.ROUND_HALF_UP, DfpField.ROUND_HALF_DOWN, DfpField.ROUND_HALF_EVEN, DfpField.ROUND_HALF_ODD, DfpField.ROUND_CEIL, DfpField.ROUND_FLOOR};
        int expectedRounds[] = {0, 1, 1, 1, 1, 1, 1, 1};

        for (int i = 0; i < roundingModes.length; i++) {
            DfpDec result = d.newInstance();
            result.round(expectedRounds[i]);
            assertEquals(expectedRounds[i], result.round(0));
        }
    }

    @Test
    public void testNextAfter() {
        DfpField field = new DfpField(10);
        DfpDec x = new DfpDec(field, 0.5);
        DfpDec y = new DfpDec(field, 0.6);
        DfpDec z = new DfpDec(field, 0.5);
        z.round(DfpField.ROUND_UP);

        DfpDec result = x.nextAfter(y);
        assertEquals(y, result);

        result = y.nextAfter(x);
        assertEquals(x, result);

        result = z.nextAfter(x);
        assertTrue(result.compareTo(x) > 0);

        result = x.nextAfter(z);
        assertFalse(result.equals(x));
        assertTrue(result.compareTo(z) < 0);
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField field = new DfpField(10);
        DfpDec d = new DfpDec(field, 12345.6789);
        int decimalDigits = d.getDecimalDigits();
        assertEquals(48, decimalDigits);
    }
}