package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int offset = 0;
 int len = 5;
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithInvalidInput() {
 char[] digitChars = new char[]{'1', '2', '3', 'a', '5'};
 int offset = 0;
 int len = 5;
 int expected = -1;
 int actual = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithZeroLengthInput() {
 char[] digitChars = new char[0];
 int offset = 0;
 int len = 0;
 int expected = 0;
 int actual = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithNegativeInput() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4'};
 int offset = 0;
 int len = 5;
 int expected = -1234;
 int actual = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithValidStringInput() {
 String input = "12345";
 int expected = 12345;
 int actual = NumberInput.parseInt(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithInvalidStringInput() {
 String input = "123a5";
 int expected = -1;
 int actual = NumberInput.parseInt(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithZeroLengthStringInput() {
 String input = "";
 int expected = 0;
 int actual = NumberInput.parseInt(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithNegativeStringInput() {
 String input = "-1234";
 int expected = -1234;
 int actual = NumberInput.parseInt(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 int offset = 0;
 int len = 9;
 long expected = 123456789L;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithInvalidInput() {
 char[] digitChars = new char[]{'1', '2', '3', 'a', '5', '6', '7', '8', '9'};
 int offset = 0;
 int len = 9;
 long expected = -1;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithZeroLengthInput() {
 char[] digitChars = new char[0];
 int offset = 0;
 int len = 0;
 long expected = 0;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithNegativeInput() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8'};
 int offset = 0;
 int len = 9;
 long expected = -123456789123L;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithValidStringInput() {
 String input = "123456789123";
 long expected = 123456789123L;
 long actual = NumberInput.parseLong(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithInvalidStringInput() {
 String input = "123a56789123";
 long expected = -1;
 long actual = NumberInput.parseLong(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithZeroLengthStringInput() {
 String input = "";
 long expected = 0;
 long actual = NumberInput.parseLong(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithNegativeStringInput() {
 String input = "-123456789123";
 long expected = -123456789123L;
 long actual = NumberInput.parseLong(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int offset = 0;
 int len = 5;
 boolean negative = false;
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, offset, len, negative);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithInvalidInput() {
 char[] digitChars = new char[]{'1', '2', '3', 'a', '5'};
 int offset = 0;
 int len = 5;
 boolean negative = false;
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(digitChars, offset, len, negative);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithZeroLengthInput() {
 char[] digitChars = new char[0];
 int offset = 0;
 int len = 0;
 boolean negative = false;
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, offset, len, negative);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithNegativeInput() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4'};
 int offset = 0;
 int len = 5;
 boolean negative = true;
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, offset, len, negative);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithValidStringInput() {
 String numberStr = "12345";
 boolean negative = false;
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(numberStr, negative);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithInvalidStringInput() {
 String numberStr = "123a5";
 boolean negative = false;
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(numberStr, negative);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithZeroLengthStringInput() {
 String numberStr = "";
 boolean negative = false;
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(numberStr, negative);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithNegativeStringInput() {
 String numberStr = "-12345";
 boolean negative = true;
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(numberStr, negative);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithDefaultValue() {
 String input = "123";
 int defaultValue = 456;
 int expected = 123;
 int actual = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithValidInput() {
 String input = "123";
 int expected = 123;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithInvalidInput() {
 String input = "12a3";
 int expected = 0;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithNegativeInput() {
 String input = "-123";
 int expected = 0;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithDefaultValue() {
 String input = "123456789012345";
 long defaultValue = 98765432109876L;
 long expected = 123456789012345L;
 long actual = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithValidInput() {
 String input = "123456789012345";
 long expected = 123456789012345L;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithInvalidInput() {
 String input = "123a56789012345";
 long expected = 0;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithNegativeInput() {
 String input = "-123456789012345";
 long expected = 0;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsDoubleWithDefaultValue() {
 String input = "123.45";
 double defaultValue = 456.78;
 double expected = 123.45;
 double actual = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testParseAsDoubleWithValidInput() {
 String input = "123.45";
 double expected = 123.45;
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testParseAsDoubleWithInvalidInput() {
 String input = "123a.45";
 double expected = 0;
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testParseAsDoubleWithNegativeInput() {
 String input = "-123.45";
 double expected = 0;
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testParseBigDecimalWithValidInput() {
 String input = "123.45";
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithInvalidInput() {
 String input = "123a.45";
 BigDecimal expected = null;
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertNull(actual);
 }

 @Test
 public void testParseBigDecimalWithZeroLengthStringInput() {
 String input = "";
 BigDecimal expected = BigDecimal.ZERO;
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithCharArrayInput() {
 char[] input = new char[]{'1', '2', '3', '.'};
 int offset = 0;
 int len = 4;
 BigDecimal expected = new BigDecimal("123.4");
 BigDecimal actual = NumberInput.parseBigDecimal(input, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithCharArrayNegativeInput() {
 char[] input = new char[]{'-', '1', '2', '3', '.'};
 int offset = 0;
 int len = 5;
 BigDecimal expected = new BigDecimal("-123.4");
 BigDecimal actual = NumberInput.parseBigDecimal(input, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithCharArrayLongInput() {
 char[] input = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 int offset = 0;
 int len = 9;
 BigDecimal expected = new BigDecimal("123456789");
 BigDecimal actual = NumberInput.parseBigDecimal(input, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithCharArrayNegativeLongInput() {
 char[] input = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8'};
 int offset = 0;
 int len = 9;
 BigDecimal expected = new BigDecimal("-123456789");
 BigDecimal actual = NumberInput.parseBigDecimal(input, offset, len);
 assertEquals(expected, actual);
 }
}