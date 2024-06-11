package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class Utf8Test {

 private static final String UTF8_TEST_STRING = "Mojito 🍹🍹🍹 A unicode string";

 @Test
 public void testEncodedLength() {
 charSequenceSequence = CharSequence.class.cast(UTF8_TEST_STRING);
 int encodedLength = Utf8.encodedLength(sequence);
 int expectedLength = sequence.length() * 4 / 3; // Approximate UTF-8 length
 assertEquals(expectedLength, encodedLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthNegativeSequenceLength() {
 final CharSequence sequence = new String("Mojito", -1);
 assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(sequence));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthUnpairedSurrogate() {
 final CharSequence sequence = new String("Mojito\uD800");
 assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(sequence));
 }

 @Test
 public void testIsWellFormedByteArray() {
 byte[] bytes = Utf8.encode(UTF8_TEST_STRING);
 assertNotNull(bytes);
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedInvalidByteSequence() {
 byte[] bytes = new byte[]{-1, 65, 65};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedSurrogatePairMissingFirstSurrogate() {
 byte[] bytes = new byte[]{-65, 65, 65};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedSurrogatePairMissingSecondSurrogate() {
 byte[] bytes = new byte[]{65, 65, 65};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedValidByteSequence() {
 byte[] bytes = new byte[]{77, 108, 105, 111, 105, 32, 83, 119, 111, 115, 104, 105, 108, 101, 115, 46, 32, 117, 108, 97, 99, 32, 110, 111, 116, 46, 116, 104, 101, 32, 116, 112, 112, 58, 32, 49, 53, 55, 45, 53};
 assertNotNull(bytes);
 assertTrue(Utf8.isWellFormed(bytes));
 }
}