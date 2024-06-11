package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.NumberFormatUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testConstructor() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        assertNotNull(properFractionFormat);

        ProperFractionFormat properFractionFormatWithCustomFormats = new ProperFractionFormat(NumberFormatUtils.createDecimalFormat("#,##0.00"));
        assertNotNull(properFractionFormatWithCustomFormats);
    }

    @Test
    public void testFormatMethod() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();

        Fraction fraction = new Fraction(2, 3);
        StringBuffer toAppendTo = new StringBuffer();

        properFractionFormat.format(fraction, toAppendTo, null);
        String expected = "2/3";
        assertEquals(expected, toAppendTo.toString());

        fraction = new Fraction(-2, 3);
        toAppendTo = new StringBuffer();
        properFractionFormat.format(fraction, toAppendTo, null);
        expected = "-2/3";
        assertEquals(expected, toAppendTo.toString());

        fraction = new Fraction(4, 5);
        toAppendTo = new StringBuffer();
        properFractionFormat.format(fraction, toAppendTo, null);
        expected = "4/5";
        assertEquals(expected, toAppendTo.toString());

        fraction = new Fraction(1, 1);
        toAppendTo = new StringBuffer();
        properFractionFormat.format(fraction, toAppendTo, null);
        expected = "1";
        assertEquals(expected, toAppendTo.toString());

        fraction = new Fraction(1, 2);
        toAppendTo = new StringBuffer();
        properFractionFormat.format(fraction, toAppendTo, null);
        expected = "1/2";
        assertEquals(expected, toAppendTo.toString());
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
        assertNotNull(wholeFormat);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNullArgumentException() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        properFractionFormat.setWholeFormat(null);
    }

    @Test
    public void testParseMethod() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();

        String source = "2/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(2, 3), fraction);
        assertEquals(7, pos.getIndex());

        source = "-2/3";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(-2, 3), fraction);
        assertEquals(7, pos.getIndex());

        source = "4/5";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(4, 5), fraction);
        assertEquals(8, pos.getIndex());

        source = "1";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(1, 1), fraction);
        assertEquals(1, pos.getIndex());

        source = "1/2";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(1, 2), fraction);
        assertEquals(3, pos.getIndex());

        source = "2";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(2, 1), fraction);
        assertEquals(1, pos.getIndex());

        source = "2/";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(1, pos.getIndex());

        source = "2/-3";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(1, pos.getIndex());

        source = "2/-3";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(1, pos.getIndex());

        source = "2/3/4";
        pos = new ParsePosition(0);
        fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
        assertEquals(3, pos.getIndex());
    }

    @Test
    public void testParseWithBigDecimal() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();

        String source = "2/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(2, 3), fraction);
        assertEquals(7, pos.getIndex());

        BigDecimal bigDecimal = new BigDecimal("2/3");
        Fraction parsedFraction = properFractionFormat.parse(bigDecimal.toPlainString(), pos);
        assertEquals(new Fraction(2, 3), parsedFraction);
        assertEquals(7, pos.getIndex());
    }

    @Test
    public void testParseWithBigInteger() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();

        String source = "2/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(2, 3), fraction);
        assertEquals(7, pos.getIndex());

        BigInteger numerator = BigInteger.valueOf(2);
        BigInteger denominator = BigInteger.valueOf(3);
        String sourceWithBigNumbers = numerator.toString() + "/" + denominator.toString();
        Fraction parsedFraction = properFractionFormat.parse(sourceWithBigNumbers, pos);
        assertEquals(new Fraction(2, 3), parsedFraction);
        assertEquals(10, pos.getIndex());
    }

    @Test
    public void testParseWithNegativeNumbers() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();

        String source = "-2/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(-2, 3), fraction);
        assertEquals(7, pos.getIndex());

        String sourceWithNegativeSign = "-2/3";
        Fraction parsedFraction = properFractionFormat.parse(sourceWithNegativeSign, pos);
        assertEquals(new Fraction(-2, 3), parsedFraction);
        assertEquals(7, pos.getIndex());

        sourceWithNegativeSign = "2/-3";
        parsedFraction = properFractionFormat.parse(sourceWithNegativeSign, pos);
        assertEquals(new Fraction(-2, 3), parsedFraction);
        assertEquals(7, pos.getIndex());

        sourceWithNegativeSign = "-2/--3";
        parsedFraction = properFractionFormat.parse(sourceWithNegativeSign, pos);
        assertEquals(new Fraction(-2, 3), parsedFraction);
        assertEquals(8, pos.getIndex());

        sourceWithNegativeSign = "-2/--3";
        parsedFraction = properFractionFormat.parse(sourceWithNegativeSign, pos);
        assertEquals(new Fraction(-2, 3), parsedFraction);
        assertEquals(8, pos.getIndex());
    }

    @Test
    public void testParseWithFractionAndWholeNumber() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();

        String source = "2 1/3";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(2, 1, 3), fraction);
        assertEquals(6, pos.getIndex());

        source = "2 1/--3";
        parsedFraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(2, 1, 3), parsedFraction);
        assertEquals(6, pos.getIndex());

        source = "2 1/3 1";
        parsedFraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(2, 1, 3, 1), parsedFraction);
        assertEquals(8, pos.getIndex());

        source = "2 1/3 1.";
        parsedFraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(2, 1, 3, 1, 1), parsedFraction);
        assertEquals(10, pos.getIndex());

        source = "2 1/--3 1.";
        parsedFraction = properFractionFormat.parse(source, pos);
        assertEquals(new Fraction(2, 1, 3, 1, 1), parsedFraction);
        assertEquals(10, pos.getIndex());
    }

    @Test
    public void testParseWithMixedNumbers() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();

        String source = "2 1/3 + 1 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        Fraction expected = new Fraction(5, 2, 3, 1);
        assertEquals(expected, fraction);
        assertEquals(14, pos.getIndex());

        source = "2 1/3 + 1 1/2.";
        Fraction parsedFraction = properFractionFormat.parse(source, pos);
        expected = new Fraction(5, 2, 3, 1, 1);
        assertEquals(expected, parsedFraction);
        assertEquals(15, pos.getIndex());

        source = "2 1/--3 + 1 1/2.";
        parsedFraction = properFractionFormat.parse(source, pos);
        expected = new Fraction(5, 2, 3, 1, 1);
        assertEquals(expected, parsedFraction);
        assertEquals(15, pos.getIndex());

        source = "2 1/--3 + 1 1/2.";
        parsedFraction = properFractionFormat.parse(source, pos);
        expected = new Fraction(5, 2, 3, 1, 1);
        assertEquals(expected, parsedFraction);
        assertEquals(15, pos.getIndex());

        source = "2 1/--3 + 1 1/--2.";
        parsedFraction = properFractionFormat.parse(source, pos);
        expected = new Fraction(5, 2, 3, 1, 1, 1);
        assertEquals(expected, parsedFraction);
        assertEquals(17, pos.getIndex());
    }

    @Test
    public void testParseWithMixedNumbersAndParentheses() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();

        String source = "2 + (1 1/2) * (1 1/3)";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        Fraction expected = new Fraction(7, 6, 2);
        assertEquals(expected, fraction);
        assertEquals(16, pos.getIndex());

        source = "2 + (1 1/2) * (1 1/3.)";
        Fraction parsedFraction = properFractionFormat.parse(source, pos);
        expected = new Fraction(7, 6, 2, 1);
        assertEquals(expected, parsedFraction);
        assertEquals(17, pos.getIndex());

        source = "2 + (1 1/--2) * (1 1/--3.)";
        parsedFraction = properFractionFormat.parse(source, pos);
        expected = new Fraction(7, 6, 2, 1, 1, 1);
        assertEquals(expected, parsedFraction);
        assertEquals(19, pos.getIndex());
    }

    @Test
    public void testParseWithFractionAndExponent() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();

        String source = "2/3^2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        Fraction expected = new Fraction(2, 3, 2);
        assertEquals(expected, fraction);
        assertEquals(5, pos.getIndex());
    }

    @Test
    public void testParseWithFractionAndExponentAndParentheses() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();

        String source = "2/(1/3)^2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        Fraction expected = new Fraction(3, 2, 2);
        assertEquals(expected, fraction);
        assertEquals(6, pos.getIndex());
    }
}