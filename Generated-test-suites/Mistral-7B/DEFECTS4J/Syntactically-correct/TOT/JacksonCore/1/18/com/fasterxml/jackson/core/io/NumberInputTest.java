package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseInt_SmallPositive() {
 char[] digitChars = {'1', '2', '3'};
 int actual = NumberInput.parseInt(digitChars, 0, 3);
 int expected = 123;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_SmallNegative() {
 char[] digitChars = {'-', '1', '2', '3'};
 int actual = NumberInput.parseInt(digitChars, 0, 4);
 int expected = -123;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_LongerPositive() {
 char[] digitChars = {'1', '2', '3', '4', '5'};
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 int expected = 12345;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_LongerNegative() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5'};
 int actual = NumberInput.parseInt(digitChars, 0, 6);
 int expected = -12345;
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInt_Invalid() {
 char[] digitChars = {'1', '2', '0', 'x'};
 int actual = NumberInput.parseInt(digitChars, 0, 4);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testParseInt_ArrayIndexOutOfBounds() {
 char[] digitChars = {'1', '2', '3'};
 int actual = NumberInput.parseInt(digitChars, 4, 3);
 fail("Expected ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testParseLong_SmallPositive() {
 char[] digitChars = {'1', '2', '3'};
 long actual = NumberInput.parseLong(digitChars, 0, 3);
 long expected = 123;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_SmallNegative() {
 char[] digitChars = {'-', '1', '2', '3'};
 long actual = NumberInput.parseLong(digitChars, 0, 4);
 long expected = -123;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_LongerPositive() {
 char[] digitChars = {'1', '2', '3', '4', '5'};
 long actual = NumberInput.parseLong(digitChars, 0, 5);
 long expected = 12345;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_LongerNegative() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5'};
 long actual = NumberInput.parseLong(digitChars, 0, 6);
 long expected = -12345;
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLong_Invalid() {
 char[] digitChars = {'1', '2', '0', 'x'};
 long actual = NumberInput.parseLong(digitChars, 0, 4);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testParseLong_ArrayIndexOutOfBounds() {
 char[] digitChars = {'1', '2', '3'};
 long actual = NumberInput.parseLong(digitChars, 4, 3);
 fail("Expected ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testParseLong_LargeValue() {
 char[] digitChars = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
 long actual = NumberInput.parseLong(digitChars, 0, 10);
 long expected = 9223372036854775807L;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_LargeNegativeValue() {
 char[] digitChars = {'-', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
 long actual = NumberInput.parseLong(digitChars, 0, 11);
 long expected = -9223372036854775808L;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_CheckInLongRange() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7'};
 boolean actual = NumberInput.inLongRange(digitChars, 0, 6, false);
 assertTrue(actual);

 char[] digitCharsNegative = {'-', '1', '2', '3', '4', '5', '6'};
 boolean actualNegative = NumberInput.inLongRange(digitCharsNegative, 0, 6, true);
 assertTrue(actualNegative);
 }

 @Test
 public void testParseLong_OutOfLongRange() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 boolean actual = NumberInput.inLongRange(digitChars, 0, 9, false);
 assertFalse(actual);

 char[] digitCharsNegative = {"-", '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 boolean actualNegative = NumberInput.inLongRange(digitCharsNegative, 0, 10, true);
 assertFalse(actualNegative);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_InvalidLong() {
 char[] digitChars = {'1', '2', '0', 'x'};
 long actual = NumberInput.parseLong(digitChars, 0, 3);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testParseAsInt_SmallPositive() {
 String input = "123";
 int actual = NumberInput.parseAsInt(input, 0);
 int expected = 123;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsInt_SmallNegative() {
 String input = "-123";
 int actual = NumberInput.parseAsInt(input, 0);
 int expected = -123;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsInt_LongerPositive() {
 String input = "12345";
 int actual = NumberInput.parseAsInt(input, 0);
 int expected = 12345;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsInt_LongerNegative() {
 String input = "-12345";
 int actual = NumberInput.parseAsInt(input, 0);
 int expected = -12345;
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsInt_Invalid() {
 String input = "123a";
 int actual = NumberInput.parseAsInt(input, 0);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testParseAsLong_SmallPositive() {
 String input = "123";
 long actual = NumberInput.parseAsLong(input, 0);
 long expected = 123;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_SmallNegative() {
 String input = "-123";
 long actual = NumberInput.parseAsLong(input, 0);
 long expected = -123;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_LongerPositive() {
 String input = "12345";
 long actual = NumberInput.parseAsLong(input, 0);
 long expected = 12345;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_LongerNegative() {
 String input = "-12345";
 long actual = NumberInput.parseAsLong(input, 0);
 long expected = -12345;
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsLong_Invalid() {
 String input = "123a";
 long actual = NumberInput.parseAsLong(input, 0);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testParseAsDouble_SmallPositive() {
 String input = "123.45";
 double actual = NumberInput.parseAsDouble(input, 0);
 double expected = 123.45;
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testParseAsDouble_SmallNegative() {
 String input = "-123.45";
 double actual = NumberInput.parseAsDouble(input, 0);
 double expected = -123.45;
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testParseAsDouble_LongerPositive() {
 String input = "12345.6789";
 double actual = NumberInput.parseAsDouble(input, 0);
 double expected = 12345.6789;
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testParseAsDouble_LongerNegative() {
 String input = "-12345.6789";
 double actual = NumberInput.parseAsDouble(input, 0);
 double expected = -12345.6789;
 assertEquals(expected, actual, 0.001);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsDouble_Invalid() {
 String input = "123a.45";
 double actual = NumberInput.parseAsDouble(input, 0);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testParseBigDecimal() {
 String input = "123.45";
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 BigDecimal expected = new BigDecimal("123.45");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_LargeValue() {
 char[] buffer = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
 BigDecimal actual = NumberInput.parseBigDecimal(buffer);
 BigDecimal expected = new BigDecimal("9999999999999");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_LargeNegativeValue() {
 char[] buffer = {'-', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
 BigDecimal actual = NumberInput.parseBigDecimal(buffer);
 BigDecimal expected = new BigDecimal("-9999999999999");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_Invalid() {
 char[] buffer = {'1', '2', '0', 'x'};
 try {
 NumberInput.parseBigDecimal(buffer);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimal_ArrayIndexOutOfBounds() {
 char[] buffer = {'1', '2', '3'};
 try {
 NumberInput.parseBigDecimal(buffer, 4, 3);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // expected
 }
 }
}