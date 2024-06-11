package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "UTF-8 test";
 int expected = sequence.length() * 7 / 3; // approximate estimation
 int actual = Utf8.encodedLength(sequence);
 assertEquals("Encoded length should match estimation", expected, actual);

 sequence = "\u00A9 UTF-8"; // copyright symbol
 expected = 3;
 actual = Utf8.encodedLength(sequence);
 assertEquals("Encoded length for single character should be 3", expected, actual);

 sequence = "\uD800\uDC00"; // surrogate pair (invalid UTF-8)
 try {
 actual = Utf8.encodedLength(sequence);
 fail("Encoded length for invalid UTF-8 sequence should throw IllegalArgumentException");
 } catch (IllegalArgumentException ex) {
 // expected
 }
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "UTF-8 test";
 int start = 3;
 int expected = sequence.length() * 7 / 3; // approximate estimation
 int actual = Utf8.encodedLengthGeneral(sequence, start);
 assertEquals("Encoded length for sub-sequence should match estimation", expected, actual);

 sequence = "\uD800\uDC00"; // surrogate pair (invalid UTF-8)
 try {
 actual = Utf8.encodedLengthGeneral(sequence, 0);
 fail("Encoded length for invalid UTF-8 sequence should throw IllegalArgumentException");
 } catch (IllegalArgumentException ex) {
 // expected
 }
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = "UTF-8 test".getBytes();
 boolean expected = true;
 boolean actual = Utf8.isWellFormed(bytes);
 assertEquals("UTF-8 encoded data should be well-formed", expected, actual);

 bytes = "UTF-8 test\u0000".getBytes(); // null byte
 expected = false;
 actual = Utf8.isWellFormed(bytes);
 assertEquals("UTF-8 encoded data with null byte should be not well-formed", expected, actual);

 bytes = "\uD800\uDC00".getBytes(); // surrogate pair (invalid UTF-8)
 expected = false;
 actual = Utf8.isWellFormed(bytes);
 assertEquals("UTF-8 encoded data with surrogate pair should be not well-formed", expected, actual);
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = "\uFDD0\uD800\uDF80\uD800".getBytes(); // invalid UTF-8
 boolean expected = false;
 boolean actual = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
 assertEquals("isWellFormedSlowPath() should return false for invalid UTF-8", expected, actual);

 bytes = "UTF-8 test".getBytes();
 expected = true;
 actual = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
 assertEquals("isWellFormedSlowPath() should return true for valid UTF-8", expected, actual);
 }
}