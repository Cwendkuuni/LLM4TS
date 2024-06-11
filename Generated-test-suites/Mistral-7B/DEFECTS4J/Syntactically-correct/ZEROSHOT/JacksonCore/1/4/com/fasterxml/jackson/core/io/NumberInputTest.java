package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] validIntDigits = new char[]{'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(validIntDigits, 0, 5);
 assertEquals("Parsing valid integer input should return expected value", expected, actual);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testParseIntWithInvalidLength() {
 char[] invalidLengthDigits = new char[]{'1', '2'};
 NumberInput.parseInt(invalidLengthDigits, 0, 2);
 }

 @Test
 public void testParseIntWithNegativeInput() {
 char[] negativeIntDigits = new char[]{'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int actual = NumberInput.parseInt(negativeIntDigits, 0, 6);
 assertEquals("Parsing negative integer input should return expected value", expected, actual);
 }

 @Test
 public void testParseIntWithValidStringInput() {
 String validIntString = "12345";
 int expected = 12345;
 int actual = NumberInput.parseInt(validIntString);
 assertEquals("Parsing valid integer string input should return expected value", expected, actual);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseIntWithInvalidStringInput() {
 String invalidStringInput = "abcd";
 NumberInput.parseInt(invalidStringInput);
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] validLongDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = 1234567890123L;
 long actual = NumberInput.parseLong(validLongDigits, 0, 18);
 assertEquals("Parsing valid long input should return expected value", expected, actual);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testParseLongWithInvalidLength() {
 char[] invalidLengthLongDigits = new char[]{'1', '2', '3', '4', '5', '6'};
 NumberInput.parseLong(invalidLengthLongDigits, 0, 12);
 }

 @Test
 public void testParseLongWithNegativeInput() {
 char[] negativeLongDigits = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = -1234567890123L;
 long actual = NumberInput.parseLong(negativeLongDigits, 0, 13);
 assertEquals("Parsing negative long input should return expected value", expected, actual);
 }

 @Test
 public void testParseLongWithValidStringInput() {
 String validLongString = "1234567890123";
 long expected = 1234567890123L;
 long actual = NumberInput.parseLong(validLongString);
 assertEquals("Parsing valid long string input should return expected value", expected, actual);
 }

 @Test (expected = NumberFormatException.class)
 public void testParseLongWithInvalidStringInput() {
 String invalidStringInput = "abcd";
 NumberInput.parseLong(invalidStringInput);
 }

 @Test
 public void testInLongRangeWithValidInput() {
 char[] validLongDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(validLongDigits, 0, 10, false);
 assertEquals("Parsing valid long input in range should return true", expected, actual);
 }

 @Test
 public void testInLongRangeWithNegativeInput() {
 char[] negativeLongDigits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(negativeLongDigits, 0, 10, true);
 assertEquals("Parsing negative long input out of range should return false", expected, actual);
 }

 @Test
 public void testInLongRangeWithValidStringInput() {
 String validLongString = "1234567890123";
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(validLongString, false);
 assertEquals("Parsing valid long string input in range should return true", expected, actual);
 }

 @Test
 public void testInLongRangeWithNegativeStringInput() {
 String negativeLongString = "-1234567890123";
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(negativeLongString, true);
 assertEquals("Parsing negative long string input out of range should return false", expected, actual);
 }

 @Test
 public void testParseAsIntWithValidInput() {
 String validInput = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt("12345", expected);
 assertEquals("Parsing valid input should return expected value", expected, actual);
 }

 @Test
 public void testParseAsIntWithDefaultValue() {
 String invalidInput = "abcd";
 int defaultValue = 0;
 int actual = NumberInput.parseAsInt("abcd", defaultValue);
 assertEquals("Parsing invalid input should return default value", defaultValue, actual);
 }

 @Test
 public void testParseAsLongWithValidInput() {
 String validInput = "1234567890123";
 long expected = 1234567890123L;
 long actual = NumberInput.parseAsLong("1234567890123", expected);
 assertEquals("Parsing valid input should return expected value", expected, actual);
 }

 @Test
 public void testParseAsLongWithDefaultValue() {
 String invalidInput = "abcd";
 long defaultValue = 0L;
 long actual = NumberInput.parseAsLong("abcd", defaultValue);
 assertEquals("Parsing invalid input should return default value", defaultValue, actual);
 }

 @Test
 public void testParseAsDoubleWithValidInput() {
 String validInput = "123.45";
 double expected = 123.45;
 double actual = NumberInput.parseAsDouble("123.45");
 assertEquals("Parsing valid input should return expected value", expected, actual, 0.001);
 }

 @Test
 public void testParseAsDoubleWithDefaultValue() {
 String invalidInput = "abcd";
 double defaultValue = 0.0;
 double actual = NumberInput.parseAsDouble("abcd", defaultValue);
 assertEquals("Parsing invalid input should return default value", defaultValue, actual, 0.001);
 }
}