package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "abcDEFGhijklmñopqrstüABCDEFGHIJKLMNOPQRSTÜ";
        int length = Utf8.encodedLength(sequence);
        assertEquals(35, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthWithIllegalArgument() {
        CharSequence sequence = "abcDEFGhijklmÅ";
        Utf8.encodedLength(sequence); // This should throw an IllegalArgumentException
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "abcDEFGhijklmøå";
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(37, length);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[]{0x41, 0x42, 0x43};
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedWithInvalidByte() {
        byte[] bytes = new byte[]{0x41, (byte) 0x80, 0x43};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedWithSurrogate() {
        byte[] bytes = new byte[]{0xD800, 0xDC00};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedWithValidSurrogate() {
        byte[] bytes = new byte[]{0xD800, 0xDBFF, 0xDC00};
        assertTrue(Utf8.isWellFormed(bytes));
    }
}