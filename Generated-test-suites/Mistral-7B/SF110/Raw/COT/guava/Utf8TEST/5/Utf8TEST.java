package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    // Test case for encodedLength method
    @Test
    public void testEncodedLength() {
        String sequence = "Hello, World!";
        int length = Utf8.encodedLength(sequence);
        int expectedLength = sequence.getBytes().length;
        assertEquals("Encoded length is not correct.", expectedLength, length);
    }

    // Test case for encodedLength method with empty sequence
    @Test
    public void testEncodedLengthEmptySequence() {
        String sequence = "";
        int length = Utf8.encodedLength(sequence);
        int expectedLength = 0;
        assertEquals("Encoded length is not correct for empty sequence.", expectedLength, length);
    }

    // Test case for encodedLength method with single character sequence
    @Test
    public void testEncodedLengthSingleCharacter() {
        String sequence = "A";
        int length = Utf8.encodedLength(sequence);
        int expectedLength = 1;
        assertEquals("Encoded length is not correct for single character sequence.", expectedLength, length);
    }

    // Test case for encodedLength method with multi-byte character sequence
    @Test
    public void testEncodedLengthMultiByteCharacter() {
        String sequence = "München";
        int length = Utf8.encodedLength(sequence);
        int expectedLength = 6; // M = 77, u = 220, n = 110, c = 111, h = 104, e = 101
        assertEquals("Encoded length is not correct for multi-byte character sequence.", expectedLength, length);
    }

    // Test case for encodedLengthGeneral method
    @Test
    public void testEncodedLengthGeneral() {
        String sequence = "👋";
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        int expectedLength = 3; // U+1F600 (emoji for "waving hand sign") is composed of U+D83C and U+DC00, each of which takes 3 bytes in UTF-8 encoding
        assertEquals("Encoded length is not correct for multi-byte character sequence.", expectedLength, length);
    }

    // Test case for isWellFormed method with valid UTF-8 byte array
    @Test
    public void testIsWellFormedValidUtf8() {
        byte[] bytes = new byte[] { 72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33 };
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue("UTF-8 byte array should be well-formed.", result);
    }

    // Test case for isWellFormed method with invalid UTF-8 byte array
    @Test
    public void testIsWellFormedInvalidUtf8() {
        byte[] bytes = new byte[] { 72, 101, 108, -1, 111, 44, 32, 87, 111, 114, 108, 100, 33 };
        boolean result = Utf8.isWellFormed(bytes);
        assertFalse("UTF-8 byte array should be invalid.", result);
    }

    // Test case for isWellFormed method with empty byte array
    @Test
    public void testIsWellFormedEmptyByteArray() {
        byte[] bytes = new byte[0];
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue("Empty byte array should be considered valid UTF-8.", result);
    }

    // Test case for isWellFormedSlowPath method
    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[] { 72, 101, 108, -1, 111, 44, 32, 87, 111, 114, 108, 100, 33, -1 };
        boolean result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse("UTF-8 byte array should be invalid.", result);
    }
}