package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = new char[]{'a', 'b', 'c'};
        int length = Utf8.encodedLength(sequence);
        assertEquals(6, length);

        sequence = new char[]{'á', 'é', 'í'};
        length = Utf8.encodedLength(sequence);
        assertEquals(9, length);

        sequence = new char[]{'😃'};
        length = Utf8.encodedLength(sequence);
        assertEquals(4, length);

        sequence = new char[]{'😃', '😃'};
        length = Utf8.encodedLength(sequence);
        assertEquals(10, length);

        sequence = new char[]{'😃', '\uD800', '\uDC00', '😃'};
        try {
            length = Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        char[] sequence = new char[]{'a', 'b', 'c'};
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(6, length);

        sequence = new char[]{'á', 'é', 'í'};
        length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(9, length);

        sequence = new char[]{'😃'};
        length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(4, length);

        sequence = new char[]{'😃', '😃'};
        length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(10, length);

        sequence = new char[]{'😃', '\uD800', '\uDC00', '😃'};
        try {
            length = Utf8.encodedLengthGeneral(sequence, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        sequence = new char[]{'😃', '\uD800', '\uDFFF', '\uD800', '😃'};
        length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(22, length);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[]{0x61, 0x62, 0x63};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0xC3, 0xA1, 0xC3, 0xA4, 0xC3, 0xA5};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0xF0, 0x9F, 0x98, 0x80, 0x9F, 0xBF, 0x98, 0x81, 0x9F, 0xBE};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0xEF, 0xBF, 0xBD, 0xE2, 0x9A, 0x85, 0xE4, 0x86, 0xD8, 0x00, 0xE1, 0x9B, 0x85};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{'-'};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0xFF, 0xFF};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0xFF, 0xFF, 0xFF};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0xFF, 0xED, 0xA0, 0x80};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0xFF, 0xED, 0xAD, 0x80, 0x80};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[]{0xFF, 0xED, 0xA0, 0x80};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{0xFF, 0xED, 0xAD, 0x80, 0x80};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }
}