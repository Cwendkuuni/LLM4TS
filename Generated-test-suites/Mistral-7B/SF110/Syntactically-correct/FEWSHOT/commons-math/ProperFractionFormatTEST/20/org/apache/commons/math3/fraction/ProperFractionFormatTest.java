package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat getTestFormat() {
        return new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = getTestFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);
        assertEquals("1/2", toAppendTo.toString());

        fraction = new Fraction(3, 4);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);
        assertEquals("3/4", toAppendTo.toString());

        fraction = new Fraction(5, 8);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);
        assertEquals("5/8", toAppendTo.toString());

        fraction = new Fraction(1, 1);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);
        assertEquals("1", toAppendTo.toString());
    }

    @Test
    public void testFormatWithWholeNumber() {
        ProperFractionFormat format = getTestFormat();
        format.setWholeFormat(NumberFormat.getNumberInstance());
        Fraction fraction = new Fraction(3, 4);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);
        assertEquals("3/4", toAppendTo.toString());

        format = new ProperFractionFormat(NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());
        fraction = new Fraction(2, 5);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);
        assertEquals("2/5", toAppendTo.toString());

        format.setWholeFormat(NumberFormat.getIntegerInstance());
        fraction = new Fraction(7, 2);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);
        assertEquals("7", toAppendTo.toString());
        toAppendTo.setLength(0);
        format.format(fraction, toAppendTo, pos);
        assertEquals("7/1", toAppendTo.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = getTestFormat();

        Fraction fraction = format.parse("1/2", new ParsePosition(0, 0));
        assertEquals(new Fraction(1, 2), fraction);

        fraction = format.parse("3/4", new ParsePosition(0, 0));
        assertEquals(new Fraction(3, 4), fraction);

        fraction = format.parse("5/8", new ParsePosition(0, 0));
        assertEquals(new Fraction(5, 8), fraction);

        fraction = format.parse("1", new ParsePosition(0, 0));
        assertEquals(new Fraction(1, 1), fraction);

        fraction = format.parse("1/", new ParsePosition(0, 0));
        assertNull(fraction);

        fraction = format.parse("1/-2", new ParsePosition(0, 0));
        assertNull(fraction);

        fraction = format.parse("-1/2", new ParsePosition(0, 0));
        assertEquals(new Fraction(-1, 2), fraction);

        fraction = format.parse("1/2/3", new ParsePosition(0, 0));
        assertNull(fraction);

        fraction = format.parse("1 / 2", new ParsePosition(0, 0));
        assertNull(fraction);

        fraction = format.parse("1\u00F72", new ParsePosition(0, 0));
        assertNull(fraction);

        fraction = format.parse("1/2.5", new ParsePosition(0, 0));
        assertNull(fraction);

        fraction = format.parse("1/-0", new ParsePosition(0, 0));
        assertNull(fraction);

        fraction = format.parse("1/INF", new ParsePosition(0, 0));
        assertNull(fraction);

        fraction = format.parse("1/-INF", new ParsePosition(0, 0));
        assertNull(fraction);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseWithInvalidWholeNumber() {
        ProperFractionFormat format = getTestFormat();

        format.setWholeFormat(NumberFormat.getNumberInstance());
        format.parse("1 a", new ParsePosition(0, 0));
    }

    @Test(expected = NumberFormatException.class)
    public void testParseWithInvalidNumerator() {
        ProperFractionFormat format = getTestFormat();

        format.parse("1/0", new ParsePosition(0, 0));
    }

    @Test(expected = NumberFormatException.class)
    public void testParseWithInvalidDenominator() {
        ProperFractionFormat format = getTestFormat();

        format.parse("1/0", new ParsePosition(0, 0));
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = getTestFormat();
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullArgument() {
        ProperFractionFormat format = getTestFormat();
        format.setWholeFormat(null);
    }
}