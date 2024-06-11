package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int expectedLength = sequence.length();
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals("Unexpected UTF-8 length for sequence: " + sequence, expectedLength, actualLength);

 sequence = "\u00a9\u03a3\u03b1\u03b4\u03ac\u03b7\u03bc\u03c1\u03c3\u03c5\u03c7\u03c9\u03cb\u03cd\u03d1";
 expectedLength = sequence.length() * 3; // Each character is a 3-byte sequence in UTF-8
 actualLength = Utf8.encodedLength(sequence);
 assertEquals("Unexpected UTF-8 length for sequence: " + sequence, expectedLength, actualLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithInvalidSequence() {
 CharSequence sequence = "abc\u0305def";
 Utf8.encodedLength(sequence); // This sequence contains an invalid UTF-16 character encoded as a single UTF-8 byte
 }

 @Test
 public void testEncodedLengthWithSurrogatePairs() {
 CharSequence sequence = "\ud800\udc00\ud801\udc00"; // Two valid surrogate pairs in UTF-16
 int expectedLength = sequence.length() * 6; // Each surrogate pair is a 6-byte sequence in UTF-8
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals("Unexpected UTF-8 length for sequence: " + sequence, expectedLength, actualLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithInvalidSurrogatePair() {
 CharSequence sequence = "\ud800\udbff"; // An invalid surrogate pair in UTF-16
 Utf8.encodedLength(sequence);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48, (byte) 0x49};
 assertTrue("UTF-8 bytes should be well-formed: " + bytes, Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidByte() {
 byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0xff, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48, (byte) 0x49};
 assertFalse("UTF-8 bytes should not be well-formed: " + bytes, Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithSurrogatePair() {
 byte[] bytes = new byte[]{(byte) 0xed, (byte) 0xa0, (byte) 0xed, (byte) 0xb0, (byte) 0xed, (byte) 0xa1, (byte) 0xed, (byte) 0xb1}; // Two surrogate pairs in UTF-8
 assertTrue("UTF-8 bytes should be well-formed: " + bytes, Utf8.isWellFormed(bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithInvalidBytes() {
 byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0xff, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48, (byte) 0x49};
 assertFalse("UTF-8 bytes should not be well-formatted: " + bytes, Utf8.isWellFormed(bytes));
 }
}