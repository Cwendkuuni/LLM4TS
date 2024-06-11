package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int length = Utf8.encodedLength(sequence);
 int expectedLength = 23;
 assertEquals(expectedLength, length);

 sequence = "\u00A0\u1F60\uD800\uDC00\uD800\uDFFF";
 length = Utf8.encodedLength(sequence);
 expectedLength = 11;
 assertEquals(expectedLength, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthIllegalArgumentException() {
 CharSequence sequence = "\uD800\uDFFF";
 try {
 Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //Expected exception
 }
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "abcdef\u0304\u0305ghijklmnopqrstuvwxyz";
 int length = Utf8.encodedLengthGeneral(sequence, 6);
 int expectedLength = 13;
 assertEquals(expectedLength, length);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[] { 72, 71, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85 };
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = new byte[] { 194, -118, 195, -111, 194, -166, 195, -97, 194, -146, 195, -65, 194, -160, 195, -68 };
 assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedSlowPathIllegalArgumentException() {
 byte[] bytes = new byte[] { 194, -118, 195, -111, 194, -166, 195, -97, 194, -146, 195, -65, 194, -160, 195, -68, 194, -128 };
 try {
 Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //Expected exception
 }
 }
}