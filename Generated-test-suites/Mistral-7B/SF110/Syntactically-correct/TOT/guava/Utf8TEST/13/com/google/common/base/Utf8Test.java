package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghij";
 int length = Utf8.encodedLength(sequence);
 assertEquals(11, length);

 sequence = "\u00e1\u03b1\u03c1";
 length = Utf8.encodedLength(sequence);
 assertEquals(6, length);

 sequence = "\uD800\uDC00\uD800\uDFFF";
 try {
 Utf8.encodedLength(sequence);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "abcdefghij";
 int length = Utf8.encodedLengthGeneral(sequence, 0);
 assertEquals(11, length);

 sequence = "\u00e1\u03b1\u03c1";
 int start = 1;
 length = Utf8.encodedLengthGeneral(sequence, start);
 assertEquals(5, length);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[]{(byte) 0x78, (byte) 0x9f, (byte) 0x80, (byte) 0xbf, (byte) 0x82, (byte) 0xbf};
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0x78, (byte) 0x9f, (byte) 0x80, (byte) 0x80, (byte) 0xbf, (byte) 0x82, (byte) 0xbf};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = new byte[]{(byte) 0x78, (byte) 0x9f, (byte) 0x80, (byte) 0xbf, (byte) 0x81, (byte) 0x80, (byte) 0xbf, (byte) 0x82};
 assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

 bytes = new byte[]{(byte) 0x78, (byte) 0x9f, (byte) 0x80, (byte) 0xbf, (byte) 0x81, (byte) 0x81, (byte) 0xbf, (byte) 0x82};
 assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }
}