package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

 private final CharSequence sequence = "test\u0304\uD800\uDC00\uD840\uDC00"; // Latin1, Latin1 with diacritic, high surrogate, low surrogate

 @Test
 public void testEncodedLength() {
 int length = Utf8.encodedLength(sequence);
 int expectedLength = 11;
 assertEquals(expectedLength, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthIllegalArgument() {
 Utf8.encodedLength(new StringCharacterIterator(""));
 }

 @Test
 public void testEncodedLengthEdgeCases() {
 int length = 0;
 for (char c = 0; c < 0x80; c++) {
 length += Utf8.encodedLength(Character.toString(c));
 }
 int expectedLength = 0;
 for (char c = 0; c < 0x80; c++) {
 expectedLength += 1;
 }
 assertEquals(expectedLength, length);
 }

 @Test
 public void testEncodedLengthWithHighSurrogate() {
 int length = Utf8.encodedLength(sequence.subSequence(2, sequence.length()));
 int expectedLength = 6;
 assertEquals(expectedLength, length);
 }

 @Test
 public void testEncodedLengthWithLowSurrogate() {
 int length = Utf8.encodedLength(sequence.subSequence(3, sequence.length()));
 int expectedLength = 4;
 assertEquals(expectedLength, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithUnpairedSurrogate() {
 CharSequence sequence = "test\uD800";
 int length = Utf8.encodedLength(sequence);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[11];
 for (int i = 0; i < 11; i++) {
 bytes[i] = (byte) Utf8.encodedLength(Character.toString((char) i).charAt(0));
 }
 boolean result = Utf8.isWellFormed(bytes);
 assertTrue(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedNegativeBytes() {
 byte[] bytes = new byte[]{-1, -2};
 boolean result = Utf8.isWellFormed(bytes);
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedInvalidLength() {
 byte[] bytes = new byte[1];
 boolean result = Utf8.isWellFormed(bytes, 2, 2);
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedInvalidOffset() {
 byte[] bytes = new byte[1];
 boolean result = Utf8.isWellFormed(bytes, -1, 1);
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedInvalidEndIndex() {
 byte[] bytes = new byte[1];
 boolean result = Utf8.isWellFormed(bytes, 0, bytes.length + 1);
 assertFalse(result);
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = new byte[]{0x7f, 0x80, 0xc2, 0x80, 0xc3, 0x80, 0xf0};
 boolean result = Utf8.isWellFormed(bytes);
 assertTrue(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedSlowPathInvalidByte() {
 byte[] bytes = new byte[]{0x7f, -1, 0xc2, 0x80, 0xc3, 0x80, 0xf0};
 boolean result = Utf8.isWellFormed(bytes);
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedSlowPathInvalidSequence() {
 byte[] bytes = new byte[]{0x7f, 0x80, -1, 0xc3, 0x80, 0xf0};
 boolean result = Utf8.isWellFormed(bytes);
 assertFalse(result);
 }

 @Test
 public void testIsWellFormedSlowPathInvalidByte2() {
 byte[] bytes = new byte[]{0x7f, 0x80, 0xc2, -1, 0xc3, 0x80, 0xf0};
 boolean result = Utf8.isWellFormed(bytes);
 assertFalse(result);
 }
}