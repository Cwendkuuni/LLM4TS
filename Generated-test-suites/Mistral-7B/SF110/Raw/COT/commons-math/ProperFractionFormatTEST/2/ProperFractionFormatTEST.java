package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.NullArgumentException;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    @org.junit.Before
    public void setUp() {
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat(Locale.US, "0");
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat);
        this.properFractionFormat = properFractionFormat;
    }

    // Test constructor with default NumberFormat
    @org.junit.Test
    public void testDefaultConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        assertNotNull(properFractionFormat);
    }

    // Test constructor with custom NumberFormat
    @org.junit.Test
    public void testCustomConstructor() {
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat(Locale.US, "#,##0");
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat);
        assertNotNull(properFractionFormat);
    }

    // Test custom constructor with three NumberFormat instances
    @org.junit.Test
    public void testCustomConstructorWithThreeFormats() {
        NumberFormat wholeFormat = NumberFormatUtils.createNumberFormat(Locale.US, "#,##0");
        NumberFormat numeratorFormat = NumberFormatUtils.createNumberFormat(Locale.US, "0");
        NumberFormat denominatorFormat = NumberFormatUtils.createNumberFormat(Locale.US, "#,##0");
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(properFractionFormat);
    }

    // Test format method with a proper fraction
    @org.junit.Test
    public void testFormatProperFraction() {
        Fraction fraction = new Fraction(3, 4);
        String expected = "1 1/2";
        StringBuffer stringBuffer = new StringBuffer();
        ProperFractionFormat.format(fraction, stringBuffer, new FieldPosition(0, 0, 0, 0));
        String actual = stringBuffer.toString();
        assertEquals(expected, actual);
    }

    // Test format method with a fraction where the whole part is not zero
    @org.junit.Test
    public void testFormatFractionWithWholePartGreaterThanZero() {
        Fraction fraction = new Fraction(5, 2);
        String expected = "2 1/2";
        StringBuffer stringBuffer = new StringBuffer();
        ProperFractionFormat.format(fraction, stringBuffer, new FieldPosition(0, 0, 0, 0));
        String actual = stringBuffer.toString();
        assertEquals(expected, actual);
    }

    // Test format method with a fraction where the whole part is zero
    @org.junit.Test
    public void testFormatFractionWithWholePartZero() {
        Fraction fraction = new Fraction(1, 3);
        String expected = "1/3";
        StringBuffer stringBuffer = new StringBuffer();
        ProperFractionFormat.format(fraction, stringBuffer, new FieldPosition(0, 0, 0, 0));
        String actual = stringBuffer.toString();
        assertEquals(expected, actual);
    }

    // Test parse method with a proper fraction
    @org.junit.Test
    public void testParseProperFraction() {
        String input = "1 1/2";
        int expectedIndex = input.length();
        Fraction fraction = ProperFractionFormat.parse(input, new ParsePosition(0));
        int actualIndex = ParsePosition.getPosition();
        assertEquals(expectedIndex, actualIndex);
        assertEquals(new Fraction(1, 2), fraction);
    }

    // Test parse method with an improper fraction
    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testParseImproperFraction() {
        String input = "2 1";
        ProperFractionFormat.parse(input, new ParsePosition(0));
    }

    // Test parse method with an empty string
    @org.junit.Test(expected = NumberFormatException.class)
    public void testParseEmptyString() {
        ProperFractionFormat.parse("", new ParsePosition(0));
    }

    // Test parse method with an invalid input
    @org.junit.Test(expected = NumberFormatException.class)
    public void testParseInvalidInput() {
        String input = "abc";
        ProperFractionFormat.parse(input, new ParsePosition(0));
    }

    // Test parse method with a fraction where the numerator is negative
    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testParseNegativeNumerator() {
        String input = "1 -1/2";
        ProperFractionFormat.parse(input, new ParsePosition(0));
    }

    // Test parse method with a fraction where the denominator is negative
    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testParseNegativeDenominator() {
        String input = "1 1/-2";
        ProperFractionFormat.parse(input, new ParsePosition(0));
    }

    // Test setWholeFormat method with null argument
    @org.junit.Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullArgument() {
        ProperFractionFormat properFractionFormat = this.properFractionFormat;
        properFractionFormat.setWholeFormat(null);
    }

    private ProperFractionFormat properFractionFormat;
}