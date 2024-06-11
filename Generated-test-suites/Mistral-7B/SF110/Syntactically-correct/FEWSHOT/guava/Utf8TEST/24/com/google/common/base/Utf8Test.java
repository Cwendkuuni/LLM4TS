package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = new char[]{'a', 'b', 'c', '\u0304'}; // Combining character
        int encodedLength = Utf8.encodedLength(new String(sequence));
        int expectedLength = 4;
        assertEquals(expectedLength, encodedLength);

        sequence = new char[]{'a', '\uD800', '\uDC00', '\uD800', '\uDC01'}; // Surrogate pair
        try {
            Utf8.encodedLength(new String(sequence));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        sequence = new char[]{'a', '\uD800', '\uDC00'}; // Incomplete surrogate pair
        try {
            Utf8.encodedLength(new String(sequence));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        char[] sequence = new char[]{'a', 'b', 'c', '\u0304', '\uD800', '\uDC00', '\uD800', '\uDC01'}; // Combining character and surrogate pair
        int encodedLength = Utf8.encodedLengthGeneral(new String(sequence), 0);
        int expectedLength = 10;
        assertEquals(expectedLength, encodedLength);

        sequence = new char[]{'a', '\uD800', '\uDC00', '\uD800', '\uDC01'}; // Incomplete surrogate pair
        try {
            Utf8.encodedLengthGeneral(new String(sequence), 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x80, (byte) 0x42, (byte) 0x80};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0x41, (byte) 0xC2, (byte) 0x80};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42, (byte) 0x80};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42, (byte) 0x81};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42, (byte) 0x82, (byte) 0x82};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42, (byte) 0x83, (byte) 0x82, (byte) 0x82};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42, (byte) 0x84, (byte) 0x82, (byte) 0x82, (byte) 0x82};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42, (byte) '\uD800', (byte) '\uDC00'};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) '\uD800', (byte) '\uDC01'};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x80, (byte) 0x42, (byte) 0x80};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{(byte) 0x41, (byte) 0xC2, (byte) 0x80};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42, (byte) 0x80};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42, (byte) 0x81};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42, (byte) 0x82, (byte) 0x82};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42, (byte) 0x83, (byte) 0x82, (byte) 0x82};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42, (byte) 0x84, (byte) 0x82, (byte) 0x82, (byte) 0x82};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) 0x42, (byte) '\uD800', (byte) '\uDC00'};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{(byte) 0x41, (byte) 0xE0, (byte) 0xA0, (byte) '\uD800', (byte) '\uDC01'};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }
}