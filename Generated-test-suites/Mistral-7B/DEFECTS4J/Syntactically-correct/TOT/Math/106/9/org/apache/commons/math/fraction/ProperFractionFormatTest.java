package org.apache.commons.math.fraction.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;

public class ProperFractionFormatTest {

    // Test constructor with default number format
    @org.junit.Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        String formatted = format.format(fraction, new StringBuffer(), null);
        Fraction parsed = format.parse(formatted, new ParsePosition(0));
        assertEquals(fraction, parsed);
    }

    // Test constructor with custom number format for whole, numerator, and denominator
    @org.junit.Test
    public void testCustomConstructor() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance();
        wholeFormat.setGroupingUsed(false);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance();
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(BigInteger.valueOf(123), BigInteger.valueOf(45));
        String formatted = format.format(fraction, new StringBuffer(), null);
        Fraction parsed = format.parse(formatted, new ParsePosition(0));
        assertEquals(fraction, parsed);
    }

    // Test format method with positive whole number
    @org.junit.Test
    public void testFormatPositiveWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(7, 2);
        String formatted = format.format(fraction, new StringBuffer(), null).toString();
        assertEquals("7", formatted.substring(0, formatted.indexOf(' ')));
    }

    // Test format method with negative whole number
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormatNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-7, 2);
        format.format(fraction, new StringBuffer(), null);
    }

    // Test format method with zero whole number
    @org.junit.Test
    public void testFormatZeroWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 1);
        String formatted = format.format(fraction, new StringBuffer(), null).toString();
        assertEquals("0", formatted);
    }

    // Test format method with positive numerator
    @org.junit.Test
    public void testFormatPositiveNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        String formatted = format.format(fraction, new StringBuffer(), null).toString();
        assertEquals("1/2", formatted);
    }

    // Test format method with negative numerator
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormatNegativeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-1, 2);
        format.format(fraction, new StringBuffer(), null);
    }

    // Test format method with zero numerator
    @org.junit.Test
    public void testFormatZeroNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 1);
        String formatted = format.format(fraction, new StringBuffer(), null).toString();
        assertEquals("0/1", formatted);
    }

    // Test format method with positive denominator
    @org.junit.Test
    public void testFormatPositiveDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        String formatted = format.format(fraction, new StringBuffer(), null).toString();
        assertEquals("1/2", formatted);
    }

    // Test format method with negative denominator
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormatNegativeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, -2);
        format.format(fraction, new StringBuffer(), null);
    }

    // Test format method with zero denominator
    @org.junit.Test(expected = ArithmeticException.class)
    public void testFormatZeroDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 0);
        format.format(fraction, new StringBuffer(), null);
    }

    // Test parse method with positive whole number
    @org.junit.Test
    public void testParsePositiveWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("7", new ParsePosition(0));
        assertEquals(new Fraction(7, 1), fraction);
    }

    // Test parse method with negative whole number
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("-7", new ParsePosition(0));
    }

    // Test parse method with zero whole number
    @org.junit.Test
    public void testParseZeroWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("0", new ParsePosition(0));
        assertEquals(new Fraction(0, 1), fraction);
    }

    // Test parse method with positive numerator
    @org.junit.Test
    public void testParsePositiveNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("1/2", new ParsePosition(0));
        assertEquals(new Fraction(1, 2), fraction);
    }

    // Test parse method with negative numerator
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseNegativeNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("-1/2", new ParsePosition(0));
    }

    // Test parse method with zero numerator
    @org.junit.Test
    public void testParseZeroNumerator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("0/1", new ParsePosition(0));
        assertEquals(new Fraction(0, 1), fraction);
    }

    // Test parse method with positive denominator
    @org.junit.Test
    public void testParsePositiveDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("1/2", new ParsePosition(0));
        assertEquals(new Fraction(1, 2), fraction);
    }

    // Test parse method with negative denominator
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseNegativeDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("1/-2", new ParsePosition(0));
    }

    // Test parse method with zero denominator
    @org.junit.Test(expected = ArithmeticException.class)
    public void testParseZeroDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("1/0", new ParsePosition(0));
    }
}