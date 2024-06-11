package org.apache.commons.math3.fraction.test;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testFormatSimpleFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        String expected = "1 3/4";
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, new FieldPosition(0, 0, 0, 0));
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFormatWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat(new DecimalFormat("#"));
        Fraction fraction = new Fraction(2);
        String expected = "2";
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, new FieldPosition(0, 0, 0, 0));
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFormatNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 4);
        String expected = "-1 3/4";
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, new FieldPosition(0, 0, 0, 0));
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFormatMixedWholeAndFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 2);
        String expected = "2 1/2";
        StringBuffer result = new StringBuffer();
        format.format(fraction, result, new FieldPosition(0, 0, 0, 0));
        assertEquals(expected, result.toString());
    }

    @Test
    public void testParseSimpleFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "1 3/4";
        Fraction expected = new Fraction(3, 4);
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = format.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "2";
        Fraction expected = new Fraction(2);
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = format.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-1 3/4";
        Fraction expected = new Fraction(-3, 4);
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = format.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseMixedWholeAndFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "2 1/2";
        Fraction expected = new Fraction(3, 2);
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = format.parse(input, pos);
        assertEquals(expected, actual);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFormat() {
        ProperFractionFormat format = new ProperFractionFormat(null);
    }
}