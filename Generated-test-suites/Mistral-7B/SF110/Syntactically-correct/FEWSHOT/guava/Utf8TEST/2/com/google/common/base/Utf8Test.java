package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = {'a', 'b', 'c', '\u0080', '\u03bf', '\u41C3', '\uF000'};
        int length = Utf8.encodedLength(sequence);
        int expectedLength = 7; // Calculate the expected length by manually encoding each character

        assertEquals(expectedLength, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthWithInvalidUtf16Sequence() {
        char[] sequence = {'a', 'b', '\uD800', '\uDC00'};
        Utf8.encodedLength(sequence);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = {0x41, 0x82, 0x83, 0x84}; // Valid UTF-8 sequence
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{-62, -63, 0x84}; // Invalid UTF-8 sequence
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{-62, -63, -65, 0x84}; // Valid UTF-8 sequence with a leading byte that is the second byte of another character
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{-19, -96, 0x84}; // Valid UTF-8 sequence with a leading byte that is the first byte of another character
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedWithLargeInput() {
        byte[] bytes = new byte[1024 * 1024]; // Generate a large byte array
        assertTrue(Utf8.isWellFormed(bytes));
    }
}