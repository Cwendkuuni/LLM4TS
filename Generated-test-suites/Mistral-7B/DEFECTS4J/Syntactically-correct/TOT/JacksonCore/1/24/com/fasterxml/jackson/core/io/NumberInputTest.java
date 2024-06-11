package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseInt_PositiveSmallNumbers() {
 char[] digits = new char[]{'1', '2', '3', '4', '5'};
 int expected = 12345;
 int result = NumberInput.parseInt(digits, 0, 5);
 assertEquals(expected, result);
 }

 @Test
 public void testParseInt_NegativeSmallNumbers() {
 char[] digits = new char[]{'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int result = NumberInput.parseInt(digits, 0, 5);
 assertEquals(expected, result);
 }

 @Test
 public void testParseInt_EdgeCases() {
 // Test minimum and maximum int values
 char[] minDigits = new char[]{'-', '2', '147', '483', '647'};
 int minInt = NumberInput.parseInt(minDigits, 0, 5);
 assertEquals(Integer.MIN_VALUE, minInt);

 char[] maxDigits = new char[]{'2', '147', '483', '647', '9'};
 int maxInt = NumberInput.parseInt(maxDigits, 0, 5);
 assertEquals(Integer.MAX_VALUE, maxInt);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInt_InvalidInput() {
 char[] invalidInput = new char[]{'A', '1', '2'};
 NumberInput.parseInt(invalidInput, 0, 2);
 }

 @Test
 public void testParseLong_PositiveSmallNumbers() {
 char[] digits = new char[]{'1', '2', '3', '4', '5', '6'};
 long expected = 123456;
 long result = NumberInput.parseLong(digits, 0, 6);
 assertEquals(expected, result);
 }

 @Test
 public void testParseLong_NegativeSmallNumbers() {
 char[] digits = new char[]{'-', '1', '2', '3', '4', '5', '6'};
 long expected = -123456;
 long result = NumberInput.parseLong(digits, 0, 7);
 assertEquals(expected, result);
 }

 @Test
 public void testParseLong_EdgeCases() {
 // Test minimum and maximum long values
 char[] minDigits = new char[]{'-', '2', '147', '483', '647', '9', '8', '7'};
 long minLong = NumberInput.parseLong(minDigits, 0, 8);
 assertEquals(Long.MIN_VALUE, minLong);

 char[] maxDigits = new char[]{'2', '147', '483', '647', '9', '8', '7', '9'};
 long maxLong = NumberInput.parseLong(maxDigits, 0, 8);
 assertEquals(Long.MAX_VALUE, maxLong);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLong_InvalidInput() {
 char[] invalidInput = new char[]{'A', '1', '2'};
 NumberInput.parseLong(invalidInput, 0, 2);
 }

 @Test
 public void testParseLong_FakeLongs() {
 String smallFakeLong = "123456789";
 long expected = (long) NumberInput.parseInt(smallFakeLong.toCharArray(), 0, smallFakeLong.length());
 long result = NumberInput.parseLong(smallFakeLong);
 assertEquals(expected, result);
 }

 @Test
 public void testParseLong_GivenLongRange() {
 String longNumberStr1 = "1234567890123456789";
 assertTrue(NumberInput.inLongRange(longNumberStr1.toCharArray(), false));

 String longNumberStr2 = "-1234567890123456789";
 assertTrue(NumberInput.inLongRange(longNumberStr2.toCharArray(), true));
 }

 @Test
 public void testParseLong_OutOfLongRange() {
 String longNumberStr1 = "12345678901234567890";
 assertFalse(NumberInput.inLongRange(longNumberStr1.toCharArray(), false));

 String longNumberStr2 = "-123456789012345678901";
 assertFalse(NumberInput.inLongRange(longNumberStr2.toCharArray(), true));
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_NonNumericInput() {
 String nonNumericInput = "abcdef";
 NumberInput.parseLong(nonNumericInput.toCharArray(), 0, nonNumericInput.length());
 }

}