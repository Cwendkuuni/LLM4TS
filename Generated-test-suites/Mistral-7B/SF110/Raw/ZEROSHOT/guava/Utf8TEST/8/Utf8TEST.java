package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
 int length = Utf8.encodedLength(sequence);
 assertEquals((int) sequence.length(), length);

 sequence = "\u00a0\u0304\u0305\u0306";
 length = Utf8.encodedLength(sequence);
 assertEquals(12, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithIllegalArgument() {
 CharSequence sequence = "\uD800";
 try {
 Utf8.encodedLength(sequence);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEncodedLengthGeneral() {
 CharSequence sequence = "abcdefghijklmnopqrstuvwxyz\uD800\uDC00";
 int length = Utf8.encodedLengthGeneral(sequence, 0);
 assertEquals(31, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthGeneralWithIllegalArgument() {
 CharSequence sequence = "\uD800\uDC00";
 try {
 Utf8.encodedLengthGeneral(sequence, 0);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = "abcdefghijklmnopqrstuvwxyz".getBytes();
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = "\u00a0\u0304\u0305\u0306".getBytes();
 assertTrue(Utf8.isWellFormed(bytes));

 bytes = "\uD800\uDC00".getBytes();
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithInvalidByte() {
 byte[] bytes = new byte[]{-1, 0};
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = new byte[]{0, -63, 0, -62, 0, -63, 0, -62, 0, -63, 0, -62};
 assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedSlowPathWithIllegalArgument() {
 byte[] bytes = new byte[]{0, -63, 0, -62, 0, -63, 0, -62, 0, -63, 0, -62, -1};
 try {
 Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}