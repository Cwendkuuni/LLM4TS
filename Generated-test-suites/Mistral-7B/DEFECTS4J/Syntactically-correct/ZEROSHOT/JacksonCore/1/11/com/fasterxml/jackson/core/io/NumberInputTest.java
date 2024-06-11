package com.fasterxml.jackson.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_ValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(12345, result);
    }

    @Test
    public void testParseInt_ValidInput_String() {
        String str = "12345";
        int result = NumberInput.parseInt(str);
        assertEquals(12345, result);
    }

    @Test
    public void testParseInt_NegativeSign_String() {
        String str = "-12345";
        int result = NumberInput.parseInt(str);
        assertEquals(-12345, result);
    }

    @Test
    public void testParseInt_InvalidInput_String() {
        String str = "abc";
        assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(str));
    }

    @Test
    public void testParseLong_ValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long result = NumberInput.parseLong(digitChars, 0, 9);
        assertEquals(123456789L, result);
    }

    @Test
    public void testParseLong_ValidInput_String() {
        String str = "123456789";
        long result = NumberInput.parseLong(str);
        assertEquals(123456789L, result);
    }

    @Test
    public void testParseLong_ValidInput_LongRange() {
        boolean isInRange = NumberInput.inLongRange(new String("1234567890123456789"), false);
        assertTrue(isInRange);

        isInRange = NumberInput.inLongRange(new String("-1234567890123456789"), true);
        assertTrue(isInRange);
    }

    @Test
    public void testParseLong_InvalidInput_String() {
        String str = "abc";
        assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(str));
    }

    @Test
    public void testParseLong_LongRange() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        boolean isInRange = NumberInput.inLongRange(digitChars, 0, 18, false);
        assertTrue(isInRange);

        isInRange = NumberInput.inLongRange(digitChars, 0, 17, false);
        assertFalse(isInRange);

        isInRange = NumberInput.inLongRange(digitChars, 0, 16, false);
        assertFalse(isInRange);
    }

    @Test
    public void testParseAsInt_ValidInput() {
        String input = "12345";
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(12345, result);
    }

    @Test
    public void testParseAsInt_DefaultValue() {
        String input = "";
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(0, result);
    }

    @Test
    public void testParseAsInt_InvalidInput() {
        String input = "abc";
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(0, result);
    }

    @Test
    public void testParseAsLong_ValidInput() {
        String input = "1234567890123456789";
        long result = NumberInput.parseAsLong(input, 0L);
        assertEquals(1234567890123456789L, result);
    }

    @Test
    public void testParseAsLong_DefaultValue() {
        String input = "";
        long result = NumberInput.parseAsLong(input, 0L);
        assertEquals(0L, result);
    }

    @Test
    public void testParseAsLong_InvalidInput() {
        String input = "abc";
        long result = NumberInput.parseAsLong(input, 0L);
        assertEquals(0L, result);
    }

    @Test
    public void testParseAsDouble() {
        String input = "123.45";
        double result = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(123.45, result, 0.001);
    }

    @Test
    public void testParseAsDouble_DefaultValue() {
        String input = "";
        double result = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testParseAsDouble_InvalidInput() {
        String input = "abc";
        double result = NumberInput.parseAsDouble(input, 0.0);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testParseBigDecimal_ValidInput() {
        String input = "123.45";
        BigDecimal result = NumberInput.parseBigDecimal(input);
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    public void testParseBigDecimal_InvalidInput() {
        String input = "abc";
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(input));
    }

    @Test
    public void testParseBigDecimal_CharArray() {
        char[] buffer = new char[]{'1', '2', '3', '.', '4', '5'};
        BigDecimal result = NumberInput.parseBigDecimal(buffer, 0, 6);
        assertEquals(new BigDecimal("123.45"), result);
    }
}