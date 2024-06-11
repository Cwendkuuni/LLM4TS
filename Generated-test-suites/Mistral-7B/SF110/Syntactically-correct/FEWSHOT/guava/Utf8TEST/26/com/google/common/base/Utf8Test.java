package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = new char[]{'a', 'b', 'c', '\u0080', '\u0123', '\u0345', '\u0800', '\uD801'}; // Valid UTF-8 characters
        int length = Utf8.encodedLength(sequence);
        int expectedLength = 8 + (1 + 1) + (1 + 2) + 3 + 3; // Calculate length based on the number of characters
        assertEquals(expectedLength, length);

        char[] invalidSequence = new char[]{'a', 'b', 'c', '\u0800', '\uD801', '\uFFFF'}; // Invalid UTF-8 character
        try {
            Utf8.encodedLength(invalidSequence);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[]{0x41, 0x42, 0x43, 0xE3, 0x80, 0x81, 0x82, 0x83, 0xEF, 0xBE, 0xAD}; // Valid UTF-8 bytes
        assertTrue(Utf8.isWellFormed(bytes));

        byte[] invalidBytes1 = new byte[]{0x41, 0x42, 0x43, 0xEF, 0xBE, 0xAD, 0xEF}; // Invalid UTF-8 byte
        assertFalse(Utf8.isWellFormed(invalidBytes1));

        byte[] invalidBytes2 = new byte[]{0x41, 0x42, 0x43, (byte) 0xEF, 0x84, 0x85, 0x86}; // Invalid UTF-8 sequence
        assertFalse(Utf8.isWellFormed(invalidBytes2));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[]{0x41, 0x42, 0xEF, (byte) 0xED, 0x84, 0x85, 0x86, 0x87, 0xEF, 0xBE, 0xAD}; // Valid UTF-8 bytes with a single invalid byte
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 5, bytes.length));

        byte[] bytes2 = new byte[]{0x41, 0x42, 0xEF, 0xEF, 0x84, 0x85, 0x86, 0x87, 0xEF, 0xBE, 0xAD}; // Invalid UTF-8 bytes with multiple invalid bytes
        assertFalse(Utf8.isWellFormedSlowPath(bytes2, 5, bytes2.length));
    }

    @Test
    public void testEncodedLengthGeneral() {
        int length = Utf8.encodedLengthGeneral("abc\u0080\u0123\u0345\u0800\uD801", 0);
        int expectedLength = 8 + (1 + 1) + (1 + 2) + 3 + 3; // Calculate length based on the number of characters
        assertEquals(expectedLength, length);

        try {
            Utf8.encodedLengthGeneral("abc\u0800\uD801\uFFFF", 0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}