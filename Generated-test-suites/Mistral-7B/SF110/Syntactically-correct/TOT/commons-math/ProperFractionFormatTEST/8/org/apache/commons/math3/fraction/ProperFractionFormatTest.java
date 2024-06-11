package org.apache.commons.math3.fraction.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testFormatProperFraction() {
        NumberFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));
        Fraction fraction = new Fraction(1, 2);
        String expected = "1 / 2";
        StringBuffer toAppendTo = new StringBuffer();
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        String actual = toAppendTo.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatMixedFraction() {
        NumberFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));
        Fraction fraction = new Fraction(3, 2);
        String expected = "1 1 / 2";
        StringBuffer toAppendTo = new StringBuffer();
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        String actual = toAppendTo.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWholeNumber() {
        NumberFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));
        Fraction fraction = new Fraction(4);
        String expected = "4";
        StringBuffer toAppendTo = new StringBuffer();
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        String actual = toAppendTo.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testParseProperFraction() {
        NumberFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));
        String source = "1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(1, 2);
        Fraction actual = format.parse(source, pos);
        assertEquals(expected, actual);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseMixedFraction() {
        NumberFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));
        String source = "1 1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(3, 2);
        Fraction actual = format.parse(source, pos);
        assertEquals(expected, actual);
        assertEquals(3, pos.getIndex());
    }

    @Test
    public void testParseWholeNumber() {
        NumberFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));
        String source = "4";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(4);
        Fraction actual = format.parse(source, pos);
        assertEquals(expected, actual);
        assertEquals(4, pos.getIndex());
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testParseInvalidSource() {
        NumberFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));
        String source = "4 1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = format.parse(source, pos);
        fail("Should throw MathIllegalArgumentException");
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testParseNegativeNumerator() {
        NumberFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));
        String source = "1 / -2";
        ParsePosition pos = new ParsePosition(0);
        Fraction actual = format.parse(source, pos);
        fail("Should throw MathIllegalArgumentException");
    }

    @Test
    public void testParseLargeFraction() {
        NumberFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, new DecimalFormat("#"), new DecimalFormat("#"));
        String source = "12345 / 67890";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(BigInteger.valueOf(12345), BigInteger.valueOf(67890));
        Fraction actual = format.parse(source, pos);
        assertEquals(expected, actual);
        BigDecimal expectedValue = new BigDecimal(expected.doubleValue(), MathContext.DECIMAL128);
        BigDecimal actualValue = new BigDecimal(actual.doubleValue(), MathContext.DECIMAL128);
        assertEquals(expectedValue, actualValue, Precision.DEFAULT);
        assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat wholeFormat = new DecimalFormat("#");
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(wholeFormat);
        NumberFormat actualWholeFormat = format.getWholeFormat();
        assertEquals(wholeFormat, actualWholeFormat);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
        fail("Should throw NullArgumentException");
    }
}