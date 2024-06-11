package com.google.common.base;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = new char[]{'a', 'b', 'c', '\u0304', '\u0308', '\u030C', '\u0345', '\u0346', '\u0347'};
        byte[] expectedBytes = new byte[]{(byte) 0x81, (byte) 0x82, (byte) 0x83, (byte) 0xC2, (byte) 0x84, (byte) 0x85, (byte) 0xCC, (byte) 0x86, (byte) 0xCD, (byte) 0xCE};

        // Test with empty sequence
        Assert.assertEquals(0, Utf8.encodedLength(Character.TO_MONOTONE_SUBSTITUTION_PLANE, 0, 0));

        // Test with valid sequences
        for (int i = 0; i < sequence.length; ++i) {
            int actualLength = Utf8.encodedLength(sequence, 0, i + 1);
            byte[] actualBytes = new byte[actualLength];
            Utf8.encode(sequence, 0, i + 1, actualBytes, 0);
            assertTrue(Arrays.equals(expectedBytes, Arrays.copyOfRange(actualBytes, 0, expectedBytes.length)));
            Assert.assertEquals(expectedBytes.length, actualLength);
        }

        // Test with invalid sequence
        char[] invalidSequence = new char[]{'a', '\uD800', '\uDC00'};
        byte[] invalidBytes = new byte[Utf8.encodedLength(invalidSequence)];
        Assert.assertFalse(Utf8.encode(invalidSequence, 0, invalidSequence.length, invalidBytes, 0));
    }

    @Test
    public void testIsWellFormed() {
        byte[] validUtf8Bytes = new byte[]{0x41, 0x42, 0xE2, 0x98, 0x83, 0xA0, 0x80};
        byte[] invalidUtf8Bytes = new byte[]{0x41, 0x42, 0xE2, 0x98, 0x83, (byte) 0x80, 0x80};

        // Test with valid UTF-8 byte array
        assertTrue(Utf8.isWellFormed(validUtf8Bytes));

        // Test with invalid UTF-8 byte array
        assertFalse(Utf8.isWellFormed(invalidUtf8Bytes));
    }

    @Test
    public void testIsWellFormedWithNull() {
        assertFalse(Utf8.isWellFormed(null));
    }

    @Test
    public void testIsWellFormedWithEmptyArray() {
        assertTrue(Utf8.isWellFormed(new byte[0]));
    }
}