package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

 @Test
 public void testToString() {
 ArchiveEntry entry = new ArchiveEntry("test.txt");
 assertEquals("- 0 test.txt", ArchiveUtils.toString(entry));

 entry.setSize(10);
 assertEquals("- 10 test.txt", ArchiveUtils.toString(entry));

 entry.setDirectory(true);
 assertEquals("d 0 test.txt", ArchiveUtils.toString(entry));
 }

 @Test
 public void testMatchAsciiBuffer() {
 final String expected = "test";
 final byte[] buffer = toAsciiBytes(expected);
 assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
 }

 @Test(expected = RuntimeException.class)
 public void testMatchAsciiBufferWithInvalidEncoding() {
 final String expected = "test";
 final byte[] buffer = new byte[5]; // not long enough to hold "test"
 assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer)); // should throw an exception
 }

 @Test
 public void testMatchAsciiBufferWithDifferentLength() {
 final String expected = "test";
 final byte[] buffer = toAsciiBytes(expected);
 final byte[] differentLengthBuffer = new byte[buffer.length + 1];
 System.arraycopy(buffer, 0, differentLengthBuffer, 0, buffer.length);
 differentLengthBuffer[buffer.length] = (byte) 0x01;
 assertFalse(ArchiveUtils.matchAsciiBuffer(expected, differentLengthBuffer));
 }

 @Test
 public void testIsEqual() {
 final byte[] buffer1 = toAsciiBytes("test1");
 final byte[] buffer2 = toAsciiBytes("test1");
 final byte[] buffer3 = toAsciiBytes("test2");

 assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
 assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));

 final byte[] buffer4 = new byte[buffer1.length + 1];
 System.arraycopy(buffer1, 0, buffer4, 0, buffer1.length);
 buffer4[buffer1.length] = (byte) 0x01;
 assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false));

 final byte[] buffer5 = new byte[buffer1.length];
 System.arraycopy(buffer1, 0, buffer5, 0, buffer1.length);
 assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length, false));
 }

 @Test
 public void testIsEqualWithNull() {
 final byte[] buffer1 = new byte[5];
 Arrays.fill(buffer1, (byte) 0x00);
 final byte[] buffer2 = new byte[5];
 Arrays.fill(buffer2, (byte) 0x00);
 final byte[] buffer3 = new byte[4];
 Arrays.fill(buffer3, (byte) 0x00);

 assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
 assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
 }

 @Test
 public void testSanitize() {
 final String input = "test\u0001\u001Ftest";
 final String sanitized = ArchiveUtils.sanitize(input);
 assertEquals("test??test", sanitized);
 }
}