package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Precision.*;

import org.apache.commons.math3.fraction.*;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.ParseException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import java.text.*;
import java.util.*;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatConstructorNoArgs() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format.getWholeFormat());
        assertNotNull(format.getNumeratorFormat());
        assertNotNull(format.getDenominatorFormat());
    }

    @Test
    public void testProperFractionFormatConstructorWithFormat() {
        DecimalFormat wholeFormat = (DecimalFormat) NumberFormatUtils.createNumberFormat("0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
        assertNotNull(format.getNumeratorFormat());
        assertNotNull(format.getDenominatorFormat());
    }

    @Test
    public void testProperFractionFormatConstructorWithFormats() {
        DecimalFormat wholeFormat = (DecimalFormat) NumberFormatUtils.createNumberFormat("0");
        DecimalFormat numeratorFormat = (DecimalFormat) NumberFormatUtils.createNumberFormat("#");
        DecimalFormat denominatorFormat = (DecimalFormat) NumberFormatUtils.createNumberFormat("0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
        assertEquals(numeratorFormat, format.getNumeratorFormat());
        assertEquals(denominatorFormat, format.getDenominatorFormat());
    }

    @Test
    public void testFormatPositiveFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(1, 2);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("1/2", toAppendTo.toString());
    }

    @Test
    public void testFormatNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(-1, 2);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("-1/2", toAppendTo.toString());
    }

    @Test
    public void testFormatZeroFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(0, 1);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("0", toAppendTo.toString());
    }

    @Test
    public void testFormatWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = (DecimalFormat) NumberFormatUtils.createNumberFormat("0");
        format.setWholeFormat(wholeFormat);
        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(3, 1);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("3", toAppendTo.toString());
    }

    @Test
    public void testFormatWholeNumberWithLeadingZero() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = (DecimalFormat) NumberFormatUtils.createNumberFormat("00");
        format.setWholeFormat(wholeFormat);
        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(7, 1);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals("07", toAppendTo.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

    @Test
    public void testParsePositiveFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Number whole = format.getWholeFormat().parse("1", pos);
        assertEquals(1, whole.intValue());
        Number num = format.getNumeratorFormat().parse("2", pos);
        assertEquals(2, num.intValue());
        Number den = format.getDenominatorFormat().parse("3", pos);
        assertEquals(3, den.intValue());
        Fraction fraction = format.parse("1 2/3", pos);
        assertEquals(1, fraction.getNumerator().intValue());
        assertEquals(3, fraction.getDenominator().intValue());
    }

    @Test
    public void testParseNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Number whole = format.getWholeFormat().parse("-1", pos);
        assertEquals(-1, whole.intValue());
        Number num = format.getNumeratorFormat().parse("2", pos);
        assertEquals(2, num.intValue());
        Number den = format.getDenominatorFormat().parse("3", pos);
        assertEquals(3, den.intValue());
        Fraction fraction = format.parse("--1 2/3", pos);
        assertEquals(-1, fraction.getNumerator().intValue());
        assertEquals(3, fraction.getDenominator().intValue());
    }

    @Test
    public void testParseZeroFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Number num = format.getNumeratorFormat().parse("0", pos);
        assertEquals(0, num.intValue());
        Number den = format.getDenominatorFormat().parse("1", pos);
        assertEquals(1, den.intValue());
        Fraction fraction = format.parse("0", pos);
        assertEquals(new Fraction(0, 1), fraction);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInvalidWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.getWholeFormat().parse("invalid", pos);
        fail("Expected NumberFormatException, but no exception was thrown.");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.parse("invalid", pos);
        fail("Expected ParseException, but no exception was thrown.");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseWhitespaceBeforeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.getWholeFormat().parse(" ", pos);
        format.parse("1 2/3", pos);
        fail("Expected NumberFormatException, but no exception was thrown.");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseWhitespaceAfterFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.parse("1 2/3 ", pos);
        fail("Expected NumberFormatException, but no exception was thrown.");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInvalidCharacterAfterNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.parse("1 a/3", pos);
        fail("Expected NumberFormatException, but no exception was thrown.");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInvalidCharacterBeforeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.parse("1 /b", pos);
        fail("Expected NumberFormatException, but no exception was thrown.");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.parse("1 a/3", pos);
        fail("Expected ParseException, but no exception was thrown.");
    }
}