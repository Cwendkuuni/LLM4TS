package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int result = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(12345, result);
 }

 @Test
 public void testParseIntWithEdgeCases() {
 // Test min and max values
 char[] minDigits = new char[]{'-', '2', '1', '4', '7', '4', '8', '3', '6', '4', '7', '6', '8', '3', '7', '5', '6', '1', '1', '5', '1', '3', '1', '2', '5', '5', '5', '5', '5'};
 int minResult = NumberInput.parseInt(minDigits, 0, minDigits.length);
 assertEquals(Long.MIN_VALUE, minResult);

 char[] maxDigits = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
 int maxResult = NumberInput.parseInt(maxDigits, 0, maxDigits.length);
 assertEquals(Long.MAX_VALUE, maxResult);

 // Test parsing negative numbers
 char[] negativeDigits = new char[]{'-', '1', '2', '3', '4', '5'};
 int negativeResult = NumberInput.parseInt(negativeDigits, 0, negativeDigits.length);
 assertEquals(-12345, negativeResult);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithInvalidInput() {
 char[] invalidDigits = new char[]{'1', 'a', '3', '4', '5'};
 NumberInput.parseInt(invalidDigits, 0, invalidDigits.length);
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '0', '0', '0', '0', '0', '1'};
 long result = NumberInput.parseLong(digitChars, 0, 12);
 assertEquals(123450000001L, result);
 }

 @Test
 public void testParseLongWithEdgeCases() {
 // Test min and max values
 char[] minDigits = new char[]{'-', '1', '8', '4', '4', '6', '7', '4', '8', '0', '0', '0', '0', '0', '1', '3', '1', '1', '5', '1', '3', '1', '2', '5', '5', '5', '5', '5', '5', '5'};
 long minResult = NumberInput.parseLong(minDigits, 0, minDigits.length);
 assertEquals(Long.MIN_VALUE, minResult);

 char[] maxDigits = new char[]{'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
 long maxResult = NumberInput.parseLong(maxDigits, 0, maxDigits.length);
 assertEquals(Long.MAX_VALUE, maxResult);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInvalidInput() {
 char[] invalidDigits = new char[]{'1', 'a', '3', '4', '5'};
 NumberInput.parseLong(invalidDigits, 0, invalidDigits.length);
 }

 @Test
 public void testParseLongAsInt() {
 char[] longAsIntDigits = new char[]{'1', '2', '3', '4', '5', '0', '0', '0'};
 int result = NumberInput.parseInt(longAsIntDigits, 0, 8);
 assertEquals(123450000, result);
 }

 @Test
 public void testParseLongWithLongRange() {
 String longString = "12345678901234567890";
 boolean inRange = NumberInput.inLongRange(longString.toCharArray(), false);
 assertTrue(inRange);
 }

 @Test
 public void testParseLongWithOutOfRange() {
 String outOfRangeString = "123456789012345678901234567890";
 boolean inRange = NumberInput.inLongRange(outOfRangeString.toCharArray(), false);
 assertFalse(inRange);
 }

 @Test
 public void testParseLongWithNegative() {
 String negativeString = "-12345678901234567890";
 boolean inRange = NumberInput.inLongRange(negativeString.toCharArray(), true);
 assertTrue(inRange);
 }

 @Test
 public void testParseLongWithNegativeOutOfRange() {
 String negativeOutOfRangeString = "-1234567890123456789012345678901234567890";
 boolean inRange = NumberInput.inLongRange(negativeOutOfRangeString.toCharArray(), true);
 assertFalse(inRange);
 }
}