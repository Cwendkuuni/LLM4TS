package com.google.common.base.test;

import static org.junit.Assert.*;

import com.google.common.base.Utf8;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = new char[]{'a', 'b', 'c', '\u0100', '\u0800'}; // normal characters and one surrogate
        int length = Utf8.encodedLength(new String(sequence));
        int expectedLength = 5 + 3; // 5 characters in sequence and 3 surrogate pairs
        assertEquals(expectedLength, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthWithMalformedSequence() {
        char[] sequence = new char[]{'a', '\u0800', 'b'}; // surrogate without pair
        try {
            Utf8.encodedLength(new String(sequence));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("UTF-8 length does not fit in int: 110210960158739105664", e.getMessage());
        }
    }

    @Test
    public void testEncodedLengthWithEmptySequence() {
        int length = Utf8.encodedLength(new String(""));
        assertEquals(0, length);
    }

    @Test
    public void testEncodedLengthWithSingleCharacter() {
        int length = Utf8.encodedLength(new String("a"));
        assertEquals(1, length);
    }

    @Test
    public void testEncodedLengthWithSingleSurrogate() {
        int length = Utf8.encodedLength(new String("\u0800"));
        assertEquals(2, length);
    }

    @Test
    public void testEncodedLengthWithMultipleSurrogates() {
        char[] sequence = new char[]{'a', '\u0800', '\u0801'}; // normal character followed by two surrogates
        int length = Utf8.encodedLength(new String(sequence));
        int expectedLength = 2 + 3; // 2 characters in sequence and 3 surrogate pairs
        assertEquals(expectedLength, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthWithUnpairedSurrogate() {
        char[] sequence = new char[]{'a', '\u0800'}; // unpaired surrogate
        try {
            Utf8.encodedLength(new String(sequence));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Unpaired surrogate at index 1", e.getMessage());
        }
    }

    @Test
    public void testIsWellFormedWithValidUtf8Bytes() {
        byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x80, (byte) 0x81, (byte) 0x82};
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedWithMalformedUtf8Bytes() {
        byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x80, (byte) 0x42};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedWithInvalidStartPosition() {
        byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43};
        byte[] subarray = new byte[]{bytes[0], bytes[1]};
        try {
            Utf8.isWellFormed(subarray, 2, 3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testIsWellFormedWithInvalidLength() {
        byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43};
        try {
            Utf8.isWellFormed(bytes, 0, 4);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}