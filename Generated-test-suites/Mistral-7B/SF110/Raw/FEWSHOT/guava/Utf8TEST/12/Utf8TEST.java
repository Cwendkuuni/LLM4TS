package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = {'a', 'b', 'c', '\u0080', '\u0123', '\u4e2d', '\uD801'};
        int length = Utf8.encodedLength(new String(sequence));
        assertEquals(7, length);
    }

    @Test
    public void testEncodedLengthNegativeIndex() {
        char[] sequence = {'a', 'b', 'c'};
        try {
            Utf8.encodedLength(new String(sequence, -1, 1));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEncodedLengthEmptyString() {
        assertEquals(0, Utf8.encodedLength(""));
    }

    @Test
    public void testEncodedLengthNull() {
        try {
            Utf8.encodedLength(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEncodedLengthSingleByte() {
        char[] sequence = {'a', 'b'};
        int length = Utf8.encodedLength(new String(sequence));
        assertEquals(2, length);
    }

    @Test
    public void testEncodedLengthSingleSurrogate() {
        char[] sequence = {'\uD800'};
        int length = Utf8.encodedLength(new String(sequence));
        assertEquals(3, length);
    }

    @Test
    public void testEncodedLengthMultipleSurrogates() {
        char[] sequence = {'\uD800', '\uDC00', '\uD800', '\uDC01'};
        int length = Utf8.encodedLength(new String(sequence));
        assertEquals(12, length);
    }

    @Test
    public void testEncodedLengthMultibyte() {
        char[] sequence = {'\u0123'};
        int length = Utf8.encodedLength(new String(sequence));
        assertEquals(1, length);
    }

    @Test
    public void testEncodedLengthMultibyteWithSurrogate() {
        char[] sequence = {'\u0123', '\uD800'};
        int length = Utf8.encodedLength(new String(sequence));
        assertEquals(4, length);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = {-72, -74, -65, 63, 68, 61, 76, 65};
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedNull() {
        assertFalse(Utf8.isWellFormed(null));
    }

    @Test
    public void testIsWellFormedEmptyArray() {
        assertTrue(Utf8.isWellFormed(new byte[0]));
    }

    @Test
    public void testIsWellFormedSingleByte() {
        byte[] bytes = {-65};
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedInvalidByte() {
        byte[] bytes = {-1, -65};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedInvalidSequenceBeginning() {
        byte[] bytes = {-1, 65, -65};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedInvalidSequenceEnding() {
        byte[] bytes = {65, -1};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedInvalidSequenceMiddle() {
        byte[] bytes = {-65, 65, -65};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSurrogatePairBeginning() {
        byte[] bytes = {-32, '\uD800'};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSurrogatePairMiddle() {
        byte[] bytes = {'\uD800', -32, '\uDC00'};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSurrogatePairEnd() {
        byte[] bytes = {'\uD800', '\uDC00', -32};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSurrogatePairMiddleAndEnd() {
        byte[] bytes = {'\uD800', -32, '\uDC00', -32};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSurrogatePairValid() {
        byte[] bytes = {'\uD800', '\uDC00'};
        assertTrue(Utf8.isWellFormed(bytes));
    }
}