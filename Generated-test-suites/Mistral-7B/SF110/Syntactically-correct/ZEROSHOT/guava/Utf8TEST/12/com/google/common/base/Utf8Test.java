package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import static com.google.common.base.Utf8.*;

public class Utf8Test {

 @Test
 public void testEncodedLength() {
 char[] sequence = new char[]{'a', 'b', 'c'};
 int expected = 3;
 int actual = encodedLength(sequence);
 assertEquals(expected, actual);

 sequence = new char[]{'a', '\u0080'};
 expected = 3;
 actual = encodedLength(sequence);
 assertEquals(expected, actual);

 sequence = new char[]{'a', '\u0100', 'b'};
 expected = 4;
 actual = encodedLength(sequence);
 assertEquals(expected, actual);

 sequence = new char[]{'a', '\uD800', '\uDC00', 'b'};
 expected = 5;
 actual = encodedLength(sequence);
 assertEquals(expected, actual);

 sequence = new char[]{'a', '\uD800', '\uDC01', 'b'};
 expected = 6;
 actual = encodedLength(sequence);
 assertNotEquals(expected, actual); // Unpaired surrogate
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthIllegalArgument() {
 char[] sequence = new char[]{'a', '\uD800'};
 encodedLength(sequence); // Unpaired surrogate not detected
 }

 @Test
 public void testEncodedLengthGeneral() {
 char[] sequence = new char[]{'a', 'b', 'c'};
 int expected = 3;
 int actual = encodedLengthGeneral(sequence, 0);
 assertEquals(expected, actual);

 sequence = new char[]{'a', '\u0100', 'b'};
 expected = 4;
 actual = encodedLengthGeneral(sequence, 0);
 assertEquals(expected, actual);

 sequence = new char[]{'a', '\uD800', '\uDC00', 'b'};
 expected = 5;
 actual = encodedLengthGeneral(sequence, 0);
 assertEquals(expected, actual);

 sequence = new char[]{'a', '\uD800', '\uDC01', 'b'};
 expected = 6;
 actual = encodedLengthGeneral(sequence, 0);
 assertNotEquals(expected, actual); // Unpaired surrogate
 }

 @Test
 public void testIsWellFormed() {
 byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43};
 boolean expected = true;
 boolean actual = isWellFormed(bytes);
 assertEquals(expected, actual);

 bytes = new byte[]{(byte) 0x41, (byte) 0x80};
 expected = false;
 actual = isWellFormed(bytes);
 assertFalse(actual);

 bytes = new byte[]{(byte) 0x41, (byte) 0x80, (byte) 0x42};
 expected = false;
 actual = isWellFormed(bytes);
 assertFalse(actual);

 bytes = new byte[]{(byte) 0x41, (byte) 0xED, (byte) 0x82, (byte) 0x42, (byte) 0x83};
 expected = true;
 actual = isWellFormed(bytes);
 assertEquals(expected, actual);

 bytes = new byte[]{(byte) 0x41, (byte) 0xED, (byte) 0x82, (byte) 0x42, (byte) 0x84};
 expected = false;
 actual = isWellFormed(bytes);
 assertFalse(actual);
 }

 @Test
 public void testIsWellFormedSlowPath() {
 byte[] bytes = new byte[]{(byte) 0x41, (byte) 0xED, (byte) 0x82, (byte) 0x42, (byte) 0x83};
 boolean expected = true;
 boolean actual = isWellFormedSlowPath(bytes, 0, bytes.length);
 assertEquals(expected, actual);

 bytes = new byte[]{(byte) 0x41, (byte) 0xED, (byte) 0x82, (byte) 0x42, (byte) 0x84};
 expected = false;
 actual = isWellFormedSlowPath(bytes, 0, bytes.length);
 assertFalse(actual);
 }
}