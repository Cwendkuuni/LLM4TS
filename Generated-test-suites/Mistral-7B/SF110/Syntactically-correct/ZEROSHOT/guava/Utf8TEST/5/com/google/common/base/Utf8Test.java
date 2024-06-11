package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "Hello, World!";
 int expected = sequence.length() * 3 / 2;
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test
 public void testEncodedLengthWithSurrogate() {
 CharSequence sequence = "\uD800\uDC00";
 int expected = 4;
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithIllegalArgument() {
 CharSequence sequence = new String(new byte[]{(byte)0xED, (byte)0xAD, (byte)0xBE, (byte)0xEF});
 Utf8.encodedLength(sequence);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithIllegalArgument() {
 byte[] bytes = new byte[]{(byte)0xED, (byte)0xAD, (byte)0xBE, (byte)0xEF};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithNegativeOffset() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 Utf8.isWellFormed(bytes, -1, bytes.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithNegativeLength() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 Utf8.isWellFormed(bytes, 0, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithOutOfBoundIndex() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 Utf8.isWellFormed(bytes, 10, bytes.length);
 }

 @Test
 public void testIsWellFormedWithLeadingBOM() {
 byte[] bytes = new byte[]{(byte)0xFE, (byte)0xFF, 0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithMiddleBOM() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0xFF, (byte)0xFF, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithTrailingBOM() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21, (byte)0xFF, (byte)0xFE};
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithLeadingInvalidByte() {
 byte[] bytes = new byte[]{(byte)0xED, (byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithMiddleInvalidByte() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0xED, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithTrailingInvalidByte() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21, (byte)0xED};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidSequence() {
 byte[] bytes = new byte[]{(byte)0xED, (byte)0xAD, (byte)0xBE, (byte)0xEF};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidSequenceWithLeadingByte() {
 byte[] bytes = new byte[]{(byte)0xED, 0x48, (byte)0xAD, (byte)0xBE, (byte)0xEF};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidSequenceWithMiddleByte() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0xAD, (byte)0xED, (byte)0xBE, (byte)0xEF};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidSequenceWithTrailingByte() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21, (byte)0xED};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidSequenceWithLeadingSurrogate() {
 byte[] bytes = new byte[]{(byte)0xD8, 0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidSequenceWithMiddleSurrogate() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0xD8, 0xDC, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidSequenceWithTrailingSurrogate() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21, (byte)0xD8};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidSequenceWithMiddleSurrogates() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0x65, (byte)0xD8, 0xDC, (byte)0xD8, 0xDC, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidSequenceWithLeadingAndTrailingSurrogates() {
 byte[] bytes = new byte[]{(byte)0xD8, 0xD8, (byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidSequenceWithSurrogatesAtMiddle() {
 byte[] bytes = new byte[]{(byte)0x48, (byte)0xD8, 0xDC, (byte)0xD8, 0xDC, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F, (byte)0x2C, (byte)0x20, (byte)0x57, (byte)0x6F, (byte)0x72, (byte)0x6C, (byte)0x64, (byte)0x21};
 assertFalse(Utf8.isWellFormed(bytes));
 }
}