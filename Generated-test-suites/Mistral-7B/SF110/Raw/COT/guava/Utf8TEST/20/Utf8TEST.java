package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        String sequence = "abcdef";
        int length = Utf8.encodedLength(sequence);
        assertEquals(length, 6);

        sequence = "\u00e1\u0323\u1f38\u00e9\u00e4\u00e7\u00f1\u00f2\u00f3\u00f4";
        length = Utf8.encodedLength(sequence);
        assertEquals(length, 12);

        sequence = "\uD800\uDC00";
        try {
            length = Utf8.encodedLength(sequence);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        String sequence = "abcdef";
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(length, 6);

        sequence = "\u00e1\u0323\u1f38\u00e9\u00e4\u00e7\u00f1\u00f2\u00f3\u00f4";
        int start = 5;
        length = Utf8.encodedLengthGeneral(sequence, start);
        assertEquals(length, 7);

        sequence = "\uD800\uDC00";
        String expectedErrorMessage = "Unpaired surrogate at index ";
        try {
            Utf8.encodedLengthGeneral(sequence, 0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            assertTrue(errorMessage.startsWith(expectedErrorMessage));
            assertTrue(errorMessage.length() > expectedErrorMessage.length());
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[]{(byte) 0x61, (byte) 0x62, (byte) 0x63, (byte) 0x64, (byte) 0x65};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0xef, (byte) 0xbd, (byte) 0x80, (byte) 0x81, (byte) 0x82, (byte) 0x83, (byte) 0x84, (byte) 0x85};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0xef, (byte) 0xbd, (byte) 0x80, (byte) 0x81, (byte) 0xbf, (byte) 0x82, (byte) 0x83, (byte) 0x84, (byte) 0x85};
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[]{(byte) 0xef, (byte) 0xbd, (byte) 0x80, (byte) 0x81, (byte) 0xbf, (byte) 0x82, (byte) 0x83, (byte) 0x84, (byte) 0x85};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{(byte) 0xef, (byte) 0xbd, (byte) 0x80, (byte) 0x81, (byte) 0x80, (byte) 0x82, (byte) 0x83, (byte) 0x84, (byte) 0x85};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{(byte) 0xef, (byte) 0xbd, (byte) 0x81, (byte) 0x80, (byte) 0x82, (byte) 0x83, (byte) 0x84, (byte) 0x85};
        assertTrue(Utf8.isWellFormedSlowPath
(bytes, 0, bytes.length));
    }
}