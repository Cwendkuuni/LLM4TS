package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

 // Test case for encodedLength method with normal characters
 @Test
 public void testEncodedLengthNormal() {
 String sequence = "Hello World";
 int expected = 11;
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 // Test case for encodedLength method with edge case of long sequence
 @Test
 public void testEncodedLengthEdgeCaseLongSequence() {
 String sequence = "᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎᠎";
 int expected = 136;
 int actual = Utf8.encodedLength(sequence);
 assertEquals(expected, actual);
 }

 // Test case for encodedLength method with exception handling
 @Test(expected = IllegalArgumentException.class)
 public void testEncodedLengthException() {
 String sequence = "Hello\uFFFE";
 int actual = Utf8.encodedLength(sequence);
 // The exception is thrown inside the test, no need to assert anything
 }

 // Test case for isWellFormed method with valid byte array
 @Test
 public void testIsWellFormedValidByteArray() {
 byte[] bytes = new byte[] { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33 };
 boolean expected = true;
 boolean actual = Utf8.isWellFormed(bytes);
 assertEquals(expected, actual);
 }

 // Test case for isWellFormed method with invalid byte array (invalid start byte)
 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedInvalidByteArrayStart() {
 byte[] bytes = new byte[] { -1 };
 boolean actual = Utf8.isWellFormed(bytes);
 // The exception is thrown inside the test, no need to assert anything
 }

 // Test case for isWellFormed method with invalid byte array (invalid byte sequence)
 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedInvalidByteArraySequence() {
 byte[] bytes = new byte[] { 72, -66, 108, 111, 111, 32, 87, 111, 114, 108, 100, 33, 128 };
 boolean actual = Utf8.isWellFormed(bytes);
 // The exception is thrown inside the test, no need to assert anything
 }

 // Test case for isWellFormed method with invalid byte array (invalid byte sequence slow path)
 @Test(expected = IllegalArgumentException.class)
 public void testIsWellFormedInvalidByteArraySequenceSlowPath() {
 byte[] bytes = new byte[] { 72, 101, -128, 108, 111, 32, 87, 111, 114, 108, 100, 33 };
 boolean actual = Utf8.isWellFormed(bytes);
 // The exception is thrown inside the test, no need to assert anything
 }

}