package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = new char[]{'a', 'b', '\uD801', '\uD840', '\uD841', 'c'};
        int length = Utf8.encodedLength(new String(sequence));
        assertEquals(11, length);

        sequence = new char[]{'a', 'b', '\uD800'};
        try {
            length = Utf8.encodedLength(new String(sequence));
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        char[] sequence = new char[]{'a', 'b', '\uD801', '\uD840', '\uD841', 'c'};
        int start = 2;
        int length = Utf8.encodedLengthGeneral(new String(sequence), start);
        assertEquals(5, length);

        sequence = new char[]{'a', 'b'};
        try {
            length = Utf8.encodedLengthGeneral(new String(sequence), 1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0xE1, (byte) 0xA0, (byte) 0x80};
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0xE1, (byte) 0xA0, (byte) 0x80};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0xE1, (byte) 0xBF};
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[]{(byte) 0xE1, (byte) 0xBF, (byte) 0xE2, (byte) 0xA0};
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[]{(byte) 0xE1, (byte) 0xBF, (byte) 0xE2, (byte) 0xA0};
        assertTrue(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));

        bytes = new byte[]{(byte) 0xE1, (byte) 0xBF, (byte) 0xE3, (byte) 0xA0};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }
}