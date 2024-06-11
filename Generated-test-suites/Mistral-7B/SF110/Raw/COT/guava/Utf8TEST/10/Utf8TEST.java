package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "test";
        int length = Utf8.encodedLength(sequence);
        assertEquals(6, length);

        sequence = "\u00e1\u0305test";
        length = Utf8.encodedLength(sequence);
        assertEquals(9, length);

        sequence = "\uD800\uDC00test";
        try {
            length = Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "test";
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(6, length);

        sequence = "\u00e1\u0305test";
        length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(9, length);

        sequence = "\uD800\uDC00test";
        try {
            length = Utf8.encodedLengthGeneral(sequence, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[] { 72, 101, 105, 115, 111, 110 };
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[] { 72, 101, 105, (byte) 0xC3, 115, 111, 110 };
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[] { 72, 101, 105, (byte) 0xE1, (byte) 0x9F, 115, 111, 110 };
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[] { 72, 101, 105, (byte) 0xD8, (byte) 0xD3, 115, 111, 110 };
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[] { 72, 101, 105, 115, 111, 110 };
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[] { -1, 72, 101, 105, 115, 111, 110 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[] { 72, -1, 101, 105, 115, 111, 110 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[] { 72, 101, 105, 115, 111, 110, 128, 128 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[] { 72, 101, 105, 115, 111, 110, 128, 129 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[] { 72, 101, 105, 115, 111, 110, 191, 193, 191, 193 };
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }
}