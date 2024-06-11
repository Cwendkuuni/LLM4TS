package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseInt_ValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_ValidInput_NegativeSign() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int actual = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseInt_InvalidInput_Length() {
 char[] digitChars = new char[]{'1', '2', '3'};
 try {
 NumberInput.parseInt(digitChars, 0, 3);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseInt_InvalidInput_Character() {
 char[] digitChars = new char[]{'1', 'a', '3', '4', '5'};
 try {
 NumberInput.parseInt(digitChars, 0, 5);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseLong_ValidInput() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(digitChars, 0, 10);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_ValidInput_NegativeSign() {
 char[] digitChars = new char[]{'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 long expected = -1234567890L;
 long actual = NumberInput.parseLong(digitChars, 0, 11);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_ValidInput_LessThanMaxLength() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7'};
 long expected = 1234567L;
 long actual = NumberInput.parseLong(digitChars, 0, 7);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong_InvalidInput_Length() {
 char[] digitChars = new char[]{'1', '2', '3'};
 try {
 NumberInput.parseLong(digitChars, 0, 3);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseLong_InvalidInput_Character() {
 char[] digitChars = new char[]{'1', '2', '3', 'a'};
 try {
 NumberInput.parseLong(digitChars, 0, 4);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseLong_OutOfRange() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '1', '0', '1'};
 boolean expected = true;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 12, false);
 assertTrue(expected == actual);
 }

 @Test
 public void testParseLong_OutOfRange_Negative() {
 char[] digitChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '1', '0', '1', '1'};
 boolean expected = false;
 boolean actual = NumberInput.inLongRange(digitChars, 0, 13, true);
 assertFalse(expected == actual);
 }

 @Test
 public void testParseAsInt_ValidInput() {
 String input = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsInt_ValidInput_NegativeSign() {
 String input = "-12345";
 int expected = -12345;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsInt_InvalidInput() {
 String input = "123ab45";
 int expected = 0;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_ValidInput() {
 String input = "1234567890";
 long expected = 1234567890L;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_ValidInput_NegativeSign() {
 String input = "-1234567890";
 long expected = -1234567890L;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLong_InvalidInput() {
 String input = "123ab45";
 long expected = 0;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsDouble_ValidInput() {
 String input = "123.45";
 double expected = 123.45;
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testParseAsDouble_InvalidInput() {
 String input = "123ab45";
 double expected = 0;
 double actual = NumberInput.parseAsDouble(input, 0);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testParseBigDecimal_ValidInput() {
 String input = "123.45";
 BigDecimal expected = new BigDecimal("123.45");
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_InvalidInput() {
 String input = "123ab45";
 BigDecimal expected = BigDecimal.ZERO;
 BigDecimal actual = NumberInput.parseBigDecimal(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimal_ValidInput_Big() {
 char[] buffer = new char[20];
 for (int i = 0; i < 20; i++) {
 buffer[i] = (char) (i + '0');
 }
 BigDecimal expected = new BigDecimal(new String(buffer));
 BigDecimal actual = NumberInput.parseBigDecimal(buffer);
 assertEquals(expected, actual);
 }
}