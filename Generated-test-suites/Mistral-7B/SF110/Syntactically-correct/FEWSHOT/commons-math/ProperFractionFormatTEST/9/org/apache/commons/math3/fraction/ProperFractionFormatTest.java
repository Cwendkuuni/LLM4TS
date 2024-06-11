package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.util.Precision;

import java.text.ParsePosition;
import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertEquals("1 2/3", pff.format(new Fraction(2, 3), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("1 2/3", pff.format(new Fraction(2, 3), new StringBuffer(), null));

        ProperFractionFormat pff1 = new ProperFractionFormat(NumberFormat.getNumberInstance(Locale.US));
        assertEquals("1 2/3", pff1.format(new Fraction(2, 3), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("1 2/3", pff1.format(new Fraction(2, 3), new StringBuffer(), null));

        ProperFractionFormat pff2 = new ProperFractionFormat(NumberFormat.getPercentInstance());
        assertEquals("1 2/3", pff2.format(new Fraction(2, 3), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("1 2/3", pff2.format(new Fraction(2, 3), new StringBuffer(), null));

        ProperFractionFormat pff3 = new ProperFractionFormat(NumberFormat.getNumberInstance(Locale.US), NumberFormat.getNumberInstance(Locale.US), NumberFormat.getNumberInstance(Locale.US));
        assertEquals("1 2/3", pff3.format(new Fraction(2, 3), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("1 2/3", pff3.format(new Fraction(2, 3), new StringBuffer(), null));

        ProperFractionFormat pff4 = new ProperFractionFormat(NumberFormat.getNumberInstance(Locale.ITALY), NumberFormat.getNumberInstance(Locale.US), NumberFormat.getNumberInstance(Locale.US));
        assertEquals("1 2/3", pff4.format(new Fraction(2, 3), new StringBuffer(), new FieldPosition(0, 0)));
        assertEquals("1 2/3", pff4.format(new Fraction(2, 3), new StringBuffer(), null));
    }

    @Test
    public void testFormatWholePart() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertEquals("1", pff.format(new Fraction(3, 1), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("1", pff.format(new Fraction(1, 1), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("2", pff.format(new Fraction(2, 1), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("3", pff.format(new Fraction(3, 1), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("4", pff.format(new Fraction(4, 1), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("5", pff.format(new Fraction(5, 1), new StringBuffer(), new FieldPosition(0, 0)).toString());
    }

    @Test
    public void testParseWholePart() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertEquals(new Fraction(3, 1), pff.parse("1", new ParsePosition(0)));
        assertEquals(new Fraction(1, 1), pff.parse("1", new ParsePosition(0)));
        assertEquals(new Fraction(2, 1), pff.parse("2", new ParsePosition(0)));
        assertEquals(new Fraction(3, 1), pff.parse("3", new ParsePosition(0)));
        assertEquals(new Fraction(4, 1), pff.parse("4", new ParsePosition(0)));
        assertEquals(new Fraction(5, 1), pff.parse("5", new ParsePosition(0)));
    }

    @Test
    public void testFormatAndParseNumerator() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertEquals("2/3", pff.format(new Fraction(2, 3), new StringBuffer(), new FieldPosition(0, 0)).substring(3));
        assertEquals("1/2", pff.format(new Fraction(1, 2), new StringBuffer(), new FieldPosition(0, 0)).substring(3));
        assertEquals("3/4", pff.format(new Fraction(3, 4), new StringBuffer(), new FieldPosition(0, 0)).substring(3));
        assertEquals("5/6", pff.format(new Fraction(5, 6), new StringBuffer(), new FieldPosition(0, 0)).substring(3));
        assertEquals("7/8", pff.format(new Fraction(7, 8), new StringBuffer(), new FieldPosition(0, 0)).substring(3));

        assertEquals(new Fraction(2, 3), pff.parse("1 2/3", new ParsePosition(0)));
        assertEquals(new Fraction(1, 2), pff.parse("1 1/2", new ParsePosition(0)));
        assertEquals(new Fraction(3, 4), pff.parse("1 3/4", new ParsePosition(0)));
        assertEquals(new Fraction(5, 6), pff.parse("1 5/6", new ParsePosition(0)));
        assertEquals(new Fraction(7, 8), pff.parse("1 7/8", new ParsePosition(0)));
    }

    @Test
    public void testFormatAndParseDenominator() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertEquals("3", pff.format(new Fraction(2, 3), new StringBuffer(), new FieldPosition(0, 0)).substring(0, 1));
        assertEquals("2", pff.format(new Fraction(1, 2), new StringBuffer(), new FieldPosition(0, 0)).substring(0, 1));
        assertEquals("4", pff.format(new Fraction(3, 4), new StringBuffer(), new FieldPosition(0, 0)).substring(0, 1));
        assertEquals("6", pff.format(new Fraction(5, 6), new StringBuffer(), new FieldPosition(0, 0)).substring(0, 1));
        assertEquals("8", pff.format(new Fraction(7, 8), new StringBuffer(), new FieldPosition(0, 0)).substring(0, 1));

        assertEquals(new Fraction(2, 3), pff.parse("1 2/3", new ParsePosition(0)));
        assertEquals(new Fraction(1, 2), pff.parse("1 1/2", new ParsePosition(0)));
        assertEquals(new Fraction(3, 4), pff.parse("1 3/4", new ParsePosition(0)));
        assertEquals(new Fraction(5, 6), pff.parse("1 5/6", new ParsePosition(0)));
        assertEquals(new Fraction(7, 8), pff.parse("1 7/8", new ParsePosition(0)));
    }

    @Test
    public void testFormatAndParseMixed() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertEquals("2 3/4", pff.format(new Fraction(7, 4), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("1 4/5", pff.format(new Fraction(4, 5), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("3 5/6", pff.format(new Fraction(19, 12), new StringBuffer(), new FieldPosition(0, 0)).toString());
        assertEquals("4 7/8", pff.format(new Fraction(32, 21), new StringBuffer(), new FieldPosition(0, 0)).toString());

        assertEquals(new Fraction(7, 4), pff.parse("2 3/4", new ParsePosition(0)));
        assertEquals(new Fraction(4, 5), pff.parse("1 4/5", new ParsePosition(0)));
        assertEquals(new Fraction(19, 12), pff.parse("3 5/6", new ParsePosition(0)));
        assertEquals(new Fraction(32, 21), pff.parse("4 7/8", new ParsePosition(0)));
    }

    @Test
    public void testParseInvalid() {
        ProperFractionFormat pff = new ProperFractionFormat();
        assertNull(pff.parse("", new ParsePosition(0)));
        assertNull(pff.parse("1", new ParsePosition(0)));
        assertNull(pff.parse("1/", new ParsePosition(0)));
        assertNull(pff.parse("1/0", new ParsePosition(0)));
        assertNull(pff.parse("1/-1", new ParsePosition(0)));
        assertNull(pff.parse("1/3/4", new ParsePosition(0)));
        assertNull(pff.parse("1 2/0", new ParsePosition(0)));
        assertNull(pff.parse("1 2/-1", new ParsePosition(0)));
        assertNull(pff.parse("1 2/3/4", new ParsePosition(0)));

        assertNull(pff.parse("1 2", new ParsePosition(0)));
        assertNull(pff.parse("1 2/", new ParsePosition(0)));
        assertNull(pff.parse("1 2/3", new ParsePosition(0)));
        assertNull(pff.parse("1 2/3/", new ParsePosition(0)));
        assertNull(pff.parse("1 2/3/4/", new ParsePosition(0)));
        assertNull(pff.parse("1 2/3/4/5", new ParsePosition(0)));
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.US);
        ProperFractionFormat pff1 = new ProperFractionFormat(wholeFormat);
        assertEquals(wholeFormat, pff1.getWholeFormat());

        pff.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, pff.getWholeFormat());

        ProperFractionFormat pff2 = new ProperFractionFormat();
        pff2.setWholeFormat(new DecimalFormat("0"));
        assertEquals(new DecimalFormat("0"), pff2.getWholeFormat());

        ProperFractionFormat pff3 = new ProperFractionFormat();
        pff3.setWholeFormat(new PercentFormat(Locale.ITALY));
        assertEquals(new PercentFormat(Locale.ITALY), pff3.getWholeFormat());

        ProperFractionFormat pff4 = new ProperFractionFormat();
        pff4.setWholeFormat(new NumberFormat() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                toAppendTo.append("X");
                return toAppendTo;
            }

            @Override
            public Object parse(String source, ParsePosition pos) {
                return null;
            }
        });
        assertEquals(new NumberFormat() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                toAppendTo.append("X");
                return toAppendTo;
            }

            @Override
            public Object parse(String source, ParsePosition pos) {
                return null;
            }
        }, pff4.getWholeFormat());
    }
}