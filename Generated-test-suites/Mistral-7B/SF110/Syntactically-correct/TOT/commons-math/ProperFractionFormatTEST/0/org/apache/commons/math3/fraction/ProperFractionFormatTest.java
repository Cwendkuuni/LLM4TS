package org.apache.commons.math3.fraction.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        String formatted = format.format(fraction, new StringBuffer(), null);
        assertEquals("1 / 2", formatted.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(formatted, pos);
        assertEquals(fraction, parsed);
    }

    @Test
    public void testCustomFormat() {
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat(Locale.US, "0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, null, null);
        Fraction fraction = new Fraction(3, 4);
        String formatted = format.format(fraction, new StringBuffer(), null);
        assertEquals("3 / 4", formatted.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(formatted, pos);
        assertEquals(fraction, parsed);
    }

    @Test
    public void testParseWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(4, 1);
        String formatted = format.format(fraction, new StringBuffer(), null);
        assertEquals("4", formatted.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(formatted, pos);
        assertEquals(fraction, parsed);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testParseNegativeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "-1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testParseNegativeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "1 / -2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testParseInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "1 a/b";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat(Locale.US, "#,##0.##");
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(wholeFormat);
        Fraction fraction = new Fraction(3, 4);
        String formatted = format.format(fraction, new StringBuffer(), null);
        assertEquals("3  /  4", formatted.toString());

        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(formatted, pos);
        assertEquals(fraction, parsed);
    }
}