package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int length = Utf8.encodedLength(sequence);
 assertEquals(23, length);

 sequence = "\u00A0\u0393\u0394\u0395";
 length = Utf8.encodedLength(sequence);
 assertEquals(11, length);

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
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int length = Utf8.encodedLengthGeneral(sequence, 0);
 assertEquals(23, length);

 sequence = "\u0393\u0394\u0395";
 int start = 1;
 length = Utf8.encodedLengthGeneral(sequence, start);
 assertEquals(3, length);

 sequence = "\uD800\uDC00";
 try {
 length = Utf8.encodedLengthGeneral(sequence, 0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[] { (byte) 0x72, (byte) 0x6F, (byte) 0x63, (byte) 0x65, (byte) 0x74, (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x65, (byte) 0x6E, (byte) 0x63, (byte) 0x6F, (byte) 0x64, (byte) 0x65, (byte) 0x64 };
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[] { (byte) 0x72, (byte) 0x6F, (byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0x74, (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x65, (byte) 0x6E, (byte) 0x63, (byte) 0x6F, (byte) 0x64, (byte) 0x65, (byte) 0x64 };
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedByteArray() {
 byte[] bytes = new byte[] { 0x72, 0x6F, 0x63, 0x65, 0x74, 0x73, 0x74, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x65, 0x64 };
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = new byte[] { 0x72, 0x6F, (byte) 0xE2, (byte) 0x82, (byte) 0xAC, 0x74, 0x73, 0x74, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x65, 0x64 };
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedIntInt() {
 byte[] bytes = new byte[] { 0x72, 0x6F, 0x63, 0x65, 0x74, 0x73, 0x74, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x65, 0x64 };
 assertTrue(Utf8.isWellFormed(bytes, 0, bytes.length));

 bytes = new byte[] { 0x72, 0x6F, (byte) 0xE2, (byte) 0x82, (byte) 0xAC, 0x74, 0x73, 0x74, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x65, 0x64 };
 assertFalse(Utf8.isWellFormed(bytes, 0, bytes.length));
 }

 @Test
 public void testIsWellFormedInvalidIndex() {
 byte[] bytes = new byte[] { 0x72, 0x6F, 0x63, 0x65, 0x74, 0x73, 0x74, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x65, 0x64 };
 try {
 Utf8.isWellFormed(bytes, -1, bytes.length);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testIsWellFormedInvalidLength() {
 byte[] bytes = new byte[] { 0x72, 0x6F, 0x63, 0x65, 0x74, 0x73, 0x74, 0x20, 0x65, 0x6E, 0x63, 0x6F, 0x64, 0x65, 0x64 };
 try {
 Utf8.isWellFormed(bytes, 0, -1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}