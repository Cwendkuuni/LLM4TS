package com.fasterxml.jackson.core.io;

import org.junit.Assert;
import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseIntWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        Assert.assertEquals("Invalid result from parseInt method", expected, actual);
    }

    @Test
    public void testParseIntWithEdgeCases() {
        char[] digitChars1 = new char[]{'0', '9'};
        int actual1 = NumberInput.parseInt(digitChars1, 0, 2);
        Assert.assertEquals("Invalid result from parseInt method for edge case 1", 9, actual1);

        char[] digitChars2 = new char[]{'1', '0'};
        int actual2 = NumberInput.parseInt(digitChars2, 0, 2);
        Assert.assertEquals("Invalid result from parseInt method for edge case 2", 1, actual2);

        char[] digitChars3 = new char[]{'3', '2', '1', '4', '6', '7', '8', '9'};
        int actual3 = NumberInput.parseInt(digitChars3, 0, 8);
        Assert.assertEquals("Invalid result from parseInt method for edge case 3", 32146789, actual3);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntWithInvalidInput() {
        char[] digitChars = new char[]{'a', 'b', 'c'};
        NumberInput.parseInt(digitChars, 0, 3); // Exception should be thrown here
    }

    @Test
    public void testParseLongWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 1234567890L;
        long actual = NumberInput.parseLong(digitChars, 0, 10);
        Assert.assertEquals("Invalid result from parseLong method", expected, actual);
    }

    @Test
    public void testParseLongWithEdgeCases() {
        char[] digitChars1 = new char[]{'0', '9'};
        long actual1 = NumberInput.parseLong(digitChars1, 0, 2);
        Assert.assertEquals("Invalid result from parseLong method for edge case 1", 9L, actual1);

        char[] digitChars2 = new char[]{'1', '0'};
        long actual2 = NumberInput.parseLong(digitChars2, 0, 2);
        Assert.assertEquals("Invalid result from parseLong method for edge case 2", 1L, actual2);

        char[] digitChars3 = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        long actual3 = NumberInput.parseLong(digitChars3, 0, 10);
        Assert.assertEquals("Invalid result from parseLong method for edge case 3", 99999999999L, actual3);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongWithInvalidInput() {
        char[] digitChars = new char[]{'a', 'b', 'c'};
        NumberInput.parseLong(digitChars, 0, 3); // Exception should be thrown here
    }

    @Test
    public void testInLongRangeWithValidInput() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(digitChars, 0, 5, false);
        Assert.assertEquals("Invalid result from inLongRange method", expected, actual);
    }

    @Test
    public void testInLongRangeWithEdgeCases() {
        char[] digitChars1 = new char[]{'0', '9'};
        boolean actual1 = NumberInput.inLongRange(digitChars1, 0, 2, false);
        Assert.assertEquals("Invalid result from inLongRange method for edge case 1", true, actual1);

        char[] digitChars2 = new char[]{'1', '0'};
        boolean actual2 = NumberInput.inLongRange(digitChars2, 0, 2, false);
        Assert.assertEquals("Invalid result from inLongRange method for edge case 2", true, actual2);

        char[] digitChars3 = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '9'};
        boolean actual3 = NumberInput.inLongRange(digitChars3, 0, 11, false);
        Assert.assertEquals("Invalid result from inLongRange method for edge case 3", false, actual3);
    }

    @Test
    public void testParseAsIntWithDefaultValue() {
        String input = "123";
        int expected = 123;
        int actual = NumberInput.parseAsInt(input, 45);
        Assert.assertEquals("Invalid result from parseAsInt method with default value", expected, actual);
    }

    @Test
    public void testParseAsIntWithInvalidInput() {
        String input = "abc";
        int expected = 45;
        int actual = NumberInput.parseAsInt(input, 45);
        Assert.assertEquals("Invalid result from parseAsInt method with invalid input", expected, actual);
    }

    @Test
    public void testParseAsLongWithDefaultValue() {
        String input = "1234567890";
        long expected = 1234567890L;
        long actual = NumberInput.parseAsLong(input, 9876543210L);
        Assert.assertEquals("Invalid result from parseAsLong method with default value", expected, actual);
    }

    @Test
    public void testParseAsLongWithInvalidInput() {
        String input = "abc";
        long expected = 9876543210L;
        long actual = NumberInput.parseAsLong(input, 9876543210L);
        Assert.assertEquals("Invalid result from parseAsLong method with invalid input", expected, actual);
    }

    @Test
    public void testParseAsDoubleWithDefaultValue() {
        String input = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseAsDouble(input, 78.9);
        Assert.assertEquals("Invalid result from parseAsDouble method with default value", expected, actual, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseAsDoubleWithInvalidInput() {
        String input = "abc";
        double expected = 78.9;
        double actual = NumberInput.parseAsDouble(input, 78.9); // Exception should be thrown here
    }

    @Test
    public void testParseDoubleWithValidInput() {
        String numStr = "123.45";
        double expected = 123.45;
        double actual = NumberInput.parseDouble(numStr);
        Assert.assertEquals("Invalid result from parseDouble method", expected, actual, 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDoubleWithInvalidInput() {
        String numStr = "abc";
        NumberInput.parseDouble(numStr); // Exception should be thrown here
    }

    @Test
    public void testParseBigDecimalWithValidInput() {
        String numStr = "123.45";
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(numStr);
        Assert.assertEquals("Invalid result from parseBigDecimal method", expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidInput() {
        String numStr = "abc";
        NumberInput.parseBigDecimal(numStr); // Exception should be thrown here
    }

    @Test
    public void testParseBigDecimalWithCharArray() {
        char[] buffer = new char[]{'1', '2', '3', '.', '4', '5'};
        BigDecimal expected = new BigDecimal("123.45");
        BigDecimal actual = NumberInput.parseBigDecimal(buffer, 0, 6);
        Assert.assertEquals("Invalid result from parseBigDecimal method with char array", expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalWithInvalidCharArray() {
        char[] buffer = new char[]{'a', 'b', 'c'};
        NumberInput.parseBigDecimal(buffer, 0, 3); // Exception should be thrown here
    }
}