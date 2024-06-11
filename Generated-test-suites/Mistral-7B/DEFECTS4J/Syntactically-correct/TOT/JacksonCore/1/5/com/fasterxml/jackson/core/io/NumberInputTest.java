package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseInt_ValidInput() {
 char[] digitChars = {'1', '2', '3', '4', '5'};
 int len = 5;
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_NegativeInput() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5'};
 int len = 6;
 int expected = -12345;
 int actual = NumberInput.parseInt(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInt_InvalidInput() {
 char[] digitChars = {'1', 'a', '3', '4', '5'};
 int len = 5;
 NumberInput.parseInt(digitChars, 0, len);
 }

 @Test
 public void testParseInt_EmptyInput() {
 char[] digitChars = {};
 int len = 0;
 int expected = 0;
 int actual = NumberInput.parseInt(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_SingleDigitInput() {
 char[] digitChars = {'5'};
 int len = 1;
 int expected = 5;
 int actual = NumberInput.parseInt(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_ValidInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 int len = 10;
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_NegativeInput() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 int len = 11;
 long expected = -1234567890L;
 long actual = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_InvalidInput() {
 char[] digitChars = {'1', 'a', '3', '4', '5', '6', '7', '8', '9'};
 int len = 9;
 NumberInput.parseLong(digitChars, 0, len);
 }

 @Test
 public void testParseLong_LongMaxInput() {
 char[] digitChars = {'9', '2', '2', '3', '4', '5', '6', '7', '8', '9'};
 int len = 10;
 long expected = Long.MAX_VALUE;
 long actual = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_LongMinInput() {
 char[] digitChars = {'9', '2', '2', '3', '4', '5', '6', '7', '8', '9', '1'};
 int len = 11;
 long expected = Long.MIN_VALUE;
 long actual = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_LongMaxWithSuffixInput() {
 char[] digitChars = {'1', '8', '4', '4', '6', '7', '8', '9', '2', '3', '1', 'E', '5'};
 int len = 13;
 long expected = Long.parseLong("18446789231231E5");
 long actual = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_LongMinWithSuffixInput() {
 char[] digitChars = {'1', '8', '4', '4', '6', '7', '8', '9', '2', '3', '1', 'E', '-5'};
 int len = 13;
 long expected = Long.parseLong("18446789231231E-5");
 long actual = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_EmptyInput() {
 char[] digitChars = {};
 int len = 0;
 long expected = 0;
 long actual = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_SingleDigitInput() {
 char[] digitChars = {'5'};
 int len = 1;
 long expected = 5;
 long actual = NumberInput.parseLong(digitChars, 0, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_InputBeyondLongRange() {
 char[] digitChars = {'1', '3', '1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
 int len = 23;
 boolean actual = NumberInput.inLongRange(digitChars, 0, len, false);
 assertFalse(actual);
 }

 @Test
 public void testParseLong_InputWithinLongRange() {
 char[] digitChars = {'1', '3', '1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
 int len = 23;
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, len, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_InputWithMinusSign() {
 char[] digitChars = {'-', '1', '3', '1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
 int len = 24;
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, len, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_InputBeyondBigDecimalRange() {
 char[] digitChars = {'1', '3', '1', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
 int len = 25;
 boolean actual = NumberInput.inLongRange(digitChars, 0, len, false);
 assertFalse(actual);
 }

 @Test
 public void testParseBigDecimal_ValidInput() {
 String numStr = "1234567890.123456";
 BigDecimal expected = new BigDecimal("1234567890.123456");
 BigDecimal actual = NumberInput.parseBigDecimal(numStr);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_EmptyInput() {
 String numStr = "";
 BigDecimal expected = BigDecimal.ZERO;
 BigDecimal actual = NumberInput.parseBigDecimal(numStr);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimal_InvalidInput() {
 String numStr = "abc";
 NumberInput.parseBigDecimal(numStr);
 }

 @Test
 public void testParseBigDecimal_NegativeInput() {
 String numStr = "-1234567890.123456";
 BigDecimal expected = new BigDecimal("-1234567890.123456");
 BigDecimal actual = NumberInput.parseBigDecimal(numStr);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_LongMaxInput() {
 String numStr = Long.MAX_VALUE + "";
 BigDecimal expected = new BigDecimal(Long.MAX_VALUE);
 BigDecimal actual = NumberInput.parseBigDecimal(numStr);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_LongMinInput() {
 String numStr = Long.MIN_VALUE + "";
 BigDecimal expected = new BigDecimal(Long.MIN_VALUE);
 BigDecimal actual = NumberInput.parseBigDecimal(numStr);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_InputWithSuffix() {
 String numStr = "1234567890.123456E-05";
 BigDecimal expected = new BigDecimal("1234567890.123456E-05");
 BigDecimal actual = NumberInput.parseBigDecimal(numStr);
 assertEquals(expected, actual);
 }
}