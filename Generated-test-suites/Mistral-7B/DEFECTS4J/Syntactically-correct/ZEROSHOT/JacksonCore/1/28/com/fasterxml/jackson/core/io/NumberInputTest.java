package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NumberInputTest {

 @Test
 public void testParseInt() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int offset = 0, len = 5;
 int num = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(12345, num);
 offset += len;
 // test with negative numbers and check that length is correct
 digitChars[offset++] = '-';
 len = 5;
 num = NumberInput.parseInt(digitChars, offset, len);
 assertEquals(-12345, num);
 offset += len;
 // test with invalid input
 digitChars[offset++] = 'a';
 len = 4;
 num = NumberInput.parseInt(digitChars, offset, len);
 assertFalse(NumberInput.inLongRange(digitChars, offset-len-1, len, false));
 assertNull(num);
 offset += len;
 }

 @Test
 public void testParseIntFromString() {
 String str = "12345";
 int num = NumberInput.parseInt(str);
 assertEquals(12345, num);
 str = "-12345";
 num = NumberInput.parseInt(str);
 assertEquals(-12345, num);
 str = "abcd";
 num = NumberInput.parseInt(str);
 assertFalse(NumberInput.inLongRange(str.toCharArray(), 0, str.length(), false));
 assertNull(num);
 }

 @Test
 public void testParseLong() {
 char[] digitChars = new char[]{'1', '2', '3'};
 int offset = 0, len1 = 2, len2 = 3;
 long num = NumberInput.parseLong(digitChars, offset, len1) * NumberInput.L_BILLION
 + NumberInput.parseLong(digitChars, offset+len1, len2);
 assertEquals(123000000000L, num);
 // test with negative numbers
 digitChars[offset++] = '-';
 len1 = 2;
 num = NumberInput.parseLong(digitChars, offset, len1) * NumberInput.L_BILLION
 + NumberInput.parseLong(digitChars, offset+len1, len2);
 assertEquals(-123000000000L, num);
 // test with invalid input
 digitChars[offset++] = 'a';
 len1 = 2;
 num = NumberInput.parseLong(digitChars, offset, len1) * NumberInput.L_BILLION
 + NumberInput.parseLong(digitChars, offset+len1, len2);
 assertFalse(NumberInput.inLongRange(digitChars, offset-len1-len2-2, len1+len2, false));
 assertNull(num);
 }

 @Test
 public void testParseLongFromString() {
 String str = "123000000000";
 long num = NumberInput.parseLong(str);
 assertEquals(123000000000L, num);
 str = "-123000000000";
 num = NumberInput.parseLong(str);
 assertEquals(-123000000000L, num);
 str = "abcd";
 num = NumberInput.parseLong(str);
 assertFalse(NumberInput.inLongRange(str.toCharArray(), 0, str.length(), false));
 assertNull(num);
 }

 @Test
 public void testParseAsInt() {
 String input = "12345";
 int defaultValue = 0;
 int num = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(12345, num);
 input = "-12345";
 num = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(-12345, num);
 input = "abcd";
 num = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(0, num);
 input = "1.23";
 num = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(0, num);
 input = "";
 num = NumberInput.parseAsInt(input, defaultValue);
 assertEquals(0, num);
 }

 @Test
 public void testParseAsLong() {
 String input = "123000000000";
 long defaultValue = 0L;
 long num = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(123000000000L, num);
 input = "-123000000000";
 num = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(-123000000000L, num);
 input = "abcd";
 num = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(0L, num);
 input = "1.23";
 num = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(0L, num);
 input = "";
 num = NumberInput.parseAsLong(input, defaultValue);
 assertEquals(0L, num);
 }

 @Test
 public void testParseAsDouble() {
 String input = "123.45";
 double defaultValue = 0.0;
 double num = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals(123.45, num, 0.001);
 input = "-123.45";
 num = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals(-123.45, num, 0.001);
 input = "abcd";
 num = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals(defaultValue, num, 0.0);
 input = "1.2e3";
 num = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals(1200.0, num, 0.01);
 input = "";
 num = NumberInput.parseAsDouble(input, defaultValue);
 assertEquals(defaultValue, num, 0.0);
 }

 @Test
 public void testParseBigDecimal() {
 String input = "123.45";
 BigDecimal defaultValue = BigDecimal.ZERO;
 BigDecimal num = NumberInput.parseBigDecimal(input);
 assertEquals(new BigDecimal("123.45"), num);
 input = "-123.45";
 num = NumberInput.parseBigDecimal(input);
 assertEquals(new BigDecimal("-123.45"), num);
 input = "abcd";
 num = NumberInput.parseBigDecimal(input);
 assertNotNull(num);
 input = "1.2e3";
 num = NumberInput.parseBigDecimal(input);
 assertEquals(new BigDecimal("1200.0"), num);
 input = "";
 num = NumberInput.parseBigDecimal(input);
 assertEquals(BigDecimal.ZERO, num);
 }

 @Test
 public void testParseBigDecimalCharArray() {
 char[] buffer = new char[]{'1', '2', '3', '.', '4', '5'};
 int offset = 0, len = 6;
 BigDecimal num = NumberInput.parseBigDecimal(buffer, offset, len);
 assertEquals(new BigDecimal("1.2345"), num);
 // test with negative numbers
 buffer[offset++] = '-';
 num = NumberInput.parseBigDecimal(buffer, offset, len);
 assertEquals(new BigDecimal("-1.2345"), num);
 offset += len;
 // test with invalid input
 buffer[offset++] = 'a';
 len = 5;
 num = NumberInput.parseBigDecimal(buffer, offset, len);
 assertNotNull(num);
 offset += len;
 }

 @Test
 public void testParseBigDecimalCharArrayWithOffsetAndLength() {
 char[] buffer = new char[]{'1', '2', '3', '.', '4', '5'};
 int offset = 2, len = 4;
 BigDecimal num = NumberInput.parseBigDecimal(buffer, offset, len);
 assertEquals(new BigDecimal("2.345"), num);
 }
}