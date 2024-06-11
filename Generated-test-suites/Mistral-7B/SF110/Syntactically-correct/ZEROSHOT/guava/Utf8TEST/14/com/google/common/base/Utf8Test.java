package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "Utf8Test";
 int length = Utf8.encodedLength(sequence);
 int expectedLength = sequence.length() * 7 / 4 + 2;
 assertEquals("Unexpected encoded length for sequence '" + sequence + "'", expectedLength, length);

 sequence = "\uD800\uDC00";
 try {
 length = Utf8.encodedLength(sequence);
 fail("Expected IllegalArgumentException for sequence '" + sequence + "'");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "Utf8Test";
 int length = Utf8.encodedLengthGeneral(sequence, 0);
 int expectedLength = sequence.length() * 7 / 4 + 2;
 assertEquals("Unexpected encoded length for sequence '" + sequence + "'", expectedLength, length);

 sequence = "\uD800\uDC00";
 try {
 length = Utf8.encodedLengthGeneral(sequence, 0);
 fail("Expected IllegalArgumentException for sequence '" + sequence + "'");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = "Utf8Test".getBytes();
 assertTrue("Unexpected result for byte array '" + bytes + "'", Utf8.isWellFormed(bytes));

 bytes = new byte[] { -128, -65, 65, 65, 65, -63, 65, 65 };
 assertTrue("Unexpected result for byte array '" + bytes + "'", Utf8.isWellFormed(bytes));

 bytes = new byte[] { -32, 226, 155, 132 };
 assertFalse("Unexpected result for byte array '" + bytes + "'", Utf8.isWellFormed(bytes));

 bytes = new byte[] { -32, 226, 155, 130, 142, 135, 156, 142 };
 assertTrue("Unexpected result for byte array '" + bytes + "'", Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = new byte[] { -128, -65, 65, 65, 65, -63, 65, 65 };
 assertTrue("Unexpected result for byte array '" + bytes + "'", Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

 bytes = new byte[] { -32, 226, 155, 132, 65, 65 };
 assertFalse("Unexpected result for byte array '" + bytes + "'", Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

 bytes = new byte[] { -32, 226, 155, 132, 65, 65, 127, 127 };
 assertTrue("Unexpected result for byte array '" + bytes + "'", Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }

}