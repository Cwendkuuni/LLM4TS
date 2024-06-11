package org.apache.commons.math.fraction.test;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ProperFractionFormatTest {

    private static final NumberFormat DEFAULT_WHOLE_FORMAT = NumberFormat.getNumberInstance(Locale.US);
    private static final NumberFormat DEFAULT_NUMERATOR_FORMAT = NumberFormat.getNumberInstance(Locale.US);
    private static final NumberFormat DEFAULT_DENOMINATOR_FORMAT = NumberFormat.getNumberInstance(Locale.US);

    @Test
    public void testDefaultFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 1/2";
        Fraction expected = new Fraction(7, 2);
        verifyFormat(format, input, expected);
    }

    @Test
    public void testCustomFormat() {
        ProperFractionFormat format = new ProperFractionFormat(
            NumberFormat.getNumberInstance(Locale.US),
            NumberFormat.getNumberInstance(Locale.US),
            NumberFormat.getNumberInstance(Locale.US)
        );
        String input = "3 1/2";
        Fraction expected = new Fraction(7, 2);
        verifyFormat(format, input, expected);

        format = new ProperFractionFormat(
            NumberFormat.getNumberInstance(Locale.US),
            NumberFormat.getPercentInstance(Locale.US),
            NumberFormat.getNumberInstance(Locale.US)
        );
        String input2 = "3 % 1/2";
        Fraction expected2 = new Fraction(7, 2);
        verifyFormat(format, input2, expected2);
    }

    @Test
    public void testParseFormat() {
        ProperFractionFormat format = new ProperFractionFormat(
            DEFAULT_WHOLE_FORMAT,
            DEFAULT_NUMERATOR_FORMAT,
            DEFAULT_DENOMINATOR_FORMAT
        );
        String input = "3 1/2";
        Fraction expected = new Fraction(7, 2);
        Fraction parsed = format.parse(input, new ParsePosition(0));
        assertEquals(expected, parsed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat(
            DEFAULT_WHOLE_FORMAT,
            DEFAULT_NUMERATOR_FORMAT,
            DEFAULT_DENOMINATOR_FORMAT
        );
        String input = "-3 -1/2";
        ParsePosition pos = new ParsePosition(0);
        format.parse(input, pos);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParseMixedNumber() {
        ProperFractionFormat format = new ProperFractionFormat(
            DEFAULT_WHOLE_FORMAT,
            DEFAULT_NUMERATOR_FORMAT,
            DEFAULT_DENOMINATOR_FORMAT
        );
        String input = "3 1/2 + 2 1/3";
        Fraction expected = new Fraction(13, 6);
        Fraction parsed = format.parse(input, new ParsePosition(0));
        assertEquals(expected, parsed);
    }

    @Test
    public void testFormatBigFraction() {
        ProperFractionFormat format = new ProperFractionFormat(
            DEFAULT_WHOLE_FORMAT,
            DEFAULT_NUMERATOR_FORMAT,
            DEFAULT_DENOMINATOR_FORMAT
        );
        Fraction input = new Fraction(new BigInteger("12345678901234567890"), new BigInteger("98765432109876543210"));
        String expected = "123.456.789.012.345.678.901.234.567.890.123";
        StringBuffer toAppendTo = new StringBuffer();
        format.format(input, toAppendTo, new FieldPosition(0, 0, 0, 0));
        assertEquals(expected, toAppendTo.toString());
    }

    @Test
    public void testFormatBigFractionWithCustomFormat() {
        ProperFractionFormat format = new ProperFractionFormat(
            NumberFormat.getNumberInstance(Locale.US),
            NumberFormat.getNumberInstance(Locale.US),
            NumberFormat.getPercentInstance(Locale.US)
        );
        Fraction input = new Fraction(new BigInteger("12345678901234567890"), new BigInteger("98765432109876543210"));
        String expected = "123.456.789.012.345.678.901.234.567.890.123 %";
        StringBuffer toAppendTo = new StringBuffer();
        format.format(input, toAppendTo, new FieldPosition(0, 0, 0, 0));
        assertEquals(expected, toAppendTo.toString());
    }

    private void verifyFormat(ProperFractionFormat format, String input, Fraction expected) {
        StringBuffer toAppendTo = new StringBuffer();
        Fraction actual = format.parse(input, new ParsePosition(0));
        format.format(actual, toAppendTo, new FieldPosition(0, 0, 0, 0));
        assertEquals(expected.toString(), toAppendTo.toString());
        assertEquals(expected.getNumerator(), actual.getNumerator());
        assertEquals(expected.getDenominator(), actual.getDenominator());
    }

    private int parseInt(String input, ParsePosition pos) {
        Number num = NumberFormat.getNumberInstance(Locale.US).parse(input, pos);
        if (num == null) {
            fail("Parse failed: " + input);
        }
        return num.intValue();
    }

    private void parseAndIgnoreWhitespace(String source, ParsePosition pos) {
        int index = pos.getIndex();
        while (index < source.length() && Character.isWhitespace(source.charAt(index))) {
            index++;
            pos.setIndex(index);
        }
    }

    private char parseNextCharacter(String source, ParsePosition pos) {
        int index = pos.getIndex();
        if (index >= source.length()) {
            return 0;
        }
        char next = source.charAt(index);
        pos.setIndex(index + 1);
        return next;
    }
}