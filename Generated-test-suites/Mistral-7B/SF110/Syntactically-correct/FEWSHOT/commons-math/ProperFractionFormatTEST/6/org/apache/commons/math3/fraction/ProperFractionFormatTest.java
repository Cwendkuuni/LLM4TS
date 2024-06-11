package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testProperFractionFormatDefault() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 1/2";
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        Fraction fraction = format.parse(input, new ParsePosition(0));
        assertNotNull(fraction);
        assertEquals("3", toAppendTo.toString());
        format.format(new Fraction(3, 2), toAppendTo, pos);
        assertEquals("3 1/2", input);
    }

    @Test
    public void testProperFractionFormatCustomFormats() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance(), new DecimalFormat("0"), new DecimalFormat("0"));
        String input = "3 1/2";
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        Fraction fraction = format.parse(input, new ParsePosition(0));
        assertNotNull(fraction);
        assertEquals("3", toAppendTo.toString());
        format.format(new Fraction(3, 2), toAppendTo, pos);
        assertEquals("3 1/2", input);
    }

    @Test
    public void testProperFractionFormatCustomWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        wholeFormat.setMinimumFractionDigits(2);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, new DecimalFormat("0"), new DecimalFormat("0"));
        String input = "3.5 1/2";
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        Fraction fraction = format.parse(input, new ParsePosition(0));
        assertNotNull(fraction);
        assertEquals("3.5", toAppendTo.toString());
        format.format(new Fraction(3, 2), toAppendTo, pos);
        assertEquals("3.5 1/2", input);
    }

    @Test
    public void testProperFractionFormatParseWhole() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance(), new DecimalFormat("0"), new DecimalFormat("0"));
        String input = "3";
        ParsePosition pos = new ParsePosition(0);

        Number whole = format.getWholeFormat().parse(input, pos);
        assertNotNull(whole);
        assertEquals("3", input);
        assertEquals("3", whole.toString());
    }

    @Test
    public void testProperFractionFormatParseNumerator() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance(), new DecimalFormat("0"), new DecimalFormat("0"));
        String input = "3 2/3";
        ParsePosition pos = new ParsePosition(0);

        Number numerator = format.parse(input, pos).getNumerator();
        assertNotNull(numerator);
        assertEquals("2/3", input.substring(3));
        assertEquals("2", numerator.toString());
    }

    @Test
    public void testProperFractionFormatParseDenominator() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance(), new DecimalFormat("0"), new DecimalFormat("0"));
        String input = "3 2/3";
        ParsePosition pos = new ParsePosition(0);

        Number denominator = format.parse(input, pos).getDenominator();
        assertNotNull(denominator);
        assertEquals("3", input.substring(0, 2));
        assertEquals("3", denominator.toString());
    }

    @Test
    public void testProperFractionFormatParseInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance(), new DecimalFormat("0"), new DecimalFormat("0"));
        String input = "3 2/0";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = format.parse(input, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testProperFractionFormatParseInvalidInputWithWhole() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance(), new DecimalFormat("0"), new DecimalFormat("0"));
        String input = "3 2/0";
        ParsePosition pos = new ParsePosition(0);

        Number whole = format.getWholeFormat().parse(input, pos);
        assertNull(whole);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testProperFractionFormatParseInvalidNumerator() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance(), new DecimalFormat("0"), new DecimalFormat("0"));
        String input = "3 -1/2";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = format.parse(input, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testProperFractionFormatParseInvalidDenominator() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance(), new DecimalFormat("0"), new DecimalFormat("0"));
        String input = "3 1/-2";
        ParsePosition pos = new ParsePosition(0);

        Fraction fraction = format.parse(input, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testProperFractionFormatParseInvalidInputWithWholeAndNumerator() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance(), new DecimalFormat("0"), new DecimalFormat("0"));
        String input = "3 -1/2";
        ParsePosition pos = new ParsePosition(0);

        Number whole = format.getWholeFormat().parse(input, pos);
        assertNull(whole);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testProperFractionFormatParseInvalidInputWithWholeAndDenominator() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance(), new DecimalFormat("0"), new DecimalFormat("0"));
        String input = "3 --2";
        ParsePosition pos = new ParsePosition(0);

        Number whole = format.getWholeFormat().parse(input, pos);
        assertNull(whole);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testProperFractionFormatFormat() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance(), new DecimalFormat("0"), new DecimalFormat("0"));
        Fraction fraction = new Fraction(BigInteger.valueOf(3), BigInteger.valueOf(2));
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);
        assertEquals("3 1/2", toAppendTo.toString());
    }

    @Test
    public void testProperFractionFormatFormatWithWhole() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getNumberInstance(), new DecimalFormat("0"), new DecimalFormat("0"));
        Fraction fraction = new Fraction(3, 2).add(new Fraction(1, 1));
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);
        assertEquals("4 1/2", toAppendTo.toString());
    }
}