package com.example;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        String sequence = "Hello World";
        int actual = Utf8.encodedLength(sequence);
        int expected = 11;
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLengthGeneral() {
        String sequence = "Mojito 🍹";
        int actual = Utf8.encodedLengthGeneral("Mojito 🍹", 0);
        int expected = 11;
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedBytes() {
        byte[] bytes = new byte[] { 72, 101, 108, 108, 101, 32, 87, 111, 114, 108, 100, 33 };
        boolean actual = Utf8.isWellFormed(bytes);
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedBytesInvalid() {
        byte[] bytes = new byte[] { -1, -2, -3, -4, 255, 255 };
        boolean actual = Utf8.isWellFormed(bytes);
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthNullSequence() {
        String sequence = null;
        int actual = Utf8.encodedLength(sequence);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthNegativeLength() {
        String sequence = "Hello World";
        int actual = Utf8.encodedLength(sequence.subSequence(0, -1));
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthInvalidSequence() {
        String sequence = "Hello 💩";
        int actual = Utf8.encodedLength(sequence);
        int expected = 10;
        assertEquals(expected, actual);

        try {
            int actual2 = Utf8.encodedLength(sequence.substring(2));
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEncodedLengthSurrogates() {
        String sequence = "👋🏼";
        int actual = Utf8.encodedLength(sequence);
        int expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLengthSurrogatesFollowedByAscii() {
        String sequence = "👋🏼 Hello";
        int actual = Utf8.encodedLength(sequence);
        int expected = 13;
        assertEquals(expected, actual);
    }
}