package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithPositiveNumbers() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int expected = 12345;
 int result = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(expected, result);
 }

 @Test
 public void testParseIntWithNegativeNumbers() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int result = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals(expected, result);
 }

 @Test
 public void testParseIntWithMaxLength() {
 char[] digitChars = new char[10];
 for (int i = 0; i < 10; i++) {
 digitChars[i] = (char) ('0' + i);
 }
 int expected = Integer.MAX_VALUE;
 int result = NumberInput.parseInt(digitChars, 0, 10);
 assertEquals(expected, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseIntWithInvalidLength() {
 char[] digitChars = new char[]{'1', '2', '3'};
 int result = NumberInput.parseInt(digitChars, 0, 3);
 fail("Expected IndexOutOfBoundsException but got: " + result);
 }

 @Test
 public void testParseLongWithPositiveNumbers() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long expected = 1234567890123L;
 long result = NumberInput.parseLong(digitChars, 0, 14);
 assertEquals(expected, result);
 }

 @Test
 public void testParseLongWithNegativeNumbers() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long expected = -1234567890123L;
 long result = NumberInput.parseLong(digitChars, 0, 15);
 assertEquals(expected, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseLongWithInvalidLength() {
 char[] digitChars = new char[]{'1', '2', '3'};
 long result = NumberInput.parseLong(digitChars, 0, 3);
 fail("Expected IndexOutOfBoundsException but got: " + result);
 }

 @Test
 public void testParseLongWithLongRange() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
 boolean expected = NumberInput.inLongRange(digitChars, 0, 13, false);
 assertTrue(expected);
 char[] digitCharsNegative = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
 boolean expectedNegative = NumberInput.inLongRange(digitCharsNegative, 0, 14, true);
 assertTrue(expectedNegative);
 }

 @Test
 public void testParseLongWithOutOfLongRange() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
 boolean expected = NumberInput.inLongRange(digitChars, 0, 15, false);
 assertFalse(expected);
 char[] digitCharsNegative = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
 boolean expectedNegative = NumberInput.inLongRange(digitCharsNegative, 0, 16, true);
 assertFalse(expectedNegative);
 }

 @Test
 public void testParseAsIntWithDefaultValue() {
 String input = null;
 int defaultValue = 42;
 int result = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(defaultValue, result);
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
 String input = "abcd";
 int expected = 0;
 int result = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, result);
 }

 @Test
 public void testParseAsLongWithDefaultValue() {
 String input = null;
 long defaultValue = 42L;
 long result = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(defaultValue, result);
 }

 @Test
 public void testParseAsLongWithValidInput() {
 String input = "1234567890123";
 long expected = 1234567890123L;
 long result = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, result);
 }

 @Test
 public void testParseAsLongWithInvalidInput() {
 String input = "abcd";
 long expected = 0L;
 long result = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, result);
 }

 @Test
 public void testParseAsDoubleWithDefaultValue() {
 String input = null;
 double defaultValue = 42.0;
 double result = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals(defaultValue, result, 0.0);
 }

 @Test
 public void testParseAsDoubleWithValidInput() {
 String input = "123.45";
 double expected = 123.45;
 double result = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, result, 0.001);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsDoubleWithInvalidInput() {
 String input = "abcd";
 double defaultValue = 0.0;
 double result = NumberInput.parseAsDouble(input, defaultValue);
 fail("Expected NumberFormatException but got: " + result);
 }
}