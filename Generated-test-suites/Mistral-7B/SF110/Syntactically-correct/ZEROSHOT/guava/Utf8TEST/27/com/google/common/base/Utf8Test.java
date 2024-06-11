package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdef";
 int length = Utf8.encodedLength(sequence);
 assertEquals(6, length);

 sequence = "\u0123\u0456\u0678\u09AB\u0CDE\u10FC";
 length = Utf8.encodedLength(sequence);
 assertEquals(6, length);

 sequence = "\uD800\uDC00";
 try {
 length = Utf8.encodedLength(sequence);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "abcdef";
 int length = Utf8.encodedLengthGeneral(sequence, 0);
 assertEquals(6, length);

 sequence = "\u0123\u0456\u0678\u09AB\u0CDE\u10FC";
 int start = 0;
 length = Utf8.encodedLengthGeneral(sequence, start);
 assertEquals(6, length);

 sequence = "\uD800\uDC00";
 start = 0;
 try {
 length = Utf8.encodedLengthGeneral(sequence, start);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[] { 0x61, 0x62, 0x63, 0x64, 0x65 };
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[] { 0xED, 0xA0, (byte) 0xF0, 0x9F, 0xBF, 0xBD, 0xBF, 0xBD };
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[] { 0xED, 0xBF, 0xBF };
 assertFalse(Utf8.isWellFormed(bytes));

 bytes = new byte[] { 0xED, 0xA0, 0xF0, 0x9F, 0xBF, 0xBD, 0xBF, 0xBD, 0xED, 0xA0 };
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = new byte[] { 0xED, 0xA0, (byte) 0xF0, 0x9F, 0xBF, 0xBD, 0xBF, 0xBD };
 assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

 bytes = new byte[] { 0xED, 0xA0, (byte) 0xF0, 0x9F, 0xBF, 0xBD, 0xBF, 0xBD, 0xED, 0xA0, 0xED, 0xA0 };
 assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }
}