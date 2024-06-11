package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz"; // ASCII characters
 int length = Utf8.encodedLength(sequence);
 assertEquals(11, length);

 sequence = "\u00A0\u0391\u0393\u0394\u0395\u20AC"; // ASCII and Unicode characters
 length = Utf8.encodedLength(sequence);
 assertEquals(7, length);

 // Edge case: empty sequence
 length = Utf8.encodedLength(Character.DEAD); // Unicode character with U+FFFD
 assertEquals(1, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithIllegalArgument() {
 CharSequence sequence = "abcdefghijklmnopq\u0394stuvwxyz"; // Unpaired surrogate
 int length = Utf8.encodedLength(sequence);
 // The test will fail with an IllegalArgumentException
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = "abcdefghijklmnopqrstuvwxyz".getBytes(); // ASCII characters
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = "\u00A0\u0391\u0393\u0394\u0395\u20AC".getBytes(); // ASCII and Unicode characters
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{-72, -111, -111, -111, -111, -111, -111, -111, -111, -111, -111, -111, -111, -111, -111, -111}; // Invalid UTF-8 sequence
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithIllegalArgument() {
 byte[] bytes = new byte[]{1, 2, 3}; // Invalid length
 assertFalse(Utf8.isWellFormed(bytes, 3, 3));
 }
}