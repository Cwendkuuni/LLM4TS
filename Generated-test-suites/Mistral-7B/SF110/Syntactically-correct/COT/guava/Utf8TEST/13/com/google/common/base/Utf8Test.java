package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    // Test case for encodedLength method with normal characters
    @Test
    public void testEncodedLengthNormalCharacters() {
        CharSequence sequence = "abcdef";
        int actualLength = Utf8.encodedLength(sequence);
        int expectedLength = sequence.length();
        assertEquals(expectedLength, actualLength);
    }

    // Test case for encodedLength method with edge cases
    @Test
    public void testEncodedLengthEdgeCases() {
        CharSequence sequence = "\u007f\u0080\u07ff\u0800\uFFFF";
        int actualLength = Utf8.encodedLength(sequence);
        int expectedLength = sequence.length();
        assertEquals(expectedLength, actualLength);
    }

    // Test case for encodedLength method with exceptions
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthException() {
        CharSequence sequence = "\uD800";
        Utf8.encodedLength(sequence);
    }

    // Test case for encodedLengthGeneral method with normal characters
    @Test
    public void testEncodedLengthGeneralNormalCharacters() {
        CharSequence sequence = "abcdef\u0345";
        int actualLength = Utf8.encodedLengthGeneral(sequence, 0);
        int expectedLength = sequence.length();
        assertEquals(expectedLength, actualLength);
    }

    // Test case for encodedLengthGeneral method with edge cases
    @Test
    public void testEncodedLengthGeneralEdgeCases() {
        CharSequence sequence = "\u007f\u0300\uD800\uDFFF\uFDD0\uFFFF\u10FFFF";
        int actualLength = Utf8.encodedLengthGeneral(sequence, 0);
        int expectedLength = sequence.length();
        assertEquals(expectedLength, actualLength);
    }

    // Test case for encodedLengthGeneral method with exceptions
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthGeneralException() {
        CharSequence sequence = "\uD800\uDC00";
        Utf8.encodedLengthGeneral(sequence, 0);
    }

    // Test case for isWellFormed method with valid UTF-8 bytes
    @Test
    public void testIsWellFormedValidUtf8Bytes() {
        byte[] bytes = new byte[] { 0x41, 0x62, 0x63, 0x64, 0x65 };
        boolean actualResult = Utf8.isWellFormed(bytes);
        assertTrue(actualResult);
    }

    // Test case for isWellFormed method with invalid UTF-8 bytes
    @Test
    public void testIsWellFormedInvalidUtf8Bytes() {
        byte[] bytes = new byte[] { 0x41, 0xFF, 0x63, 0x64, 0x65 };
        boolean actualResult = Utf8.isWellFormed(bytes);
        assertFalse(actualResult);
    }

    // Test case for isWellFormed method with edge cases
    @Test
    public void testIsWellFormedEdgeCases() {
        byte[] bytes = new byte[] { 0x7F, 0x80, 0x7FF, 0x800, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF };
        boolean actualResult = Utf8.isWellFormed(bytes);
        assertTrue(actualResult);
    }
}