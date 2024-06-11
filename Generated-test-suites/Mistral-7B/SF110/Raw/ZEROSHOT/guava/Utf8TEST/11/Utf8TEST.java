package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "Hello, World!";
 int expected = sequence.length() * 3 >> 1;
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);

 sequence = "\uD800\uDC00";
 try {
 int actual = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 sequence = "\uD800\uDC01";
 try {
 int actual = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 sequence = "\uD800\uDC00\uD800\uDC01";
 int actual = Utf8.encodedLength(sequence);
 int expected = sequence.length() * 3 >> 1;
 assertEquals(expected, actual);
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "Hello, World!";
 int start = 3;
 int expected = sequence.length() * 3 >> 1;
 int actual = Utf8.encodedLengthGeneral(sequence, start);
 assertEquals(expected, actual);

 sequence = "\uD800\uDC00";
 int actual = Utf8.encodedLengthGeneral(sequence, 0);
 try {
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 sequence = "\uD800\uDC01";
 int actual = Utf8.encodedLengthGeneral(sequence, 0);
 try {
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 sequence = "\uD800\uDC00\uD800\uDC01";
 int actual = Utf8.encodedLengthGeneral(sequence, 2);
 int expected = sequence.length() * 3 >> 1;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[]{(byte) 0x48, 0x65, 0x6C, 0x6C, 0x6F, (byte) 0x2C, (byte) 0x4F, 0x66, 0x66, 0x72, 0x6F, 0x6D, 0x61, 0x72, 0x79, (byte) 0xE1, 0xA0, 0xAF, 0xFB, 0xE1, 0xB1, 0x80};
 boolean expected = true;
 boolean actual = Utf8.isWellFormed(bytes);
 assertEquals(expected, actual);

 bytes = new byte[]{0xEF, 0xBF, 0xBD};
 boolean actual = Utf8.isWellFormed(bytes);
 assertFalse(actual);

 bytes = new byte[]{0xEF, 0xBF, 0xBD, 0x41};
 boolean actual = Utf8.isWellFormed(bytes);
 assertFalse(actual);

 bytes = new byte[]{0xEF, 0xBF, 0xBD, 0x41, 0x42};
 boolean actual = Utf8.isWellFormed(bytes);
 assertFalse(actual);

 bytes = new byte[]{0xEF, 0xBF, 0xBD, 0x41, 0xE3, 0x80, 0x42};
 boolean actual = Utf8.isWellFormed(bytes);
 assertFalse(actual);
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = new byte[]{(byte) 0x48, 0x65, 0x6C, 0x6C, 0x6F, (byte) 0x2C, (byte) 0x4F, 0x66, 0x66, 0x72, 0x6F, 0x6D, 0x61, 0x72, 0x79, (byte) 0xE1, 0xA0, 0xAF, 0xFB, 0xE1, 0xB1, 0x80};
 boolean expected = true;
 boolean actual = Utf8.isWellFormed(bytes);
 assertEquals(expected, actual);

 bytes = new byte[]{0xEF, 0xBF, 0xBD};
 assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

 bytes = new byte[]{0xEF, 0xBF, 0xBD, 0x41};
 assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

 bytes = new byte[]{0xEF, 0xBF, 0xBD, 0x41, 0x42};
 assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

 bytes = new byte[]{0xEF, 0xBF, 0xBD, 0x41, 0xE3, 0x80, 0x42};
 assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }
}