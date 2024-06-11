package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 charSequence sequence = "Hello, 世界!";
 int expected = 11;
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithIllegalArgument() {
 CharSequence sequence = new StringCharacterIterator("", false); // Empty iterator
 int actual = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException but got " + actual);
 }

 @Test
 public void testEncodedLengthWithSurrogate() {
 charSequence sequence = "\uD800\uDC00";
 int expected = 4;
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithSurrogatePair() {
 charSequence sequence = "\uD800\uDFFF";
 int actual = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException but got " + actual);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = {0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0xe4, 0xb8, 0xad, 0xec};
 boolean expected = true;
 boolean actual = Utf8.isWellFormed(bytes);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormedWithInvalidByte() {
 byte[] bytes = {0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0xef, 0xbd, 0xec};
 boolean expected = false;
 boolean actual = Utf8.isWellFormed(bytes);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormedWithMixedByte() {
 byte[] bytes = {0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0xef, 0xbd, 0x74, 0x68, 0x69, 0x6e, 0x67};
 boolean expected = true;
 boolean actual = Utf8.isWellFormed(bytes);
 assertEquals(expected, actual);
 }
}