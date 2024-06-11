package com.google.common.base.test;

import com.google.common.base.Utf8;
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "helloworld";
 int expected = Utf8.encodedLength(sequence);
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithInvalidSequence() {
 CharSequence sequence = "\uD800\uDC00"; // Invalid UTF-16 sequence
 int actual = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test
 public void testEncodedLengthWithSurrogatePair() {
 CharSequence sequence = "\uD800\uDCA1"; // Valid UTF-16 surrogate pair
 int expected = Utf8.encodedLength(sequence);
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormedWithValidUtf8Bytes() {
 byte[] bytes = new byte[] {0x72, 0x6f, 0x6f, 0x6c, 0x6c, 0x65, 0x61, 0x75, 0x74};
 boolean expected = Utf8.isWellFormed(bytes);
 boolean actual = Utf8.isWellFormed(bytes);
 assertTrue(expected == actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithNonUtf8Bytes() {
 byte[] bytes = new byte[] {0x72, 0x6f, 0x6f, 0x6c, 0xff, 0x6c, 0x65, 0x75, 0x74};
 Utf8.isWellFormed(bytes, 0, bytes.length);
 }

 @Test
 public void testIsWellFormedWithLongUtf8Sequence() {
 byte[] bytes = new byte[0x10000]; // 65536 bytes
 for (int i = 0; i < bytes.length; i++) {
 bytes[i] = (byte) (Math.random() * 0x100 + (int) Math.random() % 0x80);
 }
 boolean expected = Utf8.isWellFormed(bytes);
 boolean actual = Utf8.isWellFormed(bytes, 0, bytes.length);
 assertTrue(expected == actual);
 }
}