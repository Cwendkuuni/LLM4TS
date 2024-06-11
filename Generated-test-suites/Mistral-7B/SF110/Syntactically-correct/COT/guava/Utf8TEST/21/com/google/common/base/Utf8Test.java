package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        String sequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // ASCII characters only
        int actual = Utf8.encodedLength(sequence);
        int expected = sequence.length();
        assertEquals(expected, actual);

        String sequenceWithSingleMultiByteCharacter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\u0080"; // One multi-byte character
        int actualWithSingleMultiByteCharacter = Utf8.encodedLength(sequenceWithSingleMultiByteCharacter);
        int expectedWithSingleMultiByteCharacter = sequence.length() + 1;
        assertEquals(expectedWithSingleMultiByteCharacter, actualWithSingleMultiByteCharacter);

        String sequenceWithMultiByteCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\u03A0\u03B0\u03C0"; // Multiple multi-byte characters
        int actualWithMultiByteCharacters = Utf8.encodedLength(sequenceWithMultiByteCharacters);
        int expectedWithMultiByteCharacters = sequence.length() + 3;
        assertEquals(expectedWithMultiByteCharacters, actualWithMultiByteCharacters);

        String sequenceWithSurrogate = "ABCDEFGHIJ\uD800\uDC00KLMNOPQRSTUVWXYZ"; // Surrogate pair
        int expectedWithSurrogate = sequence.length() + 2;
        try {
            int actualWithSurrogate = Utf8.encodedLength(sequenceWithSurrogate);
            fail("Expected IllegalArgumentException but got actual: " + actualWithSurrogate);
        } catch (IllegalArgumentException e) {
            int actualWithSurrogate = Utf8.encodedLength(sequenceWithSurrogate);
            assertEquals(expectedWithSurrogate, actualWithSurrogate + 4294967296);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthWithIllegalArgumentException() {
        String sequenceWithSurrogatePairMismatch = "ABCDEFGHIJ\uD800\uD801KLMNOPQRSTUVWXYZ"; // Surrogate pair with mismatch
        Utf8.encodedLength(sequenceWithSurrogatePairMismatch);
    }

    @Test
    public void testIsWellFormed() {
        byte[] validUtf8Bytes = {0x41, 0x42, 0x43, 0x44, 0x45}; // ASCII characters only
        boolean actual = Utf8.isWellFormed(validUtf8Bytes);
        assertTrue(actual);

        byte[] validUtf8WithMultiByteBytes = {0x41, 0x42, 0xE2, 0x82, 0xAC, 0x43, 0x44, 0x45}; // ASCII and multi-byte characters
        boolean actualWithMultiByte = Utf8.isWellFormed(validUtf8WithMultiByteBytes);
        assertTrue(actualWithMultiByte);

        byte[] invalidUtf8Bytes = {0x41, 0xE2, 0x80, 0x43, 0x44, 0x45}; // Invalid UTF-8 sequence
        boolean actualWithInvalidUtf8 = Utf8.isWellFormed(invalidUtf8Bytes);
        assertFalse(actualWithInvalidUtf8);

        byte[] invalidUtf8WithLeadingByte = {0xE2, 0x80, 0x41, 0x42, 0x43, 0x44, 0x45}; // Invalid UTF-8 sequence with leading byte
        boolean actualWithInvalidUtf8WithLeadingByte = Utf8.isWellFormed(invalidUtf8WithLeadingByte);
        assertFalse(actualWithInvalidUtf8WithLeadingByte);

        byte[] invalidUtf8WithTrailingByte = {0x41, 0x42, 0x43, 0x44, 0x45, 0xE2, 0x80}; // Invalid UTF-8 sequence with trailing byte
        boolean actualWithInvalidUtf8WithTrailingByte = Utf8.isWellFormed(invalidUtf8WithTrailingByte);
        assertFalse(actualWithInvalidUtf8WithTrailingByte);
    }
}