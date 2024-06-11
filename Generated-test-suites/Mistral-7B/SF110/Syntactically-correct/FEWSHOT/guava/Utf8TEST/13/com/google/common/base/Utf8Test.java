package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = {'a', 'b', 'c'};
        int len = Utf8.encodedLength(new String(sequence));
        assertEquals(len, 3);

        char[] sequenceSurrogate = {'\uD800', '\uDC00'};
        try {
            len = Utf8.encodedLength(new String(sequenceSurrogate));
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        sequence = Character.toChars(0x110000);
        len = Utf8.encodedLength(new String(sequence));
        assertEquals(len, 4);
    }

    @Test
    public void testEncodedLengthGeneral() {
        char[] sequence = {'a', 'b', 'c'};
        final int start = 1;
        int len = Utf8.encodedLengthGeneral(new String(sequence), start);
        assertEquals(len, 2);

        char[] sequenceSurrogate = {'\uD800', '\uDC00'};
        try {
            len = Utf8.encodedLengthGeneral(new String(sequenceSurrogate), 0);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        char[] sequenceSurrogatePair = {'\uD800', '\uD801'};
        len = Utf8.encodedLengthGeneral(new String(sequenceSurrogatePair), 0);
        assertEquals(len, 3);
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = {0x41, 0x42, 0x43};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{-1, 0x42, 0x43};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0xED, 0xA0, 0x80, 0xA1};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0xEF, 0xBF, 0xBD, 0xE1};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{0xED, 0xA0, 0x80, 0x81, 0xE1};
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = {0xED, 0xB0, 0x80, 0x81, 0xE1};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{0xED, 0x80, 0x81, 0xE1};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{0xED, 0xB0, 0x80, 0x82, 0xE1};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{0xED, 0xB0, 0x81, 0x82, 0xE1};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{0xED, 0xB0, 0x81, 0x81, 0xE1};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }
}