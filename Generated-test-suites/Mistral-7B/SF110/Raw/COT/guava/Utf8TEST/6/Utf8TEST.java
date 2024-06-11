package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Basic functionality test
        CharSequence sequence = "Hello World";
        int length = Utf8.encodedLength(sequence);
        int expectedLength = 11; // 10 characters plus 1 byte for '\u0000' terminator
        assertEquals(expectedLength, length);

        // Edge cases
        sequence = "\uD800\uDC00"; // Surrogate pair
        length = Utf8.encodedLength(sequence);
        expectedLength = 4; // Each surrogate requires 3 bytes in UTF-8
        assertEquals(expectedLength, length);

        sequence = "\uD800"; // Lone surrogate
        try {
            length = Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException for invalid UTF-8 sequence");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        sequence = "\uFFFE"; // Invalid Unicode code point
        try {
            length = Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException for invalid UTF-8 sequence");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Basic functionality test
        CharSequence sequence = "Hello World";
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        int expectedLength = 11; // 10 characters plus 1 byte for '\u0000' terminator
        assertEquals(expectedLength, length);

        // Edge cases
        sequence = "\uD800\uDC00"; // Surrogate pair
        int startIndex = 0;
        length = Utf8.encodedLengthGeneral(sequence, startIndex);
        expectedLength = 4; // Each surrogate requires 3 bytes in UTF-8
        assertEquals(expectedLength, length);

        sequence = "\uD800"; // Lone surrogate
        startIndex = 0;
        try {
            length = Utf8.encodedLengthGeneral(sequence, startIndex);
            fail("Expected IllegalArgumentException for invalid UTF-8 sequence");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        sequence = "\uFFFE"; // Invalid Unicode code point
        startIndex = 0;
        try {
            length = Utf8.encodedLengthGeneral(sequence, startIndex);
            fail("Expected IllegalArgumentException for invalid UTF-8 sequence");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsWellFormed() {
        // Basic functionality test
        byte[] bytes = "Hello World\u0000".getBytes();
        boolean isWellFormed = Utf8.isWellFormed(bytes);
        assertTrue(isWellFormed);

        // Edge cases
        bytes = new byte[] { 0 }; // Empty byte array
        isWellFormed = Utf8.isWellFormed(bytes);
        assertTrue(isWellFormed);

        bytes = new byte[] { 0xFF, 0xFF, 0xFF, 0xFF }; // All bytes are 0xFF
        isWellFormed = Utf8.isWellFormed(bytes);
        assertFalse(isWellFormed);

        bytes = new byte[] { 0xED, 0xA0, 0xED, 0xB0 }; // Invalid UTF-8 sequence
        isWellFormed = Utf8.isWellFormed(bytes);
        assertFalse(isWellFormed);

        bytes = new byte[] { 0xED, 0xB0, 0xED, 0xA0 }; // Invalid UTF-8 sequence
        isWellFormed = Utf8.isWellFormed(bytes);
        assertFalse(isWellFormed);

        bytes = new byte[] { 0xD8, 0x00, 0xDC, 0x00 }; // Surrogate pair
        isWellFormed = Utf8.isWellFormed(bytes);
        assertFalse(isWellFormed);

        bytes = new byte[] { 0xD8, 0x00 }; // Lone surrogate
        isWellFormed = Utf8.isWellFormed(bytes);
        assertFalse(isWellFormed);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Basic functionality test
        byte[] bytes = "Hello World\u0000".getBytes();
        boolean isWellFormed = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue(isWellFormed);

        // Edge cases
        bytes = new byte[] {}; // Empty byte array
        isWellFormed = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue(isWellFormed);

        bytes = new byte[] { 0xFF, 0xFF, 0xFF, 0xFF }; // All bytes are 0xFF
        isWellFormed = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse(isWellFormed);

        bytes = new byte[] { 0xED, 0xA0, 0xED, 0xB0 }; // Invalid UTF-8 sequence
        isWellFormed = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse(isWellFormed);

        bytes = new byte[] { 0xED, 0xB0, 0xED, 0xA0 }; // Invalid UTF-8 sequence
        isWellFormed = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse(isWellFormed);

        bytes = new byte[] { 0xD8, 0x00, 0xDC, 0x00 }; // Surrogate pair
        isWellFormed = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse(isWellFormed);

        bytes = new byte[] { 0xD8, 0x00 }; // Lone surrogate
        isWellFormed = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse(isWellFormed);
    }
}