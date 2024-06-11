package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        int length = Utf8.encodedLength(sequence);
        assertEquals(length, 19);

        sequence = Character.toChars(0x10FFFF);
        try {
            length = Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        char[] sequence = "\u0061\u0304\uD800\uDC00".toCharArray();
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(length, 4);

        sequence = "\uD800\uDC00\uD800\uDC01".toCharArray();
        try {
            length = Utf8.encodedLengthGeneral(sequence, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[0];
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{-72, -73, -65, -68, -69, -76, -65, -72, -65};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0, 1, 2, 0xC2, 0xC3, 0xC4, 0xC5, 0xC6, 0xC7, 0xC8, 0xC9, 0xCA, 0xCB, 0xCC, 0xCD, 0xCE, 0xCF};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0, 1, 0xE0, 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0, 1, 0xE0, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0, 1, 0xF0, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0, 1, 0xF4, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = {0, -63, -65, -65};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{0, 0};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{0, -62, -65};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{-1, 0};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{-1, -65};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{-1, -1, 0};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{-1, -1, -65};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{-1, -1, -1, 0};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }
}