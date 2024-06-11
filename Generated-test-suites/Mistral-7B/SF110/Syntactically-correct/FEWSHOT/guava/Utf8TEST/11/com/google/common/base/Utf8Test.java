package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int length = Utf8.encodedLength(sequence);
 assertEquals(23, length);

 sequence = "\u00e1\u0305abcdefghijklmnopqrstuvwxyz";
 length = Utf8.encodedLength(sequence);
 assertEquals(43, length);

 sequence = "\ud800\udc00abcdefghijklmnopqrstuvwxyz";
 try {
 length = Utf8.encodedLength(sequence);
 fail("Exception expected.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int length = Utf8.encodedLengthGeneral(sequence, 0);
 assertEquals(23, length);

 sequence = "\u00e1\u0305abcdefghijklmnopqrstuvwxyz";
 int start = 0;
 int length1 = Utf8.encodedLengthGeneral(sequence, start);
 assertEquals(43, length1);
 int start2 = 2;
 int length2 = Utf8.encodedLengthGeneral(sequence, start2);
 assertEquals(21, length2);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48, (byte) 0x49, (byte) 0x4a, (byte) 0x4b, (byte) 0x4c, (byte) 0x4d, (byte) 0x4e, (byte) 0x4f, (byte) 0x50, (byte) 0x51};
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48, (byte) 0x49, (byte) 0x4a, (byte) 0x4b, (byte) 0x4c, (byte) 0x4d, (byte) 0x4e, (byte) 0x4f, (byte) 0x50, (byte) 0x3B, (byte) 0x41};
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48, (byte) 0x49, (byte) 0x4a, (byte) 0x4b, (byte) 0x4c, (byte) 0x4d, (byte) 0x4e, (byte) 0x4f, (byte) 0x50, (byte) 0x80, (byte) 0x00};
 assertFalse(Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48, (byte) 0x49, (byte) 0x4a, (byte) 0x4b, (byte) 0x4c, (byte) 0x4d, (byte) 0x4e, (byte) 0x4f, (byte) 0x50};
 try {
 Utf8.isWellFormed(bytes);
 fail("Exception expected.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48, (byte) 0x49, (byte) 0x4a, (byte) 0x4b, (byte) 0x4c, (byte) 0x4d, (byte) 0x4e, (byte) 0x4f, (byte) 0x50, (byte) 0x51, (byte) 0x52};
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48, (byte) 0x49, (byte) 0x4a, (byte) 0x4b, (byte) 0x4c, (byte) 0x4d, (byte) 0x4e, (byte) 0x4f, (byte) 0x50, (byte) 0x3B, (byte) 0x41, (byte) 0x52};
 assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

 bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48, (byte) 0x49, (byte) 0x4a, (byte) 0x4b, (byte) 0x4c, (byte) 0x4d, (byte) 0x4e, (byte) 0x4f, (byte) 0x50, (byte) 0x80, (byte) 0x00};
 assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }
}