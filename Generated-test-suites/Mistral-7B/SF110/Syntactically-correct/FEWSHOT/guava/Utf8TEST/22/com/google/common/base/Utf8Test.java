package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char sequence[] = {'a', 'b', 'c'};
        int length = Utf8.encodedLength(sequence);
        assertEquals(6, length);

        char sequence2[] = {'\u00e0', '\u03b4', '\u03c9'}; // Latin Capital Letter A with Grave, Combining Macron, and Combining Diaeresis
        int length2 = Utf8.encodedLength(sequence2);
        assertEquals(11, length2);

        char sequence3[] = {'\uD800', '\uDC00', '\uD800', '\uDC01'}; // High Surrogate, Low Surrogate, High Surrogate, Low Surrogate
        int length3 = Utf8.encodedLength(sequence3);
        assertEquals(12, length3); // Each surrogate pair is encoded as 3 bytes

        char sequence4[] = {'\ufffd'}; // Unicode Replacement Character
        int length4 = Utf8.encodedLength(sequence4);
        assertEquals(1, length4); // Replacement character is encoded as 1 byte
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthWithIllegalCharacter() {
        char sequence[] = {'\uFFFF'}; // Unicode Character 'FFFF' is illegal
        int length = Utf8.encodedLength(sequence);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[]{0x41, 0x42, 0x43}; // ASCII 'A', 'B', 'C'
        assertTrue(Utf8.isWellFormed(bytes));

        byte[] bytes2 = new byte[]{0xC3, (byte) 0x83, 0xC3, 0x84}; // UTF-8 encoded 'Ä', 'Ä'
        assertTrue(Utf8.isWellFormed(bytes2));

        byte[] bytes3 = new byte[]{0xE1, (byte) 0x99, 0x80}; // UTF-8 encoded 'Æ' followed by an invalid byte
        assertFalse(Utf8.isWellFormed(bytes3));

        byte[] bytes4 = new byte[]{0xE1, (byte) 0x99, 0xA0}; // UTF-8 encoded 'Æ' followed by a valid but unused byte
        assertTrue(Utf8.isWellFormed(bytes4));
    }

    @Test
    public void testIsWellFormedWithLargeInput() {
        byte[] bytes = new byte[1024 * 1024]; // 1 MB of random data
        assertTrue(Utf8.isWellFormed(bytes));
    }
}