package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int offset = 0, len = 5;
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseIntWithInvalidInputLength() {
 char[] digitChars = new char[]{'1'};
 int offset = 0, len = 1;
 NumberInput.parseInt(digitChars, offset, len);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseIntWithInvalidInput() {
 char[] digitChars = new char[]{'1', 'a', '3', '4', '5'};
 int offset = 0, len = 5;
 NumberInput.parseInt(digitChars, offset, len);
 }

 @Test
 public void testParseIntWithNegativeNumber() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
 int offset = 0, len = 6;
 int expected = -12345;
 int actual = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 int offset = 0, len = 10;
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseLongWithInvalidInputLength() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int offset = 0, len = 5;
 NumberInput.parseLong(digitChars, offset, len);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseLongWithInvalidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', 'a'};
 int offset = 0, len = 6;
 NumberInput.parseLong(digitChars, offset, len);
 }

 @Test
 public void testParseLongWithNegativeNumber() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 int offset = 0, len = 11;
 long expected = -12345678901L;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongAsStringWithValidInput() {
 String str = "1234567890";
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(str);
 assertEquals(expected, actual);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseLongAsStringWithInvalidInput() {
 String str = "123a456";
 NumberInput.parseLong(str);
 }

 @Test
 public void testIsInLongRangeWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 5, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInLongRangeWithLongNumber() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 10, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInLongRangeWithNegativeNumber() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChards, 0, 11, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInLongRangeWithNumberOutOfLongRange() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 11, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithValidInputAsString() {
 String str = "12345";
 int expected = 12345;
 int actual = NumberInput.parseInt(str);
 assertEquals(expected, actual);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseIntWithInvalidInputAsString() {
 String str = "12a34";
 NumberInput.parseInt(str);
 }

 @Test
 public void testParseLongWithValidInputAsString() {
 String str = "12345678901234567890";
 long expected = 12345678901234567890L;
 long actual = NumberInput.parseLong(str);
 assertEquals(expected, actual);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseLongWithInvalidInputAsString() {
 String str = "123a45678901234567890";
 NumberInput.parseLong(str);
 }

 @Test
 public void testParseAsIntWithValidInput() {
 String str = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt(str, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithDefaultValue() {
 String str = "123a45";
 int expected = 0;
 int actual = NumberInput.parseAsInt(str, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithValidInput() {
 String str = "12345678901234567890";
 long expected = 12345678901234567890L;
 long actual = NumberInput.parseAsLong(str, 0L);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithDefaultValue() {
 String str = "123a45678901234567890";
 long expected = 0L;
 long actual = NumberInput.parseAsLong(str, 0L);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsDoubleWithValidInput() {
 String str = "123.45";
 double expected = 123.45;
 double actual = NumberInput.parseAsDouble(str, 0.0);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testParseAsDoubleWithDefaultValue() {
 String str = "123a.45";
 double expected = 0.0;
 double actual = NumberInput.parseAsDouble(str, 0.0);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testParseBigDecimalWithValidInput() {
 String str = "1234567890.12345";
 BigDecimal expected = new BigDecimal("1234567890.12345");
 BigDecimal actual = NumberInput.parseBigDecimal(str);
 assertEquals(expected, actual);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseBigDecimalWithInvalidInput() {
 String str = "123a4567890.12345";
 NumberInput.parseBigDecimal(str);
 }

 @Test
 public void testParseBigDecimalWithCharArray() {
 char[] buffer = new char[]{'1', '2', '3', '4', '5', '.', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5'};
 int offset = 0, len = 17;
 BigDecimal expected = new BigDecimal("12345.6789012345");
 BigDecimal actual = NumberInput.parseBigDecimal(buffer, offset, len);
 assertEquals(expected, actual);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseBigDecimalWithInvalidLength() {
 char[] buffer = new char[]{'1', '2', '3', '4', '5', '.'};
 int offset = 0, len = 6;
 NumberInput.parseBigDecimal(buffer, offset, len);
 }
}