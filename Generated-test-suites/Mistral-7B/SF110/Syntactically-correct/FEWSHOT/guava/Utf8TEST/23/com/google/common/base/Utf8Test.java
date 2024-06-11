package com.google.common.base;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
        int length = Utf8.encodedLength(sequence);
        byte[] bytes = sequence.toString().getBytes(StandardCharsets.UTF_8);
        assertEquals(length, bytes.length);

        sequence = "\u00e0\u0394\u1026\u2460";
        length = Utf8.encodedLength(sequence);
        bytes = sequence.toString().getBytes(StandardCharsets.UTF_8);
        assertEquals(length, bytes.length);

        sequence = "\uD800\uDC00";
        try {
            length = Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        CharSequence sequence = "abcdefghijklmnopqrstuvwxyz";
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        byte[] bytes = sequence.toString().getBytes(StandardCharsets.UTF_8);
        assertEquals(length, bytes.length);

        sequence = "\u00e0\u0394\u1026\u2460";
        int start = 2;
        length = Utf8.encodedLengthGeneral(sequence, start);
        byte[] bytesSubsequence = new byte[sequence.length() - start];
        System.arraycopy(sequence.toString().getBytes(StandardCharsets.UTF_8), start, bytesSubsequence, 0, bytesSubsequence.length);
        assertEquals(length, bytesSubsequence.length);
    }

    @Test
    public void testIsWellFormed() {
        byte[] validUtf8Bytes = {0x41, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f};
        assertTrue(Utf8.isWellFormed(validUtf8Bytes));

        byte[] invalidUtf8Bytes1 = {0x41, 0x62, 0xc3, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f};
        assertFalse(Utf8.isWellFormed(invalidUtf8Bytes1));

        byte[] invalidUtf8Bytes2 = {0x41, 0x62, 0xef, 0xbf, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f};
        assertFalse(Utf8.isWellFormed(invalidUtf8Bytes2));

        byte[] invalidUtf8Bytes3 = {0x41, 0x62, 0xef, 0xbd, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f};
        assertFalse(Utf8.isWellFormed(invalidUtf8Bytes3));

        byte[] invalidUtf8Bytes4 = {0x41, 0x62, 0xef, 0xbe, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f};
        assertFalse(Utf8.isWellFormed(invalidUtf8Bytes4));

        byte[] invalidUtf8Bytes5 = null;
        assertFalse(Utf8.isWellFormed(invalidUtf8Bytes5));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] validUtf8SlowPathBytes = {0x41, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f};
        assertTrue(Utf8.isWellFormedSlowPath(validUtf8SlowPathBytes, 0, validUtf8SlowPathBytes.length));

        byte[] invalidUtf8SlowPathBytes1 = {0x41, 0x62, 0xc3, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f};
        assertFalse(Utf8.isWellFormedSlowPath(invalidUtf8SlowPathBytes1, 0, invalidUtf8SlowPathBytes1.length));

        byte[] invalidUtf8SlowPathBytes2 = {0x41, 0x62, 0xef, 0xbf, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f};
        assertFalse(Utf8.isWellFormedSlowPath(invalidUtf8SlowPathBytes2, 0, invalidUtf8SlowPathBytes2.length));

        byte[] invalidUtf8SlowPathBytes3 = {0x41, 0x62, 0xef, 0xbe, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f};
        assertFalse(Utf8.isWellFormedSlowPath(invalidUtf8SlowPathBytes3, 0, invalidUtf8SlowPathBytes3.length));

        byte[] invalidUtf8SlowPathBytes4 = {0x41, 0x62, 0xef, 0xbe, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f};
        assertFalse(Utf8.isWellFormedSlowPath(invalidUtf8SlowPathBytes4, 0, invalidUtf8SlowPathBytes4.length));

        byte[] invalidUtf8SlowPathBytes5 = null;
        assertFalse(Utf8.isWellFormedSlowPath(invalidUtf8SlowPathBytes5, 0, invalidUtf8SlowPathBytes5.length));
    }
}