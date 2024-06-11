package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
 int length = Utf8.encodedLength(sequence);
 int expectedLength = 39;
 assertEquals(expectedLength, length);

 sequence = "😃😄😌";
 length = Utf8.encodedLength(sequence);
 expectedLength = 12;
 assertEquals(expectedLength, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithIllegalArgument() {
 CharSequence sequence = "😃😄😌\uD800";
 int length = Utf8.encodedLength(sequence);
 fail("IllegalArgumentException expected but not thrown.");
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[]{(byte) 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F};
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0xED, 0x9F, 0xBF, 0xBD, 0x9F, 0xBE, 0x9F, 0xBF};
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[]{(byte) 0xE1, 0xBF, 0xBF, 0xBF, 0xE2, 0x82, 0xAC, 0x9C, 0x9F};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithIllegalArgument() {
 byte[] bytes = new byte[]{(byte) 0xED, 0x9F, 0xBF, 0xBD, 0x9F, 0xBE, 0x9F, 0xBF, (byte) 0xEF};
 fail("IllegalArgumentException expected but not thrown.");
 }
}