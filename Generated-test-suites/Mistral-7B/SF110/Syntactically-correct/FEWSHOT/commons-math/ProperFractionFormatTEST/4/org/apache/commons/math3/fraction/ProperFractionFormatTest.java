package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(3, 4);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("3 / 4", toAppendTo.toString());

        fraction = new Fraction(5, 2);
        toAppendTo.setLength(0);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("5 / 2", toAppendTo.toString());

        fraction = new Fraction(-5, 2);
        toAppendTo.setLength(0);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("-5 / 2", toAppendTo.toString());

        fraction = new Fraction(0, 4);
        toAppendTo.setLength(0);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("0", toAppendTo.toString());

        fraction = new Fraction(1, 1);
        toAppendTo.setLength(0);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("1", toAppendTo.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = format.parse("3 / 4", pos);
        assertEquals(new Fraction(3, 4), fraction);
        assertEquals(7, pos.getIndex());

        fraction = format.parse("5 / 2", pos);
        assertEquals(new Fraction(5, 2), fraction);
        assertEquals(6, pos.getIndex());

        fraction = format.parse("-5 / 2", pos);
        assertEquals(new Fraction(-5, 2), fraction);
        assertEquals(7, pos.getIndex());

        fraction = format.parse("0", pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());

        fraction = format.parse("1", pos);
        assertEquals(new Fraction(1, 1), fraction);
        assertEquals(1, pos.getIndex());

        fraction = format.parse("3 apples / 4 oranges", pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = new DecimalFormat("#");
        format.setWholeFormat(wholeFormat);

        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(3, 4);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("3 / 4", toAppendTo.toString());

        toAppendTo.setLength(0);
        format.setWholeFormat(null);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertNull(toAppendTo.toString());
    }
}