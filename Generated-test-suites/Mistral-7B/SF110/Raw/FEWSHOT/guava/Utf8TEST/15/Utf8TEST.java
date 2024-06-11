package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = new char[]{'a', 'b', 'c', 'd', '\u00e9', '\u03c9'}; // é, Έ
        int expected = 6 + encodedLengthGeneral(sequence, 5) + 3;

        int result = Utf8.encodedLength(new String(sequence));
        assertEquals("Unexpected UTF-8 length for sequence: " + new String(sequence), expected, result);
    }

    @Test
    public void testEncodedLengthGeneral() {
        char[] sequence = new char[]{'a', 'b', '\u20ac'}; // €
        int expected = 6;

        int result = Utf8.encodedLengthGeneral(new String(sequence), 2);
        assertEquals("Unexpected UTF-8 length for sequence: " + new String(sequence), expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthIllegalArgument() {
        char[] sequence = new char[]{'a', '\uD800', '\uDC00'}; // invalid UTF-8 sequence

        Utf8.encodedLength(new String(sequence));
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[]{(byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80}; // invalid UTF-8 sequence

        assertFalse("Unexpected true result for invalid UTF-8 sequence", Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0x80, (byte) 0x81, (byte) 0x82, (byte) 0x83}; // valid UTF-8 sequence

        assertTrue("Unexpected false result for valid UTF-8 sequence", Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[]{(byte) 0xEF, (byte) 0xBF, (byte) 0xBD, (byte) 0xBE}; // UTF-8 sequence with invalid character

        assertTrue("Unexpected false result for UTF-8 sequence with invalid character",
                Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }
}