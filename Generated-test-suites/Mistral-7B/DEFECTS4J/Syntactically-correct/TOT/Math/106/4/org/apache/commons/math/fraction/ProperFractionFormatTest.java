package org.apache.commons.math.fraction.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.fraction.DefaultFraction;
import org.apache.commons.math3.util.MathUtils;

import org.apache.commons.math.fraction.ProperFractionFormat;

public class ProperFractionFormatTest {

    /**
     * Test proper formatting of a fraction.
     */
    @org.junit.Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String expected = "4 3/5";
        StringBuffer buffer = new StringBuffer();
        FormatTestHelper.testFormat(format, new Fraction(4, 3, 5), buffer);
        assertEquals(expected, buffer.toString());
    }

    /**
     * Test proper formatting of a fraction with custom whole format.
     */
    @org.junit.Test
    public void testFormatWithCustomWhole() {
        Locale locale = new Locale("en", "US");
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(locale);
        wholeFormat.setMinimumFractionDigits(1);
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        String expected = "-2.5 3/4";
        StringBuffer buffer = new StringBuffer();
        FormatTestHelper.testFormat(format, new Fraction(-2, 1, 5), buffer);
        assertEquals(expected, buffer.toString());
    }

    /**
     * Test improper formatting of a fraction.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-3 -1/2";
        StringBuffer buffer = new StringBuffer();
        format.format(new Fraction(-3, -1, 2), buffer, new FieldPosition(0, 0));
    }

    /**
     * Test parse of a proper fraction.
     */
    @org.junit.Test
    public void testParseProper() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String input = "2 3/4";
        Fraction expected = new Fraction(2, 3, 4);
        Fraction actual = format.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

    /**
     * Test parse of an improper fraction.
     */
    @org.junit.Test
    public void testParseImproper() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String input = "8 1/2";
        Fraction expected = new Fraction(8, 1);
        Fraction actual = format.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

    /**
     * Test parse of a fraction with negative whole.
     */
    @org.junit.Test
    public void testParseNegativeWhole() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String input = "-3 1/2";
        Fraction expected = new Fraction(-3, 1, 2);
        Fraction actual = format.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

    /**
     * Test parse of a fraction with custom whole format.
     */
    @org.junit.Test
    public void testParseCustomWhole() {
        Locale locale = new Locale("en", "US");
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(locale);
        wholeFormat.setMinimumFractionDigits(1);
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        ParsePosition pos = new ParsePosition(0);
        String input = "-2.5 3/4";
        Fraction expected = new Fraction(-2, 1, 4, 3);
        Fraction actual = format.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

    /**
     * Test parse of a fraction with negative sign in denominator.
     */
    @org.junit.Test
    public void testParseNegativeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String input = "1 -2/3";
        Fraction expected = new Fraction(1, -2, 3);
        Fraction actual = format.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

    /**
     * Test parse of a fraction with custom numerator and denominator formats.
     */
    @org.junit.Test
    public void testParseCustomNumeratorAndDenominator() {
        Locale locale = new Locale("en", "US");
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(locale);
        numeratorFormat.setMinimumFractionDigits(1);
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance(locale);
        denominatorFormat.setMinimumFractionDigits(1);
        ProperFractionFormat format = new ProperFractionFormat(null, numeratorFormat,
                denominatorFormat);
        ParsePosition pos = new ParsePosition(0);
        String input = "-2.5 3/4";
        Fraction expected = new Fraction(-2, 1, 4, 3);
        Fraction actual = format.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

    /**
     * Test parse of a fraction with an invalid input.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        String input = "3 -1/2";
        format.parse(input, pos);
    }

    /**
     * Helper class to test formatting and parsing of fractions.
     */
    private static class FormatTestHelper {

        public static void testFormat(FractionFormat format, Fraction fraction,
                StringBuffer buffer) {
            Fraction clone = fraction.clone();
            clone.setDenominator(clone.getDenominator().abs());
            format.format(clone, buffer, new FieldPosition(0, 0));
        }

        public static void testFormat(FractionFormat format, String input,
                String expected) {
            StringBuffer buffer = new StringBuffer();
            testFormat(format, new Fraction(input), buffer);
            assertEquals(expected, buffer.toString());
        }

        public static void testFormat(FractionFormat format, String input,
                String expected, Locale locale) {
            NumberFormat numeratorFormat = NumberFormat.getNumberInstance(locale);
            numeratorFormat.setMinimumFractionDigits(1);
            NumberFormat denominatorFormat = NumberFormat.getNumberInstance(locale);
            denominatorFormat.setMinimumFractionDigits(1);
            ProperFractionFormat properFormat = new ProperFractionFormat(
                    numeratorFormat, denominatorFormat);
            StringBuffer buffer = new StringBuffer();
            testFormat(properFormat, new Fraction(input), buffer);
            assertEquals(expected, buffer.toString());
        }

        public static void parseAndIgnoreWhitespace(String input, ParsePosition pos) {
            int index = pos.getIndex();
            int length = input.length();
            while (index < length && Character.isWhitespace(input.charAt(index))) {
                index++;
            }
            pos.setIndex(index);
        }

        public static char parseNextCharacter(String input, ParsePosition pos) {
            int index = pos.getIndex();
            if (index < input.length()) {
                char c = input.charAt(index);
                pos.setIndex(index + 1);
                return c;
            }
            return 0;
        }
    }
}