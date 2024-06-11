package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int expected = sequence.length();
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);

 sequence = "\u007f\u0080\u0800";
 expected = 7;
 actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);

 sequence = "\ud800\udc00";
 assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(sequence));
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int expected = sequence.length();
 int actual = Utf8.encodedLengthGeneral(sequence, 0);
 assertEquals(expected, actual);

 sequence = "\u007f\u0080\u0800";
 expected = 7;
 actual = Utf8.encodedLengthGeneral(sequence, 0);
 assertEquals(expected, actual);

 sequence = "\ud800\udc00";
 assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLengthGeneral(sequence, 0));
 }

 @Test
 public void testIsWellFormedByteArray() {
 byte[] bytes = new byte[0];
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[] { 72, 65, 67, 6F, 72, 65, 67, 6F, 72, 65, 67, 6F, 72, 65, 67 };
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[] { -63, -62, -63, -62, -63, -62, -63, -62, -63, -62, -63, -62, -63, -62, -63, -62 };
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedByteArrayWithOffsetAndLength() {
 byte[] bytes = new byte[0];
 assertTrue(Utf8.isWellFormed(bytes, 0, bytes.length));

 bytes = new byte[] { 72, 65, 67, 6F, 72, 65, 67, 6F, 72, 65, 67, 6F, 72, 65, 67 };
 assertTrue(Utf8.isWellFormed(bytes, 0, bytes.length));

 bytes = new byte[] { -63, -62, -63, -62, -63, -62, -63, -62, -63, -62, -63, -62, -63, -62, -63, -62, 72 };
 assertFalse(Utf8.isWellFormed(bytes, 0, bytes.length));
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = new byte[0];
 assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

 bytes = new byte[] { 72, 65, 67, 6F, 72, 65, 67, 6F, 72, 65, 67, 6F, 72, 65, 67 };
 assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

 bytes = new byte[] { -63, -62, -63, -62, -63, -62, -63, -62, -63, -62, -63, -62, -63, -62, -63, -62, 72 };
 assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }
}