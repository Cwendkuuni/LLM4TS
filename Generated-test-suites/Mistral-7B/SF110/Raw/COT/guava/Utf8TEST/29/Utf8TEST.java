package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "abcdef";
        int actual = Utf8.encodedLength(sequence);
        int expected = 6;
        assertEquals(expected, actual);

        sequence = "\u00e1\u0305\u0304\u00f1";
        actual = Utf8.encodedLength(sequence);
        expected = 6; // 3 characters, each occupying 2 bytes in UTF-8 encoding
        assertEquals(expected, actual);

        sequence = "\uD800\uDC00"; // invalid UTF-8 sequence (unpaired surrogate)
        try {
            actual = Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "abcdef";
        int actual = Utf8.encodedLengthGeneral(sequence, 0);
        int expected = 6;
        assertEquals(expected, actual);

        sequence = "\u00e1\u0305\u0304\u00f1";
        actual = Utf8.encodedLengthGeneral(sequence, 0);
        expected = 6; // 3 characters, each occupying 2 bytes in UTF-8 encoding
        assertEquals(expected, actual);

        sequence = "\uD800\uDC00"; // invalid UTF-8 sequence (unpaired surrogate)
        try {
            actual = Utf8.encodedLengthGeneral(sequence, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45 };
        boolean actual = Utf8.isWellFormed(bytes);
        assertTrue(actual);

        bytes = new byte[] { 0x41, 0x42, (byte) 0x80, 0x43, 0x44, 0x45 };
        actual = Utf8.isWellFormed(bytes);
        assertFalse(actual);

        bytes = new byte[] { 0x41, 0x42, 0xe0, 0x80, 0x43, 0x44, 0x45 };
        actual = Utf8.isWellFormed(bytes);
        assertTrue(actual);

        bytes = new byte[] { 0x41, 0x42, 0xf0, 0x80, 0x80, 0x43, 0x44, 0x45 };
        actual = Utf8.isWellFormed(bytes);
        assertFalse(actual);

        bytes = new byte[] { 0x41, 0x42, 0xf8, 0x80, 0x80, 0x80, 0x43, 0x44, 0x45 };
        actual = Utf8.isWellFormed(bytes);
        assertFalse(actual);

        bytes = new byte[] { 0x41, 0x42, (byte) 0xe1, 0x80, 0x80, 0x80, 0x43, 0x44, 0x45 };
        actual = Utf8.isWellFormed(bytes);
        assertTrue(actual);

        bytes = new byte[] { 0x41, 0x42, 0xff, 0x80, 0x80, 0x80, 0x43, 0x44, 0x45 };
        actual = Utf8.isWellFormed(bytes);
        assertFalse(actual);
    }
}