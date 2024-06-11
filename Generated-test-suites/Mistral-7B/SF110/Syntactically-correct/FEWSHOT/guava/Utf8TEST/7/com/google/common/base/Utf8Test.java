package com.google.common.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 int sequenceLength = 3;
 char[] sequence = new char[sequenceLength];
 sequence[0] = 'A';
 sequence[1] = 'ø';
 sequence[2] = 'B';

 int encodedLength = Utf8.encodedLength(sequence);
 int expectedEncodedLength = 4;
 assertTrue("Encoded length should be " + expectedEncodedLength, encodedLength == expectedEncodedLength);
 }

 @Test
 public void testEncodedLength_InvalidInput() {
 char[] sequence = new char[1];
 sequence[0] = '\uD800'; // high surrogate

 IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(sequence));
 String expectedMessage = "UTF-8 length does not fit in int: ";
 assertTrue("Unexpected exception message", exception.getMessage().startsWith(expectedMessage));
 }

 @Test
 public void testIsWellFormed_ValidInput() {
 byte[] bytes = new byte[3];
 bytes[0] = (byte) 0x41; // A
 bytes[1] = (byte) 0xC3; // ø
 bytes[2] = (byte) 0x42; // B

 assertTrue("UTF-8 sequence should be well-formed", Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormed_InvalidInput_LeadingByte() {
 byte[] bytes = new byte[1];
 bytes[0] = (byte) 0x80; // invalid leading byte

 assertFalse("UTF-8 sequence should be invalid", Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormed_InvalidInput_TrailingBytes() {
 byte[] bytes = new byte[3];
 bytes[0] = (byte) 0x41; // A
 bytes[1] = (byte) 0xC3; // ø
 bytes[2] = (byte) 0x80; // invalid trailing byte

 assertFalse("UTF-8 sequence should be invalid", Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormed_InvalidInput_MiddleBytes() {
 byte[] bytes = new byte[3];
 bytes[0] = (byte) 0x41; // A
 bytes[1] = (byte) 0xE0; // ø (invalid leading byte for 2-byte sequence)
 bytes[2] = (byte) 0x80; // invalid trailing byte

 assertFalse("UTF-8 sequence should be invalid", Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormed_InvalidInput_Surrogates() {
 byte[] bytes = new byte[3];
 bytes[0] = (byte) 0xD8; // high surrogate
 bytes[1] = (byte) 0xDC; // low surrogate
 bytes[2] = (byte) 0x42; // B

 assertFalse("UTF-8 sequence should be invalid", Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedSlowPath_ValidInput() {
 byte[] bytes = new byte[3];
 bytes[0] = (byte) 0x41; // A
 bytes[1] = (byte) 0xC3; // ø
 bytes[2] = (byte) 0x42; // B

 assertTrue("UTF-8 sequence should be well-formed", Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }

 @Test
 public void testIsWellFormedSlowPath_InvalidInput_LeadingByte() {
 byte[] bytes = new byte[1];
 bytes[0] = (byte) 0x80; // invalid leading byte

 assertFalse("UTF-8 sequence should be invalid", Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }

 @Test
 public void testIsWellFormedSlowPath_InvalidInput_TrailingBytes() {
 byte[] bytes = new byte[3];
 bytes[0] = (byte) 0x41; // A
 bytes[1] = (byte) 0xC3; // ø
 bytes[2] = (byte) 0x80; // invalid trailing byte

 assertFalse("UTF-8 sequence should be invalid", Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }

 @Test
 public void testIsWellFormedSlowPath_InvalidInput_MiddleBytes() {
 byte[] bytes = new byte[3];
 bytes[0] = (byte) 0x41; // A
 bytes[1] = (byte) 0xE0; // ø (invalid leading byte for 2-byte sequence)
 bytes[2] = (byte) 0x80; // invalid trailing byte

 assertFalse("UTF-8 sequence should be invalid", Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }
}