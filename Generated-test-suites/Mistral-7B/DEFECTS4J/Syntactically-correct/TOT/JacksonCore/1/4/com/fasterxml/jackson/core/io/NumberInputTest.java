package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseInt_PositiveSmallNumber() {
 char[] digitChars = {'1', '2', '3'};
 int offset = 0;
 int len = 3;
 int expected = 123;
 int actual = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_NegativeSmallNumber() {
 char[] digitChars = {'-', '1', '2', '3'};
 int offset = 0;
 int len = 4;
 int expected = -123;
 int actual = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_Overflow() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 int offset = 0;
 int len = 10;
 int expected = Integer.MAX_VALUE;
 int actual = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInt_InvalidCharacter() {
 char[] digitChars = {'1', '2', '3', 'x'};
 int offset = 0;
 int len = 4;
 NumberInput.parseInt(digitChars, offset, len);
 fail("Expected NumberFormatException but did not get it.");
 }

 @Test
 public void testParseInt_EmptyString() {
 String str = "";
 int expected = 0;
 int actual = NumberInput.parseInt(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_LongString() {
 String str = "12345678901234567890";
 int expected = 12345678901234567890;
 int actual = NumberInput.parseInt(str);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInt_NegativeLongString() {
 String str = "-12345678901234567890";
 NumberInput.parseInt(str);
 fail("Expected NumberFormatException but did not get it.");
 }

 @Test
 public void testParseLong_SmallPositiveNumber() {
 char[] digitChars = {'1', '2', '3'};
 int offset = 0;
 int len = 3;
 long expected = 123;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_SmallNegativeNumber() {
 char[] digitChars = {'-', '1', '2', '3'};
 int offset = 0;
 int len = 4;
 long expected = -123;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_LargeNumber() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
 int offset = 0;
 int len = 13;
 long expected = 12345678912345678912L;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_InvalidCharacter() {
 char[] digitChars = {'1', '2', '3', 'x'};
 int offset = 0;
 int len = 4;
 NumberInput.parseLong(digitChars, offset, len);
 fail("Expected NumberFormatException but did not get it.");
 }

 @Test
 public void testParseLong_EmptyString() {
 String str = "";
 long expected = 0;
 long actual = NumberInput.parseLong(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_LongString() {
 String str = "12345678901234567890";
 long expected = 12345678901234567890L;
 long actual = NumberInput.parseLong(str);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_NegativeLongString() {
 String str = "-12345678901234567890";
 NumberInput.parseLong(str);
 fail("Expected NumberFormatException but did not get it.");
 }

 @Test
 public void testParseLong_OutOfRange() {
 String str = Long.MAX_VALUE + "1";
 boolean actual = NumberInput.inLongRange(str.toCharArray(), false);
 assertFalse(actual);
 }

 @Test
 public void testParseLong_InRange() {
 String str = Long.MAX_VALUE.toString();
 boolean actual = NumberInput.inLongRange(str.toCharArray(), false);
 assertTrue(actual);
 }

 @Test
 public void testParseLong_NegativeInRange() {
 String str = Long.MIN_VALUE.toString();
 boolean actual = NumberInput.inLongRange(str.toCharArray(), true);
 assertTrue(actual);
 }

 @Test
 public void testParseLong_NegativeOutOfRange() {
 String str = Long.MIN_VALUE + "1";
 boolean actual = NumberInput.inLongRange(str.toCharArray(), true);
 assertFalse(actual);
 }

 @Test
 public void testParseAsInt_ValidInput() {
 String input = "123";
 int expected = 123;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsInt_EmptyString() {
 String input = "";
 int expected = 0;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsInt_InvalidCharacter() {
 String input = "123x";
 int expected = 0;
 int actual = NumberInput.parseAsInt(input, 0);
 fail("Expected NumberFormatException but did not get it.");
 }

 @Test
 public void testParseAsInt_NegativeNumber() {
 String input = "-123";
 int expected = -123;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_ValidInput() {
 String input = "12345678901234567890";
 long expected = 12345678901234567890L;
 long actual = NumberInput.parseAsLong(input, 0L);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsLong_InvalidCharacter() {
 String input = "123x";
 long expected = 0L;
 long actual = NumberInput.parseAsLong(input, 0L);
 fail("Expected NumberFormatException but did not get it.");
 }

 @Test
 public void testParseAsLong_NegativeNumber() {
 String input = "-123";
 long expected = -123;
 long actual = NumberInput.parseAsLong(input, 0L);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsDouble_ValidInput() {
 String input = "123.45";
 double expected = 123.45;
 double actual = NumberInput.parseAsDouble(input, 0.0);
 assertEquals(expected, actual, 0.001);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsDouble_InvalidInput() {
 String input = "123x";
 double expected = 0.0;
 double actual = NumberInput.parseAsDouble(input, 0.0);
 fail("Expected NumberFormatException but did not get it.");
 }

 @Test
 public void testParseBigDecimal_ValidInput() {
 String input = "123.45";
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_EmptyString() {
 String input = "";
 BigDecimal expected = BigDecimal.ZERO;
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimal_InvalidCharacter() {
 String input = "123x";
 BigDecimal expected = BigDecimal.ZERO;
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 fail("Expected NumberFormatException but did not get it.");
 }

 @Test
 public void testParseBigDecimal_NegativeNumber() {
 String input = "-123.45";
 BigDecimal expected = new BigDecimal("-123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_BigNumber() {
 char[] buffer = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 int offset = 0;
 int len = 19;
 BigDecimal expected = new BigDecimal("1234567890.123456789");
 BigDecimal actual = NumberInput.parseBigDecimal(buffer, offset, len);
 assertEquals(expected, actual);
 }
}