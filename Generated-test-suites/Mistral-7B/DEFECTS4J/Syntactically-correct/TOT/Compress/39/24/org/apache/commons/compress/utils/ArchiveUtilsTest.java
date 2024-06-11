package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.test.ArchiveTestUtils;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

 @Test
 public void testToStringMethod() {
 ArchiveEntry exampleEntry = ArchiveTestUtils.createTestEntry("testfile.txt", false, 10);
 String toStringResult = ArchiveUtils.toString(exampleEntry);
 assertEquals("- 10 testfile.txt", toStringResult);
 }

 @Test
 public void testMatchAsciiBufferMethod() {
 byte[] buffer1 = toAsciiBytes("ExpectedString");
 byte[] buffer2 = Arrays.copyOfRange(buffer1, 0, 5);
 boolean result = ArchiveUtils.matchAsciiBuffer(new String(buffer1), buffer2, 0, 5);
 assertTrue(result);
 }

 @Test
 public void testMatchAsciiBufferMethodFalse() {
 byte[] buffer1 = toAsciiBytes("ExpectedString");
 byte[] buffer2 = Arrays.copyOfRange(buffer1, 0, 5);
 byte wrongByte = (byte) 0x1a; // control character
 buffer2[0] = wrongByte;
 boolean result = ArchiveUtils.matchAsciiBuffer(new String(buffer1), buffer2, 0, 5);
 assertFalse(result);
 }

 @Test
 public void testMatchAsciiBufferMethodEmptyStrings() {
 String string1 = "";
 byte[] buffer1 = toAsciiBytes(string1);
 String string2 = "";
 byte[] buffer2 = new byte[0];
 boolean result = ArchiveUtils.matchAsciiBuffer(string1, buffer2);
 assertTrue(result);
 }

 @Test
 public void testMatchAsciiBufferMethodNullString() {
 String string1 = null;
 byte[] buffer1 = toAsciiBytes("ExpectedString");
 byte[] buffer2 = null;
 boolean result = ArchiveUtils.matchAsciiBuffer(string1, buffer2);
 assertFalse(result);
 }

 @Test
 public void testIsEqualMethod() {
 byte[] byteArray1 = toAsciiBytes("ExpectedString1");
 byte[] byteArray2 = toAsciiBytes("ExpectedString2");
 boolean result = ArchiveUtils.isEqual(byteArray1, 0, byteArray1.length, byteArray2, 0, byteArray2.length);
 assertFalse(result);
}

 @Test
 public void testIsEqualMethodWithSameStrings() {
 byte[] byteArray1 = toAsciiBytes("ExpectedString1");
 byte[] byteArray2 = toAsciiBytes("ExpectedString1");
 boolean result = ArchiveUtils.isEqual(byteArray1, 0, byteArray1.length, byteArray2, 0, byteArray2.length, false);
 assertTrue(result);
 }

 @Test
 public void testIsEqualMethodWithTrailingNulls() {
 byte[] byteArray1 = toAsciiBytes("ExpectedString1");
 byte[] byteArray2 = Arrays.copyOfRange(byteArray1, 0, byteArray1.length - 1);
 byte[] byteArray3 = toAsciiBytes("ExpectedString2");
 byte[] byteArray4 = Arrays.copyOfRange(byteArray3, 0, byteArray3.length - 1);
 boolean result = ArchiveUtils.isEqual(byteArray1, 0, byteArray1.length, byteArray2, 0, byteArray2.length, true);
 boolean result2 = ArchiveUtils.isEqual(byteArray3, 0, byteArray3.length, byteArray4, 0, byteArray4.length, true);
 assertTrue(result);
 assertTrue(result2);
 }

 @Test
 public void testIsEqualMethodWithNullArrays() {
 byte[] byteArray1 = null;
 byte[] byteArray2 = toAsciiBytes("ExpectedString");
 boolean result = ArchiveUtils.isEqual(byteArray1, 0, byteArray1.length, byteArray2, 0, byteArray2.length, false);
 assertFalse(result);
 }

 @Test
 public void testIsEqualMethodWithDifferentLengths() {
 byte[] byteArray1 = toAsciiBytes("ExpectedString1");
 byte[] byteArray2 = Arrays.copyOfRange(byteArray1, 0, byteArray1.length - 1);
 boolean result = ArchiveUtils.isEqual(byteArray1, 0, byteArray1.length, byteArray2, 0, byteArray2.length, false);
 assertFalse(result);
 }

 @Test
 public void testIsEqualMethodWithOnlyTrailingNulls() {
 byte[] byteArray1 = toAsciiBytes("ExpectedString1");
 byte[] byteArray2 = Arrays.copyOfRange(byteArray1, 0, byteArray1.length - 1);
 byte[] byteArray3 = toAsciiBytes("ExpectedString2");
 byte[] byteArray4 = Arrays.copyOfRange(byteArray3, 0, byteArray3.length - 1);
 byte[] byteArray5 = toAsciiBytes("ExpectedString1");
 byte[] byteArray6 = Arrays.copyOfRange(byteArray5, 0, byteArray5.length - 1);
 boolean result = ArchiveUtils.isEqual(byteArray1, 0, byteArray1.length, byteArray2, 0, byteArray2.length, true);
 boolean result2 = ArchiveUtils.isEqual(byteArray3, 0, byteArray3.length, byteArray4, 0, byteArray4.length, true);
 boolean result3 = ArchiveUtils.isEqual(byteArray5, 0, byteArray5.length, byteArray6, 0, byteArray6.length, true);
 assertTrue(result);
 assertTrue(result2);
 assertTrue(result3);
 }

 @Test
 public void testIsEqualMethodWithEqualArraysButDifferentOffsets() {
 byte[] byteArray1 = toAsciiBytes("ExpectedString1");
 byte[] byteArray2 = Arrays.copyOfRange(byteArray1, 3, byteArray1.length);
 byte[] byteArray3 = toAsciiBytes("ExpectedString2");
 byte[] byteArray4 = Arrays.copyOfRange(byteArray3, 3, byteArray3.length);
 boolean result = ArchiveUtils.isEqual(byteArray1, 0, byteArray1.length, byteArray2, 0, byteArray2.length, false);
 boolean result2 = ArchiveUtils.isEqual(byteArray3, 0, byteArray3.length, byteArray4, 0, byteArray4.length, false);
 assertTrue(result);
 assertTrue(result2);
 }

 @Test
 public void testIsEqualMethodWithEqualArraysButDifferentOffsetsAndLengths() {
 byte[] byteArray1 = toAsciiBytes("ExpectedString1");
 byte[] byteArray2 = Arrays.copyOfRange(byteArray1, 3, byteArray1.length);
 byte[] byteArray3 = Arrays.copyOfRange(toAsciiBytes("ExpectedString2"), 3, toAsciiBytes("ExpectedString2").length);
 boolean result = ArchiveUtils.isEqual(byteArray1, 0, byteArray1.length, byteArray2, 0, byteArray2.length, false);
 boolean result2 = ArchiveUtils.isEqual(byteArray3, 0, byteArray3.length, byteArray2, 0, byteArray2.length, false);
 assertTrue(result);
 assertTrue(result2);
 }

 @Test
 public void testIsEqualMethodWithEqualArraysButDifferentOffsetsAndLengthsAndTrailingNulls() {
 byte[] byteArray1 = toAsciiBytes("ExpectedString1");
 byte[] byteArray2 = Arrays.copyOfRange(byteArray1, 3, byteArray1.length);
 byte[] byteArray3 = Arrays.copyOfRange(toAsciiBytes("ExpectedString2"), 3, toAsciiBytes("ExpectedString2").length);
 byte[] byteArray4 = Arrays.copyOfRange(byteArray1, 0, byteArray1.length - 1);
 byte[] byteArray5 = Arrays.copyOfRange(byteArray3, 0, byteArray3.length - 1);
 boolean result = ArchiveUtils.isEqual(byteArray1, 0, byteArray1.length, byteArray2, 0, byteArray2.length, true);
 boolean result2 = ArchiveUtils.isEqual(byteArray3, 0, byteArray3.length, byteArray4, 0, byteArray4.length, true);
 boolean result3 = ArchiveUtils.isEqual(byteArray1, 0, byteArray1.length, byteArray5, 0, byteArray5.length, true);
 assertTrue(result);
 assertTrue(result2);
 assertTrue(result3);
 }

 @Test
 public void testIsEqualMethodWithEqualArraysButDifferentOffsetsAndLengthsAndTrailingNullsAndIgnoreTrailingNulls() {
 byte[] byteArray1 = toAsciiBytes("ExpectedString1");
 byte[] byteArray2 = Arrays.copyOfRange(byteArray1, 3, byteArray1.length);
 byte[] byteArray3 = Arrays.copyOfRange(toAsciiBytes("ExpectedString2"), 3, toAsciiBytes("ExpectedString2").length);
 byte[] byteArray4 = Arrays.copyOfRange(byteArray1, 0, byteArray1.length - 1);
 byte[] byteArray5 = Arrays.copyOfRange(byteArray3, 0, byteArray3.length - 1);
 boolean result = ArchiveUtils.isEqual(byteArray1, 0, byteArray1.length, byteArray2, 0, byteArray2.length, false);
 boolean result2 = ArchiveUtils.isEqual(byteArray3, 0, byteArray3.length, byteArray4, 0, byteArray4.length, false);
 boolean result3 = ArchiveUtils.isEqual(byteArray1, 0, byteArray1.length, byteArray5, 0, byteArray5.length, true);
 assertTrue(result);
 assertTrue(result2);
 assertTrue(result3);
 }

 @Test
 public void testIsArrayZeroMethod() {
 byte[] byteArray = new byte[0];
 boolean result = ArchiveUtils.isArrayZero(byteArray, 0);
 assertTrue(result);
 }

 @Test
 public void testIsArrayZeroMethodFalse() {
 byte[] byteArray = {1, 2, 3};
 boolean result = ArchiveUtils.isArrayZero(byteArray, 0);
 assertFalse(result);
 }

 @Test
 public void testSanitizeMethod() {
 String inputString = "abç123ë ÀÄÅÅÅÅÅÅ";
 String result = ArchiveUtils.sanitize(inputString);
 assertEquals("ab?123? À?Ä?Å?Å?Å?Å?", result);
 }

}