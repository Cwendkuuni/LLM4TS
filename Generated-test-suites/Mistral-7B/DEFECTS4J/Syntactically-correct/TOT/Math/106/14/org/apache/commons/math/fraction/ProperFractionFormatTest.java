package org.apache.commons.math.fraction.test;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.ParseException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link ProperFractionFormat} class.
 */
public class ProperFractionFormatTest {

    /**
     * Test proper formatting of a fraction with default number formats.
     */
    @Test
    public void testDefaultFormatting() {
        ProperFractionFormat format = new ProperFractionFormat();
        String expected = "3 1/2";
        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(3, 2);

        format.format(fraction, toAppendTo, null);

        assertEquals(expected, toAppendTo.toString());
    }

    /**
     * Test proper formatting of a fraction with custom number formats.
     */
    @Test
    public void testCustomFormatting() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat,
                numeratorFormat, denominatorFormat);
        String expected = "42 1/3";
        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(42, 3);

        format.format(fraction, toAppendTo, null);

        assertEquals(expected, toAppendTo.toString());
    }

    /**
     * Test parsing a proper fraction with default number formats.
     */
    @Test
    public void testDefaultParsing() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "3 1/2";

        Fraction fraction = format.parse(source, pos);

        assertEquals(new Fraction(3, 2), fraction);
        assertTrue(pos.getIndex() == source.length());
    }

    /**
     * Test parsing a proper fraction with custom number formats.
     */
    @Test
    public void testCustomParsing() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat,
                numeratorFormat, denominatorFormat);
        ParsePosition pos = new ParsePosition(0);
        String source = "42 1/3";

        Fraction fraction = format.parse(source, pos);

        assertEquals(new Fraction(42, 3), fraction);
        assertTrue(pos.getIndex() == source.length());
    }

    /**
     * Test parsing an improper fraction with default number formats.
     */
    @Test
    public void testImproperParsing() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "5 1/2";

        Fraction fraction = format.parse(source, pos);

        assertNull(fraction);
        assertTrue(pos.getErrorIndex() > 0);
    }

    /**
     * Test parsing an improper fraction with custom number formats.
     */
    @Test
    public void testCustomImproperParsing() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat,
                numeratorFormat, denominatorFormat);
        ParsePosition pos = new ParsePosition(0);
        String source = "5 1/2";

        Fraction fraction = format.parse(source, pos);

        assertNull(fraction);
        assertTrue(pos.getErrorIndex() > 0);
    }

    /**
     * Test parsing a proper fraction with negative whole number.
     */
    @Test
    public void testNegativeWholeParsing() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "-3 1/2";

        Fraction fraction = format.parse(source, pos);

        assertEquals(new Fraction(-3, 2), fraction);
        assertTrue(pos.getIndex() == source.length());
    }

    /**
     * Test parsing a proper fraction with negative whole number and custom number formats.
     */
    @Test
    public void testCustomNegativeWholeParsing() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat,
                numeratorFormat, denominatorFormat);
        ParsePosition pos = new ParsePosition(0);
        String source = "-3 1/2";

        Fraction fraction = format.parse(source, pos);

        assertEquals(new Fraction(-3, 2), fraction);
        assertTrue(pos.getIndex() == source.length());
    }

    /**
     * Test parsing a proper fraction with negative numerator.
     */
    @Test
    public void testNegativeNumeratorParsing() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "3 -1/2";

        Fraction fraction = format.parse(source, pos);

        assertNull(fraction);
        assertTrue(pos.getErrorIndex() > 0);
    }

    /**
     * Test parsing a proper fraction with negative numerator and custom number formats.
     */
    @Test
    public void testCustomNegativeNumeratorParsing() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat,
                numeratorFormat, denominatorFormat);
        ParsePosition pos = new ParsePosition(0);
        String source = "3 -1/2";

        Fraction fraction = format.parse(source, pos);

        assertNull(fraction);
        assertTrue(pos.getErrorIndex() > 0);
    }

    /**
     * Test parsing a proper fraction with double slash.
     */
    @Test
    public void testDoubleSlashParsing() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String source = "-3 -1/2";

        Fraction fraction = format.parse(source, pos);

        assertNull(fraction);
        assertTrue(pos.getErrorIndex() > 0);
    }

    /**
     * Test parsing a proper fraction with double slash and custom number formats.
     */
    @Test
    public void testCustomDoubleSlashParsing() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat,
                numeratorFormat, denominatorFormat);
        ParsePosition pos = new ParsePosition(0);
        String source = "-3 -1/2";

        Fraction fraction = format.parse(source, pos);

        assertNull(fraction);
        assertTrue(pos.getErrorIndex() > 0);
    }
}