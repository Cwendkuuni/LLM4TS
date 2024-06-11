package com.google.common.base.test;

import com.google.common.base.Utf8;
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 char[] sequence = new char[]{'a', 'b', 'c', 'd', '\u0123', '\u0345', '\u0567'}; // Basic Latin, Latin-1 Supplement, Latin Extended-B, and Devanagari scripts
 int expectedLength = Utf8.encodedLength(sequence);
 int actualLength = Utf8.encodedLength(new String(sequence));
 assertEquals(expectedLength, actualLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthThrowsExceptionForInvalidSequence() {
 char[] sequence = new char[]{'\uD800', '\uDC00'}; // Surrogate pair
 Utf8.encodedLength(sequence);
 }

 @Test
 public void testIsWellFormedForValidUtf8ByteArray() {
 byte[] validUtf8Bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46};
 assertTrue(Utf8.isWellFormed(validUtf8Bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedThrowsExceptionForInvalidByteArray() {
 byte[] invalidUtf8Bytes = new byte[]{(byte) 0x7F, (byte) 0x80}; // First byte is over 0x7F
 assertFalse(Utf8.isWellFormed(invalidUtf8Bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedThrowsExceptionForMisalignedSurrogatePair() {
 byte[] misalignedSurrogatePairBytes = new byte[]{(byte) 0xD8, (byte) 0x01, (byte) 0xDC, (byte) 0x00}; // The second byte of the pair is missing
 assertFalse(Utf8.isWellFormed(misalignedSurrogatePairBytes));
 }

 @Test
 public void testIsWellFormedForValidUtf8String() {
 String validUtf8String = new String(new char[]{'a', 'b', 'c', 'd', '\u0123', '\u0345', '\u0567'});
 assertTrue(Utf8.isWellFormed(validUtf8String.getBytes()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedThrowsExceptionForInvalidUtf8String() {
 String invalidUtf8String = new String(new char[]{'\uD800', '\uDC00'}); // Surrogate pair
 assertFalse(Utf8.isWellFormed(invalidUtf8String.getBytes()));
 }
}