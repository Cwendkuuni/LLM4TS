package org.apache.commons.math3.dfp;

import static org.junit.Assert.*;

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

        DfpDec d3 = new DfpDec(field, 1);
        DfpDec d4 = d3.newInstance();
        assertEquals(d3, d4);

        DfpDec d5 = new DfpDec(field, 1.0);
        DfpDec d6 = d5.newInstance();
        assertEquals(d5, d6);

        DfpDec d7 = new DfpDec(field, DfpDec.ONE);
        DfpDec d8 = d7.newInstance();
        assertEquals(d7, d8);
    }

    @Test
    public void testNewInstanceWithDifferentRadixDigits() {
        DfpField field1 = new Dfp64Field();
        DfpDec d1 = new DfpDec(field1, 1);

        DfpField field2 = new Dfp128Field();
        DfpDec d2 = new DfpDec(field2, 1);

        DfpDec d3 = d1.newInstance();
        DfpDec d4 = d2.newInstance(d3);

        assertTrue(d3.getClass() == DfpDec.class);
        assertTrue(d4.getClass() == DfpDec.class);
        assertNotEquals(d3, d4);
    }

    @Test
    public void testRound() {
        DfpField field = new Dfp128Field();
        DfpDec d = new DfpDec(field, 1.5);

        int result = d.round(0);
        assertEquals(0, result);
        assertEquals(1.0, d.decValue(), 0.0);

        result = d.round(1);
        assertEquals(1, result);
        assertEquals(1.5, d.decValue(), 0.0);

        result = d.round(2);
        assertEquals(2, result);
        assertEquals(1.5, d.decValue(), 0.05);

        result = d.round(3);
        assertEquals(3, result);
        assertEquals(2.0, d.decValue(), 0.0);

        result = d.round(4);
        assertEquals(4, result);
        assertEquals(1.5, d.decValue(), 0.05);

        DfpDec d2 = new DfpDec(field, 1.1111111111111111111111111111111);
        result = d2.round(0);
        assertEquals(0, result);
        assertEquals(1.1, d2.decValue(), 0.0);

        result = d2.round(5);
        assertEquals(5, result);
        assertEquals(1.11111, d2.decValue(), 0.00001);
    }

    @Test
    public void testNextAfter() {
        DfpField field = new Dfp128Field();
        DfpDec d1 = new DfpDec(field, 0.5);
        DfpDec d2 = new DfpDec(field, 1.0);

        DfpDec result = d1.nextAfter(d2);
        assertEquals(1.0, result.decValue(), 0.0);

        result = d1.nextAfter(DfpDec.ZERO);
        assertEquals(0.5, result.decValue(), 0.0);

        result = d1.nextAfter(d1);
        assertEquals(1.0, result.decValue(), 0.0);

        result = d2.nextAfter(d1);
        assertEquals(1.0, result.decValue(), 0.0);

        result = d2.nextAfter(d2);
        assertEquals(2.0, result.decValue(), 0.0);
    }
}