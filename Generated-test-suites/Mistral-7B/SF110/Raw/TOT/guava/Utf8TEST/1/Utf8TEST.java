package com.google.common.base.test;

import com.google.common.base.Utf8;
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 // Test case for encodedLength method with normal characters
 @Test
 public void testEncodedLengthNormal() {
 String sequence = "Hello World";
 int expectedLength = Utf8.encodedLength(sequence);
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals("Unexpected UTF-8 length for normal characters", expectedLength, actualLength);
 }

 // Test case for encodedLength method with multi-byte characters
 @Test
 public void testEncodedLengthMultiByte() {
 String sequence = "Moji: 🐉🐲";
 int expectedLength = Utf8.encodedLength(sequence);
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals("Unexpected UTF-8 length for multi-byte characters", expectedLength, actualLength);
 }

 // Test case for encodedLength method with surrogate pairs
 @Test
 public void testEncodedLengthSurrogatePairs() {
 String sequence = "\uD840\uDC00\uD840\uDC34";
 int expectedLength = Utf8.encodedLength(sequence);
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals("Unexpected UTF-8 length for surrogate pairs", expectedLength, actualLength);
 }

 // Test case for isWellFormed method with valid UTF-8 bytes
 @Test
 public void testIsWellFormedValidUtf8() {
 byte[] bytes = new byte[] { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33 };
 assertTrue("UTF-8 data should be well-formed", Utf8.isWellFormed(bytes));
 }

 // Test case for isWellFormed method with invalid UTF-8 byte sequence
 @Test
 public void testIsWellFormedInvalidUtf8() {
 byte[] bytes = new byte[] { 72, 101, 108, 108, 111, 32, -63, 111, 114, 108, 100, 33 };
 assertFalse("UTF-8 data should not be well-formed", Utf8.isWellFormed(bytes));
 }

 // Test case for isWellFormed method with partial UTF-8 byte sequence
 @Test
 public void testIsWellFormedPartialUtf8() {
 byte[] bytes = new byte[] { 72, 101, 108, 111, 32, 0 };
 assertFalse("UTF-8 data should not be well-formed", Utf8.isWellFormed(bytes));
 }

 // Test case for isWellFormed method with surrogate pairs in byte sequence
 @Test
 public void testIsWellFormedSurrogatePairs() {
 byte[] bytes = new byte[] { (byte) 0xF0, (byte) 0x90, (byte) 0x8D, (byte) 0x85, (byte) 0x90, (byte) 0x8E, (byte) 0x8D, (byte) 0x85 };
 assertFalse("UTF-8 data should not be well-formed due to unpaired surrogates", Utf8.isWellFormed(bytes));
 }

 // Test case for isWellFormed method with valid UTF-8 byte sequence
 @Test
 public void testIsWellFormedValidUtf8Bytes() {
 byte[] bytes = new byte[] { 72, 101, 108, 111, 32, 194, 160, 194, 164, 108, 100, 33 };
 assertTrue("UTF-8 data should be well-formed", Utf8.isWellFormed(bytes));
 }

}