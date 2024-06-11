package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithPositiveNumber() {
 char[] digitChars = {'1', '2', '3', '4', '5'};
 int result = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(12345, result);
 }

 @Test
 public void testParseIntWithNegativeNumber() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5'};
 int result = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals(-12345, result);
 }

 @Test
 public void testParseIntWithLengthOutOfRange() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7'};
 try {
 NumberInput.parseInt(digitChars, 0, 7);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseIntWithInvalidCharacters() {
 char[] digitChars = {'1', 'a', '3', '4', '5'};
 try {
 NumberInput.parseInt(digitChars, 0, 5);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseLongWithValidNumber() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
 long result = NumberInput.parseLong(digitChars, 0, 12);
 assertEquals(123456789101L, result);
 }

 @Test
 public void testParseLongWithNegativeNumber() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
 long result = NumberInput.parseLong(digitChars, 0, 13);
 assertEquals(-1234567891012L, result);
 }

 @Test
 public void testParseLongWithLengthOutOfRange() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
 try {
 NumberInput.parseLong(digitChars, 0, 14);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseLongWithInvalidCharacters() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a'};
 try {
 NumberInput.parseLong(digitChars, 0, 11);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseLongWithOutOfRangeNumber() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 boolean isInRange = NumberInput.inLongRange(digitChars, 0, 10, false);
 assertTrue(isInRange);

 digitChars[10] = '0';
 isInRange = NumberInput.inLongRange(digitChars, 0, 11, false);
 assertFalse(isInRange);
 }

 @Test
 public void testParseLongWithNegativeOutOfRangeNumber() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'} ;
 boolean isInRange = NumberInput.inLongRange(digitChars, 0, 10, true);
 assertTrue(isInRange);

 digitChars[10] = '0';
 isInRange = NumberInput.inLongRange(digitChars, 0, 11, true);
 assertFalse(isInRange);
 }

 @Test
 public void testParseStringWithValidNumber() {
 String numStr = "12345";
 int result = NumberInput.parseInt(numStr.toCharArray(), 0, numStr.length());
 assertEquals(12345, result);
 }

 @Test
 public void testParseStringWithNegativeNumber() {
 String numStr = "-12345";
 int result = NumberInput.parseInt(numStr.toCharArray(), 0, numStr.length());
 assertEquals(-12345, result);
 }

 @Test
 public void testParseStringWithLengthOutOfRange() {
 String numStr = "1234567891012";
 try {
 NumberInput.parseInt(numStr.toCharArray(), 0, numStr.length());
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseStringWithInvalidCharacters() {
 String numStr = "1a2345";
 try {
 NumberInput.parseInt(numStr.toCharArray(), 0, numStr.length());
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseStringWithOutOfRangeNumber() {
 String numStr = "1234567891012";
 boolean isInRange = NumberInput.inLongRange(numStr.toCharArray(), 0, numStr.length(), false);
 assertTrue(isInRange);

 numStr += "3";
 isInRange = NumberInput.inLongRange(numStr.toCharArray(), 0, numStr.length(), false);
 assertFalse(isInRange);
 }

 @Test
 public void testParseStringWithNegativeOutOfRangeNumber() {
 String numStr = "-1234567891012";
 boolean isInRange = NumberInput.inLongRange(numStr.toCharArray(), 0, numStr.length(), true);
 assertTrue(isInRange);

 numStr += "3";
 isInRange = NumberInput.inLongRange(numStr.toCharArray(), 0, numStr.length(), true);
 assertFalse(isInRange);
 }

 @Test
 public void testParseStringWithValidLongNumber() {
 String numStr = "1234567890123456789";
 long result = NumberInput.parseLong(numStr.toCharArray(), 0, numStr.length());
 assertEquals(1234567890123456789L, result);
 }

 @Test
 public void testParseStringWithOutOfRangeLongNumber() {
 String numStr = "12345678901234567890123456789";
 try {
 NumberInput.parseLong(numStr.toCharArray(), 0, numStr.length());
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseStringWithNegativeNumber() {
 String numStr = "-1234567890123456789";
 long result = NumberInput.parseLong(numStr.toCharArray(), 0, numStr.length());
 assertEquals(-1234567890123456789L, result);
 }

 @Test
 public void testParseStringWithInvalidCharacters() {
 String numStr = "1a2345";
 try {
 NumberInput.parseLong(numStr.toCharArray(), 0, numStr.length());
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseStringWithLongNumberBiggerThanLongMaxValue() {
 String numStr = "9223372036854775808";
 try {
 NumberInput.parseLong(numStr.toCharArray(), 0, numStr.length());
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseStringWithLongNumberBiggerThanLongMinValue() {
 String numStr = "-9223372036854775808";
 try {
 NumberInput.parseLong(numStr.toCharArray(), 0, numStr.length());
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseAsIntWithValidNumber() {
 String numStr = "12345";
 int result = NumberInput.parseAsInt(numStr, 0);
 assertEquals(12345, result);
 }

 @Test
 public void testParseAsIntWithNegativeNumber() {
 String numStr = "-12345";
 int result = NumberInput.parseAsInt(numStr, 0);
 assertEquals(-12345, result);
 }

 @Test
 public void testParseAsIntWithDefaultValue() {
 String numStr = "";
 int result = NumberInput.parseAsInt(numStr, 42);
 assertEquals(42, result);
 }

 @Test
 public void testParseAsIntWithInvalidCharacters() {
 String numStr = "1a2345";
 int result = NumberInput.parseAsInt(numStr, 0);
 assertEquals(0, result);
 }

 @Test
 public void testParseAsIntWithOutOfRangeNumber() {
 String numStr = "1234567890123456789";
 int result = NumberInput.parseAsInt(numStr, 0);
 assertEquals(Integer.MAX_VALUE, result);
 }

 @Test
 public void testParseAsIntWithNegativeOutOfRangeNumber() {
 String numStr = "-1234567890123456789";
 int result = NumberInput.parseAsInt(numStr, 0);
 assertEquals(Integer.MIN_VALUE, result);
 }

 @Test
 public void testParseAsLongWithValidNumber() {
 String numStr = "1234567890123456789";
 long result = NumberInput.parseAsLong(numStr, 0L);
 assertEquals(1234567890123456789L, result);
 }

 @Test
 public void testParseAsLongWithNegativeNumber() {
 String numStr = "-1234567890123456789";
 long result = NumberInput.parseAsLong(numStr, 0L);
 assertEquals(-1234567890123456789L, result);
 }

 @Test
 public void testParseAsLongWithDefaultValue() {
 String numStr = "";
 long result = NumberInput.parseAsLong(numStr, 42L);
 assertEquals(42L, result);
 }

 @Test
 public void testParseAsLongWithInvalidCharacters() {
 String numStr = "1a2345";
 long result = NumberInput.parseAsLong(numStr, 0L);
 assertEquals(0L, result);
 }

 @Test
 public void testParseAsLongWithOutOfRangeNumber() {
 String numStr = "9223372036854775808";
 long result = NumberInput.parseAsLong(numStr, 0L);
 assertEquals(Long.MAX_VALUE, result);
 }

 @Test
 public void testParseAsLongWithNegativeOutOfRangeNumber() {
 String numStr = "-9223372036854775808";
 long result = NumberInput.parseAsLong(numStr, 0L);
 assertEquals(Long.MIN_VALUE, result);
 }

 @Test
 public void testParseAsDoubleWithValidNumber() {
 String numStr = "123.45";
 double result = NumberInput.parseAsDouble(numStr, 0.0);
 assertEquals(123.45, result, 0.001);
 }

 @Test
 public void testParseAsDoubleWithNegativeNumber() {
 String numStr = "-123.45";
 double result = NumberInput.parseAsDouble(numStr, 0.0);
 assertEquals(-123.45, result, 0.001);
 }

 @Test
 public void testParseAsDoubleWithDefaultValue() {
 String numStr = "";
 double result = NumberInput.parseAsDouble(numStr, 42.0);
 assertEquals(42.0, result, 0.001);
 }

 @Test
 public void testParseAsDoubleWithInvalidCharacters() {
 String numStr = "1a23.45";
 double result = NumberInput.parseAsDouble(numStr, 0.0);
 assertEquals(0.0, result, 0.001);
 }

 @Test
 public void testParseAsDoubleWithOutOfRangeNumber() {
 String numStr = "1.234567890123456789e200";
 double result = NumberInput.parseAsDouble(numStr, 0.0);
 assertEquals(Double.POSITIVE_INFINITY, result);
 }

 @Test
 public void testParseAsDoubleWithNegativeOutOfRangeNumber() {
 String numStr = "-1.234567890123456789e200";
 double result = NumberInput.parseAsDouble(numStr, 0.0);
 assertEquals(Double.NEGATIVE_INFINITY, result);
 }

 @Test
 public void testParseBigDecimalWithValidNumber() {
 String numStr = "123.45";
 BigDecimal result = NumberInput.parseBigDecimal(numStr);
 assertEquals(new BigDecimal("123.45"), result);
 }

 @Test
 public void testParseBigDecimalWithNegativeNumber() {
 String numStr = "-123.45";
 BigDecimal result = NumberInput.parseBigDecimal(numStr);
 assertEquals(new BigDecimal("-123.45"), result);
 }

 @Test
 public void testParseBigDecimalWithInvalidCharacters() {
 String numStr = "1a23.45";
 try {
 NumberInput.parseBigDecimal(numStr);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseBigDecimalWithOutOfRangeNumber() {
 String numStr = "1.234567890123456789e200";
 try {
 NumberInput.parseBigDecimal(numStr);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseBigDecimalWithNegativeOutOfRangeNumber() {
 String numStr = "-1.234567890123456789e200";
 try {
 NumberInput.parseBigDecimal(numStr);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }
}