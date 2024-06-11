package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 charSequence sequence = "abcdef";
 int expected = 6;
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);

 sequence = "\u00A1\u0394\u041C\u0561\u0627\u062A\u093E\u093F";
 expected = 11;
 actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithSurrogate() {
 charSequence sequence = "\uD800\uDC00";
 Utf8.encodedLength(sequence);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[] { 72, 71, 65, 74, 79, 70, 65, 72, 65, 73, 73, 73, 73, 61, 72, 65, 73, 73 };
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithInvalidByte() {
 byte[] bytes = new byte[] { 72, 71, 65, 74, 79, 70, 65, 72, 65, 73, 73, -1, 61, 72, 65, 73, 73 };
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithInvalidSequence() {
 byte[] bytes = new byte[] { 72, 71, 65, 74, 79, (byte) 0xE0, 65, 72, 65, 73, 73, 73, 61, 72, 65, 73, 73 };
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithShortSequence() {
 byte[] bytes = new byte[] { 72, 71 };
 assertFalse(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithEmptySequence() {
 byte[] bytes = new byte[0];
 assertTrue(Utf8.isWellFormed(bytes));
 }

}