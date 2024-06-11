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
        DfpField factory = new Dfp32Field();
        DfpDec d1 = new DfpDec(factory);
        DfpDec d2 = new DfpDec(factory.newInstance(1.2345));
        DfpDec d3 = new DfpDec(factory.newInstance("-1.1234"));
        DfpDec d4 = new DfpDec(factory, 456);
        DfpDec d5 = new DfpDec(factory, 789L);
        DfpDec d6 = new DfpDec(factory, 123.45789);

        assertEquals(d2, d1.newInstance(1.2345));
        assertEquals(d3, d1.newInstance("-1.1234"));
        assertEquals(d4, d1.newInstance(456));
        assertEquals(d5, d1.newInstance(789L));
        assertEquals(d6, d1.newInstance(123.45789));
    }

    @Test
    public void testNewInstanceWithDfp() {
        DfpField factory = new Dfp32Field();
        DfpDec d1 = new DfpDec(factory);
        DfpDec d2 = new DfpDec(factory, d1.newInstance(1.2345));
        DfpDec d3 = new DfpDec(factory, d1);

        assertEquals(d2, d1.newInstance(d1));
        assertEquals(d3, d1.newInstance(d1));
    }

    @Test
    public void testNewInstanceWithString() {
        DfpField factory = new Dfp32Field();
        DfpDec d1 = new DfpDec(factory);
        DfpDec d2 = new DfpDec(factory, "1.2345");
        DfpDec d3 = new DfpDec(factory, "-1.1234");
        DfpDec d4 = new DfpDec(factory, "456");
        DfpDec d5 = new DfpDec(factory, "789");
        DfpDec d6 = new DfpDec(factory, "123.45789");
        DfpDec d7 = new DfpDec(factory, "0");
        DfpDec d8 = new DfpDec(factory, "NaN");
        DfpDec d9 = new DfpDec(factory, "Infinity");

        assertEquals(d2, d1.newInstance("1.2345"));
        assertEquals(d3, d1.newInstance("-1.1234"));
        assertEquals(d4, d1.newInstance("456"));
        assertEquals(d5, d1.newInstance("789"));
        assertEquals(d6, d1.newInstance("123.45789"));
        assertNull(d1.newInstance(""));
        assertNull(d1.newInstance("NaN"));
        assertNull(d1.newInstance("Infinity"));
        assertNull(d1.newInstance("Infinity."));
        assertEquals(d7, d1.newInstance("0"));
        assertEquals(d8, d1.newInstance("NaN"));
        assertEquals(d9, d1.newInstance("Infinity"));
    }

    @Test
    public void testRound() {
        DfpField factory = new Dfp32Field();
        DfpDec d1 = new DfpDec(factory, 1.2345);
        DfpDec d2 = new DfpDec(factory, 1.111111);
        DfpDec d3 = new DfpDec(factory, -1.5);
        DfpDec d4 = new DfpDec(factory, 3.5);

        assertEquals(1, d1.round(0));
        assertEquals(1, d2.round(0));
        assertEquals(1, d3.round(0));
        assertEquals(1, d4.round(0));

        assertEquals(2, d1.round(1));
        assertEquals(1, d2.round(1));
        assertEquals(2, d3.round(1));
        assertEquals(4, d4.round(1));

        assertEquals(3, d1.round(2));
        assertEquals(1, d2.round(2));
        assertEquals(3, d3.round(2));
        assertEquals(9, d4.round(2));

        assertEquals(4, d1.round(3));
        assertEquals(1, d2.round(3));
        assertEquals(3, d3.round(3));
        assertEquals(27, d4.round(3));

        assertEquals(1, d1.round(4));
        assertEquals(1, d2.round(4));
        assertEquals(3, d3.round(4));
        assertEquals(81, d4.round(4));

        assertEquals(6, d1.round(5));
        assertEquals(1, d2.round(5));
        assertEquals(3, d3.round(5));
        assertEquals(32767, d4.round(5));

        assertEquals(-1, d1.round(-1));
        assertEquals(-1, d2.round(-1));
        assertEquals(-2, d3.round(-1));
        assertEquals(-2147483648, d4.round(-1));

        assertEquals(-2, d1.round(-2));
        assertEquals(-1, d2.round(-2));
        assertEquals(-3, d3.round(-2));
        assertEquals(-838860800, d4.round(-2));

        assertEquals(-3, d1.round(-3));
        assertEquals(-1, d2.round(-3));
        assertEquals(-4, d3.round(-3));
        assertEquals(-1677721600, d4.round(-3));

        assertEquals(-4, d1.round(-4));
        assertEquals(-1, d2.round(-4));
        assertEquals(-5, d3.round(-4));
        assertEquals(-4294967296, d4.round(-4));

        assertEquals(-11, d1.round(-5));
        assertEquals(-1, d2.round(-5));
        assertEquals(-5, d3.round(-5));
        assertEquals(-1073741824, d4.round(-5));
    }

    @Test
    public void testGetDecimalDigits() {
        DfpField factory = new Dfp32Field();
        DfpDec d1 = new DfpDec(factory, 1.2345);
        DfpDec d2 = new DfpDec(factory, 1.111111);
        DfpDec d3 = new DfpDec(factory, -1.5);
        DfpDec d4 = new DfpDec(factory, 3.5);

        assertEquals(9, d1.getDecimalDigits());
        assertEquals(9, d2.getDecimalDigits());
        assertEquals(0, d3.getDecimalDigits());
        assertEquals(0, d4.getDecimalDigits());
    }
}