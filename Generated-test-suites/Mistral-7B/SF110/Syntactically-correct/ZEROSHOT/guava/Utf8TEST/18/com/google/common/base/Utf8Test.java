package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "utf8 test";
 int length = Utf8.encodedLength(sequence);
 int expected = sequence.length() * 7 / 3 + 2; // approximation based on ASCII characters in the sequence
 assertEquals(expected, length);

 sequence = "\u00e9\u0305\u0304\u00dc\u00f6"; // test surrogate pairs and multibyte characters
 length = Utf8.encodedLength(sequence);
 int expected2 = sequence.length() * 3;
 assertEquals(expected2, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthIllegalArgument() {
 CharSequence sequence = "☻"; // test invalid UTF-8 sequence
 Utf8.encodedLength(sequence);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes1 = new byte[]{(byte) 0x72, (byte) 0x6f, (byte) 0x6f, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e};
 assertTrue(Utf8.isWellFormed(bytes1));

 byte[] bytes2 = new byte[]{(byte) 0x72, (byte) 0x6f, (byte) 0x6f, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x74}; // test invalid byte appended
 assertFalse(Utf8.isWellFormed(bytes2));
 }

 @Test
 public void testIsWellFormedByteArray() {
 byte[] bytes = new byte[]{(byte) 0x72, (byte) 0x6f, (byte) 0x6f, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e};
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedSubArray() {
 byte[] bytes = new byte[]{(byte) 0x72, (byte) 0x6f, (byte) 0x6f, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x74};
 byte[] subArray = new byte[]{(byte) 0x72, (byte) 0x6f, (byte) 0x6f};
 assertTrue(Utf8.isWellFormed(bytes, 0, 3));
 assertFalse(Utf8.isWellFormed(subArray));
 }

 @Test
 public void testIsWellFormedNegativeIndex() {
 byte[] bytes = new byte[]{(byte) 0x72, (byte) 0x6f, (byte) 0x6f, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x74};
 assertFalse(Utf8.isWellFormed(bytes, -1, bytes.length));
 }

 @Test
 public void testIsWellFormedArrayBoundary() {
 byte[] bytes = new byte[]{(byte) 0x72, (byte) 0x6f, (byte) 0x6f, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x74};
 assertFalse(Utf8.isWellFormed(bytes, bytes.length, 1));
 }

 @Test
 public void testIsWellFormedArrayLengthZero() {
 byte[] bytes = new byte[0];
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "utf8 test";
 int start = 5;
 int length = Utf8.encodedLengthGeneral(sequence, start);
 int expected = 3; // approximation based on U+0065 and U+0072 in the sequence
 assertEquals(expected, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthGeneralIllegalArgument() {
 CharSequence sequence = "☻";
 int start = 5; // index out of bounds
 Utf8.encodedLengthGeneral(sequence, start);
 }
}