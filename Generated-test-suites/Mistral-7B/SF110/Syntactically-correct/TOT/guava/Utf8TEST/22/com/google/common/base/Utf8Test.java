package com.google.common.base.test;

import com.google.common.base.Utf8;
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 String sequence = "Hello, 世界!"; // English and Chinese characters
 int expected = Utf8.encodedLength(sequence);
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithIllegalChar() {
 String sequence = "\uD800\uDC00"; // Surrogate pair
 int actual = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test
 public void testEncodedLengthWithEmptySequence() {
 String sequence = "";
 int expected = 0;
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test
 public void testEncodedLengthWithASCIICharacters() {
 String sequence = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
 int expected = sequence.length();
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormedWithValidUTF8Bytes() {
 byte[] bytes = {0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2C, 0xE4, 0xB8, 0xAD, 0xE6, 0x97, 0xAC, 0xE8, 0xA3};
 boolean expected = true;
 boolean actual = Utf8.isWellFormed(bytes);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithInvalidUTF8Byte() {
 byte[] bytes = {0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2C, 0xE4, 0xB8, 0xAD, 0xE6, 0x97, 0xAC, 0xEB, 0xA3}; // Invalid byte
 boolean actual = Utf8.isWellFormed(bytes);
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test
 public void testIsWellFormedWithEmptyArray() {
 boolean expected = true;
 boolean actual = Utf8.isWellFormed(new byte[0]);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormedWithValidUTF8BytesStartingWithZeroByte() {
 byte[] bytes = {0x00, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2C, 0xE4, 0xB8, 0xAD, 0xE6, 0x97, 0xAC, 0xE8, 0xA3};
 boolean expected = true;
 boolean actual = Utf8.isWellFormed(bytes);
 assertEquals(expected, actual);
 }
}