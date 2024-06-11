package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        String testString = "3/4";
        Fraction expected = new Fraction(3, 4);
        StringBuffer result = new StringBuffer();
        FormatUtils.format(expected, format, result, new FieldPosition(0, 0));
        assertEquals(testString, result.toString());
    }

    @Test
    public void testConstructorWithFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        String testString = "2/3";
        Fraction expected = new Fraction(2, 3);
        StringBuffer result = new StringBuffer();
        FormatUtils.format(expected, format, result, new FieldPosition(0, 0));
        assertEquals(testString, result.toString());
    }

    @Test
    public void testConstructorWithFormats() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        NumberFormat numeratorFormat = NumberFormat.getPercentInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        String testString = "1 1/2";
        Fraction expected = new Fraction(1, 2);
        StringBuffer result = new StringBuffer();
        FormatUtils.format(expected, format, result, new FieldPosition(0, 0));
        assertEquals(testString, result.toString());
    }

    @Test
    public void testFormatMethod() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer result = new StringBuffer();
        FormatUtils.format(fraction, format, result, new FieldPosition(0, 0));
        String testString = "1/2";
        assertEquals(testString, result.toString());
    }

    @Test
    public void testFormatWithWholePart() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(4, 2);
        StringBuffer result = new StringBuffer();
        FormatUtils.format(fraction, format, result, new FieldPosition(0, 0));
        String testString = "2 1/2";
        assertEquals(testString, result.toString());
    }

    @Test
    public void testParseMethod() {
        ProperFractionFormat format = new ProperFractionFormat();
        String testString = "3/4";
        Fraction expected = new Fraction(3, 4);
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(testString, pos);
        assertEquals(expected, result);
        assertEquals(testString.length(), pos.getIndex());
    }

    @Test
    public void testParseWithWholePart() {
        ProperFractionFormat format = new ProperFractionFormat();
        String testString = "2 1/2";
        Fraction expected = new Fraction(2, 2);
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(testString, pos);
        assertEquals(expected, result);
        assertEquals(testString.length(), pos.getIndex());
    }

    @Test
    public void testParseWithInvalidFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String testString = "3/0";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ArithmeticException.class, () -> format.parse(testString, pos));
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseWithInvalidNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String testString = "1 -1/2";
        ParsePosition pos = new ParsePosition(0);
        assertNull(format.parse(testString, pos));
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseWithInvalidDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String testString = "1/0";
        ParsePosition pos = new ParsePosition(0);
        assertNull(format.parse(testString, pos));
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullArgument() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}