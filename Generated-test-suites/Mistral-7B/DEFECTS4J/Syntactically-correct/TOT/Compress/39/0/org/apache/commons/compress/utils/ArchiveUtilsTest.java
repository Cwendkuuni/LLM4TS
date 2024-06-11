package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.test.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

 @Test
 public void testToStringMethod() {
 ArchiveEntry testEntry = TestUtils.createTestArchiveEntry("path/to/file.txt", false, 1024);
 String toStringResult = ArchiveUtils.toString(testEntry);
 String expectedOutput = "- 1024 path/to/file.txt";
 assertEquals(expectedOutput, toStringResult);
 }

 @Test
 public void testMatchAsciiBufferMethodWithStringAndByteArray() {
 String expected = "expectedString";
 byte[] buffer = toAsciiBytes(expected);
 boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 assertTrue(result);
 }

 @Test
 public void testMatchAsciiBufferMethodWithByteArrayAndByteArray() {
 String expected = "expectedString";
 byte[] buffer1 = toAsciiBytes(expected);
 byte[] buffer2 = Arrays.copyOfRange(buffer1, 0, 5);
 boolean result = ArchiveUtils.matchAsciiBuffer(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 assertTrue(result);
 }

 @Test(expected = RuntimeException.class)
 public void testMatchAsciiBufferMethodWithInvalidEncoding() {
 String expected = "expectedString";
 byte[] buffer = expected.getBytes(); // this will use default encoding, which is not US-ASCII
 boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 fail("Expected RuntimeException to be thrown");
 }

 @Test
 public void testIsEqualMethodWithByteArraysAndOffsetsAndLengths() {
 byte[] buffer1 = new byte[] { 1, 2, 3, 4 };
 byte[] buffer2 = new byte[] { 1, 2, 3, 4 };
 boolean result = ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 4);
 assertTrue(result);
 }

 @Test
 public void testIsEqualMethodWithByteArraysAndDifferentLengths() {
 byte[] buffer1 = new byte[] { 1, 2, 3, 4 };
 byte[] buffer2 = new byte[] { 1, 2, 3 };
 boolean result = ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 3);
 assertFalse(result);
 }

 @Test
 public void testIsEqualMethodWithByteArraysAndDifferentOffsetsAndLengths() {
 byte[] buffer1 = new byte[] { 1, 2, 3, 4 };
 byte[] buffer2 = new byte[] { 3, 2, 1 };
 boolean result = ArchiveUtils.isEqual(buffer1, 2, 2, buffer2, 0, 3);
 assertFalse(result);
 }

 @Test
 public void testIsEqualMethodWithByteArrays() {
 byte[] buffer1 = new byte[] { 1, 2, 3, 4 };
 byte[] buffer2 = new byte[] { 1, 2, 3, 4 };
 boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
 assertTrue(result);
 }

 @Test
 public void testIsEqualMethodWithByteArraysAndDifferentContents() {
 byte[] buffer1 = new byte[] { 1, 2, 3, 4 };
 byte[] buffer2 = new byte[] { 1, 2, 3, 5 };
 boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
 assertFalse(result);
 }

 @Test
 public void testIsEqualMethodWithByteArraysAndTrailingNulls() {
 byte[] buffer1 = new byte[] { 1, 0, 0 };
 byte[] buffer2 = new byte[] { 1, 2, 3, 0, 0 };
 boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 5);
 assertTrue(result);
 }

 @Test
 public void testIsEqualMethodWithByteArraysAndTrailingNullsButDifferentLengths() {
 byte[] buffer1 = new byte[] { 1, 0, 0 };
 byte[] buffer2 = new byte[] { 1, 2, 3, 0 };
 boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 4);
 assertFalse(result);
 }

 @Test
 public void testIsEqualMethodWithByteArraysWithTrailingNullsAndDifferentContents() {
 byte[] buffer1 = new byte[] { 1, 0, 0 };
 byte[] buffer2 = new byte[] { 1, 2, 3, 0, 5 };
 boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 5);
 assertFalse(result);
 }

 @Test
 public void testIsArrayZeroMethod() {
 byte[] zeroArray = new byte[5];
 assertTrue(ArchiveUtils.isArrayZero(zeroArray, 5));
 }

 @Test
 public void testIsArrayZeroMethodWithPartialZeroArray() {
 byte[] zeroArray = new byte[] { 0, 1, 0, 1, 0 };
 assertTrue(ArchiveUtils.isArrayZero(zeroArray, 3));
 }

 @Test
 public void testIsArrayZeroMethodWithNonZeroArray() {
 byte[] nonZeroArray = new byte[] { 1, 1, 1 };
 assertFalse(ArchiveUtils.isArrayZero(nonZeroArray, 3));
 }

 @Test
 public void testSanitizeMethod() {
 String testString = "This\u0061s\u00E2\u0303\u0304test";
 String sanitizedString = ArchiveUtils.sanitize(testString);
 String expectedOutput = "This?s??test";
 assertEquals(expectedOutput, sanitizedString);
 }
}