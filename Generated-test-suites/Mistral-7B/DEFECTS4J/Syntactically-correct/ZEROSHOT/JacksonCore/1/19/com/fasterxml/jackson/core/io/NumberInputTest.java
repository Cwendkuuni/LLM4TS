package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3'};
 int expected = 123;
 int result = NumberInput.parseInt(digitChars, 0, digitChars.length);
 assertEquals(expected, result);
 }

 @Test
 public void testParseIntWithInvalidLength() {
 char[] digitChars = new char[]{'1'};
 try {
 NumberInput.parseInt(digitChars, 0, digitChars.length);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseIntWithNegativeNumber() {
 char[] digitChars = new char[]{'-', '1', '2', '3'};
 int expected = -123;
 int result = NumberInput.parseInt(digitChars, 0, digitChars.length);
 assertEquals(expected, result);
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = 1234567890L;
 long result = NumberInput.parseLong(digitChars, 0, digitChars.length);
 assertEquals(expected, result);
 }

 @Test
 public void testParseLongWithInvalidLength() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6'};
 try {
 NumberInput.parseLong(digitChars, 0, digitChars.length);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseLongWithNegativeNumber() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = -1234567890L;
 long result = NumberInput.parseLong(digitChars, 0, digitChars.length);
 assertEquals(expected, result);
 }

 @Test
 public void testParseLongAsBigDecimal() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 BigDecimal expected = new BigDecimal("1234567890");
 BigDecimal result = NumberInput.parseBigDecimal(digitChars, 0, digitChars.length);
 assertEquals(expected, result);
 }

 @Test
 public void testParseLongAsBigDecimalWithNegativeNumber() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 BigDecimal expected = new BigDecimal("-1234567890");
 BigDecimal result = NumberInput.parseBigDecimal(digitChars, 0, digitChars.length);
 assertEquals(expected, result);
 }

 @Test
 public void testParseStringAsInt() {
 String input = "123";
 int expected = 123;
 int result = NumberInput.parseInt(input);
 assertEquals(expected, result);
 }

 @Test
 public void testParseStringAsIntWithNegativeNumber() {
 String input = "-123";
 int expected = -123;
 int result = NumberInput.parseInt(input);
 assertEquals(expected, result);
 }

 @Test
 public void testParseStringAsLong() {
 String input = "1234567890";
 long expected = 1234567890L;
 long result = NumberInput.parseLong(input);
 assertEquals(expected, result);
 }

 @Test
 public void testParseStringAsLongWithNegativeNumber() {
 String input = "-1234567890";
 long expected = -1234567890L;
 long result = NumberInput.parseLong(input);
 assertEquals(expected, result);
 }

 @Test
 public void testParseStringAsBigDecimal() {
 String input = "1234567890";
 BigDecimal expected = new BigDecimal("1234567890");
 BigDecimal result = NumberInput.parseBigDecimal(input);
 assertEquals(expected, result);
 }

 @Test
 public void testParseStringAsBigDecimalWithNegativeNumber() {
 String input = "-1234567890";
 BigDecimal expected = new BigDecimal("-1234567890");
 BigDecimal result = NumberInput.parseBigDecimal(input);
 assertEquals(expected, result);
 }

 @Test
 public void testParseStringAsDouble() {
 String input = "123.45";
 double expected = 123.45;
 double result = NumberInput.parseAsDouble(input, 0.0);
 assertEquals(expected, result, 0.001);
 }

 @Test
 public void testParseStringAsDoubleWithNegativeNumber() {
 String input = "-123.45";
 double expected = -123.45;
 double result = NumberInput.parseAsDouble(input, 0.0);
 assertEquals(expected, result, 0.001);
 }

 @Test
 public void testParseStringAsDoubleWithInvalidInput() {
 String input = "abcd";
 double expected = 0.0;
 double result = NumberInput.parseAsDouble(input, 0.0);
 assertEquals(expected, result, 0.0);
 }

 @Test
 public void testParseStringAsDoubleWithInvalidFormat() {
 String input = "123e45";
 double expected = Double.NaN;
 double result = NumberInput.parseAsDouble(input, 0.0);
 assertEquals(expected, result, 0.0);
 }

 @Test
 public void testParseStringAsDoubleWithScientificNotation() {
 String input = "123.45e+45";
 double expected = Double.POSITIVE_INFINITY;
 double result = NumberInput.parseAsDouble(input, 0.0);
 assertEquals(expected, result, 0.0);
 }

 @Test
 public void testParseStringAsDoubleWithVerySmallNumber() {
 String input = NumberInput.NASTY_SMALL_DOUBLE;
 double expected = Double.MIN_VALUE;
 double result = NumberInput.parseAsDouble(input, 0.0);
 assertEquals(expected, result, 0.0);
 }

}