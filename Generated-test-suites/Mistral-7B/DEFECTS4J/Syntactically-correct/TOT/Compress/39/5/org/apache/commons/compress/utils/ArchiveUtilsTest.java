package org.apache.commons.compress.utils.test;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

 @Test
 public void testToString(){
 ArchiveEntry archiveEntry = new ArchiveEntry("testfile.txt");
 String result = ArchiveUtils.toString(archiveEntry);
 String expected = "-\t0\t testfile.txt";
 assertEquals(expected, result);
 }

 @Test
 public void testMatchAsciiBuffer_CorrectlyMatches(){
 String expected = "teststring";
 byte[] buffer = ArchiveUtils.toAsciiBytes(expected);
 boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 assertTrue(actual);
 }

 @Test
 public void testMatchAsciiBuffer_Mismatches(){
 String expected = "teststring";
 byte[] buffer = new byte[]{'t', 'e', 's', 't', 'r', 'i', 'n', 'g', 0x01};
 boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 assertFalse(actual);
 }

 @Test
 public void testMatchAsciiBuffer_EmptyString(){
 byte[] buffer = new byte[0];
 String expected = "";
 boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 assertTrue(actual);
 }

 @Test(expected = RuntimeException.class)
 public void testMatchAsciiBuffer_UnsupportedEncodingException(){
 String expected = "teststring";
 byte[] buffer = new byte[10]; // Buffer longer than expected string
 boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 }

 @Test
 public void testIsEqual_ByteArrays_SameSize_Equal(){
 byte[] buffer1 = new byte[]{0x01, 0x02, 0x03};
 byte[] buffer2 = new byte[]{0x01, 0x02, 0x03};
 boolean actual = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 assertTrue(actual);
 }

 @Test
 public void testIsEqual_ByteArrays_SameSize_Different(){
 byte[] buffer1 = new byte[]{0x01, 0x02, 0x03};
 byte[] buffer2 = new byte[]{0x01, 0x04, 0x05};
 boolean actual = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 assertFalse(actual);
 }

 @Test
 public void testIsEqual_ByteArrays_DifferentSizes_EqualTrailingNulls(){
 byte[] buffer1 = new byte[]{0x01, 0x02, 0x03, 0};
 byte[] buffer2 = new byte[]{0x01, 0x02, 0x03, 0, 0};
 boolean actual = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 assertTrue(actual);
 }

 @Test
 public void testIsEqual_ByteArrays_DifferentSizes_NoTrailingNulls(){
 byte[] buffer1 = new byte[]{0x01, 0x02, 0x03};
 byte[] buffer2 = new byte[]{0x01, 0x02, 0x03, 0x04};
 boolean actual = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 assertFalse(actual);
 }

 @Test
 public void testIsEqual_ByteArrays_SameSize_IgnoreTrailingNulls(){
 byte[] buffer1 = new byte[]{0x01, 0x02, 0x03, 0};
 byte[] buffer2 = new byte[]{0x01, 0x02, 0x03, 0, 0};
 boolean actual = ArchiveUtils.isEqual(buffer1, buffer1, true);
 assertTrue(actual);
 }

 @Test
 public void testIsEqual_ByteArrays_SameSize_Different(){
 byte[] buffer1 = new byte[]{0x01, 0x02, 0x03};
 byte[] buffer2 = new byte[]{0x01, 0x02, 0x04};
 boolean actual = ArchiveUtils.isEqual(buffer1, buffer1, true);
 assertFalse(actual);
 }

 @Test
 public void testIsEqual_ByteArrays_DifferentSizes_EqualTrailingNulls(){
 byte[] buffer1 = new byte[]{0x01, 0x02, 0x03, 0};
 byte[] buffer2 = new byte[]{0x01, 0x02, 0x03, 0, 0, 0};
 boolean actual = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 assertTrue(actual);
 }

 @Test
 public void testIsEqual_ByteArrays_DifferentSizes_NoTrailingNulls(){
 byte[] buffer1 = new byte[]{0x01, 0x02, 0x03};
 byte[] buffer2 = new byte[]{0x01, 0x02, 0x03, 0x04};
 boolean actual = ArchiveUtils.isEqual(buffer1, buffer1, false);
 assertFalse(actual);
 }
}