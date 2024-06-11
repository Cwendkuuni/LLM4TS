package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Precision.equals;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.fraction.impl.DefaultFraction;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class ProperFractionFormatTest {
    private static final NumberFormat WHOLE_FORMAT = NumberFormatUtils.createNumberFormat("0.##");
    private static final NumberFormat NUMERATOR_FORMAT = NumberFormatUtils.createNumberFormat("0.##");
    private static final NumberFormat DENOMINATOR_FORMAT = NumberFormatUtils.createNumberFormat("0");

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals(NUMERATOR_FORMAT, format.getNumeratorFormat());
        assertEquals(DENOMINATOR_FORMAT, format.getDenominatorFormat());
        assertNull(format.getWholeFormat());
        ProperFractionFormat expected = new ProperFractionFormat(FractionFormat.getDefaultNumberFormat());
        Fraction actual = format.parse("1/2", new ParsePosition(0, null, null));
        assertEquals(expected.parse("1/2", new ParsePosition(0, null, null)), actual);
    }

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
        StringBuffer toAppendTo = new StringBuffer();
        Fraction fraction = new Fraction(1, 2);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0, 0));
        assertEquals("1 1/2", toAppendTo.toString());

        fraction = new Fraction(3, 4);
        toAppendTo.setLength(0);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0, 0));
        assertEquals("3 1/2", toAppendTo.toString());

        fraction = new Fraction(-1, 2);
        toAppendTo.setLength(0);
        format.format(fraction, toAppendTo, new FieldPosition(0, 0, 0));
        assertEquals("-1 1/2", toAppendTo.toString());
    }

    @Test
    public void testParse() {
        Fraction expected = null;
        ParsePosition pos = new ParsePosition(0, null, null);

        // Test default constructor
        ProperFractionFormat format = new ProperFractionFormat();
        expected = new Fraction(1, 2);
        Fraction actual = format.parse("1/2", pos);
        assertEquals(expected, actual);
        assertEquals(1, pos.getIndex());

        // Test constructor with NumberFormat
        format = new ProperFractionFormat(WHOLE_FORMAT, NUMERATOR_FORMAT, DENOMINATOR_FORMAT);
        expected = new Fraction(1, 2);
        actual = format.parse("1 1/2", pos);
        assertEquals(expected, actual);
        assertEquals(5, pos.getIndex());

        // Test parse with whole number
        pos.setIndex(0);
        expected = new Fraction(3, 1);
        actual = format.parse("3", pos);
        assertEquals(expected, actual);
        assertEquals(3, pos.getIndex());

        // Test parse with fraction
        pos.setIndex(0);
        expected = new Fraction(1, 3);
        actual = format.parse("1/3", pos);
        assertEquals(expected, actual);
        assertEquals(3, pos.getIndex());

        // Test parse with negative fraction
        pos.setIndex(0);
        expected = new Fraction(-1, 2);
        actual = format.parse("-1/2", pos);
        assertEquals(expected, actual);
        assertEquals(5, pos.getIndex());

        // Test parse with improper fraction
        pos.setIndex(0);
        expected = new Fraction(5, 2);
        actual = format.parse("2 1/2", pos);
        assertEquals(expected, actual);
        assertEquals(6, pos.getIndex());

        // Test parse with empty string
        pos.setIndex(0);
        assertNull(format.parse("", pos));
        assertEquals(0, pos.getIndex());

        // Test parse with invalid input
        pos.setIndex(0);
        String input = "1/0";
        Fraction actualParsed = format.parse(input, pos);
        assertNull(actualParsed);
        assertEquals(0, pos.getIndex());
        String errorMessage = expectedExceptionMessageForInput(input);
        assertEquals(errorMessage, pos.getMessage());
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat("0.#");
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    private String expectedExceptionMessageForInput(String input) {
        return "Expected number, but encountered: " + input;
    }
}