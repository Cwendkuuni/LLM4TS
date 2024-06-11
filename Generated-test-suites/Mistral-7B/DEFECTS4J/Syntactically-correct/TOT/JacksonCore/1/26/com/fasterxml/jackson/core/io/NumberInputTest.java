package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = {'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithLongInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = 123456789L;
 int actual = NumberInput.parseInt(digitChars, 0, 9);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithNegativeSign() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithInvalidInput() {
 char[] digitChars = {'1', 'a', '2', '3', '4'};
 NumberInput.parseInt(digitChars, 0, 4);
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long expected = 1234567890123L;
 long actual = NumberInput.parseLong(digitChars, 0, 13);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithMinusSign() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long expected = -1234567890123L;
 long actual = NumberInput.parseLong(digitChars, 0, 13);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInvalidInput() {
 char[] digitChars = {'1', 'a', '2', '3', '4', '5', '6', '7', '8'};
 NumberInput.parseLong(digitChars, 0, 8);
 }

 @Test
 public void testParseLongAsIntWithValidInput() {
 String str = "12345";
 int expected = 12345;
 int actual = NumberInput.parseInt(str);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongAsIntWithInvalidInput() {
 String str = "123ab4";
 NumberInput.parseInt(str);
 }

 @Test
 public void testParseLongAsLongWithValidInput() {
 String str = "1234567890123";
 long expected = 1234567890123L;
 long actual = NumberInput.parseLong(str);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongAsLongWithInvalidInput() {
 String str = "123ab4cd";
 NumberInput.parseLong(str);
 }

 @Test
 public void testInLongRangeWithValidInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 7, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithLongInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 9, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testInLongRangeWithNegativeInput() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8'};
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 9, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithValidInput() {
 String input = "12345";
 int defaultValue = 0;
 int expected = 12345;
 int actual = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithInvalidInput() {
 String input = "123ab4";
 int defaultValue = 0;
 int expected = 0;
 int actual = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithEmptyString() {
 String input = "";
 int defaultValue = 0;
 int expected = 0;
 int actual = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithValidInput() {
 String input = "1234567890123";
 long defaultValue = 0L;
 long expected = 1234567890123L;
 long actual = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithInvalidInput() {
 String input = "123ab4cd";
 long defaultValue = 0L;
 long expected = 0L;
 long actual = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithEmptyString() {
 String input = "";
 long defaultValue = 0L;
 long expected = 0L;
 long actual = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsDoubleWithValidInput() {
 String input = "123.45";
 double defaultValue = 0.0;
 double expected = 123.45;
 double actual = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals(expected, actual, 0.001);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsDoubleWithInvalidInput() {
 String input = "123ab4cd";
 double defaultValue = 0.0;
 NumberInput.parseAsDouble(input, defaultValue);
 }

 @Test
 public void testParseAsDoubleWithEmptyString() {
 String input = "";
 double defaultValue = 0.0;
 double expected = 0.0;
 double actual = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals(expected, actual, 0.0);
 }

 @Test
 public void testParseBigDecimalWithValidInput() {
 String input = "123.45";
 BigDecimal defaultValue = BigDecimal.ZERO;
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalWithInvalidInput() {
 String input = "123ab4cd";
 BigDecimal defaultValue = BigDecimal.ZERO;
 NumberInput.parseBigDecimal(input);
 }

 @Test
 public void testParseBigDecimalWithEmptyString() {
 String input = "";
 BigDecimal defaultValue = BigDecimal.ZERO;
 BigDecimal expected = BigDecimal.ZERO;
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithCharArray() {
 char[] buffer = {'1', '2', '3', '.' , '4', '5'};
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(buffer, 0, 6);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalWithInvalidCharArray() {
 char[] buffer = {'1', '2', '3', 'a', '4', '5'};
 NumberInput.parseBigDecimal(buffer, 0, 6);
 }

 @Test
 public void testParseBigDecimalWithEmptyCharArray() {
 char[] buffer = {};
 BigDecimal expected = BigDecimal.ZERO;
 BigDecimal actual = NumberInput.parseBigDecimal(buffer);
 assertEquals(expected, actual);
 }
}