package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.commons.math3.fraction.Fraction;
import org.apache.commons.commons.math3.fraction.FractionFormat;
import org.apache.commons.commons.math3.fraction.impl.DefaultFraction;
import org.apache.commons.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math3.exception.MathParseException;
import org.apache.commons.commons.math3.util.MathUtils;
import org.apache.commons.commons.math3.util.Precision;
import org.apache.commons.commons.math3.util.MathUtils.NonNegativeNumberFormat;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals(FractionFormat.getDefaultNumberFormat(), format.getWholeFormat());
    }

    @Test
    public void testWholeFormatConstructor() {
        Locale locale = Locale.ENGLISH;
        NumberFormat wholeFormat = new DecimalFormat("#,##0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test
    public void testThreeArgumentConstructor() {
        NumberFormat wholeFormat = new DecimalFormat("#,##0");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
        assertEquals(numeratorFormat, format.getNumeratorFormat());
        assertEquals(denominatorFormat, format.getDenominatorFormat());
    }

    @Test
    public void testFormat() {
        Locale locale = Locale.ENGLISH;
        NumberFormat wholeFormat = new DecimalFormat("#,##0");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0.#");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(new BigInteger("12345"), new BigInteger("67890"));
        StringBuffer toAppendTo = new StringBuffer(100);
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);

        assertEquals("1,234,500 / 678,900", toAppendTo.toString());
    }

    @Test
    public void testFormatWithWhole() {
        Locale locale = Locale.ENGLISH;
        NumberFormat wholeFormat = new DecimalFormat("#,##0");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0.#");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(new BigInteger("1234567890"), new BigInteger("9876543210"));
        StringBuffer toAppendTo = new StringBuffer(100);
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);

        assertEquals("1,234,567,890 / 987,654,321", toAppendTo.toString());
    }

    @Test
    public void testParse() {
        Locale locale = Locale.ENGLISH;
        NumberFormat wholeFormat = new DecimalFormat("#,##0");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0.#");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "1,234,500 / 678,900";
        ParsePosition pos = new ParsePosition(0);

        Fraction parsedFraction = format.parse(source, pos);
        assertEquals(new Fraction(new BigInteger("1234500"), new BigInteger("678900")), parsedFraction);
        assertEquals(source.length(), pos.getIndex());

        source = "123 / 456";
        parsedFraction = format.parse(source, pos);
        assertEquals(new Fraction(new BigInteger("123"), new BigInteger("456")), parsedFraction);
        assertEquals(source.length(), pos.getIndex());

        source = "1,234,567,890 / 987,654,321";
        parsedFraction = format.parse(source, pos);
        assertEquals(new Fraction(new BigInteger("1234567890"), new BigInteger("9876543210")), parsedFraction);
        assertEquals(source.length(), pos.getIndex());
    }

    @Test
    public void testParseWithWhole() {
        Locale locale = Locale.ENGLISH;
        NumberFormat wholeFormat = new DecimalFormat("#,##0");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0.#");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "1,234,567,890 and 1 / 987,654,321";
        ParsePosition pos = new ParsePosition(0);

        Fraction parsedFraction = format.parse(source, pos);
        assertEquals(new Fraction(new BigInteger("1"), new BigInteger("9876543210")), parsedFraction);
        assertEquals(source.length(), pos.getIndex());
    }

    @Test(expected = MathParseException.class)
    public void testParseInvalidFormat() {
        Locale locale = Locale.ENGLISH;
        NumberFormat wholeFormat = new DecimalFormat("#,##0");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0.#");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "1,234,500 / 678,900 and 1 / 987,654";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test(expected = MathParseException.class)
    public void testParseInvalidNumerator() {
        Locale locale = Locale.ENGLISH;
        NumberFormat wholeFormat = new DecimalFormat("#,##0");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0.#");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "1,234,500 / -678,900";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test(expected = MathParseException.class)
    public void testParseInvalidDenominator() {
        Locale locale = Locale.ENGLISH;
        NumberFormat wholeFormat = new DecimalFormat("#,##0");
        NumberFormat numeratorFormat = new DecimalFormat("#,##0.#");
        NumberFormat denominatorFormat = new DecimalFormat("#,##0");
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "1,234,500 / 678,900 and -1 / 987,654,321";
        ParsePosition pos = new ParsePosition(0);

        format.parse(source, pos);
    }

    @Test
    public void testSetWholeFormat() {
        Locale locale = Locale.ENGLISH;
        NumberFormat wholeFormat = new DecimalFormat("#,##0");
        ProperFractionFormat format = new ProperFractionFormat();

        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNullFormat() {
        Locale locale = Locale.ENGLISH;
        ProperFractionFormat format = new ProperFractionFormat();

        format.setWholeFormat(null);
    }

    @Test
    public void testFormatWithNonNegativeNumberFormat() {
        NonNegativeNumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(new BigInteger("-1234567890"), new BigInteger("67890"));
        StringBuffer toAppendTo = new StringBuffer(100);
        FieldPosition pos = new FieldPosition(0, 0);

        format.format(fraction, toAppendTo, pos);

        assertTrue(toAppendTo.toString().startsWith("(-"));
    }

    @Test
    public void testParseWithNonNegativeNumberFormat() {
        Locale locale = Locale.ENGLISH;
        NonNegativeNumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "-1,234,500 / 678,900";
        ParsePosition pos = new ParsePosition(0);

        Fraction parsedFraction = format.parse(source, pos);
        assertEquals(new Fraction(new BigInteger("-1234500"), new BigInteger("678900")), parsedFraction);
        assertEquals(source.length(), pos.getIndex());
    }
}