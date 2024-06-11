package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.fraction.DefaultFraction;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import java.text.NumberFormat;
import java.util.Locale;

public class ProperFractionFormatTest {
    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat pff = new ProperFractionFormat();
        NumberFormat wholeFormat = pff.getWholeFormat();
        assertEquals(NumberFormat.getNumberInstance(Locale.US), wholeFormat);
    }

    @Test
    public void testCustomWholeFormatConstructor() {
        NumberFormat customWholeFormat = NumberFormatUtils.createNumberFormat(Locale.US, "#,##0.#");
        ProperFractionFormat pff = new ProperFractionFormat(customWholeFormat);
        assertEquals(customWholeFormat, pff.getWholeFormat());
    }

    @Test
    public void testFormatMethod() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Fraction testFraction = new Fraction(3, 4);
        StringBuffer toAppendTo = new StringBuffer(100);
        FieldPosition pos = new FieldPosition(0, 0);

        String expectedOutput = "1 3/4";
        pff.format(testFraction, toAppendTo, pos);
        assertEquals(expectedOutput, toAppendTo.toString());
    }

    @Test
    public void testFormatWithWholePartMethod() {
        ProperFractionFormat pff = new ProperFractionFormat();
        Fraction testFraction = new Fraction(7, 2);
        StringBuffer toAppendTo = new StringBuffer(100);
        FieldPosition pos = new FieldPosition(0, 0);

        String expectedOutput = "2 1/2";
        pff.setWholeFormat(NumberFormat.getNumberInstance(Locale.US));
        pff.format(testFraction, toAppendTo, pos);
        assertEquals(expectedOutput, toAppendTo.toString());
    }

    @Test
    public void testParseMethod() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String testString = "1 3/4";
        ParsePosition pos = new ParsePosition(0);

        Fraction expectedFraction = new Fraction(1, 4);
        Fraction parsedFraction = pff.parse(testString, pos);
        assertEquals(expectedFraction, parsedFraction);
    }

    @Test
    public void testParseMethodWithWholePart() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String testString = "2 1/2";
        ParsePosition pos = new ParsePosition(0);

        Fraction expectedFraction = new Fraction(2, 2);
        Fraction parsedFraction = pff.parse(testString, pos);
        assertEquals(expectedFraction, parsedFraction);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNull() {
        ProperFractionFormat pff = new ProperFractionFormat();
        pff.setWholeFormat(null);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseWithInvalidStringFormat() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String testString = "1 a/b";
        ParsePosition pos = new ParsePosition(0);

        Fraction parsedFraction = pff.parse(testString, pos);
        assertNull(parsedFraction);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseWithInvalidDenominator() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String testString = "1 0/2";
        ParsePosition pos = new ParsePosition(0);

        Fraction parsedFraction = pff.parse(testString, pos);
        assertNull(parsedFraction);
    }

    @Test
    public void testParseWithLargeFraction() {
        ProperFractionFormat pff = new ProperFractionFormat();
        String testString = "2147483647 2147483647/2147483647";
        ParsePosition pos = new ParsePosition(0);

        Fraction expectedFraction = new Fraction(2147483647, 1);
        Fraction parsedFraction = pff.parse(testString, pos);
        assertEquals(expectedFraction, parsedFraction);
    }
}