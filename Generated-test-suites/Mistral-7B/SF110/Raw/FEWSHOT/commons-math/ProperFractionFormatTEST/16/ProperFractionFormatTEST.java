package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.util.Precision;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.junit.Test;

public class ProperFractionFormatTest {

    private NumberFormat defaultNumberFormat = NumberFormatUtils.getDefaultNumberFormat();
    private NumberFormat wholeFormat = new DecimalFormat("#");
    private NumberFormat numeratorFormat = NumberFormatUtils.getDefaultNumberFormat();
    private NumberFormat denominatorFormat = NumberFormatUtils.getDefaultNumberFormat();

    @Test
    public void testProperFractionFormatDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertSame(defaultNumberFormat, format.getWholeFormat());
    }

    @Test
    public void testProperFractionFormatNumberFormatConstructor() {
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        assertSame(wholeFormat, format.getWholeFormat());
    }

    @Test
    public void testProperFractionFormatThreeArgumentConstructor() {
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertSame(wholeFormat, format.getWholeFormat());
        assertSame(numeratorFormat, format.getNumeratorFormat());
        assertSame(denominatorFormat, format.getDenominatorFormat());
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction = new Fraction(8, 3);

        StringBuffer toAppendTo = new StringBuffer();
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));

        assertEquals("2 2/3", toAppendTo.toString());
    }

    @Test
    public void testFormatWithCustomWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat(new DecimalFormat("#,##0"));

        Fraction fraction = new Fraction(1234, 5);

        StringBuffer toAppendTo = new StringBuffer();
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));

        assertEquals("246 227/5", toAppendTo.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();

        ParsePosition pos = new ParsePosition(0);

        // Test parsing whole number
        String source = "2";
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(2, 1), result);
        assertEquals(4, pos.getIndex());

        // Test parsing fraction with no leading whitespace
        source = "2/3";
        result = format.parse(source, pos);
        assertEquals(new Fraction(2, 3), result);
        assertEquals(3, pos.getIndex());

        // Test parsing fraction with leading whitespace
        source = " 2/3";
        result = format.parse(source, pos);
        assertEquals(new Fraction(2, 3), result);
        assertEquals(3, pos.getIndex());

        // Test parsing fraction with leading and trailing hyphens
        source = "--2/--3--";
        result = format.parse(source, pos);
        assertEquals(new Fraction(2, 3), result);
        assertEquals(5, pos.getIndex());

        // Test parsing fraction with leading and trailing quotes
        source = "\"--2/--3--\"";
        result = format.parse(source, pos);
        assertEquals(new Fraction(2, 3), result);
        assertEquals(5, pos.getIndex());

        // Test parsing fraction with non-numeric characters
        source = "2/a";
        result = format.parse(source, pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());

        // Test parsing fraction with negative numerator
        source = "2/-3";
        result = format.parse(source, pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());

        // Test parsing fraction with negative denominator
        source = "-2/3";
        result = format.parse(source, pos);
        assertEquals(new Fraction(-2, 3), result);
        assertEquals(2, pos.getIndex());

        // Test parsing fraction with invalid number format
        source = "2/3a";
        result = format.parse(source, pos);
        assertNull(result);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        NumberFormat newWholeFormat = new DecimalFormat("#,##0.##");
        format.setWholeFormat(newWholeFormat);
        assertSame(newWholeFormat, format.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullArgument() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}