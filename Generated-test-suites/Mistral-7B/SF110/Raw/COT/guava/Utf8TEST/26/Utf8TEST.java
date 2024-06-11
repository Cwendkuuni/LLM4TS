package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        String input = "Hello World!";
        int expected = input.length();
        int actual = Utf8.encodedLength(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_EmptyString() {
        String input = "";
        int expected = 0;
        int actual = Utf8.encodedLength(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_SingleByteChar() {
        String input = "A";
        int expected = 1;
        int actual = Utf8.encodedLength(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_MultipleByteChars() {
        String input = "Mojīkā"; // Unicode characters: U+043F, U+0441, U+0438, U+0441
        int expected = 10;
        int actual = Utf8.encodedLength(input);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_InvalidInput() {
        String input = "💥"; // Unicode character: U+1F4A9
        int actual = Utf8.encodedLength(input);
        fail("Expected IllegalArgumentException but got: " + actual);
    }

    @Test
    public void testEncodedLengthGeneral() {
        String input = "💥💣"; // Unicode characters: U+1F4A9, U+1F4A8
        int expected = 4;
        int actual = Utf8.encodedLengthGeneral(input, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed() {
        byte[] input = new byte[] { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33 };
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_InvalidInput() {
        byte[] input = new byte[] { -1, 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33 };
        boolean actual = Utf8.isWellFormed(input);
        assertFalse(actual);
    }

    @Test
    public void testIsWellFormed_EmptyInput() {
        byte[] input = new byte[0];
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_ShortInput() {
        byte[] input = new byte[] { 72, 101, 108 };
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] input = new byte[] { -66, 67, -67, 67, 67, 67, 67, 67, 67, 67, 67, 67, 67, 67, 67 };
        boolean expected = true;
        boolean actual = Utf8.isWellFormedSlowPath(input, 0, input.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedSlowPath_InvalidInput() {
        byte[] input = new byte[] { -1, 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33, 65 };
        boolean actual = Utf8.isWellFormedSlowPath(input, 0, input.length);
        assertFalse(actual);
    }
}