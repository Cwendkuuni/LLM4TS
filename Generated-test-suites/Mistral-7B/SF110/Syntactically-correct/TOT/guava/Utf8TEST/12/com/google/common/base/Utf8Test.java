package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "Hello, 世界!";
 int expectedLength = Utf8.encodedLength(sequence);
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals("Incorrect UTF-8 length for sequence: " + sequence, expectedLength, actualLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithEmptySequence() {
 int expectedLength = Utf8.encodedLength(Character.TEST_CHARACTER);
 int actualLength = Utf8.encodedLength(Character.TEST_CHARACTER);
 fail("Encoded length should not be calculated for empty sequence");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithInvalidCharacter() {
 CharSequence sequence = "\uD800\uDC00";
 int actualLength = Utf8.encodedLength(sequence);
 fail("Encoded length should throw IllegalArgumentException for invalid character sequence: " + sequence);
 }

 @Test
 public void testIsWellFormedWithValidUtf8Bytes() {
 byte[] bytes = new byte[]{(byte) 0x48, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x2C, (byte) 0xE4, (byte) 0xB8, (byte) 0xA5, (byte) 0x21, (byte) 0x21};
 boolean expectedResult = Utf8.isWellFormed(bytes);
 boolean actualResult = Utf8.isWellFormed(bytes);
 assertEquals("UTF-8 bytes are not well-formed", expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithInvalidUtf8Bytes() {
 byte[] bytes = new byte[]{(byte) 0x48, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x2C, (byte) 0xE4, (byte) 0xB9, (byte) 0xA5, (byte) 0x21, (byte) 0x21};
 boolean actualResult = Utf8.isWellFormed(bytes);
 fail("UTF-8 bytes are not valid (should throw IllegalArgumentException)");
 }

 @Test
 public void testIsWellFormedWithEmptyBytesArray() {
 boolean expectedResult = Utf8.isWellFormed(new byte[0]);
 boolean actualResult = Utf8.isWellFormed(new byte[0]);
 assertEquals("UTF-8 bytes array should be considered well-formed when empty", expectedResult, actualResult);
 }
}