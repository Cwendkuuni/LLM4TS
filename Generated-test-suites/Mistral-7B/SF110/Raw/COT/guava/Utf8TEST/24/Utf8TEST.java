package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 char[] sequence = new char[]{'a', 'b', 'c', '\u0123', '\u0800', '\uD800'}; // Basic case
 int length = Utf8.encodedLength(new String(sequence));
 assertEquals(11, length);

 char[] invalidSequence = new char[]{'a', 'b', '\u0123', '\u0800'}; // Missing last character
 assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(new String(invalidSequence)));
 }

 @Test
 public void testEncodedLengthGeneral_StartWithValidChar() {
 char[] sequence = new char[]{'a', '\u0800', '\uD800'}; // Start with valid character
 int length = Utf8.encodedLengthGeneral(new String(sequence), 0);
 assertEquals(3, length);
 }

 @Test
 public void testEncodedLengthGeneral_StartWithInvalidSurrogate() {
 char[] sequence = new char[]{'a', '\uD800'}; // Start with invalid surrogate
 assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral(new String(sequence), 0));
 }

 @Test
 public void testEncodedLengthGeneral_MiddleOfValidCharAndInvalidSurrogate() {
 char[] sequence = new char[]{'a', '\u0800', '\uD800'}; // Start with valid character, followed by invalid surrogate
 int length = Utf8.encodedLengthGeneral(new String(sequence), 1);
 assertEquals(3, length);
 }

 @Test
 public void testEncodedLengthGeneral_MiddleOfValidCharAndValidSurrogate() {
 char[] sequence = new char[]{'a', '\u0800', '\uD800'}; // Start with valid character, followed by valid surrogate
 int length = Utf8.encodedLengthGeneral(new String(sequence), 1);
 assertEquals(4, length);
 }

 @Test
 public void testEncodedLengthGeneral_EndWithValidChar() {
 char[] sequence = new char[]{'a', '\u0800', '\uD800'}; // End with valid character
 int length = Utf8.encodedLengthGeneral(new String(sequence), 2);
 assertEquals(3, length);
 }

 @Test
 public void testEncodedLengthGeneral_EndWithValidSurrogate() {
 char[] sequence = new char[]{'a', '\u0800', '\uD800'}; // End with valid surrogate
 int length = Utf8.encodedLengthGeneral(new String(sequence), 2);
 assertEquals(4, length);
 }

 @Test
 public void testEncodedLengthGeneral_EndWithInvalidSurrogate() {
 char[] sequence = new char[]{'a', '\u0800', '\uD800'}; // End with invalid surrogate
 assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral(new String(sequence), 2));
 }

 @Test
 public void testIsWellFormed_ValidUtf8ByteArray() {
 byte[] validUtf8Bytes = new byte[]{0x41, 0x83, 0xac, 0x9e, 0xbc, 0xed};
 boolean result = Utf8.isWellFormed(validUtf8Bytes);
 assertTrue(result);
 }

 @Test
 public void testIsWellFormed_InvalidUtf8ByteArray() {
 byte[] invalidUtf8Bytes = new byte[]{-1, -2, -3, -4, -5, -6};
 assertFalse(Utf8.isWellFormed(invalidUtf8Bytes));
 }

 @Test
 public void testIsWellFormed_InvalidUtf8ByteArrayWithSurrogates() {
 byte[] invalidUtf8BytesWithSurrogates = new byte[]{0xff, 0xdb, 0xdc, 0xff, 0xdb, 0xdc};
 assertFalse(Utf8.isWellFormed(invalidUtf8BytesWithSurrogates));
 }

 @Test
 public void testIsWellFormed_InvalidUtf8ByteArrayWithMixedValidAndInvalidBytes() {
 byte[] invalidUtf8BytesWithMixedBytes = new byte[]{0x41, 0x83, -1, 0xac, 0x9e, 0xff, 0xdb, 0xdc};
 assertFalse(Utf8.isWellFormed(invalidUtf8BytesWithMixedBytes));
 }

 @Test
 public void testIsWellFormedSlowPath_ValidUtf8ByteArrayWithMixedBytes() {
 byte[] validUtf8BytesWithMixedBytes = new byte[]{0x41, 0x83, -1, 0xac, 0x9e, 0xff, 0xdb, 0xdc};
 assertFalse(Utf8.isWellFormedSlowPath(validUtf8BytesWithMixedBytes, 0));
 }

}