package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int expected = 12345;
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntWithMinusSign() {
        char[] digitChars = {'-', '1', '2', '3', '4', '5'};
        int expected = -12345;
        int result = NumberInput.parseInt(digitChars, 0, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testParseIntWithInvalidInput() {
        char[] digitChars = {'1', '2', 'x', '3', '4'};
        int expected = 0;
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithNegativeLength() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        int result = NumberInput.parseInt(digitChars, 6, 3);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 1234567890L;
        long result = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithValidInputGivenString() {
        String str = "1234567890";
        long expected = 1234567890L;
        long result = NumberInput.parseLong(str);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInput() {
        char[] digitChars = {'1', '2', '3', 'x', '4'};
        NumberInput.parseLong(digitChars, 0, 5);
    }

    @Test
    public void testParseLongWithLongMinusOne() {
        char[] digitChars = {'9', '2', '2', '3', '4', '5', '6', '7', '8', '9', '9'};
        long expected = Long.MAX_VALUE - 1;
        long result = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLongWithMaxLongValue() {
        char[] digitChars = {'9', '2', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9'};
        long result = NumberInput.parseLong(digitChars, 0, 12);
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithNegativeLength() {
        char[] digitChars = {'1', '2', '3', '4', '5'};
        NumberInput.parseLong(digitChars, 6, 3);
    }

    @Test
    public void testParseLongGivenStringWithInRange() {
        String str = "12345678901234567890";
        long result = NumberInput.parseLong(str);
        assertTrue(NumberInput.inLongRange(str.toCharArray(), false));
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test
    public void testParseLongGivenStringOutOfRange() {
        String str = "9223372036854775809";
        long result = NumberInput.parseLong(str);
        assertFalse(NumberInput.inLongRange(str.toCharArray(), false));
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test
    public void testParseAsIntWithValidInput() {
        String input = "12345";
        int expected = 12345;
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsIntWithInvalidInput() {
        String input = "1234x5";
        int expected = 0;
        int result = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsLongWithValidInput() {
        String input = "12345678901234567890";
        long expected = 12345678901234567890L;
        long result = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsLongWithInvalidInput() {
        String input = "12345678901234567890x";
        long expected = 0;
        long result = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsDoubleWithValidInput() {
        String input = "123.45";
        double expected = 123.45;
        double result = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testParseAsDoubleWithInvalidInput() {
        String input = "123.45x";
        double expected = 0;
        double result = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testParseDoubleWithValidInput() {
        String numStr = "123.45";
        double expected = 123.45;
        double result = NumberInput.parseDouble(numStr);
        assertEquals(expected, result, 0.0001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDoubleWithInvalidInput() {
        String numStr = "123.45x";
        NumberInput.parseDouble(numStr);
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String numStr = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInput() {
        String numStr = "123.45x";
        NumberInput.parseBigDecimal(numStr);
    }

    @Test
    public void testParseBigDecimalWithValidInputChars() {
        char[] buffer = {'1', '2', '3', '.', '4', '5'};
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(buffer, 0, 6);
        assertEquals(expected, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInputChars() {
        char[] buffer = {'1', '2', '3', '.', '4', 'x'};
        NumberInput.parseBigDecimal(buffer, 0, 6);
    }
}