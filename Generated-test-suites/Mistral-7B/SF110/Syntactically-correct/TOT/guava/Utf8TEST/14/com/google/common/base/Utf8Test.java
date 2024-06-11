package com.google.common.base.test;

import com.google.common.annotation.Beta;
import com.google.common.base.Utf8;
import com.google.common.base.Preconditions;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

@Beta
public class Utf8Test {

 @Test
 public void testEncodedLength() {
 char[] sequence = new char[]{'a', 'b', 'c', '\uD801'};
 byte[] expectedBytes = new byte[]{(byte) 0xA1, (byte) 0x80, (byte) 0xA2, (byte) 0x80, (byte) 0xA3, (byte) 0x80, (byte) 0xD8, (byte) 0x34, (byte) 0x35};
 int actualLength = Utf8.encodedLength(new String(sequence));
 byte[] actualBytes = Utf8.encode(sequence);
 assertEquals(expectedLength, actualLength);
 assertArrayEquals(expectedBytes, actualBytes);
 }

 @Test
 public void testEncodedLengthWithShortSequence() {
 char[] sequence = new char[]{'a', 'b', 'c'};
 int actualLength = Utf8.encodedLength(new String(sequence));
 assertEquals(actualLength, 3);
 }

 @Test
 public void testEncodedLengthWithEmptySequence() {
 int actualLength = Utf8.encodedLength(new String(""));
 assertEquals(actualLength, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithInvalidSequence() {
 char[] sequence = new char[]{'a', '\u0000', 'b'};
 assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(new String(sequence)));
 }

 @Test
 public void testIsWellFormedWithValidByteArray() {
 byte[] validBytes = new byte[]{(byte) 0x41, (byte) 0x2E, (byte) 0x6F, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x20, (byte) 0x42, (byte) 0x6F, (byte) 0x6F, (byte) 0x20, (byte) 0x57, (byte) 0x65, (byte) 0x6E, (byte) 0x74, (byte) 0x20, (byte) 0x4E, (byte) 0x61, (byte) 0x6D, (byte) 0x65};
 assertFalse(Utf8.isWellFormed(validBytes));
 }

 @Test
 public void testIsWellFormedWithInvalidByteArrayStartingWithInvalidByte() {
 byte[] invalidBytes = new byte[]{(byte) 0xC1, (byte) 0x2E, (byte) 0x6F, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x20, (byte) 0x42, (byte) 0x6F, (byte) 0x6F, (byte) 0x20, (byte) 0x57, (byte) 0x65, (byte) 0x6E, (byte) 0x74, (byte) 0x20, (byte) 0x4E, (byte) 0x61, (byte) 0x6D, (byte) 0x65};
 assertFalse(Utf8.isWellFormed(invalidBytes));
 }

 @Test
 public void testIsWellFormedWithInvalidByteArrayContainingSurrogates() {
 char[] sequence = new char[]{'a', '\uD800', '\uDC00', 'b'};
 byte[] invalidBytes = Utf8.encode(sequence);
 assertFalse(Utf8.isWellFormed(invalidBytes));
 }

 @Test
 public void testIsWellFormedWithValidByteArrayEncodedFromValidSequence() {
 char[] sequence = new char[]{'a', '\u0041', '\u0061', '\u00F0'};
 byte[] validBytes = Utf8.encode(sequence);
 assertFalse(Utf8.isWellFormedSlowPath(validBytes, 0, validBytes.length));
 }

 @Test
 public void testIsWellFormedWithValidByteArrayEncodedFromValidSequenceSlowPath() {
 char[] sequence = new char[]{'a', '\u0041', '\u0061', '\u00F0'};
 byte[] validBytes = Utf8.encode(sequence);
 assertFalse(Utf8.isWellFormed(validBytes));
 assertTrue(Utf8.isWellFormedSlowPath(validBytes, 0, validBytes.length));
 }
}