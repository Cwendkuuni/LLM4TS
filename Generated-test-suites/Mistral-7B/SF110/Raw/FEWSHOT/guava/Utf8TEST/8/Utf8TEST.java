package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = {'a', 'b', 'c', '\u0100', '\u0394', '\u4e2d'};
        int length = Utf8.encodedLength(sequence);
        int expectedLength = 8 + 2 * ((sequence.length - 6) >> 1);
        assertEquals(expectedLength, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthIllegalArgumentException() {
        CharSequence sequence = new StringBuilder().append("abcdefghijklmnopqrstuvwxyz").toString();
        Utf8.encodedLength(sequence);
    }

    @Test
    public void testEncodedLengthGeneral() {
        char[] sequence = {'a', 'b', 'c', '\u0100', '\u0394', '\u4e2d'};
        int start = 3;
        int actualLength = Utf8.encodedLengthGeneral(sequence, start);
        int expectedLength = 2 * ((sequence.length - start) >> 1);
        assertEquals(expectedLength, actualLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthGeneralIllegalArgumentException() {
        char[] sequence = {'a', 'b', 'c'};
        Utf8.encodedLengthGeneral(sequence, sequence.length);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes1 = {0x41, 0x42, 0x43, 0x44};
        assertTrue(Utf8.isWellFormed(bytes1));

        byte[] bytes2 = {0x41, 0x42, 0xFF, 0x43};
        assertFalse(Utf8.isWellFormed(bytes2));

        byte[] bytes3 = {0x41, 0x42, 0xED, 0x9F, 0x80, 0x43};
        assertTrue(Utf8.isWellFormed(bytes3));

        byte[] bytes4 = {0xFF, 0x80, 0xED, 0x9F, 0x41, 0x42, 0x43};
        assertFalse(Utf8.isWellFormed(bytes4));

        byte[] bytes5 = {0x41, 0x42, 0xE0, 0xA0, 0x80, 0x43};
        assertTrue(Utf8.isWellFormed(bytes5));

        byte[] bytes6 = {0xE0, 0xA0, 0x80, 0x41, 0x42, 0x43};
        assertFalse(Utf8.isWellFormed(bytes6));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes1 = {0x41, 0x42, 0xFF, 0x43};
        assertTrue(Utf8.isWellFormedSlowPath(bytes1, 0, bytes1.length));

        byte[] bytes2 = {0xFF, 0x80, 0xED, 0x9F, 0x41, 0x42, 0x43};
        assertTrue(Utf8.isWellFormedSlowPath(bytes2, 0, bytes2.length));

        byte[] bytes3 = {0x41, 0x42, 0xE0, 0xA0, 0x80, 0x43};
        assertTrue(Utf8.isWellFormedSlowPath(bytes3, 0, bytes3.length));

        byte[] bytes4 = {0x41, 0x42, 0xE0, 0x80, 0xA0, 0x43};
        assertTrue(Utf8.isWellFormedSlowPath(bytes4, 0, bytes4.length));

        byte[] bytes5 = {0xFF, 0xED, 0x9F, 0x80, 0x41, 0x42, 0x43};
        assertFalse(Utf8.isWellFormedSlowPath(bytes5, 0, bytes5.length));

        byte[] bytes6 = {0x41, 0x42, 0xED, 0x9F, 0xFF, 0x43};
        assertFalse(Utf8.isWellFormedSlowPath(bytes6, 0, bytes6.length));
    }
}