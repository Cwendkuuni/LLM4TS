package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "abcdef";
        int length = Utf8.encodedLength(sequence);
        assertEquals(6, length);

        sequence = "abcdefghijklmnopqrst";
        length = Utf8.encodedLength(sequence);
        assertEquals(11, length);

        sequence = "abcdefghijklmnopqrstü";
        length = Utf8.encodedLength(sequence);
        assertEquals(12, length);

        sequence = "abcdefghijklmnopqrstůě";
        length = Utf8.encodedLength(sequence);
        assertEquals(14, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthIllegalArgument() {
        CharSequence sequence = "abcdef\uF000";
        Utf8.encodedLength(sequence); // Should throw an IllegalArgumentException
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "abcdefghijklmnopqrst";
        int start = 6;
        int length = Utf8.encodedLengthGeneral(sequence, start);
        assertEquals(10, length);

        sequence = "abcdefghijklmnopqrst\uD800\uDC00";
        start = 11;
        length = Utf8.encodedLengthGeneral(sequence, start);
        assertEquals(2, length);

        sequence = "abcdefghijklmnopqrst\uD800\uDCA1";
        start = 11;
        length = Utf8.encodedLengthGeneral(sequence, start);
        assertEquals(1, length); // This surrogate pair is valid as it is a supplementary character
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthGeneralIllegalArgument() {
        CharSequence sequence = "abcdefghijklmnopqrst\uD800";
        int start = 11;
        Utf8.encodedLengthGeneral(sequence, start); // Should throw an IllegalArgumentException
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 };
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, (byte) 0xE1, (byte) 0xB0, 0x47 };
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[] { 0x41, 0x42, 0x43, 0x44, 0xE1, 0xB0, 0x45, 0x46, 0x47 };
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[] { 0x41, 0x42, 0x43, 0xE1, 0xB0, 0x45, 0x46, 0x47 };
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[] { 0x41, 0x42, 0xE1, 0xB0, 0x43, 0x44, 0x45, 0x46, 0x47 };
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[] { 0xE1, 0xB0, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47 };
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[] { 0x41, 0x42, 0xE1, 0xB0, 0x43, 0x44, 0x45, 0x46, 0x47, 0xE1, 0xB0 };
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[] { 0x41, 0x42, 0xE1, 0xB0, 0xE1, 0xB0 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }

}