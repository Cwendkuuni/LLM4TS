package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

public class NumberInputTest {

 @Test
 public void testParseInt_Valid_SmallNumbers() {
 int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
 for (int number : numbers) {
 char[] digits = new char[10];
 int offset = 0;
 int length = String.valueOf(number).length();
 NumberInput.parseInt(digits, offset, length);
 assertEquals(number, NumberInput.parseInt(new String(digits, 0, length)));
 }
 }

 @Test
 public void testParseInt_Valid_LargeNumbers() {
 long lBillion = NumberInput.L_BILLION;
 char[] digits1 = new char[19];
 int offset1 = 0;
 long number1 = 12345678901234567L;
 NumberInput.parseInt(digits1, offset1, 19);
 long parsedNumber1 = NumberInput.parseLong(new String(digits1, 0, 19)) / lBillion;
 assertEquals(number1 / lBillion, parsedNumber1);

 char[] digits2 = new char[19];
 int offset2 = 0;
 long number2 = Long.MAX_VALUE;
 NumberInput.parseInt(digits2, offset2, 19);
 long parsedNumber2 = NumberInput.parseLong(new String(digits2, 0, 19));
 assertEquals(number2, parsedNumber2);
 }

 @Test
 public void testParseInt_Invalid_NegativeNumber_WithoutLeadingMinus() {
 char[] digits = new char[] {'4', '2', '2', '5', '0', '7', '3', '8', '5', '8'};
 assertFalse(NumberInput.inLongRange(digits, 10, digits.length, false));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(digits, 0, digits.length));
 }

 @Test
 public void testParseInt_Invalid_NegativeNumber_WithLeadingMinus() {
 char[] digits = new char[] {'-', '2', '2', '5', '0', '7', '3', '8', '5', '8'};
 assertFalse(NumberInput.inLongRange(digits, 10, digits.length, true));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(digits, 0, digits.length));
 }

 @Test
 public void testParseInt_Invalid_EmptyString() {
 assertThrows(NullPointerException.class, () -> NumberInput.parseInt(null, 0, 0));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(new char[0], 0, 0));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseInt("", 0, 0));
 }

 @Test
 public void testParseInt_Invalid_NonDigitCharacters() {
 char[] digits = new char[] {'1', 'a', 'b', 'c', '2', '3'};
 assertFalse(NumberInput.inLongRange(digits, 10, digits.length, false));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseInt(digits, 0, digits.length));
 }

 @Test
 public void testParseLong_Valid_SmallNumbers() {
 long[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
 for (long number : numbers) {
 char[] digits = new char[19];
 int offset = 0;
 NumberInput.parseLong(digits, 0, 1 + String.valueOf(number).length());
 long parsedNumber = NumberInput.parseLong(new String(digits));
 assertEquals(number, parsedNumber);
 }
 }

 @Test
 public void testParseLong_Valid_LargeNumbers() {
 char[] digits1 = new char[19];
 int offset1 = 0;
 long number1 = 1234567890123456789L;
 NumberInput.parseLong(digits1, offset1, 19);
 long parsedNumber1 = NumberInput.parseLong(new String(digits1));
 assertEquals(number1, parsedNumber1);

 char[] digits2 = new char[20];
 int offset2 = 0;
 long number2 = Long.MAX_VALUE;
 NumberInput.parseLong(digits2, offset2, 19);
 long parsedNumber2 = NumberInput.parseLong(new String(digits2));
 assertEquals(number2, parsedNumber2);
 }

 @Test
 public void testParseLong_Valid_FakeLong() {
 int length = 10;
 char[] digits = new char[length];
 for (int i = 0; i < length; i++) {
 digits[i] = (char) ('0' + (Math.random() * 10));
 }
 if (NumberInput.parseInt(new String(digits)) <= 0) {
 // if it's a negative number, just skip this test case
 return;
 }
 assertEquality(NumberInput.parseLong(digits), NumberInput.parseInt(digits));
 }

 @Test
 public void testParseLong_Invalid_NegativeNumber_WithoutLeadingMinus() {
 char[] digits = new char[19];
 int offset = 0;
 NumberInput.parseLong(digits, offset, 19);
 assertFalse(NumberInput.inLongRange(digits, 10, digits.length, false));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(digits, 0, digits.length));
 }

 @Test
 public void testParseLong_Invalid_NegativeNumber_WithLeadingMinus() {
 char[] digits = new char[19];
 int offset = 0;
 digits[0] = '-';
 NumberInput.parseLong(digits, offset, 18);
 assertFalse(NumberInput.inLongRange(digits, 10, digits.length, true));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(digits, 0, digits.length));
 }

 @Test
 public void testParseLong_Invalid_EmptyString() {
 assertThrows(NullPointerException.class, () -> NumberInput.parseLong(null));
 assertThrows(NullPointerException.class, () -> NumberInput.parseLong(new char[0]));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(""));
 }

 @Test
 public void testParseLong_Invalid_NonDigitCharacters() {
 char[] digits = new char[19];
 digits[0] = 'a';
 assertFalse(NumberInput.inLongRange(digits, 10, digits.length, false));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(digits, 0, digits.length));
 }

 @Test
 public void testParseLong_Invalid_Length() {
 char[] digits = new char[20];
 int offset = 0;
 assertFalse(NumberInput.inLongRange(digits, 10, digits.length, false));
 assertThrows(ArrayIndexOutOfBoundsException.class, () -> NumberInput.parseLong(digits, offset, digits.length));
 }

 @Test
 public void testParseLong_Invalid_Format() {
 char[] digits = new char[19];
 int offset = 0;
 digits[0] = '+';
 assertFalse(NumberInput.inLongRange(digits, 10, digits.length, false));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(digits, offset, digits.length));
 }

 @Test
 public void testParseLong_Invalid_InputRange() {
 char[] digits1 = new char[20];
 int offset1 = 0;
 // this should be out of range
 for (int i = 0; i < digits1.length; i++) {
 digits1[i] = (char) ('0' + (Math.random() * 10));
 }
 digits1[18] = '0';
 assertFalse(NumberInput.inLongRange(digits1, 10, digits1.length, false));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(digits1, offset1, digits1.length));

 char[] digits2 = new char[20];
 int offset2 = 0;
 // this should be out of range
 for (int i = 0; i < digits2.length; i++) {
 digits2[i] = (char) ('0' + (Math.random() * 10));
 }
 digits2[0] = '9';
 assertFalse(NumberInput.inLongRange(digits2, 10, digits2.length, false));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseLong(digits2, offset2, digits2.length));
 }

 @Test
 public void testParseBigDecimal() {
 String numStr = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901";
 BigDecimal expected = new BigDecimal(numStr);
 char[] digits = numStr.toCharArray();
 BigDecimal parsed = NumberInput.parseBigDecimal(digits);
 assertEquals(expected, parsed);
 }

 @Test
 public void testParseBigDecimal_Invalid_EmptyString() {
 assertThrows(NullPointerException.class, () -> NumberInput.parseBigDecimal(null));
 assertThrows(NullPointerException.class, () -> NumberInput.parseBigDecimal(new char[0]));
 assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(""));
 }

 @Test
 public void testParseBigDecimal_Invalid_Format() {
 char[] digits = new char[] {'+', '1'};
 assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(digits, 0, digits.length));

 char[] digits2 = new char[1];
 assertThrows(ArrayIndexOutOfBoundsException.class, () -> NumberInput.parseBigDecimal(digits2, 0, digits2.length));
 }

 @Test
 public void testParseBigDecimal_Invalid_Length() {
 char[] digits = new char[2];
 assertThrows(ArrayIndexOutOfBoundsException.class, () -> NumberInput.parseBigDecimal(digits, 0, digits.length));
 }

 @Test
 public void testParseBigDecimal_Invalid_InputRange() {
 char[] digits = new char[21];
 for (int i = 0; i < digits.length; i++) {
 digits[i] = (char) ('0' + (Math.random() * 10));
 }
 digits[20] = '0';
 assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal(digits, 0, digits.length));
 }

 private void assertEquality(long a, int b) {
 assertEquals((long) b, a);
 }

}