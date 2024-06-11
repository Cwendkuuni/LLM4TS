package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.FieldPosition;
import java.util.Locale;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatEmptyConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
    }

    @Test
    public void testProperFractionFormatWithFormatConstructor() {
        ProperFractionFormat format = new ProperFractionFormat(new DecimalFormat("0"));
        assertNotNull(format);
    }

    @Test
    public void testProperFractionFormatWithThreeFormatsConstructor() {
        NumberFormat wholeFormat = new DecimalFormat("0");
        NumberFormat numeratorFormat = new DecimalFormat("0");
        NumberFormat denominatorFormat = new DecimalFormat("0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(format);
    }

    @Test
    public void testFormatMethodWithZeroFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 1);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0, 0);
        format.format(fraction, toAppendTo, fieldPosition);
        assertEquals("0", toAppendTo.toString());
    }

    @Test
    public void testFormatMethodWithProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0, 0);
        format.format(fraction, toAppendTo, fieldPosition);
        assertEquals("3/4", toAppendTo.toString());
    }

    @Test
    public void testFormatMethodWithMixedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(5, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0, 0);
        format.format(fraction, toAppendTo, fieldPosition);
        assertEquals("2 1/2", toAppendTo.toString());
    }

    @Test
    public void testFormatMethodWithNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 4);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0, 0);
        format.format(fraction, toAppendTo, fieldPosition);
        assertEquals("-3/4", toAppendTo.toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertEquals(Format.getDefaultNumberFormat(), wholeFormat);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

    @Test
    public void testParseMethodWithZeroFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("0", pos);
        assertEquals(new Fraction(0, 1), fraction);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParseMethodWithProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("3/4", pos);
        assertEquals(new Fraction(3, 4), fraction);
        assertEquals(3, pos.getIndex());
    }

    @Test
    public void testParseMethodWithMixedFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("2 1/2", pos);
        assertEquals(new Fraction(1, 2, new BigInteger("2")), fraction);
        assertEquals(4, pos.getIndex());
    }

    @Test
    public void testParseMethodWithNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("-3/4", pos);
        assertEquals(new Fraction(-3, 4), fraction);
        assertEquals(3, pos.getIndex());
    }

    @Test(expected = NullArgumentException.class)
    public void testParseMethodWithNullSource() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.parse(null, pos);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseMethodWithInvalidSource() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.parse("invalid", pos);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseMethodWithInvalidCharacter() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.parse("3/a", pos);
    }
}