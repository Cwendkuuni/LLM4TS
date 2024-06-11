package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseInt() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int len = 5;
 int expectedResult = 12345;
 int result = NumberInput.parseInt(digitChars, 0, len);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseIntWithNegativeSign() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
 int len = 6;
 int expectedResult = -12345;
 int result = NumberInput.parseInt(digitChars, 0, len);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseIntWithInvalidInput() {
 char[] digitChars = new char[]{'1', '2', 'a', '3', '4', '5'};
 int len = 6;
 int expectedResult = 0;
 int result = NumberInput.parseInt(digitChars, 0, len);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseIntWithLongInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 int len = 11;
 long expectedLongResult = 1234567890L;
 int result = NumberInput.parseInt(digitChars, 0, len);
 assertEquals(expectedLongResult, (long) result);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseIntWithInvalidLength() {
 char[] digitChars = new char[]{'1', '2', '3'};
 int len = 3;
 NumberInput.parseInt(digitChars, 0, len);
 }

 @Test
 public void testParseLong() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 int len = 9;
 long expectedResult = 123456789;
 long result = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseLongWithNegativeSign() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 int len = 11;
 long expectedResult = -123456789;
 long result = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseLongWithInvalidInput() {
 char[] digitChars = new char[]{'1', '2', 'a', '3', '4', '5', '6', '7', '8', '9'};
 int len = 10;
 long expectedResult = 0;
 long result = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseLongWithLongInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
 int len = 13;
 long expectedResult = 123456789012;
 long result = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expectedResult, result);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseLongWithInvalidLength() {
 char[] digitChars = new char[]{'1', '2', '3'};
 int len = 3;
 NumberInput.parseLong(digitChars, 0, len);
 }

 @Test
 public void testInLongRange() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8'};
 int offset = 0;
 int len = 8;
 boolean expectedResult = true;
 boolean result = NumberInput.inLongRange(digitChars, offset, len, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testInLongRangeWithNegativeNumber() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8'};
 int offset = 0;
 int len = 9;
 boolean expectedResult = true;
 boolean result = NumberInput.inLongRange(digitChars, offset, len, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testInLongRangeWithLongNumber() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
 int offset = 0;
 int len = 12;
 boolean expectedResult = false;
 boolean result = NumberInput.inLongRange(digitChars, offset, len, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseAsInt() {
 String input = "12345";
 int defaultValue = 0;
 int expectedResult = 12345;
 int result = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseAsIntWithDefaultValue() {
 String input = null;
 int defaultValue = 0;
 int expectedResult = 0;
 int result = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseAsIntWithNegativeInput() {
 String input = "-12345";
 int defaultValue = 0;
 int expectedResult = -12345;
 int result = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseAsIntWithLongInput() {
 String input = "1234567890";
 int defaultValue = 0;
 long expectedLongResult = 1234567890L;
 int result = (int) NumberInput.parseAsLong(input, defaultValue);
 assertEquals((int) expectedLongResult, result);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseAsIntWithInvalidInput() {
 String input = "1a2345";
 int defaultValue = 0;
 NumberInput.parseAsInt(input, defaultValue);
 }

 @Test
 public void testParseAsLong() {
 String input = "1234567890";
 long defaultValue = 0;
 long expectedResult = 1234567890L;
 long result = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseAsLongWithDefaultValue() {
 String input = null;
 long defaultValue = 0;
 long expectedResult = 0;
 long result = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseAsLongWithNegativeInput() {
 String input = "-1234567890";
 long defaultValue = 0;
 long expectedResult = -1234567890L;
 long result = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseAsLongWithLongInput() {
 String input = "12345678901234567890";
 long defaultValue = 0;
 long expectedResult = 12345678901234567890L;
 long result = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(expectedResult, result);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseAsLongWithInvalidInput() {
 String input = "1a2345";
 long defaultValue = 0;
 NumberInput.parseAsLong(input, defaultValue);
 }

 @Test
 public void testParseDouble() {
 String input = "123.45";
 double defaultValue = 0.0;
 double expectedResult = 123.45;
 double result = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals(expectedResult, result, 0.001);
 }

 @Test
 public void testParseDoubleWithDefaultValue() {
 String input = null;
 double defaultValue = 0.0;
 double expectedResult = 0.0;
 double result = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals(expectedResult, result, 0.0);
 }

 @Test
 public void testParseDoubleWithNegativeInput() {
 String input = "-123.45";
 double defaultValue = 0.0;
 double expectedResult = -123.45;
 double result = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals(expectedResult, result, 0.001);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseDoubleWithInvalidInput() {
 String input = "1a2.3";
 double defaultValue = 0.0;
 NumberInput.parseAsDouble(input, defaultValue);
 }

 @Test
 public void testParseBigDecimal() {
 String input = "123.45";
 BigDecimal defaultValue = BigDecimal.ZERO;
 BigDecimal expectedResult = new BigDecimal("123.45");
 BigDecimal result = NumberInput.parseBigDecimal(input);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseBigDecimalWithDefaultValue() {
 String input = null;
 BigDecimal defaultValue = BigDecimal.ZERO;
 BigDecimal expectedResult = BigDecimal.ZERO;
 BigDecimal result = NumberInput.parseBigDecimal(input);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseBigDecimalWithNegativeInput() {
 String input = "-123.45";
 BigDecimal defaultValue = BigDecimal.ZERO;
 BigDecimal expectedResult = new BigDecimal("-123.45");
 BigDecimal result = NumberInput.parseBigDecimal(input);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseBigDecimalWithLongInput() {
 char[] buffer = new char[]{'1', '2', '3', '.', '4', '5'};
 int offset = 0;
 int len = 6;
 BigDecimal expectedResult = new BigDecimal("123.45");
 BigDecimal result = NumberInput.parseBigDecimal(buffer, offset, len);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseBigDecimalWithLongInputAndDefaultValue() {
 char[] buffer = new char[]{'1', '2', '3', '.'};
 int offset = 0;
 int len = 4;
 BigDecimal expectedResult = BigDecimal.ZERO;
 BigDecimal result = NumberInput.parseBigDecimal(buffer, offset, len);
 assertEquals(expectedResult, result);
 }
}