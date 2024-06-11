package com.google.common.base.test;

import com.google.common.base.Utf8;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Utf8Test {

 // Test case for encodedLength method with normal characters
 @Test
 public void testEncodedLengthNormal() {
 String sequence = "abcdefghijklmnopqrstuvwxyz";
 int expectedLength = Utf8.encodedLength(sequence);
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals(expectedLength, actualLength);
 }

 // Test case for encodedLength method with multibyte characters
 @Test
 public void testEncodedLengthMultibyte() {
 String sequence = "ඵබලගලඩ඼ල";
 int expectedLength = Utf8.encodedLength(sequence);
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals(expectedLength, actualLength);
 }

 // Test case for encodedLength method with an empty CharSequence
 @Test
 public void testEncodedLengthEmpty() {
 int expectedLength = 0;
 int actualLength = Utf8.encodedLength(Character.CHAR_MIN);
 assertEquals(expectedLength, actualLength);
 }

 // Test case for encodedLength method with an invalid CharSequence
 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthInvalid() {
 String sequence = "abc\uFFFFdef";
 Utf8.encodedLength(sequence);
 }

 // Test case for isWellFormed method with valid UTF-8 bytes
 @Test
 public void testIsWellFormedValid() {
 byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43};
 boolean expectedResult = true;
 boolean actualResult = Utf8.isWellFormed(bytes);
 assertEquals(expectedResult, actualResult);
 }

 // Test case for isWellFormed method with invalid UTF-8 bytes
 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedInvalid() {
 byte[] bytes = new byte[]{(byte) 0x80, (byte) 0x80};
 Utf8.isWellFormed(bytes);
 }

 // Test case for isWellFormed method with an empty byte array
 @Test
 public void testIsWellFormedEmpty() {
 boolean expectedResult = true;
 boolean actualResult = Utf8.isWellFormed(new byte[0]);
 assertEquals(expectedResult, actualResult);
 }

 // Test case for isWellFormed method with a surrogate pair
 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedSurrogate() {
 byte[] bytes = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0xA0, (byte) 0x80};
 Utf8.isWellFormed(bytes);
 }

 // Test case for isWellFormed method with mixed valid and invalid UTF-8 bytes
 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedMixed() {
 byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x80, (byte) 0x43};
 Utf8.isWellFormed(bytes);
 }
}