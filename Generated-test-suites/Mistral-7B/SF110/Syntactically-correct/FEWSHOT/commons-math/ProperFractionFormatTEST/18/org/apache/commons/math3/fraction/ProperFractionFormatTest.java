package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    private NumberFormat defaultNumberFormat;
    private NumberFormat integerFormat;
    private NumberFormat decimalFormat;
    private ProperFractionFormat properFractionFormat;
    private ProperFractionFormat properFractionFormatWithCustomFormats;

    @Before
    public void setUp() {
        defaultNumberFormat = NumberFormat.getNumberInstance();
        integerFormat = new DecimalFormat("#");
        decimalFormat = new DecimalFormat("#.#");
        properFractionFormat = new ProperFractionFormat();
        properFractionFormatWithCustomFormats = new ProperFractionFormat(decimalFormat, integerFormat, decimalFormat);
    }

    @Test
    public void testFormatFraction() {
        Fraction fraction = new Fraction(1, 2);
        String expectedOutput = "1/2";
        StringBuffer toAppendTo = new StringBuffer();
        ProperFractionFormat format = properFractionFormat;
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals(expectedOutput, toAppendTo.toString());

        fraction = new Fraction(3, 4);
        expectedOutput = "3/4";
        toAppendTo = new StringBuffer();
        format = properFractionFormat;
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals(expectedOutput, toAppendTo.toString());

        fraction = new Fraction(-5, 3);
        expectedOutput = "-5/3";
        toAppendTo = new StringBuffer();
        format = properFractionFormat;
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals(expectedOutput, toAppendTo.toString());

        fraction = new Fraction(0, 4);
        expectedOutput = "0 / 4";
        toAppendTo = new StringBuffer();
        format = properFractionFormat;
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals(expectedOutput, toAppendTo.toString());

        fraction = new Fraction(1, 1);
        expectedOutput = "1";
        toAppendTo = new StringBuffer();
        format = properFractionFormatWithCustomFormats;
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals(expectedOutput, toAppendTo.toString());

        fraction = new Fraction(2, 1);
        expectedOutput = "2";
        toAppendTo = new StringBuffer();
        format = properFractionFormatWithCustomFormats;
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals(expectedOutput, toAppendTo.toString());

        fraction = new Fraction(1, 5);
        expectedOutput = "1/5";
        toAppendTo = new StringBuffer();
        format = properFractionFormatWithCustomFormats;
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals(expectedOutput, toAppendTo.toString());

        fraction = new Fraction(-3, 5);
        expectedOutput = "-3/5";
        toAppendTo = new StringBuffer();
        format = properFractionFormatWithCustomFormats;
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals(expectedOutput, toAppendTo.toString());

        fraction = new Fraction(BigDecimal.valueOf(1.2345), BigDecimal.valueOf(5.6789));
        expectedOutput = "0.2345/5.6789";
        toAppendTo = new StringBuffer();
        format = properFractionFormatWithCustomFormats;
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals(expectedOutput, toAppendTo.toString());

        fraction = new Fraction(new BigDecimal("123456789012345678901234567890123456789"), new BigDecimal("9876543210987654321098765432109876543210987654321098765432109876543210987654321"));
        expectedOutput = "123456789012345678901234567890123456789/9876543210987654321098765432109876543210987654321098765432109876543210987654321";
        toAppendTo = new StringBuffer();
        format = properFractionFormatWithCustomFormats;
        format.format(fraction, toAppendTo, new FieldPosition(0, 0));
        assertEquals(expectedOutput, toAppendTo.toString());
    }

    @Test
    public void testParseFraction() {
        String source = "1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction expectedFraction = new Fraction(1, 2);
        Fraction parsedFraction = properFractionFormat.parse(source, pos);
        assertEquals(expectedFraction, parsedFraction);
        assertEquals(pos.getIndex(), source.length());

        source = "3/4";
        expectedFraction = new Fraction(3, 4);
        parsedFraction = properFractionFormat.parse(source, pos);
        assertEquals(expectedFraction, parsedFraction);
        assertEquals(pos.getIndex(), source.length());

        source = "-5/3";
        expectedFraction = new Fraction(-5, 3);
        parsedFraction = properFractionFormat.parse(source, pos);
        assertEquals(expectedFraction, parsedFraction);
        assertEquals(pos.getIndex(), source.length());

        source = "0 / 4";
        expectedFraction = new Fraction(0, 4);
        parsedFraction = properFractionFormat.parse(source, pos);
        assertEquals(expectedFraction, parsedFraction);
        assertEquals(pos.getIndex(), source.length());

        source = "1";
        expectedFraction = new Fraction(1, 1);
        parsedFraction = properFractionFormatWithCustomFormats.parse(source, pos);
        assertEquals(expectedFraction, parsedFraction);
        assertEquals(pos.getIndex(), source.length());

        source = "2";
        expectedFraction = new Fraction(2, 1);
        parsedFraction = properFractionFormatWithCustomFormats.parse(source, pos);
        assertEquals(expectedFraction, parsedFraction);
        assertEquals(pos.getIndex(), source.length());

        source = "1/5";
        expectedFraction = new Fraction(1, 5);
        parsedFraction = properFractionFormatWithCustomFormats.parse(source, pos);
        assertEquals(expectedFraction, parsedFraction);
        assertEquals(pos.getIndex(), source.length());

        source = "-3/5";
        expectedFraction = new Fraction(-3, 5);
        parsedFraction = properFractionFormatWithCustomFormats.parse(source, pos);
        assertEquals(expectedFraction, parsedFraction);
        assertEquals(pos.getIndex(), source.length());

        source = "0.2345/5.6789";
        expectedFraction = new Fraction(new BigDecimal("0.2345"), new BigDecimal("5.6789"));
        parsedFraction = properFractionFormatWithCustomFormats.parse(source, pos);
        assertEquals(expectedFraction, parsedFraction);
        assertEquals(pos.getIndex(), source.length());

        source = "123456789012345678901234567890123456789/98765432109876543210987654321098765432109876543210987654321098765432109876543210987654321";
        expectedFraction = new Fraction(new BigDecimal("123456789012345678901234567890123456789"), new BigDecimal("987654321098765432109876543210987654321098765432109876543210987654321098765432109876543210987654321"));
        parsedFraction = properFractionFormatWithCustomFormats.parse(source, pos);
        assertEquals(expectedFraction, parsedFraction);
        assertEquals(pos.getIndex(), source.length());

        source = "1/0";
        assertNull(properFractionFormat.parse(source, pos));
        assertEquals(pos.getIndex(), 0);

        source = "1/-0";
        assertNull(properFractionFormat.parse(source, pos));
        assertEquals(pos.getIndex(), 0);

        source = "1/3+4/5";
        assertNull(properFractionFormat.parse(source, pos));
        assertEquals(pos.getIndex(), 0);

        source = "1.2345/5.6789";
        assertNull(properFractionFormatWithCustomFormats.parse(source, pos));
        assertEquals(pos.getIndex(), 0);

        source = "1/3+4/5";
        assertNull(properFractionFormatWithCustomFormats.parse(source, pos));
        assertEquals(pos.getIndex(), 0);

        source = "1/3*4/5";
        assertNull(properFractionFormatWithCustomFormats.parse(source, pos));
        assertEquals(pos.getIndex(), 0);

        source = "1/3*4/5+1/3*2/3";
        assertNull(properFractionFormatWithCustomFormats.parse(source, pos));
        assertEquals(pos.getIndex(), 0);

        source = "1/3*4/5*2/3";
        expectedFraction = new Fraction(new BigDecimal("2"), new BigDecimal("3"));
        parsedFraction = properFractionFormatWithCustomFormats.parse(source, pos);
        assertEquals(expectedFraction, parsedFraction);
        assertEquals(pos.getIndex(), source.length());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}