package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = new char[]{'a', 'b', 'c', '\u0304', '\u0305'}; // Combining characters
        byte[] bytes = Utf8.encode(sequence);

        int length = Utf8.encodedLength(sequence);
        int encodedLength = Utf8.encodedLength(bytes, 0, bytes.length);
        assertEquals(length, encodedLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthNegativeLength() {
        Utf8.encodedLength(new String("test")); // String is CharSequence
    }

    @Test
    public void testEncodedLengthSingleChar() {
        assertEquals(1, Utf8.encodedLength("A"));
    }

    @Test
    public void testEncodedLengthMultibyteChar() {
        assertEquals(2, Utf8.encodedLength("\u00E4")); // Latin Small Letter Ä
    }

    @Test
    public void testEncodedLengthSurrogate() {
        assertEquals(3, Utf8.encodedLength("\uD835\uDD1E")); // High Surrogate + Low Surrogate
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthSurrogatePair() {
        assertEquals(3, Utf8.encodedLength("\uD800\uDC00")); // Unpaired surrogate
    }

    @Test
    public void testEncodedLengthLongString() {
        String longString = "This is a long string test to check the encoded length functionality.";
        int length = longString.length();
        int encodedLength = Utf8.encodedLength(longString);
        assertEquals(length, encodedLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthInvalidUtf8() {
        byte[] invalidUtf8Bytes = new byte[]{0xED, 0xA0, 0xED, 0xA0, 0xED, 0xA0};
        Utf8.encodedLength(invalidUtf8Bytes, 0, invalidUtf8Bytes.length);
    }

    @Test
    public void testEncodedLengthEmptyString() {
        assertEquals(0, Utf8.encodedLength(""));
    }

    @Test
    public void testEncodedLengthNull() {
        assertNull(Utf8.encodedLength(null));
    }

    // test encodedLengthGeneral method

    @Test
    public void testEncodedLengthGeneralSingleChar() {
        char[] sequence = new char[]{'A'};
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(1, length);
    }

    @Test
    public void testEncodedLengthGeneralMultibyteChar() {
        char[] sequence = new char[]{'€'}; // UTF-16 code unit for UTF-8 byte sequence: 0xC3 0xA4
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(2, length);
    }

    @Test
    public void testEncodedLengthGeneralSurrogate() {
        char[] sequence = new char[]{'𠮷'}; // High Surrogate: U+20BB, Low Surrogate: U+0303
        int length = Utf8.encodedLengthGeneral(sequence, 0);
        assertEquals(4, length);
    }

    @Test
    public void testEncodedLengthGeneralLongString() {
        char[] longString = new char[]{'This', 'is', 'a', 'long', 'string', 'test', 'to', 'check', 'the', 'encoded', 'length', 'functionality.'};
        int length = Utf8.encodedLengthGeneral(longString, 0);
        int expectedLength = longString.length * 2; // Each Unicode character requires at most 2 bytes in UTF-8
        assertEquals(expectedLength, length);
    }

    @Test
    public void testIsWellFormed() {
        byte[] validUtf8Bytes = new byte[]{0x41, 0x62, 0x63, 0xC3, 0xA4}; // Latin capital letter A, Latin small letter b, Latin small letter c, Euro sign
        assertTrue(Utf8.isWellFormed(validUtf8Bytes));
    }

    @Test
    public void testIsWellFormedInvalidUtf8() {
        byte[] invalidUtf8Bytes = new byte[]{0xED, 0xA0, 0xED, 0xA0, 0xED, 0xA0}; // Invalid UTF-8 sequence
        assertFalse(Utf8.isWellFormed(invalidUtf8Bytes));
    }

    @Test
    public void testIsWellFormedNull() {
        assertNull(Utf8.isWellFormed(null));
    }

    @Test
    public void testIsWellFormedEmptyArray() {
        assertFalse(Utf8.isWellFormed(new byte[0]));
    }

    @Test
    public void testIsWellFormedEmptyString() {
        assertTrue(Utf8.isWellFormed("".getBytes()));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] slowPathBytes = new byte[]{0x41, 0x80, 0xED, 0xA0, 0xED, 0xA0, 0xED, 0xA0}; // Latin capital letter A, invalid byte sequence
        assertTrue(Utf8.isWellFormedSlowPath(slowPathBytes, 0, slowPathBytes.length));
    }

    @Test
    public void testIsWellFormedLongArray() {
        byte[] longArray = new byte[100000]; // Generate a large byte array to test isWellFormed method performance
        for (int i = 0; i < longArray.length; ++i) {
            longArray[i] = (byte) (Math.random() * 128);
        }
        assertTrue(Utf8.isWellFormed(longArray));
    }
}