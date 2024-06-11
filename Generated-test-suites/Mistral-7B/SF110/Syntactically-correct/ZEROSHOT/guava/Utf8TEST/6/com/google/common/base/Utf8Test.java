package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int expectedLength = sequence.length();
 int actualLength = Utf8.encodedLength(sequence);
 assertEquals(expectedLength, actualLength);

 sequence = "\u00A0\u03B4\u03C0\u03C3\u03F5"; // Unicode characters for Latin-1 Supplement and Greek Extended
 expectedLength = sequence.length();
 actualLength = Utf8.encodedLength(sequence);
 assertEquals(expectedLength, actualLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthThrowsExceptionWithIllegalArgument() {
 CharSequence sequence = "abcdefghijklm\uFFFE";
 Utf8.encodedLength(sequence);
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz\uD800\uDC00"; // Unicode characters for Basic Multilingual Plane and Surrogate
 int expectedLength = sequence.length();
 int actualLength = Utf8.encodedLengthGeneral(sequence, 0);
 assertEquals(expectedLength, actualLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthGeneralThrowsExceptionWithIllegalArgument() {
 CharSequence sequence = "abcdefghijklm\uD801"; // Unpaired surrogate
 Utf8.encodedLengthGeneral(sequence, 0);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = "abcdefghijklmnopqrstuvwxyz".getBytes();
 boolean expectedResult = true;
 boolean actualResult = Utf8.isWellFormed(bytes);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsWellFormedWithInvalidByte() {
 byte[] bytes = {-1, 0};
 boolean expectedResult = false;
 boolean actualResult = Utf8.isWellFormed(bytes);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = {-1, -69, 65, 65, 65};
 boolean expectedResult = false;
 boolean actualResult = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsWellFormedSlowPathWithValidByteSequence() {
 byte[] bytes = {-1, -68, 65, 65, 65}; // 1 byte sequence: \u00FF followed by valid bytes
 boolean expectedResult = true;
 boolean actualResult = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
 assertEquals(expectedResult, actualResult);
 }
}