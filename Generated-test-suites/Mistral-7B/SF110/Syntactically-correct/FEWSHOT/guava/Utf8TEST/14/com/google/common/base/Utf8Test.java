package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        char[] sequence = {'\u0001', '\u007f', '\u0410', '\u0800', '\uD800', '\uDFFF', '\uFFFF'};
        int length = Utf8.encodedLength(new String(sequence));
        assertEquals(12, length);

        char[] invalidSequence = {'\u0001', '\u007f', '\u0410', '\uD800', '\uDFFF', '\uFFFF', '\ufffe'};
        try {
            length = Utf8.encodedLength(new String(invalidSequence));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        char[] sequence = {'\u0001', '\u007f', '\u0410', '\u0800', '\uD800', '\uDFFF', '\uFFFF'};
        int length = Utf8.encodedLengthGeneral(new String(sequence), 0);
        assertEquals(12, length);

        char[] invalidSequence = {'\u0001', '\u007f', '\u0410', '\uD800', '\uDFFF', '\uFFFF', '\ufffe'};
        try {
            length = Utf8.encodedLengthGeneral(new String(invalidSequence), 0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = {0x41, 0x80, 0x80, 0x80};
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue(result);

        bytes = new byte[]{-62, 65, -65, 65};
        result = Utf8.isWellFormed(bytes);
        assertFalse(result);

        bytes = new byte[]{0xed, 0x9e, 0xbf, 0xbf, 0xed, 0x9e, 0xbf, 0xbf};
        result = Utf8.isWellFormed(bytes);
        assertTrue(result);

        bytes = new byte[]{-128, -61, 65, 65, -128, -61, 65, 65};
        result = Utf8.isWellFormed(bytes);
        assertTrue(result);

        bytes = new byte[]{0xc3, 0x80, 0xc3, 0x80, 0xc3, 0x80, 0xc3, 0x80};
        result = Utf8.isWellFormed(bytes);
        assertFalse(result);

        bytes = new byte[]{-65, -65, -128, -61, 65, 65, 65, 65};
        result = Utf8.isWellFormed(bytes);
        assertFalse(result);

        bytes = new byte[]{-32, 0x41, 0x80, 0x80};
        result = Utf8.isWellFormed(bytes);
        assertFalse(result);

        bytes = new byte[]{-32, 0x41, 0x80, 0x81};
        result = Utf8.isWellFormed(bytes);
        assertFalse(result);

        bytes = new byte[]{0xff, 0xff, 0xff, 0xff};
        result = Utf8.isWellFormed(bytes);
        assertTrue(result);

        bytes = new byte[]{0xff, 0xff, 0xff, 0xff, 0xff, 0xff};
        result = Utf8.isWellFormed(bytes);
        assertTrue(result);

        bytes = new byte[]{0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff};
        result = Utf8.isWellFormed(bytes);
        assertTrue(result);

        bytes = new byte[]{-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65,
            -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65};
        result = Utf8.isWellFormed(bytes);
        assertFalse(result);

        bytes = new byte[]{-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65,
            -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue(result);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = {0x41, 0x80, 0x80, 0x80};
        boolean result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue(result);

        bytes = new byte[]{-62, 65, -65, 65};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse(result);

        bytes = new byte[]{0xed, 0x9e, 0xbf, 0xbf, 0xed, 0x9e, 0xbf, 0xbf};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue(result);

        bytes = new byte[]{-128, -61, 65, 65, -128, -61, 65, 65};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue(result);

        bytes = new byte[]{0xc3, 0x80, 0xc3, 0x80, 0xc3, 0x80, 0xc3, 0x80};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse(result);

        bytes = new byte[]{-65, -65, -128, -61, 65, 65, 65, 65};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertFalse(result);

        bytes = new byte[]{0xff, 0xff, 0xff, 0xff};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue(result);

        bytes = new byte[]{0xff, 0xff, 0xff, 0xff, 0xff, 0xff};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue(result);

        bytes = new byte[]{0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue(result);

        bytes = new byte[]{-65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65,
            -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65, -65};
        result = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
        assertTrue(result);
    }
}