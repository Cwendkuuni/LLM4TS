package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "UTF-8 length test";
 int length = Utf8.encodedLength(sequence);
 byte[] expectedBytes = sequence.toString().getBytes(StandardCharsets.UTF_8);
 int expectedUtf8Length = expectedBytes.length;
 assertEquals(expectedUtf8Length, length);

 sequence = "\uD800";
 try {
 length = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 sequence = "\uD800\uDC00";
 try {
 length = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "UTF-8 length test";
 int startIndex = 1;
 int length = Utf8.encodedLengthGeneral(sequence, startIndex);
 byte[] expectedBytes = sequence.subSequence(0, startIndex).toString().getBytes(StandardCharsets.UTF_8);
 byte[] restBytes = sequence.subSequence(startIndex, sequence.length()).getBytes(StandardCharsets.UTF_8);
 byte[] combinedBytes = new byte[expectedBytes.length + restBytes.length];
 System.arraycopy(expectedBytes, 0, combinedBytes, 0, expectedBytes.length);
 System.arraycopy(restBytes, 0, combinedBytes, expectedBytes.length, restBytes.length);
 int expectedUtf8Length = combinedBytes.length;
 assertEquals(expectedUtf8Length, length);
 }

 @Test
 public void testIsWellFormed() {
 byte[] wellFormedBytes = "UTF-8 well formed".getBytes(StandardCharsets.UTF_8);
 assertTrue(Utf8.isWellFormed(wellFormedBytes));

 byte[] invalidBytes = new byte[]{-45, -61, -6D, 66, -61, -6D, 65, 6E, 0};
 assertFalse(Utf8.isWellFormed(invalidBytes));
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = new byte[]{0, -35, -86, 63, 61, 6D, 65, 6E, 0, 0};
 assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

 byte[] invalidBytes = new byte[]{-45, -61, -6D, 66, -61, -6D, 65, 6E, -4, 3};
 assertFalse(Utf8.isWellFormedSlowPath(invalidBytes, 0, invalidBytes.length));
 }
}