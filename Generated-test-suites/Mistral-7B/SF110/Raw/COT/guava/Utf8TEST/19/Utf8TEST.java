package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Basic functionality test
        assertEquals(encodedLength(""), 0);
        assertEquals(encodedLength("abc"), 3);
        assertEquals(encodedLength("abcdefghijklmnopqrstuvwxyz"), 39);
        assertEquals(encodedLength("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"), 78);
        assertEquals(encodedLength("😃"), 4);
        assertEquals(encodedLength("😃😍"), 7);

        // Edge cases and exceptions testing
        assertThrows(IllegalArgumentException.class, () -> encodedLength("😃😃")); // Surrogate pairs
        assertThrows(IllegalArgumentException.class, () -> encodedLength("😃\uD800")); // Surrogate before emoji
        assertThrows(IllegalArgumentException.class, () -> encodedLength("\uD800😃")); // Surrogate after emoji
        assertThrows(IllegalArgumentException.class, () -> encodedLength("😃\uD800\uDFFF")); // Surrogate pair at the end
        assertThrows(IllegalArgumentException.class, () -> encodedLength("😃\uD800\uD800")); // Multiple surrogates

        assertThrows(IllegalArgumentException.class, () -> encodedLength("\uD800")); // Lone surrogate
        assertThrows(IllegalArgumentException.class, () -> encodedLength("\uD800\uD800\uD800")); // Multiple lone surrogates

        assertThrows(IllegalArgumentException.class, () -> encodedLength("😃\uFFFE")); // High surrogate followed by low surrogate
        assertThrows(IllegalArgumentException.class, () -> encodedLength("\uFFFE😃")); // Low surrogate followed by high surrogate
        assertThrows(IllegalArgumentException.class, () -> encodedLength("😃\uFFFE\uFFFE")); // Multiple surrogates, not a pair

        assertThrows(IllegalArgumentException.class, () -> encodedLength("😃\u110000")); // High Unicode point
        assertThrows(IllegalArgumentException.class, () -> encodedLength("\u110000😃")); // Unicode point followed by emoji
        assertThrows(IllegalArgumentException.class, () -> encodedLength("😃\u110000\u110000")); // Multiple high Unicode points
    }

    @Test
    public void testIsWellFormed() {
        // Basic functionality test
        assertTrue(isWellFormed(new byte[]{}));
        assertTrue(isWellFormed(new byte[]{0x41, 0x42, 0x43}));
        assertTrue(isWellFormed(new byte[]{0xED, 0x9F, 0xBF, 0xBD, 0xE0, 0xA1, 0x80, 0x41, 0x42, 0x43}));

        // Edge cases and exceptions testing
        assertFalse(isWellFormed(new byte[]{0xED, 0x9F, 0xBF, 0xBD, 0xE0})); // Incomplete sequence
        assertFalse(isWellFormed(new byte[]{0xED, 0x9F, 0xBF, 0xBD, 0xE0, 0x80})); // Overflow

        assertFalse(isWellFormed(new byte[]{0xED, 0x9F, 0xBF, 0xBD, 0xE0, 0x81, 0x00})); // Trailing garbage
        assertFalse(isWellFormed(new byte[]{0xED, 0x9F, 0xBF, 0xBD, 0xE0, 0x80, 0x01})); // Leading garbage

        assertFalse(isWellFormed(new byte[]{0xED, 0x9F, 0xBF, 0xBD, 0xE0, 0x80, 0x81, 0x01})); // Multiple leading and trailing garbage

        assertFalse(isWellFormed(new byte[]{0xED, 0x9F, 0xBF, 0xBD, 0xE0, 0x81, 0x01, 0xED, 0x9F, 0xBF, 0xBD, 0xE0, 0x81, 0x01})); // Multiple leading and trailing garbage, repeated

        assertFalse(isWellFormed(new byte[]{0xED, 0x9F, 0xBF, 0xBD, 0xE0, 0x80, 0x7F})); // Invalid byte sequence
        assertFalse(isWellFormed(new byte[]{0xED, 0x9F, 0xBF, 0xBD, 0xE0, 0x80, 0x81, 0x7F})); // Invalid byte sequence with garbage
    }
}