package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Utf8Test {

 private static final CharSequence sequence = "Mëtäl Hëll";
 private static final byte[] bytes = new byte[]{(byte) 0x4d, (byte) 0xc3, (byte) 0x65, (byte) 0x65, (byte) 0x74, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x69, (byte) 0x6e, (byte) 0x65};

 @Test
 public void testEncodedLength() {
     int actual = Utf8.encodedLength(sequence);
     int expected = 12;
     assertEquals(expected, actual);
 }

 @Test
 public void testEncodedLengthWithSurrogate() {
     CharSequence surrogateSequence = Character.toString((char) 0x10400);
     int actual = Utf8.encodedLength(surrogateSequence);
     int expected = 4;
     assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthThrowsExceptionWhenLengthDoesNotFitInInt() {
     CharSequence sequence = new StringBuilder().append("A").append(Integer.MAX_VALUE).toString();
     assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(sequence));
 }

 @Test
 public void testIsWellFormedWithValidBytes() {
     boolean actual = Utf8.isWellFormed(bytes);
     boolean expected = true;
     assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormedWithInvalidBytes() {
     byte[] invalidBytes = new byte[]{0x78, 0xf8, 0x78, 0xf8};
     boolean actual = Utf8.isWellFormed(invalidBytes);
     boolean expected = false;
     assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormedWithInvalidBytesSlowPath() {
     byte[] invalidBytes = new byte[]{0x78, (byte) 0x80};
     boolean actual = Utf8.isWellFormedSlowPath(invalidBytes, 0, invalidBytes.length);
     boolean expected = false;
     assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormedWithEmptyBytes() {
     byte[] emptyBytes = new byte[]{};
     boolean actual = Utf8.isWellFormed(emptyBytes);
     boolean expected = true;
     assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormedWithLongBytes() {
     byte[] longBytes = new byte[1024 * 1024];
     boolean actual = Utf8.isWellFormed(longBytes);
     boolean expected = true;
     assertEquals(expected, actual);
 }

 @Test
 public void testIsWellFormedWithNullBytes() {
     assertFalse(Utf8.isWellFormed(null));
 }

 @Test
 public void testIsWellFormedWithNegativeOffset() {
     byte[] bytes = new byte[]{0x41};
     assertThrows(IndexOutOfBoundsException.class, () -> Utf8.isWellFormed(bytes, -1, bytes.length));
 }

 @Test
 public void testIsWellFormedWithNegativeLength() {
     byte[] bytes = new byte[]{0x41};
     assertThrows(IndexOutOfBoundsException.class, () -> Utf8.isWellFormed(bytes, 0, -1));
 }

 @Test
 public void testIsWellFormedWithOffsetGreaterThanLength() {
     byte[] bytes = new byte[]{0x41};
     assertThrows(IndexOutOfBoundsException.class, () -> Utf8.isWellFormed(bytes, bytes.length, 1));
 }

 @Test
 public void testIsWellFormedWithOffsetEqualToLength() {
     byte[] bytes = new byte[]{0x41};
     boolean actual = Utf8.isWellFormed(bytes, 0, bytes.length);
     boolean expected = true;
     assertEquals(expected, actual);
 }
}