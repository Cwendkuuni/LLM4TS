package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test empty CharSequence
        assertEquals(0, Utf8.encodedLength(Character.CHAR_MIN_VALUE));

        // Test single character
        for (int i = 0; i < 128; i++) {
            assertEquals(1, Utf8.encodedLength(Character.toString((char) i)));
        }

        // Test multiple characters
        for (int i = 128; i < 0x10000; i++) {
            assertEquals(2, Utf8.encodedLength(Character.toString((char) i)));
        }

        for (int i = 0x10000; i < 0x110000; i++) {
            int length = 0;
            int index = 0;
            int charCode = Character.codePointAt(Character.toString((char) i), index);
            while (charCode < 0x10000) {
                length += 1;
                index++;
                charCode = Character.codePointAt(Character.toString((char) i), index);
            }
            assertEquals(length * 2, Utf8.encodedLength(Character.toString((char) i)));
        }

        // Test invalid UTF-8 sequence
        try {
            Utf8.encodedLength("fd");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Test empty CharSequence
        assertEquals(0, Utf8.encodedLengthGeneral(Character.CHAR_MIN_VALUE, 0));

        // Test single character
        for (int i = 0; i < 128; i++) {
            assertEquals(1, Utf8.encodedLengthGeneral(Character.toString((char) i), 0));
        }

        // Test multiple characters
        for (int i = 128; i < 0x10000; i++) {
            int length = 0;
            int index = 0;
            int charCode = Character.codePointAt(Character.toString((char) i), index);
            while (charCode < 0x10000) {
                length += 1;
                index++;
                charCode = Character.codePointAt(Character.toString((char) i), index);
            }
            assertEquals(length * 2, Utf8.encodedLengthGeneral(Character.toString((char) i), 0));
        }

        for (int i = 0x10000; i < 0x110000; i++) {
            int length = 0;
            int index = 0;
            int charCode = Character.codePointAt(Character.toString((char) i), index);
            while (charCode < 0x10000) {
                length += 1;
                index++;
                charCode = Character.codePointAt(Character.toString((char) i), index);
            }
            assertEquals(length * 2, Utf8.encodedLengthGeneral(Character.toString((char) i), 0));
        }

        // Test invalid UTF-8 sequence
        try {
            Utf8.encodedLengthGeneral("fd", 0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test valid UTF-8 sequence with start index > 0
        assertEquals(2, Utf8.encodedLengthGeneral("abcd\uD800\uDC00", 2));
    }

    @Test
    public void testIsWellFormed() {
        // Test byte array with valid UTF-8 sequences
        byte[] validUtf8Bytes = {0x41, 0x62, 0xE2, 0x82, 0xAC, 0};
        assertTrue(Utf8.isWellFormed(validUtf8Bytes));

        // Test byte array with invalid UTF-8 sequences
        byte[] invalidUtf8Bytes = {0xE1, 0x83, 0xBF, 0x84, 0x85, 0xFD};
        assertFalse(Utf8.isWellFormed(invalidUtf8Bytes));

        // Test byte array with valid and invalid UTF-8 sequences
        byte[] mixedUtf8Bytes = {0x41, 0x62, 0xE2, 0x82, 0xAC, 0xE1, 0x83, 0xBF, 0x84, 0x85, 0xFD};
        assertFalse(Utf8.isWellFormed(mixedUtf8Bytes));

        // Test empty byte array
        assertTrue(Utf8.isWellFormed(new byte[0]));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test byte array with valid UTF-8 sequences
        byte[] validUtf8Bytes = {0x41, 0x62, 0xE2, 0x82, 0xAC, 0};
        assertTrue(Utf8.isWellFormed(validUtf8Bytes));

        // Test byte array with invalid UTF-8 sequences
        byte[] invalidUtf8Bytes = {0xE1, 0x83, 0xBF, 0x84, 0x85, 0xFD};
        assertFalse(Utf8.isWellFormed(invalidUtf8Bytes));

        // Test byte array with valid and invalid UTF-8 sequences
        byte[] mixedUtf8Bytes = {0x41, 0x62, 0xE2, 0x82, 0xAC, 0xE1, 0x83, 0xBF, 0x84, 0x85, 0xFD};
        assertFalse(Utf8.isWellFormed(mixedUtf8Bytes));

        // Test byte array with valid UTF-8 sequences, but with start index > 0
        byte[] validUtf8MixedStartBytes = {0x41, 0x62, 0xE2, 0x82, 0xAC, 0xE1, 0x83, 0xBF, 0x84, 0x85, 0xFD, 0x43, 0x64};
        assertTrue(Utf8.isWellFormed(validUtf8MixedStartBytes, 0, validUtf8MixedStartBytes.length));

        // Test byte array with valid and invalid UTF-8 sequences, but with start index > 0
        byte[] mixedUtf8MixedStartBytes = {0x41, 0x62, 0xE2, 0x82, 0xAC, 0xE1, 0x83, 0xBF, 0x84, 0x85, 0xFD, 0x43, 0x64, 0xE0};
        assertFalse(Utf8.isWellFormed(mixedUtf8MixedStartBytes, 0, mixedUtf8MixedStartBytes.length));
    }
}