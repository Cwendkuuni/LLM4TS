package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = {'a', 'b', 'c', '\u0100', '\u03c0'};
        int length = Utf8.encodedLength(sequence);
        assertEquals(11, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthThrowsExceptionWithInvalidCharSequence() {
        char[] sequence = {'\uD800', '\uDC00'};
        Utf8.encodedLength(sequence); // should throw IllegalArgumentException
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = {0x41, 0x42, 0x43, 0xE1, 0xBC, 0xA0};
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsWellFormedThrowsExceptionWithInvalidByteSequence() {
        byte[] bytes = {0x41, 0x5B, 0xE1, 0xBC, 0xA0};
        Utf8.isWellFormed(bytes); // should throw IllegalArgumentException
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = {0x41, 0xFF, 0xE0, 0x80, 0x42, 0x80, 0x43};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsWellFormedSlowPathThrowsExceptionWithInvalidByteSequence() {
        byte[] bytes = {0x41, 0xFF, 0xE0, 0x81, 0x42, 0x80, 0x43};
        Utf8.isWellFormedSlowPath(bytes, 0, bytes.length); // should throw IllegalArgumentException
    }
}