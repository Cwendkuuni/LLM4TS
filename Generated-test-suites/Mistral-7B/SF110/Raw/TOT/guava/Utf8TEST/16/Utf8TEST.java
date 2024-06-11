package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 char[] sequence = {'\u0041', '\u00E2', '\u041C', '\uD800', '\uDC00', '\uD801', '\uDC00'};
 int expected = 7 + 2 * 3; // 3 surrogate pairs
 int actual = Utf8.encodedLength(new String(sequence));
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithIllegalSequence() {
 char[] sequence = {'\u0041', '\u00E2', '\uD800', '\uDC00'};
 try {
 Utf8.encodedLength(new String(sequence));
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEncodedLengthWithEmptySequence() {
 char[] sequence = {};
 int actual = Utf8.encodedLength(new String(sequence));
 assertEquals(0, actual);
 }

 @Test
 public void testEncodedLengthWithASCIIOnlySequence() {
 char[] sequence = {'A', 'B', 'C'};
 int actual = Utf8.encodedLength(new String(sequence));
 assertEquals(3, actual);
 }

 @Test
 public void testIsWellFormedWithValidUTF8Bytes() {
 byte[] bytes = {0x41, 0xE2, 0x43, 0x44};
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidUTF8Bytes() {
 byte[] bytes = {0x41, 0xE2, 0xFF, 0x44};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidUTF8BytesSlowPath() {
 byte[] bytes = {0x41, 0xFF, 0x44, 0x44};
 assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }

 @Test
 public void testIsWellFormedWithInvalidUTF16Surrogate() {
 byte[] bytes = {0x41, 0xD800, 0xDC00};
 assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }

 @Test
 public void testIsWellFormedWithValidUTF16SurrogatePair() {
 byte[] bytes = {0x41, 0xD800, 0xDC01, 0xD800, 0xDC01};
 assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }

 @Test
 public void testIsWellFormedWithValidUTF16SurrogatePairAndTrailingBytes() {
 byte[] bytes = {0x41, 0xD800, 0xDC01, 0xD800, 0xDC01, 0x44, 0x45};
 assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }
}