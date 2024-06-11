package com.google.common.base;

 import org.junit.Test;
 import org.junit.Assert.*;
 import static org.junit.Assert.assertEquals;

 public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "UTF-8 Test";
 int expectedLength = 14;
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals(expectedLength, actualLength);

 sequence = "\uD800\uDC00"; // Surrogate pair
 expectedLength = 4;
 actualLength = Utf8.encodedLength(sequence);
 assertEquals(expectedLength, actualLength);

 sequence = "\uD800"; // Lone surrogate
 expectedLength = -1; // IllegalArgumentException
 try {
 Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "UTF-8 Test";
 int expectedLength = 14;
 int startIndex = 5; // From the third character
 int actualLength = Utf8.encodedLengthGeneral(sequence, startIndex);
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testIsWellFormedByteArray() {
 byte[] bytes = "UTF-8 Test".getBytes();
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedByteArrayWithSurrogates() {
 byte[] bytes = "\uD800\uDC00".getBytes();
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedByteArrayWithInvalidBytes() {
 byte[] bytes = {-68, -65, -65, -6, 255};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedByteArrayWithInvalidSurrogates() {
 byte[] bytes = new byte[]{-65534, -65, -65}; // Invalid high surrogate
 assertFalse(Utf8.isWellFormed(bytes));

 byte[] bytes2 = new byte[]{-65, -65534, -65}; // Invalid low surrogate
 assertFalse(Utf8.isWellFormed(bytes2));
 }

 @Test
 public void testIsWellFormedByteArrayWithLongSequence() {
 byte[] bytes = new byte[1_000_000];
 for (int i = 0; i < bytes.length; ++i) {
 bytes[i] = (byte) Math.random(); // Random bytes
 }
 assertTrue(Utf8.isWellFormed(bytes));
 }
 }