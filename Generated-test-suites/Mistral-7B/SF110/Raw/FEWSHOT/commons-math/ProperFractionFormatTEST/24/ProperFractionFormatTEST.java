package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.NumberFormatUtils;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatDefault() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals("3 1/2", format.format(new Fraction(3, 2), new StringBuffer(), null));
        assertEquals(new Fraction(3, 2), format.parse("3 1/2", new ParsePosition(0)));
    }

    @Test
    public void testProperFractionFormatCustom() {
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat(Locale.ENGLISH, "#,##0.##");
        NumberFormat numeratorFormat = NumberFormatUtils.createNumberFormat(Locale.ENGLISH, "#,##0");
        NumberFormat denominatorFormat = NumberFormatUtils.createNumberFormat(Locale.ENGLISH, "#,##0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertEquals("3 1,25 / 2", format.format(new Fraction(3, 2), new StringBuffer(), null));
        assertEquals(new Fraction(3, 2), format.parse("3 1,25 / 2", new ParsePosition(0)));
    }

    @Test
    public void testProperFractionFormatWhole() {
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat(Locale.ENGLISH, "#,##0.##");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        assertEquals("3 1/2", format.format(new Fraction(3, 2), new StringBuffer(), null));
        assertEquals(new Fraction(3, 2), format.parse("3 1/2", new ParsePosition(0)));
        assertEquals("3 100,5 / 2", format.format(new Fraction(3, 100), new StringBuffer(), null));
        assertNull(format.parse("3 100,5 / 2", new ParsePosition(0)));
    }

    @Test
    public void testProperFractionFormatNumerator() {
        NumberFormat numeratorFormat = NumberFormatUtils.createNumberFormat(Locale.ENGLISH, "#,##0");
        ProperFractionFormat format = new ProperFractionFormat(null, numeratorFormat);
        assertEquals("1/2", format.format(new Fraction(1, 2), new StringBuffer(), null));
        assertEquals(new Fraction(1, 2), format.parse("1/2", new ParsePosition(0)));
        assertEquals("1,45 / 2", format.format(new Fraction(1, 4), new StringBuffer(), null));
        assertNull(format.parse("1,45 / 2", new ParsePosition(0)));
    }

    @Test
    public void testProperFractionFormatDenominator() {
        NumberFormat denominatorFormat = NumberFormatUtils.createNumberFormat(Locale.ENGLISH, "#,##0");
        ProperFractionFormat format = new ProperFractionFormat(null, null, denominatorFormat);
        assertEquals("3 / 2", format.format(new Fraction(3, 2), new StringBuffer(), null));
        assertEquals(new Fraction(3, 2), format.parse("3 / 2", new ParsePosition(0)));
        assertEquals("3 / 100", format.format(new Fraction(3, 100), new StringBuffer(), null));
        assertNull(format.parse("3 / 100", new ParsePosition(0)));
    }

    @Test(expected = NumberFormatException.class)
    public void testProperFractionFormatParseInvalidNumberFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.parse("Invalid", new ParsePosition(0));
    }

    @Test(expected = NumberFormatException.class)
    public void testProperFractionFormatParseInvalidWholeFormat() {
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat(Locale.ENGLISH, "0#,##0.##");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        format.parse("3 1/2", new ParsePosition(0));
    }

    @Test(expected = NumberFormatException.class)
    public void testProperFractionFormatParseInvalidNumeratorFormat() {
        NumberFormat numeratorFormat = NumberFormatUtils.createNumberFormat(Locale.ENGLISH, "#,##0.##");
        ProperFractionFormat format = new ProperFractionFormat(null, numeratorFormat);
        format.parse("3 1/2", new ParsePosition(0));
    }

    @Test(expected = NumberFormatException.class)
    public void testProperFractionFormatParseInvalidDenominatorFormat() {
        NumberFormat denominatorFormat = NumberFormatUtils.createNumberFormat(Locale.ENGLISH, "#,##0.##");
        ProperFractionFormat format = new ProperFractionFormat(null, null, denominatorFormat);
        format.parse("3 / 2", new ParsePosition(0));
    }

    @Test(expected = LocalizedFormats.WHOLE_FORMAT_MUST_NOT_BE_NULL_EXCEPTION.class)
    public void testProperFractionFormatSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}