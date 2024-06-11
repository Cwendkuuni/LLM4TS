package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcDEFGHIJ"; // ASCII characters
 int length = Utf8.encodedLength(sequence);
 assertEquals(9, length);

 sequence = "\u00E1mno\u00E9pqr\u00E5st"; // UTF-8 characters
 length = Utf8.encodedLength(sequence);
 assertEquals(12, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthInvalidSequence() {
 CharSequence sequence = "\uD800\uDC00"; // Surrogate pair
 int length = Utf8.encodedLength(sequence);
 fail("Unexpected success");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthOverflow() {
 CharSequence sequence = new String(new char[110000]); // Sequence longer than int can represent
 int length = Utf8.encodedLength(sequence);
 fail("Unexpected success");
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[]{-72, -75, -68, -65, -74, -73, -72, -65, -76, -69, -73, -74, -65}; // "program.java"
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{-72, -75, -68, -65, -74, -73, -72, -65, -76, -69, -73, -74, -65, 191}; // "program.java" followed by a byte outside UTF-8 range
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedInvalidBytes() {
 byte[] bytes = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1}; // All bytes negative
 assertFalse(Utf8.isWellFormed(bytes));

 bytes = new byte[]{192, 192, 192, 192, 192, 192, 192, 192, 192, 192}; // All bytes in the first multi-byte sequence
 assertFalse(Utf8.isWellFormed(bytes));

 bytes = new byte[]{193, 193, 193, 193, 193, 193, 193, 193, 193, 193}; // All bytes in the second multi-byte sequence
 assertFalse(Utf8.isWellFormed(bytes));

 bytes = new byte[]{194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207}; // Sequence of valid bytes
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedSurrogatePairs() {
 byte[] bytes = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0xBF, (byte) 0x80, (byte) 0x81}; // A valid surrogate pair followed by another byte
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0xBF}; // A valid surrogate pair without the following byte
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedLongSequence() {
 byte[] bytes = new byte[10000]; // Array of 10000 bytes
 for (int i = 0; i < bytes.length; ++i) {
 bytes[i] = (byte) (Math.random() * 256);
 }
 assertTrue(Utf8.isWellFormed(bytes));
 }
}