package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

/**
 * Test class for NumberInput methods
 *
 * @author John Doe
 */
public class NumberInputTest {

    @Test
    public void testParseInt_ValidCases() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(12345, result);
    }

    @Test
    public void testParseInt_ValidString() {
        String str = "12345";
        int result = NumberInput.parseInt(str);
        assertEquals(12345, result);
    }

    @Test
    public void testParseInt_ValidLongWithSign() {
        String str = "-123456789012345";
        int result = NumberInput.parseInt(str);
        assertEquals(-123456789012345, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInt_InvalidString() {
        String str = "abc";
        NumberInput.parseInt(str);
    }

    @Test
    public void testParseLong_ValidCases() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long result = NumberInput.parseLong(digitChars, 0, 9);
        assertEquals(1234567890L, result);
    }

    @Test
    public void testParseLong_ValidString() {
        String str = "1234567890";
        long result = NumberInput.parseLong(str);
        assertEquals(1234567890L, result);
    }

    @Test
    public void testParseLong_LongWithSign() {
        String str = "-9223372036854775807";
        long result = NumberInput.parseLong(str);
        assertEquals(-9223372036854775807L, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLong_InvalidString() {
        String str = "abc";
        NumberInput.parseLong(str);
    }

    @Test
    public void testInLongRange_ValidCases() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
        boolean result = NumberInput.inLongRange(digitChars, 0, 7, false);
        assertTrue(result);

        digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        result = NumberInput.inLongRange(digitChars, 0, 9, false);
        assertTrue(result);

        digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8'};
        result = NumberInput.inLongRange(digitChars, 0, 9, true);
        assertTrue(result);

        digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        result = NumberInput.inLongRange(digitChars, 0, 10, true);
        assertFalse(result);
    }

    @Test
    public void testInLongRange_ValidStrings() {
        String str = "123456789";
        boolean result = NumberInput.inLongRange(str, false);
        assertTrue(result);

        str = "-9876543210";
        result = NumberInput.inLongRange(str, true);
        assertTrue(result);

        str = "-98765432101";
        result = NumberInput.inLongRange(str, true);
        assertFalse(result);
    }

    @Test
    public void testParseAsInt_ValidCases() {
        String input = "12345";
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(12345, result);
    }

    @Test
    public void testParseAsInt_DefaultValue() {
        String input = null;
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(0, result);
    }

    @Test
    public void testParseAsInt_InvalidCases() {
        String input = "abc";
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(0, result);
    }

    @Test
    public void testParseAsLong_ValidCases() {
        String input = "123456789012345";
        long result = NumberInput.parseAsLong(input, 0);
        assertEquals(123456789012345L, result);
    }

    @Test
    public void testParseAsLong_DefaultValue() {
        String input = null;
        long result = NumberInput.parseAsLong(input, 0);
        assertEquals(0, result);
    }

    @Test
    public void testParseAsLong_InvalidCases() {
        String input = "abc";
        long result = NumberInput.parseAsLong(input, 0);
        assertEquals(0, result);
    }

    @Test
    public void testParseAsDouble_ValidCases() {
        String input = "123.45";
        double result = NumberInput.parseAsDouble(input, 0);
        assertEquals(123.45, result, 0.001);
    }

    @Test
    public void testParseAsDouble_DefaultValue() {
        String input = null;
        double result = NumberInput.parseAsDouble(input, 0);
        assertEquals(0, result, 0.001);
    }

    @Test
    public void testParseAsDouble_InvalidCases() {
        String input = "abc";
        double result = NumberInput.parseAsDouble(input, 0);
        assertEquals(0, result, 0.001);
    }

    @Test
    public void testParseBigDecimal_ValidCases() {
        String input = "123.45";
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    public void testParseBigDecimal_CharArray() {
        char[] input = new char[]{'1', '2', '3', '.', '4', '5'};
        BigDecimal result = NumberInput.parseBigDecimal(input, 0, input.length);
        assertEquals(new BigDecimal("123.45"), result);
    }
}