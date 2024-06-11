package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithPositiveSmallNumber() {
 char[] smallNumber = {'1', '2', '3', '4', '5'};
 int result = NumberInput.parseInt(smallNumber, 0, 5);
 assertEquals(12345, result);
 }

 @Test
 public void testParseIntWithNegativeNumber() {
 char[] negativeNumber = {'-', '1', '2', '3', '4', '5'};
 int result = NumberInput.parseInt(negativeNumber, 0, 6);
 assertEquals(-12345, result);
 }

 @Test
 public void testParseIntWithLongNumber() {
 char[] longNumber = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
 int result = NumberInput.parseInt(longNumber, 0, 10);
 assertEquals(123456789, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIntWithInvalidCharacter() {
 char[] invalidNumber = {'1', '2', '3', 'x', '4', '5'};
 try {
 NumberInput.parseInt(invalidNumber, 0, 6);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException, but got: " + e.getMessage());
 }
 }

 @Test
 public void testParseLongWithPositiveSmallNumber() {
 char[] smallNumber = {'1', '2', '3', '4'};
 long result = NumberInput.parseLong(smallNumber, 0, 4);
 assertEquals(1234, result);
 }

 @Test
 public void testParseLongWithNegativeNumber() {
 char[] negativeNumber = {'-', '1', '2', '3', '4'};
 long result = NumberInput.parseLong(negativeNumber, 0, 5);
 assertEquals(-1234, result);
 }

 @Test
 public void testParseLongWithLongNumber() {
 char[] longNumber = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long result = NumberInput.parseLong(longNumber, 0, 11);
 assertEquals(1234567890L, result);
 }

 @Test
 public void testParseLongWithStringInput() {
 String input = "12345";
 long result = NumberInput.parseLong(input.toCharArray(), 0, input.length());
 assertEquals(12345, result);
 }

 @Test
 public void testParseLongWithLongOutOfRangeNumber() {
 char[] longNumber = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
 boolean result = NumberInput.inLongRange(longNumber, 0, 13, false);
 assertEquals(false, result);
 }

 @Test
 public void testParseLongWithLongInRangeNumber() {
 char[] longNumber = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 boolean result = NumberInput.inLongRange(longNumber, 0, 12, false);
 assertEquals(true, result);
 }

 @Test
 public void testParseIntWithStringInput() {
 String input = "12345";
 int result = NumberInput.parseInt(input);
 assertEquals(12345, result);
 }

 @Test
 public void testParseIntWithStringLongNumber() {
 String input = "1234567890";
 int result = NumberInput.parseInt(input);
 assertEquals(1234567890, result);
 }

 @Test
 public void testParseIntWithStringOutOfRangeNumber() {
 String input = "12345678901";
 int result = NumberInput.parseInt(input);
 assertEquals(1234567890, result);
 }

 @Test
 public void testParseIntWithStringNegativeNumber() {
 String input = "-12345";
 int result = NumberInput.parseInt(input);
 assertEquals(-12345, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithStringInvalidCharacter() {
 String input = "123x45";
 try {
 NumberInput.parseInt(input);
 } catch (NumberFormatException e) {
 fail("Expected NumberFormatException, but got: " + e.getMessage());
 }
 }

 @Test
 public void testParseLongWithStringLongNumber() {
 String input = "123456789012345";
 long result = NumberInput.parseLong(input);
 assertEquals(123456789012345L, result);
 }

 @Test
 public void testParseLongWithStringNegativeNumber() {
 String input = "-123456789012345";
 long result = NumberInput.parseLong(input);
 assertEquals(-123456789012345L, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithStringInvalidCharacter() {
 String input = "123x45";
 try {
 NumberInput.parseLong(input);
 } catch (NumberFormatException e) {
 fail("Expected NumberFormatException, but got: " + e.getMessage());
 }
 }

 @Test
 public void testParseBigDecimalWithValidNumber() {
 String number = "123.45";
 BigDecimal result = NumberInput.parseBigDecimal(number);
 BigDecimal expected = new BigDecimal(number);
 assertEquals(expected, result);
 }

 @Test
 public void testParseBigDecimalWithValidLongNumber() {
 char[] longNumber = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 BigDecimal result = NumberInput.parseBigDecimal(longNumber, 0, 11);
 BigDecimal expected = new BigDecimal(Long.toString(NumberInput.parseLong(longNumber, 0, 11)));
 assertEquals(expected, result);
 }

 @Test
 public void testParseBigDecimalWithValidStringNumber() {
 String number = "123456789012345.12345";
 BigDecimal result = NumberInput.parseBigDecimal(number);
 BigDecimal expected = new BigDecimal(number);
 assertEquals(expected, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalWithInvalidCharacter() {
 String number = "123x45";
 try {
 NumberInput.parseBigDecimal(number.toCharArray());
 } catch (NumberFormatException e) {
 fail("Expected NumberFormatException, but got: " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBigDecimalWithNegativeNumber() {
 char[] negativeNumber = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 try {
 NumberInput.parseBigDecimal(negativeNumber, 0, 12);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException, but got: " + e.getMessage());
 }
 }
}