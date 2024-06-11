package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int expected = sequence.length() * 3 / 2;
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);

 sequence = "\u00A9\u0414\uD800\uDFFF";
 try {
 actual = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 sequence = "\uD800\uDFFF\u00A9\u0414";
 try {
 actual = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int expected = sequence.length() * 3 / 2;
 int start = 5;
 int actual = Utf8.encodedLengthGeneral(sequence, start);
 assertEquals(expected + 2, actual);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d, 0x4e, 0x4f };
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[] { 0xED, 0x9F, 0xBF, 0xBD, 0xE0, 0xA0, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80 };
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = new byte[] { 0xED, 0x9F, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80 };
 assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

 bytes = new byte[] { 0xED, 0x9F, 0x80, 0x81, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80 };
 assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }
}