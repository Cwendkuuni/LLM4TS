package com.example;

import org.junit.Test;
import static org.junit.Assert.*;;

public class Utf8Test {

    @Test
    public void testEncodedLength_Basic() {
        CharSequence sequence = "abcdef";
        int expected = 6;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_IllegalArgumentException() {
        CharSequence sequence = "abcdefghij\u0000";
        Utf8.encodedLength(sequence); // should throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_EmptyString() {
        CharSequence sequence = "";
        Utf8.encodedLength(sequence); // should throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_SurrogatePair() {
        CharSequence sequence = "abcdefghij\ud834\udc00"; // Unpaired surrogate
        Utf8.encodedLength(sequence); // should throw IllegalArgumentException
    }

    @Test
    public void testEncodedLength_MultiByteCharacter() {
        CharSequence sequence = "abcdefghij\u03b4"; // Greek letter "iota"
        int expected = 10;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsWellFormed_IllegalArgumentException() {
        byte[] bytes = new byte[] { 0x41, 0x42, 0x43, (byte) 0xff, 0x45 };
        Utf8.isWellFormed(bytes); // should throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsWellFormed_IllegalArgumentException_EndIndex() {
        byte[] bytes = new byte[] { 0x41, 0x42, 0x43 };
        Utf8.isWellFormed(bytes, 3, 1); // should throw IllegalArgumentException
    }

    @Test
    public void testIsWellFormed_InvalidUTF8() {
        byte[] bytes = new byte[] { 0x41, 0x42, 0x43, 0xef, 0xbf }; // Invalid UTF-8 sequence
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_BOM() {
        byte[] bytes = new byte[] { 0xef, 0xbb, 0xbf, 0x41, 0x42, 0x43 }; // UTF-8 BOM
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }
}