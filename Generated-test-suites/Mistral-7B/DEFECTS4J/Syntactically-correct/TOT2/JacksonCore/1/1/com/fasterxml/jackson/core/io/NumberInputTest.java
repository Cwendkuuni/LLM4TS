package com.fasterxml.jackson.core.io; // package declaration for testing purpose

import static org.junit.Assert.*;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5'}; // valid input for parseInt method
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithNegativeSign() {
        char[] digitChars = {'-', '1', '2', '3', '4', '5'}; // negative input for parseInt method
        int expected = -12345;
        int actual = NumberInput.parseInt(digitChars, 0, 6);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseIntWithEdgeCase() {
        char[] digitChars = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'}; // max value for int
        int expected = Integer.MAX_VALUE;
        int actual = NumberInput.parseInt(digitChars, 0, 10);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInput() {
        char[] digitChars = {'A', '2', '3', '4', '5'}; // invalid input for parseInt method
        NumberInput.parseInt(digitChars, 0, 5);
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'}; // valid input for parseLong method
        long expected = 123456789L;
        long actual = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithNegativeSign() {
        char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'}; // negative input for parseLong method
        long expected = -123456789L;
        long actual = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithEdgeCase() {
        char[] digitChars = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'}; // max value for long
        long expected = Long.MAX_VALUE;
        long actual = NumberInput.parseLong(digitChars, 0, 13);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInput() {
        char[] digitChars = {'A', '2', '3', '4', '5'}; // invalid input for parseLong method
        NumberInput.parseLong(digitChars, 0, 5);
    }

    @Test
    public void testParseLongWithSimpleLong() {
        String longStr = "123456789";
        long expected = 123456789L;
        long actual = NumberInput.parseLong(longStr);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLongWithFakeLong() {
        String longStr = "1234567890123456789"; // known to be an int
        long expected = (long) 123456789;
        long actual = NumberInput.parseLong(longStr);
        assertEquals(expected, actual);
    }

    @Test
    public void testInLongRangeWithValidInput() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8'}; // valid input for inLongRange method
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 7, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testInLongRangeWithNegativeInput() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8'}; // valid input for inLongRange method with negative flag
        boolean expected = false;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 7, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testInLongRangeWithEdgeCase() {
        char[] digitChars = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'}; // max value for long
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 10, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithDefaultValue() {
        String input = "123";
        int expected = 123;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithEmptyInput() {
        String input = "";
        int expected = 0;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsIntWithInvalidInput() {
        String input = "abcd";
        int expected = 0;
        int actual = NumberInput.parseAsInt(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithDefaultValue() {
        String input = "1234567890";
        long expected = 1234567890L;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithEmptyInput() {
        String input = "";
        long expected = 0;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsLongWithInvalidInput() {
        String input = "abcd";
        long expected = 0;
        long actual = NumberInput.parseAsLong(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsDoubleWithDefaultValue() {
        String input = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testParseAsDoubleWithEmptyInput() {
        String input = "";
        double expected = 0;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testParseAsDoubleWithInvalidInput() {
        String input = "abcd";
        double expected = 0;
        double actual = NumberInput.parseAsDouble(input, 0);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testParseDoubleWithValidInput() {
        String numStr = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseDouble(numStr);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDoubleWithInvalidInput() {
        String numStr = "abcd";
        NumberInput.parseDouble(numStr);
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String numStr = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInput() {
        String numStr = "abcd";
        NumberInput.parseBigDecimal(numStr);
    }

    @Test
    public void testParseBigDecimalWithCharArray() {
        char[] buffer = {'1', '2', '3', '.', '4', '5'};
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(buffer, 0, 6);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidCharArray() {
        char[] buffer = {'A', 'B', 'C'};
        NumberInput.parseBigDecimal(buffer, 0, 2);
    }
}