package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int expected = sequence.length();
 int actual = Utf8.encodedLength(sequence);
 assertEquals("Incorrect encoded length for sequence 'abcdefghijklmnopqrstuvwxyz'", expected, actual);

 sequence = "\u00e1\u0304abc\u0305def\u0306";
 expected += Utf8.encodedLengthGeneral(sequence, 0, 3) + Utf8.encodedLengthGeneral(sequence, 4, sequence.length());
 actual = Utf8.encodedLength(sequence);
 assertEquals("Incorrect encoded length for sequence '\u00e1\u0304abc\u0305def\u0306'", expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithInvalidSequence() {
 CharSequence sequence = "\uD800";
 Utf8.encodedLength(sequence);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46};
 assertTrue("Well-formed byte array should be considered well-formed", Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F};
 assertTrue("Byte array with some invalid bytes should still be considered well-formed if all invalid bytes are at the end", Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0xF8, (byte) 0x44, (byte) 0x45, (byte) 0x46};
 assertFalse("Byte array with invalid byte sequence should not be considered well-formed", Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0x41, (byte) 0xF8, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46};
 assertFalse("Byte array with invalid byte at the beginning should not be considered well-formed", Utf8.isWellFormed(bytes));
 }
}