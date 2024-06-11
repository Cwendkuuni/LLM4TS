package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_ValidPositiveInteger() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
        int expected = 12345;
        int actual = NumberInput.parseInt(digitChars, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseInt_ValidNegativeInteger() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
        int expected = -12345;
        int actual = NumberInput.parseInt(digitChars, 0, 6);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInt_LengthLessThanOne() {
        char[] digitChars = new char[0];
        NumberInput.parseInt(digitChars, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInt_LengthGreaterThanLimit() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        NumberInput.parseInt(digitChars, 0, 11);
    }

    @Test
    public void testParseInt_ValidString() {
        String input = "12345";
        int expected = 12345;
        int actual = NumberInput.parseInt(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInt_InvalidString() {
        String input = "abcdef";
        NumberInput.parseInt(input);
    }

    @Test
    public void testParseLong_ValidPositiveLong() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 1234567890L;
        long actual = NumberInput.parseLong(digitChars, 0, 10);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_ValidNegativeLong() {
        char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = -1234567890L;
        long actual = NumberInput.parseLong(digitChars, 0, 11);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLong_LengthLessThanOne() {
        char[] digitChars = new char[0];
        NumberInput.parseLong(digitChars, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLong_LengthGreaterThanLimit() {
        char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
        NumberInput.parseLong(digitChars, 0, 13);
    }

    @Test
    public void testParseLong_ValidString() {
        String input = "1234567890";
        long expected = 1234567890L;
        long actual = NumberInput.parseLong(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLong_InvalidString() {
        String input = "abcdef";
        NumberInput.parseLong(input);
    }

    @Test
    public void testParseLong_WithinLongRange() {
        String numberStr = "9223372036854775807";
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(numberStr.toCharArray(), false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_BeyondLongRange() {
        String numberStr = "9223372036854775808";
        boolean expected = false;
        boolean actual = NumberInput.inLongRange(numberStr.toCharArray(), false);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_WithinLongRangeNegative() {
        String numberStr = "-9223372036854775808";
        boolean expected = true;
        boolean actual = NumberInput.inLongRange(numberStr.toCharArray(), true);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseLong_BeyondLongRangeNegative() {
        String numberStr = "-9223372036854775809";
        boolean expected = false;
        boolean actual = NumberInput.inLongRange(numberStr.toCharArray(), true);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseAsInt() {
        String input1 = "12345";
        int expected1 = 12345;
        int actual1 = NumberInput.parseAsInt(input1, 0);
        assertEquals(expected1, actual1);

        String input2 = "123";
        int expected2 = 123;
        int actual2 = NumberInput.parseAsInt(input2, 45);
        assertEquals(expected2, actual2);

        String input3 = "abcd";
        int expected3 = 0;
        int actual3 = NumberInput.parseAsInt(input3, 0);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testParseAsLong() {
        String input1 = "1234567890";
        long expected1 = 1234567890L;
        long actual1 = NumberInput.parseAsLong(input1, 0L);
        assertEquals(expected1, actual1);

        String input2 = "123";
        long expected2 = 123L;
        long actual2 = NumberInput.parseAsLong(input2, 987654321012345L);
        assertEquals(expected2, actual2);

        String input3 = "abcd";
        long expected3 = 0L;
        long actual3 = NumberInput.parseAsLong(input3, 0L);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testParseAsDouble() {
        String input1 = "12345.67";
        double expected1 = 12345.67;
        double actual1 = NumberInput.parseAsDouble(input1, 0.0);
        assertEquals(expected1, actual1, 0.0001);

        String input2 = "-12345.67";
        double expected2 = -12345.67;
        double actual2 = NumberInput.parseAsDouble(input2, 0.0);
        assertEquals(expected2, actual2, 0.0001);

        String input3 = "abcd";
        double expected3 = 0.0;
        double actual3 = NumberInput.parseAsDouble(input3, 0.0);
        assertEquals(expected3, actual3, 0.0001);
    }

    @Test
    public void testParseBigDecimal() {
        String input1 = "12345.67";
        BigDecimal expected1 = new BigDecimal("12345.67");
        BigDecimal actual1 = NumberInput.parseBigDecimal(input1);
        assertEquals(expected1, actual1);

        String input2 = "-12345.67";
        BigDecimal expected2 = new BigDecimal("-12345.67");
        BigDecimal actual2 = NumberInput.parseBigDecimal(input2);
        assertEquals(expected2, actual2);

        String input3 = "abcd";
        BigDecimal expected3 = BigDecimal.ZERO;
        BigDecimal actual3 = NumberInput.parseBigDecimal(input3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testParseBigDecimal_CharArray() {
        char[] input1 = new char[]{'1', '2', '3', '4', '5', '.', '6', '7'};
        BigDecimal expected1 = new BigDecimal("12345.67");
        BigDecimal actual1 = NumberInput.parseBigDecimal(input1, 0, 8);
        assertEquals(expected1, actual1);

        char[] input2 = new char[]{'-', '1', '2', '3', '4', '5', '.', '6', '7'};
        BigDecimal expected2 = new BigDecimal("-12345.67");
        BigDecimal actual2 = NumberInput.parseBigDecimal(input2, 0, 10);
        assertEquals(expected2, actual2);

        char[] input3 = new char[]{'a', 'b', 'c', 'd'};
        BigDecimal expected3 = BigDecimal.ZERO;
        BigDecimal actual3 = NumberInput.parseBigDecimal(input3, 0, 4);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testParseBigDecimal_CharArray_OffsetLength() {
        char[] input1 = new char[]{'1', '2', '3', '4', '5', '.', '6', '7'};
        BigDecimal expected1 = new BigDecimal("12345.67");
        BigDecimal actual1 = NumberInput.parseBigDecimal(input1, 0, 5);
        assertEquals(expected1, actual1);

        char[] input2 = new char[]{'-', '1', '2', '3', '4', '5', '.', '6', '7'};
        BigDecimal expected2 = new BigDecimal("-12345.67");
        BigDecimal actual2 = NumberInput.parseBigDecimal(input2, 1, 9);
        assertEquals(expected2, actual2);

        char[] input3 = new char[]{'a', 'b', 'c', 'd'};
        BigDecimal expected3 = BigDecimal.ZERO;
        BigDecimal actual3 = NumberInput.parseBigDecimal(input3, 0, 4);
        assertEquals(expected3, actual3);
    }
}