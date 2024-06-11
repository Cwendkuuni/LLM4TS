package org.apache.commons.math.fraction.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParsePosition;
import java.util.Locale;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.util.MathUtils;

public class ProperFractionFormatTest {

    @org.junit.Test
    public void testDefaultFormat() {
        FractionFormat format = new ProperFractionFormat();
        // Test default format with some simple cases
        Fraction input = new Fraction(1, 2);
        String expected = "1/2";
        String output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        input = new Fraction(-1, 2);
        expected = "-1/2";
        output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        input = new Fraction(3, 4);
        expected = "3/4";
        output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        input = new Fraction(-3, 4);
        expected = "-3/4";
        output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        input = new Fraction(5, 8);
        expected = "5/8";
        output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        input = new Fraction(-5, 8);
        expected = "-5/8";
        output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        // Test default format with some larger numbers
        input = new Fraction(BigInteger.valueOf(123456789), BigInteger.valueOf(987654321));
        expected = "123456789/987654321";
        output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        input = new Fraction(BigInteger.valueOf(-123456789), BigInteger.valueOf(987654321));
        expected = "-123456789/987654321";
        output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());
    }

    @org.junit.Test
    public void testCustomFormat() {
        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat numeratorFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        NumberFormat denominatorFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
        FractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        // Test custom format with some simple cases
        Fraction input = new Fraction(1, 2);
        String expected = "1 %/2 %";
        String output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        input = new Fraction(-1, 2);
        expected = "-1 %/2 %";
        output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        input = new Fraction(3, 4);
        expected = "3 %/4 %";
        output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        input = new Fraction(-3, 4);
        expected = "-3 %/4 %";
        output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        input = new Fraction(5, 8);
        expected = "5 %/8 %";
        output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        input = new Fraction(-5, 8);
        expected = "-5 %/8 %";
        output = format.format(input, new StringBuffer(), null);
        assertEquals(expected, output.toString());

        // Test custom format with some larger numbers
        input = new Fraction(new BigDecimal("-123456789.12345"), BigDecimal.ONE);
        String expectedFormat = "%-12.6f % %";
        String outputFormat = format.getFormatString();
        String expected = String.format(expectedFormat, input.getWholeNumber(), input.getFraction());
        String output = format.format(input, new StringBuffer(), null).toString();
        assertEquals(expected, output);

        input = new Fraction(new BigDecimal("123456789.12345"), BigDecimal.ONE);
        expectedFormat = "%+12.6f % %";
        outputFormat = format.getFormatString();
        expected = String.format(expectedFormat, input.getWholeNumber(), input.getFraction());
        output = format.format(input, new StringBuffer(), null).toString();
        assertEquals(expected, output);
    }

    @org.junit.Test
    public void testParse() {
        FractionFormat format = new ProperFractionFormat();

        // Test parse with some simple cases
        String inputString1 = "1/2";
        Fraction expected1 = new Fraction(1, 2);
        ParsePosition pos1 = new ParsePosition(0);
        Fraction output1 = format.parse(inputString1, pos1);
        assertEquals(expected1, output1);
        assertEquals(inputString1.length(), pos1.getIndex());

        String inputString2 = "-1/2";
        Fraction expected2 = new Fraction(-1, 2);
        ParsePosition pos2 = new ParsePosition(0);
        Fraction output2 = format.parse(inputString2, pos2);
        assertEquals(expected2, output2);
        assertEquals(inputString2.length(), pos2.getIndex());

        String inputString3 = "3/4";
        Fraction expected3 = new Fraction(3, 4);
        ParsePosition pos3 = new ParsePosition(0);
        Fraction output3 = format.parse(inputString3, pos3);
        assertEquals(expected3, output3);
        assertEquals(inputString3.length(), pos3.getIndex());

        String inputString4 = "-3/4";
        Fraction expected4 = new Fraction(-3, 4);
        ParsePosition pos4 = new ParsePosition(0);
        Fraction output4 = format.parse(inputString4, pos4);
        assertEquals(expected4, output4);
        assertEquals(inputString4.length(), pos4.getIndex());

        String inputString5 = "5/8";
        Fraction expected5 = new Fraction(5, 8);
        ParsePosition pos5 = new ParsePosition(0);
        Fraction output5 = format.parse(inputString5, pos5);
        assertEquals(expected5, output5);
        assertEquals(inputString5.length(), pos5.getIndex());

        String inputString6 = "-5/8";
        Fraction expected6 = new Fraction(-5, 8);
        ParsePosition pos6 = new ParsePosition(0);
        Fraction output6 = format.parse(inputString6, pos6);
        assertEquals(expected6, output6);
        assertEquals(inputString6.length(), pos6.getIndex());

        // Test parse with some larger numbers
        String inputString7 = "123456789/987654321";
        Fraction expected7 = new Fraction(BigInteger.valueOf(123456789), BigInteger.valueOf(987654321));
        ParsePosition pos7 = new ParsePosition(0);
        Fraction output7 = format.parse(inputString7, pos7);
        assertEquals(expected7, output7);
        assertEquals(inputString7.length(), pos7.getIndex());

        String inputString8 = "-123456789/987654321";
        Fraction expected8 = new Fraction(BigInteger.valueOf(-123456789), BigInteger.valueOf(987654321));
        ParsePosition pos8 = new ParsePosition(0);
        Fraction output8 = format.parse(inputString8, pos8);
        assertEquals(expected8, output8);
        assertEquals(inputString8.length(), pos8.getIndex());

        // Test parse with invalid cases
        String inputString9 = "-3 -1/2";
        ParsePosition pos9 = new ParsePosition(0);
        assertNull(format.parse(inputString9, pos9));
        assertEquals(0, pos9.getIndex());

        String inputString10 = "1/0";
        ParsePosition pos10 = new ParsePosition(0);
        assertNull(format.parse(inputString10, pos10));
        assertEquals(0, pos10.getIndex());

        String inputString11 = "1/3/2";
        ParsePosition pos11 = new ParsePosition(0);
        assertNull(format.parse(inputString11, pos11));
        assertEquals(0, pos11.getIndex());

        String inputString12 = "1 2/3";
        ParsePosition pos12 = new ParsePosition(0);
        assertNull(format.parse(inputString12, pos12));
        assertEquals(0, pos12.getIndex());
    }

    // Additional test methods for getWholeFormat(), getNumeratorFormat(), and getDenominatorFormat() can be added here.

    @org.junit.Test
    public void testSetWholeFormat() {
        FractionFormat format = new ProperFractionFormat();

        NumberFormat wholeFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        format.setWholeFormat(wholeFormat);

        Fraction input = new Fraction(1, 2);
        String expected = "1";
        String output = format.format(input, new StringBuffer(), null).toString();
        assertEquals(expected, output);
    }
}