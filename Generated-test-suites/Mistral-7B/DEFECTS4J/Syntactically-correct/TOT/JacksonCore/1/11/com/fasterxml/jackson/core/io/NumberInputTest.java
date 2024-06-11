package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class NumberInputTest {

 @Test
 public void testParseInt_ValidInput() {
 char[] digits = new char[]{'1', '2', '3', '4', '5'};
 int num = NumberInput.parseInt(digits, 0, 5);
 assertEquals(12345, num);
 }

 @Test
 public void testParseInt_EmptyArray() {
 char[] digits = new char[0];
 int num = NumberInput.parseInt(digits, 0, 0);
 assertNull(num);
 }

 @Test
 public void testParseInt_NegativeInput() {
 char[] digits = new char[]{'-', '1', '2', '3'};
 int num = NumberInput.parseInt(digits, 0, 3);
 assertEquals(-123, num);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInt_InvalidInput() {
 char[] digits = new char[]{'a', '1', '2', '3'};
 NumberInput.parseInt(digits, 0, 3);
 }

 @Test
 public void testParseLong_ValidInput() {
 char[] digits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long num = NumberInput.parseLong(digits, 0, 10);
 assertEquals(1234567890L, num);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_InvalidInput() {
 char[] digits = new char[]{'a', '1', '2', '3'};
 NumberInput.parseLong(digits, 0, 3);
 }

 @Test
 public void testParseLong_MaxValue() {
 char[] digits = new char[]{'9', '2', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9'};
 long num = NumberInput.parseLong(digits, 0, 12);
 assertEquals(Long.MAX_VALUE, num);
 }

 @Test
 public void testParseLong_MinValue() {
 char[] digits = new char[]{'-', '9', '2', '2', '3', '4', '5', '6', '7', '8', '9', '9', '9'};
 long num = NumberInput.parseLong(digits, 0, 13);
 assertEquals(Long.MIN_VALUE, num);
 }

 @Test
 public void testParseLong_StringInput() {
 String str = "1234567890";
 long num = NumberInput.parseLong(str.toCharArray(), 0, str.length());
 assertEquals(1234567890L, num);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_NegativeStringInput() {
 String str = "-1234567890";
 NumberInput.parseLong(str.toCharArray(), 0, str.length());
 }

 @Test
 public void testParseLong_StringInputWithLeadingZeroes() {
 String str = "001234567890";
 long num = NumberInput.parseLong(str.toCharArray(), 0, str.length());
 assertEquals(1234567890L, num);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLong_InvalidStringInput() {
 String str = "a123";
 NumberInput.parseLong(str.toCharArray(), 0, str.length());
 }

 @Test
 public void testInLongRange() {
 char[] digits = new char[]{'1', '2', '3', '4', '5'};
 assertFalse(NumberInput.inLongRange(digits, 0, 5, false));
 assertFalse(NumberInput.inLongRange(digits, 0, 5, true));
 char[] digitsLong = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 assertFalse(NumberInput.inLongRange(digitsLong, 0, 9, false));
 assertTrue(NumberInput.inLongRange(digitsLong, 0, 9, true));
 char[] digitsLongMinus = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 assertFalse(NumberInput.inLongRange(digitsLongMinus, 0, 10, false));
 assertTrue(NumberInput.inLongRange(digitsLongMinus, 0, 10, true));
 }

 @Test
 public void testParseAsInt() {
 String input = "123";
 int num = NumberInput.parseAsInt(input, 0);
 assertEquals(123, num);
 }

 @Test
 public void testParseAsInt_EmptyInput() {
 String input = "";
 int num = NumberInput.parseAsInt(input, 0);
 assertEquals(0, num);
 }

 @Test
 public void testParseAsInt_LeadingZeroes() {
 String input = "00123";
 int num = NumberInput.parseAsInt(input, 0);
 assertEquals(123, num);
 }

 @Test
 public void testParseAsInt_NegativeInput() {
 String input = "-123";
 int num = NumberInput.parseAsInt(input, 0);
 assertEquals(-123, num);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsInt_InvalidInput() {
 String input = "a123";
 NumberInput.parseAsInt(input, 0);
 }

 @Test
 public void testParseAsLong() {
 String input = "1234567890";
 long num = NumberInput.parseAsLong(input, 0L);
 assertEquals(1234567890L, num);
 }

 @Test
 public void testParseAsLong_EmptyInput() {
 String input = "";
 long num = NumberInput.parseAsLong(input, 0L);
 assertEquals(0L, num);
 }

 @Test
 public void testParseAsLong_LeadingZeroes() {
 String input = "001234567890";
 long num = NumberInput.parseAsLong(input, 0L);
 assertEquals(1234567890L, num);
 }

 @Test
 public void testParseAsLong_NegativeInput() {
 String input = "-1234567890";
 long num = NumberInput.parseAsLong(input, 0L);
 assertEquals(-1234567890L, num);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsLong_InvalidInput() {
 String input = "a123";
 NumberInput.parseAsLong(input, 0L);
 }

 @Test
 public void testParseAsDouble() {
 String input = "123.45";
 double num = NumberInput.parseAsDouble(input, 0.0);
 assertEquals(123.45, num, 0.001);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsDouble_EmptyInput() {
 NumberInput.parseAsDouble("", 0.0);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsDouble_InvalidInput() {
 String input = "a123.45";
 NumberInput.parseAsDouble(input, 0.0);
 }

 @Test
 public void testParseBigDecimal() {
 String input = "123.45";
 BigDecimal num = NumberInput.parseBigDecimal(input);
 assertEquals(new BigDecimal("123.45"), num);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimal_EmptyInput() {
 NumberInput.parseBigDecimal("");
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimal_InvalidInput() {
 String input = "a123.45";
 NumberInput.parseBigDecimal(input);
 }

 @Test
 public void testParseBigDecimal_InvalidInputWithParseDouble() {
 String input = "a123.45";
 assertThrows(NumberFormatException.class, () -> NumberInput.parseAsDouble(input, 0.0));
 NumberInput.parseBigDecimal(input);
 }

 @Test
 public void testParseBigDecimal_NegativeInput() {
 String input = "-123.45";
 BigDecimal num = NumberInput.parseBigDecimal(input);
 assertEquals(new BigDecimal("-123.45"), num);
 }

 @Test
 public void testParseBigDecimal_LongInput() {
 char[] digits = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 BigDecimal num = NumberInput.parseBigDecimal(digits, 0, digits.length);
 assertEquals(new BigDecimal(new String(digits)), num);
 }
}