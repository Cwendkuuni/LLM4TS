package com.google.common.base.test;

import static org.junit.Assert.*;

import com.google.common.base.Utf8;
import org.junit.Test;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 final CharSequence sequence = "UTF-8 Test";
 int length = Utf8.encodedLength(sequence);
 int expectedLength = sequence.length() * 7 / 3 + 2; // approximate calculation based on ASCII and UTF-8 encoding
 assertEquals(expectedLength, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithIllegalCharSequence() {
 final CharSequence sequence = "\uD800\uDC00"; // surrogate pair
 int length = Utf8.encodedLength(sequence);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testIsWellFormedWithValidByteArray() {
 byte[] bytes = new byte[]{'71', '73', '74', '65', '73', '74', '61', '6D', '70', '6C', '65'};
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithInvalidByteArray() {
 byte[] bytes = new byte[]{-5, -128, 127, 128, 127, 128};
 assertFalse(Utf8.isWellFormed(bytes));
 assertThrows(IllegalArgumentException.class, () -> Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }

 @Test
 public void testEncodedLengthGeneral() {
 final CharSequence sequence = "Mööß"; // CharSequence representation of characters that need more than 1 byte in UTF-8 encoding
 int length = Utf8.encodedLengthGeneral(sequence, 0);
 int expectedLength = sequence.length() * 3; // approximate calculation based on ASCII and UTF-8 encoding
 assertEquals(expectedLength, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthGeneralWithSurrogatePairs() {
 final CharSequence sequence = "👋"; // Emoji character
 try {
 int length = Utf8.encodedLengthGeneral(sequence, 0);
 fail("IllegalArgumentException expected but not thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}