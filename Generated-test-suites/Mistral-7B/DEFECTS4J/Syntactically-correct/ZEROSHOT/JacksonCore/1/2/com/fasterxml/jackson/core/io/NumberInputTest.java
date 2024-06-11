package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseInt() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int result = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(12345, result);

 digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
 result = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals(-12345, result);

 digitChars = new char[]{'1', '2', '3'};
 try {
 result = NumberInput.parseInt(digitChars, 0, 3);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }

 String str = "12345";
 result = NumberInput.parseInt(str.toCharArray(), 0, str.length());
 assertEquals(12345, result);

 str = "-12345";
 result = NumberInput.parseInt(str.toCharArray(), 0, str.length());
 assertEquals(-12345, result);

 str = "123";
 result = NumberInput.parseInt(str.toCharArray(), 0, str.length());
 assertEquals(123, result);
 }

 @Test
 public void testParseLong() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long result = NumberInput.parseLong(digitChars, 0, 10);
 assertEquals(123456789L, result);

 digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 result = NumberInput.parseLong(digitChars, 0, 11);
 assertEquals(-123456789L, result);

 digitChars = new char[]{'1', '2', '3', '4', '5'};
 result = NumberInput.parseLong(digitChars, 0, 5);
 try {
 result = NumberInput.parseLong(digitChars, 0, 5);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }

 String str = "12345678901234567890";
 result = NumberInput.parseLong(str.toCharArray(), 0, str.length());
 assertEquals(12345678901234567890L, result);

 str = "-12345678901234567890";
 result = NumberInput.parseLong(str.toCharArray(), 0, str.length());
 assertEquals(-12345678901234567890L, result);

 str = "123";
 result = NumberInput.parseLong(str.toCharArray(), 0, str.length());
 try {
 result = NumberInput.parseLong(str.toCharArray(), 0, str.length());
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseAsInt() {
 String input = "12345";
 int result = NumberInput.parseAsInt(input, 0);
 assertEquals(12345, result);

 input = "-12345";
 result = NumberInput.parseAsInt(input, 0);
 assertEquals(-12345, result);

 input = "123";
 result = NumberInput.parseAsInt(input, 0);
 assertEquals(123, result);

 input = "abcd";
 result = NumberInput.parseAsInt(input, 0);
 assertEquals(0, result);
 }

 @Test
 public void testParseAsLong() {
 String input = "12345678901234567890";
 long result = NumberInput.parseAsLong(input, 0L);
 assertEquals(12345678901234567890L, result);

 input = "-12345678901234567890";
 result = NumberInput.parseAsLong(input, 0L);
 assertEquals(-12345678901234567890L, result);

 input = "123";
 result = NumberInput.parseAsLong(input, 0L);
 assertEquals(0L, result);
 }

 @Test
 public void testInLongRange() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 boolean result = NumberInput.inLongRange(digitChars, 0, 5, false);
 assertTrue(result);

 digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 result = NumberInput.inLongRange(digitChars, 0, 10, false);
 assertTrue(result);

 digitChars = new char[]{'1', '2', '3', '4', '5', '1', '1', '1', '1', '1'};
 result = NumberInput.inLongRange(digitChars, 0, 11, false);
 assertFalse(result);

 String numberStr = "12345";
 result = NumberInput.inLongRange(numberStr.toCharArray(), 0, numberStr.length(), false);
 assertTrue(result);

 numberStr = "12345678901234567890";
 result = NumberInput.inLongRange(numberStr.toCharArray(), 0, numberStr.length(), false);
 assertTrue(result);

 numberStr = "123456789012345678901";
 result = NumberInput.inLongRange(numberStr.toCharArray(), 0, numberStr.length(), false);
 assertFalse(result);
 }

 @Test
 public void testInLongRangeNegative() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 boolean result = NumberInput.inLongRange(digitChars, 0, 5, true);
 assertTrue(result);

 digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 result = NumberInput.inLongRange(digitChars, 0, 10, true);
 assertTrue(result);

 digitChars = new char[]{'1', '2', '3', '4', '5', '1', '1', '1', '1', '1'};
 result = NumberInput.inLongRange(digitChars, 0, 11, true);
 assertFalse(result);

 String numberStr = "12345";
 result = NumberInput.inLongRange(numberStr.toCharArray(), 0, numberStr.length(), true);
 assertTrue(result);

 numberStr = "12345678901234567890";
 result = NumberInput.inLongRange(numberStr.toCharArray(), 0, numberStr.length(), true);
 assertTrue(result);

 numberStr = "123456789012345678901";
 result = NumberInput.inLongRange(numberStr.toCharArray(), 0, numberStr.length(), true);
 assertFalse(result);
 }
}