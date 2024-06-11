package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 char[] sequence = {'\u0041', '\u00E4', '\u03A9', '\u041C', '\uF09F'};
 int length = Utf8.encodedLength(sequence);
 assertEquals(11, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthWithInvalidSequence() {
 char[] sequence = {'\u0041', '\u00E4', '\uD800', '\uDFFF', '\uF09F'};
 try {
 Utf8.encodedLength(sequence);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEncodedLengthWithEmptySequence() {
 char[] sequence = {};
 int length = Utf8.encodedLength(sequence);
 assertEquals(0, length);
 }

 @Test
 public void testEncodedLengthWithSingleByteSequence() {
 char[] sequence = {'A'};
 int length = Utf8.encodedLength(sequence);
 assertEquals(1, length);
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = {0x41, 0xE4, 0x303, 0x43, 0xF0};
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedWithInvalidBytes() {
 byte[] bytes = {0x41, (byte) 0xE7, 0x303, 0x43, 0xF0};
 try {
 Utf8.isWellFormed(bytes);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIsWellFormedWithEmptyArray() {
 byte[] bytes = {};
 assertTrue(Utf8.isWellFormed(bytes));
 }

 @Test
 public void testIsWellFormedWithSingleByteArray() {
 byte[] bytes = {(byte) 0x41};
 assertTrue(Utf8.isWellFormed(bytes));
 }
}