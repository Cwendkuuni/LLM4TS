package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.nio.charset.StandardCharsets;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int length = Utf8.encodedLength(sequence);
 int expectedLength = sequence.length() * 3 / 2;
 assertEquals(expectedLength, length);

 sequence = "\u00a0\u0394\u0395\u0396";
 length = Utf8.encodedLength(sequence);
 expectedLength = 6;
 assertEquals(expectedLength, length);

 sequence = "\uD800\uDC00";
 assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(sequence));
 }

 @Test
 public void testIsWellFormedByteArray() {
 byte[] bytes = new byte[0];
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{"72", "6F", "63", "65", "64"};
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{0xED, 0x81, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B};
 assertFalse(Utf8.isWellFormed(bytes));

 bytes = new byte[]{"72", "6F", "63", "65", "64", 0xED, 0x81, 0x9B};
 assertFalse(Utf8.isWellFormed(bytes));

 bytes = new byte[]{"72", "6F", "63", "65", "64", 0xFD, (byte) 0x9E, 0x80, 0x80};
 assertFalse(Utf8.isWellFormed(bytes));

 bytes = new byte[]{"72", "6F", "63", "65", "64", 0xF4, 0x90, 0x8F, 0xBF};
 assertFalse(Utf8.isWellFormed(bytes));

 bytes = new byte[]{"72", "6F", "63", "65", "64", 0xE0, 0xA0, 0x80, 0x80, 0x80};
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedByteArrayWithOffsetAndLength() {
 byte[] bytes = new byte[0];
 int offset = 0;
 int length = bytes.length;
 assertTrue(Utf8.isWellFormed(bytes, offset, length));

 bytes = new byte[]{"72", "6F", "63", "65", "64"};
 offset = 0;
 length = bytes.length;
 assertTrue(Utf8.isWellFormed(bytes, offset, length));

 bytes = new byte[]{0xED, 0x81, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B, 0x9B};
 offset = 0;
 length = bytes.length;
 assertFalse(Utf8.isWellFormed(bytes, offset, length));

 bytes = new byte[]{"72", "6F", "63", "65", "64", 0xED, 0x81, 0x9B};
 offset = 0;
 length = 8;
 assertFalse(Utf8.isWellFormed(bytes, offset, length));

 bytes = new byte[]{"72", "6F", "63", "65", "64", 0xFD, (byte) 0x9E, 0x80, 0x80};
 offset = 0;
 length = 10;
 assertFalse(Utf8.isWellFormed(bytes, offset, length));

 bytes = new byte[]{"72", "6F", "63", "65", "64", 0xF4, 0x90, 0x8F, 0xBF};
 offset = 0;
 length = 10;
 assertFalse(Utf8.isWellFormed(bytes, offset, length));

 bytes = new byte[]{"72", "6F", "63", "65", "64", 0xE0, 0xA0, 0x80, 0x80, 0x80};
 offset = 0;
 length = 11;
 assertTrue(Utf8.isWellFormed(bytes, offset, length));
 }

 @Test
 public void testEncodedLengthWithSubSequence() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int length = Utf8.encodedLength(sequence.subSequence(0, 5));
 int expectedLength = 5 * 3 / 2;
 assertEquals(expectedLength, length);

 length = Utf8.encodedLength(sequence.subSequence(5, 11));
 expectedLength = 6;
 assertEquals(expectedLength, length);
 }

 @Test
 public void testEncodedLengthWithSubSequenceThrowsIllegalArgumentException() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(sequence.subSequence(15, 1)));
 }

 @Test
 public void testEncodedLengthWithSubSequenceAtEnd() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int length = Utf8.encodedLength(sequence.subSequence(0, sequence.length()));
 int expectedLength = sequence.length() * 3 / 2;
 assertEquals(expectedLength, length);
 }
}