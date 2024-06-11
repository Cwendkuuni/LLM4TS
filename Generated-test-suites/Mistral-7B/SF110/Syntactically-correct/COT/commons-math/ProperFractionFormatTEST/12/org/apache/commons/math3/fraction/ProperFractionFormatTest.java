package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testEmptyConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        assertNotNull(properFractionFormat);
    }

    @Test
    public void testConstructorWithFormat() {
        NumberFormat format = new DecimalFormat();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(format);
        assertNotNull(properFractionFormat);
    }

    @Test
    public void testConstructorWithWholeFormatNumeratorFormatDenominatorFormat() {
        NumberFormat wholeFormat = new DecimalFormat();
        NumberFormat numeratorFormat = new DecimalFormat();
        NumberFormat denominatorFormat = new DecimalFormat();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(properFractionFormat);
    }

    @Test
    public void testFormatMethodWithValidFraction() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer expected = new StringBuffer();
        properFractionFormat.format(fraction, expected, new FieldPosition(0, 0));
        String actual = expected.toString();
        String expectedFormatted = "3 / 4";
        assertEquals(expectedFormatted, actual);
    }

    @Test
    public void testFormatMethodWithZeroWholePart() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(4, 4);
        StringBuffer expected = new StringBuffer();
        properFractionFormat.format(fraction, expected, new FieldPosition(0, 0));
        String actual = expected.toString();
        String expectedFormatted = "0 / 1";
        assertEquals(expectedFormatted, actual);
    }

    @Test
    public void testFormatMethodWithNegativeNumerator() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 4);
        StringBuffer expected = new StringBuffer();
        properFractionFormat.format(fraction, expected, new FieldPosition(0, 0));
        String actual = expected.toString();
        String expectedFormatted = "-3 / 4";
        assertEquals(expectedFormatted, actual);
    }

    @Test
    public void testFormatMethodWithNegativeDenominator() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, -4);
        StringBuffer expected = new StringBuffer();
        properFractionFormat.format(fraction, expected, new FieldPosition(0, 0));
        String actual = expected.toString();
        String expectedFormatted = "3 / -4";
        assertEquals(expectedFormatted, actual);
    }

    @Test
    public void testParseMethodWithValidInput() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "3/4";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction expectedResult = new Fraction(3, 4);
        Fraction actualResult = properFractionFormat.parse(input, parsePosition);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseMethodWithInvalidInputNoSlash() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "3";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction expectedResult = null;
        Fraction actualResult = properFractionFormat.parse(input, parsePosition);
        assertNull(actualResult);
        assertEquals(0, parsePosition.getIndex());
    }

    @Test
    public void testParseMethodWithInvalidInputNoNumerator() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "/4";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction expectedResult = null;
        Fraction actualResult = properFractionFormat.parse(input, parsePosition);
        assertNull(actualResult);
        assertEquals(0, parsePosition.getIndex());
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testParseMethodWithInvalidInputNegativeNumerator() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "-3/4";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(input, parsePosition);
        fail("Expected MathIllegalArgumentException but got " + result);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testParseMethodWithInvalidInputNegativeDenominator() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        String input = "3/-4";
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction result = properFractionFormat.parse(input, parsePosition);
        fail("Expected MathIllegalArgumentException but got " + result);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        try {
            properFractionFormat.setWholeFormat(null);
        } catch (NullArgumentException e) {
            assertEquals(LocalizedFormats.WHOLE_FORMAT, e.getLocalizedMessage());
            throw e;
        }
    }
}