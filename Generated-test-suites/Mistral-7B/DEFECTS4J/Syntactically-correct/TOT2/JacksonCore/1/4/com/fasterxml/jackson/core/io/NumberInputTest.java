package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 // Test case for parseInt method with positive integers
 @Test
 public void testParseIntPositive() {
 char[] digitChars = {'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(expected, actual);
 }

 // Test case for parseInt method with negative integers
 @Test
 public void testParseIntNegative() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int actual = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals(expected, actual);
 }

 // Test case for parseInt method with invalid input
 @Test(expected = NumberFormatException.class)
 public void testParseIntInvalidInput() {
 char[] digitChars = {'a', '1', '2', '3', '4', '5'};
 NumberInput.parseInt(digitChars, 0, 6);
 }

 // Test case for parseInt method with input longer than the expected integer value
 @Test(expected = NumberFormatException.class)
 public void testParseIntLongInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
 NumberInput.parseInt(digitChars, 0, 11);
 }

 // Test case for parseLong method with positive longs
 @Test
 public void testParseLongPositive() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8'};
 long expected = 12345678;
 long actual = NumberInput.parseLong(digitChars, 0, 8);
 assertEquals(expected, actual);
 }

 // Test case for parseLong method with negative longs
 @Test
 public void testParseLongNegative() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8'};
 long expected = -12345678;
 long actual = NumberInput.parseLong(digitChars, 0, 10);
 assertEquals(expected, actual);
 }

 // Test case for parseLong method with invalid input
 @Test(expected = NumberFormatException.class)
 public void testParseLongInvalidInput() {
 char[] digitChars = {'a', '1', '2', '3', '4', '5', '6', '7', '8'};
 NumberInput.parseLong(digitChars, 0, 9);
 }

 // Test case for parseLong method with input longer than the expected long value
 @Test(expected = NumberFormatException.class)
 public void testParseLongLongInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
 NumberInput.parseLong(digitChars, 0, 12);
 }

 // Test case for inLongRange method with positive number
 @Test
 public void testInLongRangePositive() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7'};
 boolean actual = NumberInput.inLongRange(digitChars, 0, 7, false);
 assertTrue(actual);
 }

 // Test case for inLongRange method with negative number
 @Test
 public void testInLongRangeNegative() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7'};
 boolean actual = NumberInput.inLongRange(digitChars, 0, 8, false);
 assertTrue(actual);
 }

 // Test case for inLongRange method with number out of long range
 @Test
 public void testInLongRangeOutOfRange() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
 boolean actual = NumberInput.inLongRange(digitChars, 0, 13, false);
 assertFalse(actual);
 }

 // Test case for inLongRange method with negative number out of long range
 @Test
 public void testInLongRangeNegativeOutOfRange() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
 boolean actual = NumberInput.inLongRange(digitChars, 0, 14, false);
 assertFalse(actual);
 }

 // Test case for inLongRange method with negative number in long range
 @Test
 public void testInLongRangeNegativeInRange() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7'};
 boolean actual = NumberInput.inLongRange(digitChars, 0, 8, true);
 assertTrue(actual);
 }

 // Test case for parseAsInt method with positive input
 @Test
 public void testParseAsIntPositive() {
 String input = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 // Test case for parseAsInt method with negative input
 @Test
 public void testParseAsIntNegative() {
 String input = "-12345";
 int expected = -12345;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 // Test case for parseAsInt method with invalid input
 @Test(expected = NumberFormatException.class)
 public void testParseAsIntInvalidInput() {
 String input = "1a345";
 NumberInput.parseAsInt(input, 0);
 }

 // Test case for parseAsInt method with input longer than the expected integer value
 @Test(expected = NumberFormatException.class)
 public void testParseAsIntLongInput() {
 String input = "12345678901";
 NumberInput.parseAsInt(input, 0);
 }

 // Test case for parseAsLong method with positive input
 @Test
 public void testParseAsLongPositive() {
 String input = "1234567890123";
 long expected = 1234567890123L;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, actual);
 }

 // Test case for parseAsLong method with negative input
 @Test
 public void testParseAsLongNegative() {
 String input = "-1234567890123";
 long expected = -1234567890123L;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, actual);
 }

 // Test case for parseAsLong method with invalid input
 @Test(expected = NumberFormatException.class)
 public void testParseAsLongInvalidInput() {
 String input = "1a34567890123";
 NumberInput.parseAsLong(input, 0);
 }

 // Test case for parseAsLong method with input longer than the expected long value
 @Test(expected = NumberFormatException.class)
 public void testParseAsLongLongInput() {
 String input = "123456789012345678901234567890123";
 NumberInput.parseAsLong(input, 0);
 }

 // Test case for parseAsDouble method with positive double
 @Test
 public void testParseAsDoublePositive() {
 String input = "123.45";
 double expected = 123.45;
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, actual, 0.001);
 }

 // Test case for parseAsDouble method with negative double
 @Test
 public void testParseAsDoubleNegative() {
 String input = "-123.45";
 double expected = -123.45;
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, actual, 0.001);
 }

 // Test case for parseAsDouble method with invalid input
 @Test(expected = NumberFormatException.class)
 public void testParseAsDoubleInvalidInput() {
 String input = "1a3.45";
 NumberInput.parseAsDouble(input, 0);
 }

 // Test case for parseAsDouble method with input longer than the expected double value
 @Test(expected = NumberFormatException.class)
 public void testParseAsDoubleLongInput() {
 String input = "1234567890.1234567890";
 NumberInput.parseAsDouble(input, 0);
 }

 // Test case for parseBigDecimal method with valid input
 @Test
 public void testParseBigDecimalValidInput() {
 String input = "12345.6789";
 BigDecimal expected = new BigDecimal("12345.6789");
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 // Test case for parseBigDecimal method with invalid input
 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalInvalidInput() {
 char[] buffer = {'a', '1', '2', '3', '4', '5', '.', '6', '7', '8'};
 NumberInput.parseBigDecimal(buffer, 0, 12);
 }

 // Test case for parseBigDecimal method with input longer than the expected BigDecimal value
 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalLongInput() {
 char[] buffer = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5'};
 NumberInput.parseBigDecimal(buffer, 0, 15);
 }
}