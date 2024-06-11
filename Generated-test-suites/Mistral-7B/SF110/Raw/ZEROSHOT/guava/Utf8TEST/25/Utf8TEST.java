package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "UTF-8 test";
 int length = Utf8.encodedLength(sequence);
 assertEquals(12, length);

 sequence = "\u00e9UTF-8\u0305test";
 length = Utf8.encodedLength(sequence);
 assertEquals(16, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithIllegalCharSequence() {
 CharSequence sequence = new String("\uD800\uDC00");
 try {
 Utf8.encodedLength(sequence);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = "UTF-8 test".getBytes();
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = "\uE000\u00E4UTF-8\u0305test".getBytes();
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = "\uD800\uDC00".getBytes(); // Surrogate pair
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithIllegalByteArray() {
 byte[] bytes = new byte[] { '\u00ff' };
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithNegativeOffset() {
 byte[] bytes = "UTF-8 test".getBytes();
 try {
 Utf8.isWellFormed(bytes, -5, bytes.length);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIsWellFormedWithNegativeLength() {
 byte[] bytes = "UTF-8 test".getBytes();
 try {
 Utf8.isWellFormed(bytes, 0, -5);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIsWellFormedWithInvalidByteArray() {
 byte[] bytes = new byte[] { '\u00e9', 0, '\u0305' };
 assertFalse(Utf8.isWellFormed(bytes));
 }
}