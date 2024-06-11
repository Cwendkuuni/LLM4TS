package org.apache.commons.math3.fraction.test;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testFormatProperFraction() {
        FractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(4, 5);
        String expected = "1 3/5";
        StringBuffer result = new StringBuffer(100);
        format.format(fraction, result, new FieldPosition(0, 0));
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testFormatWholeNumber() {
        FractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat("0.0%");
        FractionFormat newFormat = new ProperFractionFormat(wholeFormat, null, null);
        int number = 123;
        String expected = "123";
        StringBuffer result = new StringBuffer(100);
        newFormat.format(new Fraction(number, 1), result, new FieldPosition(0, 0));
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testFormatNegativeNumber() {
        FractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat("0.0%");
        FractionFormat newFormat = new ProperFractionFormat(wholeFormat, null, null);
        int number = -123;
        String expected = "-1 23/1";
        StringBuffer result = new StringBuffer(100);
        newFormat.format(new Fraction(number, 1), result, new FieldPosition(0, 0));
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testParseProperFraction() {
        FractionFormat format = new ProperFractionFormat();
        String input = "1 3/5";
        Fraction expected = new Fraction(1, 5, 3);
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        Assert.assertEquals(expected, result);
        Assert.assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseWholeNumber() {
        FractionFormat format = new ProperFractionFormat();
        String input = "123";
        Fraction expected = new Fraction(123);
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        Assert.assertEquals(expected, result);
        Assert.assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseFractionWithWholeNumber() {
        FractionFormat format = new ProperFractionFormat();
        String input = "1 3/5";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(input, pos);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.getNumerator().intValueExact());
        Assert.assertEquals(5, result.getDenominator().intValueExact());
        Assert.assertEquals(input.length(), pos.getIndex());
    }

    @Test
    public void testParseNegativeFraction() {
        FractionFormat format = new ProperFractionFormat();
        String input = "-1 3/5";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(-1, 5, 3);
        Fraction result = format.parse(input, pos);
        Assert.assertEquals(expected, result);
        Assert.assertEquals(input.length(), pos.getIndex());
    }

    @Test(expected = NullArgumentException.class)
    public void testParseNullFormat() {
        FractionFormat format = new ProperFractionFormat(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testParseNullSource() {
        FractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        format.parse(null, pos);
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullParsePosition() {
        FractionFormat format = new ProperFractionFormat();
        String source = "1 2/3";
        format.parse(source, null);
    }
}