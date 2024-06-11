package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = new char[] {'1', '2', '3'};
 int offset = 0;
 int len = 3;
 int expected = 123;
 int actual = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithValidInputString() {
 String str = "123";
 int actual = NumberInput.parseInt(str);
 int expected = 123;
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithValidInputLongerThanExpected() {
 char[] digitChars = new char[] {'1', '2', '3', '4', '5'};
 int offset = 0;
 int len = 5;
 int expected = 123;
 int actual = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithInvalidInput() {
 char[] digitChars = new char[] {'1', '2', 'k'};
 int offset = 0;
 int len = 3;
 NumberInput.parseInt(digitChars, offset, len);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithNegativeInput() {
 char[] digitChars = new char[] {'-', '1', '2', '3'};
 int offset = 0;
 int len = 4;
 NumberInput.parseInt(digitChars, offset, len);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithInputLongerThanExpectedForNegative() {
 char[] digitChars = new char[] {'-', '1', '2', '3', '4'};
 int offset = 0;
 int len = 5;
 NumberInput.parseInt(digitChars, offset, len);
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6'};
 int offset = 0;
 int len = 6;
 long expected = 123456;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithValidInputString() {
 String str = "1234567890123";
 long actual = NumberInput.parseLong(str);
 long expected = 1234567890123L;
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInvalidInput() {
 char[] digitChars = new char[] {'1', '2', 'k'};
 int offset = 0;
 int len = 3;
 NumberInput.parseLong(digitChars, offset, len);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithNegativeInput() {
 char[] digitChars = new char[] {'-', '1', '2', '3', '4', '5'};
 int offset = 0;
 int len = 6;
 NumberInput.parseLong(digitChars, offset, len);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInputLongerThanExpected() {
 char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6', '7'};
 int offset = 0;
 int len = 7;
 NumberInput.parseLong(digitChars, offset, len);
 }

 @Test
 public void testParseLongGivenStringAsInputWhichIsLongerThanLongTypeCanHandle() {
 String str = "9223372036854775807";
 boolean actual = NumberInput.inLongRange(str, false);
 assertTrue(actual);
 }

 @Test
 public void testParseLongGivenStringAsInputWhichIsShorterThanLongTypeCanHandle() {
 String str = "12345";
 boolean actual = NumberInput.inLongRange(str, false);
 assertTrue(actual);
 }

 @Test
 public void testParseLongGivenStringAsInputWhichIsNegative() {
 String str = "-12345";
 boolean actual = NumberInput.inLongRange(str, false);
 assertTrue(actual);
 }

 @Test
 public void testParseLongGivenStringAsInputWhichIsLongerThanLongTypeCanHandleNegative() {
 String str = "-9223372036854775808";
 boolean actual = NumberInput.inLongRange(str, false);
 assertFalse(actual);
 }

 @Test
 public void testParseLongWithValidInputForNegative() {
 char[] digitChars = new char[] {'-', '1', '2', '3', '4', '5'};
 int offset = 0;
 int len = 6;
 long expected = -123456;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithValidInputForNegativeLongerThanExpected() {
 char[] digitChars = new char[] {'-', '1', '2', '3', '4', '5', '6'};
 int offset = 0;
 int len = 7;
 long expected = -1234567;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithInvalidInputForNegative() {
 char[] digitChars = new char[] {'-', '1', '2', 'k'};
 int offset = 0;
 int len = 3;
 NumberInput.parseLong(digitChars, offset, len);
 }

 @Test
 public void testParseLongWithNegativeInputLongerThanExpected() {
 char[] digitChars = new char[] {'-', '1', '2', '3', '4', '5', '6'};
 int offset = 0;
 int len = 7;
 NumberInput.parseLong(digitChars, offset, len);
 }

 @Test
 public void testParseLongWithValidInputForNegativeLongerThanLongTypeCanHandle() {
 char[] digitChars = new char[] {'-', '1', '2', '3', '4', '5', '6', '7'};
 int offset = 0;
 int len = 8;
 long expected = -9223372036854775808L;
 long actual = NumberInput.parseLong(digitChars, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithNegativeInputLongerThanLongTypeCanHandle() {
 char[] digitChars = new char[] {'-', '1', '2', '3', '4', '5', '6', '7', '8'};
 int offset = 0;
 int len = 9;
 NumberInput.parseLong(digitChars, offset, len);
 }

 @Test
 public void testParseBigDecimalWithValidInput() {
 String numStr = "123.45";
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(numStr);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithValidInputCharArray() {
 char[] buffer = new char[] {'1', '2', '.', '3', '4', '5'};
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(buffer);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithValidInputCharArrayLongerThanExpected() {
 char[] buffer = new char[] {'1', '2', '.', '3', '4', '5', '6'};
 BigDecimal expected = new BigDecimal("123.456");
 BigDecimal actual = NumberInput.parseBigDecimal(buffer, 0, 7);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithValidInputCharArrayNegative() {
 char[] buffer = new char[] {'-', '1', '2', '.', '3', '4', '5'};
 BigDecimal expected = new BigDecimal("-123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(buffer);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithValidInputCharArrayNegativeLongerThanExpected() {
 char[] buffer = new char[] {'-', '1', '2', '.', '3', '4', '5', '6'};
 BigDecimal expected = new BigDecimal("-123.456");
 BigDecimal actual = NumberInput.parseBigDecimal(buffer, 0, 8);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalWithInvalidInput() {
 char[] digitChars = new char[] {'1', '2', 'k'};
 int offset = 0;
 int len = 3;
 NumberInput.parseBigDecimal(digitChars, offset, len);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalWithNegativeInput() {
 char[] digitChars = new char[] {'-', '1', '2', '3'};
 int offset = 0;
 int len = 4;
 NumberInput.parseBigDecimal(digitChars, offset, len);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalWithInputLongerThanExpected() {
 char[] digitChars = new char[] {'1', '2', '3', '4', '5', '6', 'k'};
 int offset = 0;
 int len = 8;
 NumberInput.parseBigDecimal(digitChars, offset, len);
 }

 @Test
 public void testParseBigDecimalWithInvalidInputNegative() {
 char[] digitChars = new char[] {'-', '1', '2', 'k'};
 int offset = 0;
 int len = 4;
 try {
 NumberInput.parseBigDecimal(digitChars, offset, len);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseBigDecimalWithNegativeInputLongerThanExpected() {
 char[] digitChars = new char[] {'-', '1', '2', '3', '4', '5', '6', 'k'};
 int offset = 0;
 int len = 8;
 try {
 NumberInput.parseBigDecimal(digitChars, offset, len);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testParseBigDecimalWithInputLongerThanExpectedNegative() {
 char[] digitChars = new char[] {'-', '1', '2', '3', '4', '5', '6', '7', 'k'};
 int offset = 0;
 int len = 10;
 try {
 NumberInput.parseBigDecimal(digitChars, offset, len);
 fail();
 } catch (NumberFormatException e) {
 // expected
 }
 }
}