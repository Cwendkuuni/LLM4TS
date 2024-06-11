package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrst";
 int expected = sequence.length() * 3 / 2; // approximate value based on ASCII characters
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);

 sequence = "\u00A0\u03B4\uD840\uDC00\uD841\uDC35"; // various Unicode characters
 expected = 11; // calculated by manually encoding the characters
 actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithInvalidSequence() {
 CharSequence sequence = "\uD800\uD801"; // invalid sequence of surrogate characters
 Utf8.encodedLength(sequence);
 }

 @Test
 public void testIsWellFormed() {
 byte[] validUtf8 = new byte[] { 72, 101, 108, 108, 111, 119, 105, 110, 103, 101, 32, 83, 111, 117, 115, 101, 115, 111, 118, 101, 100, 33 };
 assertTrue(Utf8.isWellFormed(validUtf8));

 byte[] invalidUtf8 = new byte[] { 72, 101, 108, 108, 111, -64, 119, 105, 110, 103, 101, 32, 83, 111, 117, 115, 101, 115, 111, 118, 101, 100, 65, 33 }; // contains a negative byte
 assertFalse(Utf8.isWellFormed(invalidUtf8));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithInvalidByteArray() {
 Utf8.isWellFormed(new byte[] { 1, 2 }); // insufficient bytes for valid UTF-8 encoding
 }
}