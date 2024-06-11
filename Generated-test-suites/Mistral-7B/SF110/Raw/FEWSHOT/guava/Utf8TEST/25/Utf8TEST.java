package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = {'a', 'b', 'c', '\u0304', '\u2234', '\uD800'}; // surrogate pair
        int length = Utf8.encodedLength(sequence);
        int expected = 11;
        assertEquals(expected, length);

        char[] sequence2 = {'\uD800'};
        try {
            length = Utf8.encodedLength(sequence2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        char[] sequence = {'a', 'b', 'c', '\u0304', '\u2234', '\uD800', '\uDC00'}; // surrogate pair
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        int expected = 14;
        assertEquals(expected, length);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = {0x41, 0x42, 0x43, 0x44, 0xEF, 0xBD, 0x9E, 0xBF}; // valid UTF-8 sequence
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue(result);

        byte[] bytes2 = {0x41, 0x42, 0x43, 0xEF, 0xBD, 0x9E, 0xBF}; // invalid UTF-8 sequence
        result = Utf8.isWellFormed(bytes2);
        assertFalse(result);

        byte[] bytes3 = {0x41, 0x42, 0x43}; // valid UTF-8 sequence, empty byte array should be considered valid
        result = Utf8.isWellFormed(bytes3);
        assertTrue(result);

        byte[] bytes4 = null;
        try {
            result = Utf8.isWellFormed(bytes4);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        byte[] bytes5 = {0xFF, 0xFF, 0xFF, 0xFF}; // invalid UTF-8 sequence
        result = Utf8.isWellFormed(bytes5);
        assertFalse(result);

        byte[] bytes6 = {0xD8, 0x00}; // invalid UTF-8 sequence, surrogate pair without partner
        result = Utf8.isWellFormed(bytes6);
        assertFalse(result);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = {0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF}; // invalid UTF-8 sequence
        boolean result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse(result);
    }
}